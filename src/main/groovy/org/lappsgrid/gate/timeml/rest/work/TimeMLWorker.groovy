package org.lappsgrid.gate.timeml.rest.work

import gate.Annotation
import groovy.util.logging.Slf4j
import org.lappsgrid.gate.time.TimeMLEvents
import org.lappsgrid.gate.timeml.rest.services.JobStatus
import org.lappsgrid.gate.timeml.rest.services.ManagerService
import org.lappsgrid.gate.timeml.rest.util.IDGenerator
import org.lappsgrid.pubannotation.model.Denotation
import org.lappsgrid.pubannotation.model.Span
import gate.Document as GateDocument

import java.util.concurrent.BlockingQueue

/**
 * Each worker task gets an instance of the TimeMLEvents tagger from the
 * workers queue, tags a document, returns the tagger to the queue, converts
 * the GATE/XML to a PubAnnotation Document, and returns the result.
 */
@Slf4j("logger")
class TimeMLWorker implements Runnable {
    BlockingQueue<TimeMLEvents> workers
    ManagerService manager
    WorkOrder order

    TimeMLWorker(ManagerService manager, BlockingQueue<TimeMLEvents> workers, WorkOrder order) {
        this.workers = workers
        this.manager = manager
        this.order = order
    }

    void run() {
        order.status = JobStatus.IN_PROGRESS
        TimeMLEvents tagger
        try {
            // We return the worker to the queue in the work method so it can
            // be returned as quickly as possible.
            tagger = workers.take()
            work(tagger)
        }
        catch (InterruptedException e) {
            // Reset the interrupt flag since we do not throw.
            Thread.currentThread().interrupt()
            manager.aborted(order.id)
        }
        catch (Throwable t) {
            manager.failed(order.id, t)
        }
    }

    void work(TimeMLEvents tagger) {
        GateDocument gateDoc
        try {
            gateDoc = tagger.createDocumentFromText(order.document.text)
            tagger.execute(gateDoc)
        }
        finally {
            workers.put(tagger)
        }

        List<Denotation> result = []
        IDGenerator ids = new IDGenerator()

        logger.trace("Processing annotation sets")
        for (Annotation a : gateDoc.annotations) {
            long start = a.startNode.offset
            long end = a.endNode.offset
            if ('TOKEN' == a.type) {
                String cat = a.features.get('category')
                result << new Denotation(ids.get('tok'), cat, new Span(start, end))
            }
            else if ('SENTENCE' == a.type) {
                result << new Denotation(ids.get('s'), 'Sentence', new Span(start, end))
            }
            else if ('EVENT' == a.type) {
                String type = a.features.get('majorType')
                result << new Denotation(ids.get('event', type, new Span(start, end)))
            }
        }
        if (order.document.denotations) {
            order.document.denotations.addAll(result)
        }
        else {
            order.document.denotations = result
        }
        logger.trace("Processing order {} complete.", order.id)
        manager.complete(order)
    }

}

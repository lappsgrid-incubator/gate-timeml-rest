package org.lappsgrid.gate.timeml.rest.services

import groovy.util.logging.Slf4j
import org.lappsgrid.gate.time.TimeMLEvents
import org.lappsgrid.gate.timeml.rest.util.Time
import org.lappsgrid.gate.timeml.rest.work.JobDescription
import org.lappsgrid.gate.timeml.rest.work.TimeMLWorker
import org.lappsgrid.gate.timeml.rest.work.WorkOrder
import org.lappsgrid.pubannotation.model.Document
import org.lappsgrid.serialization.Serializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 *
 */
@Slf4j("logger")
@Service
class ManagerService {

    @Autowired
    StorageService storage

    int nThreads

    ExecutorService executor
    BlockingQueue<TimeMLEvents> taggers
    Map<String, JobDescription> jobs

    ManagerService() {
        logger.info("Staring manager service")
        nThreads = 4
        jobs = new HashMap<>()
        executor = Executors.newFixedThreadPool(nThreads)
        taggers = new ArrayBlockingQueue<>()
        nThreads.times {
            taggers.put(new TimeMLEvents())
        }
        logger.debug("Started manager service with {} threads", nThreads)
    }

    JobDescription get(String id) {
        return jobs[id]
    }

    JobDescription submit(Document document) {
        logger.debug("Document {} submitted", document.id)
        WorkOrder order = new WorkOrder(document)
        JobDescription job = new JobDescription()
        job.id = order.id
        job.status = JobStatus.IN_QUEUE
        job.submittedAt = Time.now()
        jobs[job.id] = job
        TimeMLWorker worker = new TimeMLWorker(this, taggers, order)
        executor.execute(worker)
        return job
    }

    void aborted(String id) {
        logger.warn("Order {} was aborted.", id)
        JobDescription job = jobs[id]
        if (job == null) {
            logger.warn("Order {} was cancelled but there is no job description.", id)
            return
        }
        job.stoppedAt = Time.now()
        job.finishedAt = Time.now()
        job.message = 'Processing was interrupted. Try submitting the job again.'
        job.status = JobStatus.STOPPED
    }

    void failed(String id, Throwable t) {
        logger.error("Failed to process order {}", id, t)
        JobDescription job = jobs[id]
        if (job == null) {
            logger.warn("Order {} failed but there is no job description.", id)
            return
        }
        job.finishedAt = Time.now()
        job.message = 'Processing failed.\n' + t.message
        job.status = JobStatus.ERROR
    }

    void complete(WorkOrder order) {
        logger.info("Work order complete: {}", order.id)
        JobDescription job = jobs[order.id]
        if (job == null) {
            logger.warn("Order {} was completed but there is no job description.", order.id)
            return
        }
        String json = Serializer.toJson(order.document)
        storage.add(order.id, json)
        job.message = 'ok'
        job.finishedAt = Time.now()
        job.resultUrl = "/download/${id}"
        job.status = JobStatus.DONE
    }
}

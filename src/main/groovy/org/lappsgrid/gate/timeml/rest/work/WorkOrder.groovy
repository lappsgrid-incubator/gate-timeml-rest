package org.lappsgrid.gate.timeml.rest.work

import org.lappsgrid.gate.timeml.rest.services.JobStatus
import org.lappsgrid.pubannotation.model.Document

/**
 *
 */
class WorkOrder {
    final String id
    JobStatus status
    String message
    Document document

    WorkOrder() {
        this.id = UUID.randomUUID().toString()
        status = JobStatus.IN_QUEUE
    }

    WorkOrder(Document document) {
        this()
        this.document = document
    }

}

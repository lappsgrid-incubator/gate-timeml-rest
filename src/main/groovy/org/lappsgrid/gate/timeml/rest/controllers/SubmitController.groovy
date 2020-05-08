package org.lappsgrid.gate.timeml.rest.controllers

import groovy.util.logging.Slf4j
import org.lappsgrid.api.WebService
import org.lappsgrid.discriminator.Discriminators
import org.lappsgrid.gate.core.BaseGateService
import org.lappsgrid.gate.time.TimeMLEvents
import org.lappsgrid.gate.timeml.rest.Media
import org.lappsgrid.gate.timeml.rest.errors.BadRequest
import org.lappsgrid.gate.timeml.rest.errors.InternalServerError
import org.lappsgrid.gate.timeml.rest.services.ManagerService
import org.lappsgrid.gate.timeml.rest.work.JobDescription
import org.lappsgrid.pubannotation.model.Denotation
import org.lappsgrid.pubannotation.model.Document as PubDocument
import org.lappsgrid.pubannotation.model.Span
import org.lappsgrid.pubannotation.model.Track
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.serialization.Data
import org.lappsgrid.serialization.Serializer
import org.lappsgrid.serialization.lif.Annotation
import org.lappsgrid.serialization.lif.Container
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import gate.Document as GateDocument

/**
 *
 */
@Slf4j("logger")
@RestController("/submit")
class SubmitController {

    @Autowired
    ManagerService manager

//    TaskQueue queue

    @PostMapping(consumes = Media.TEXT, produces = Media.JSON)
    JobDescription postText(@RequestBody String text) {
        logger.debug('POST /submit TEXT JSON')
        PubDocument document = new PubDocument()
        document.text = text
        JobDescription job = manager.submit(document)
        return job
    }

    @PostMapping(consumes = Media.JSON, produces = Media.JSON)
    JobDescription postJson(@RequestBody String json) {
        logger.debug('POST /{}/{} JSON JSON', 'stanford', service)
        PubDocument document = Serializer.parse(json, PubDocument)
        JobDescription job = manager.submit(document)
        return job
    }

}

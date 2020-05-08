package org.lappsgrid.gate.timeml.rest.controllers

import groovy.util.logging.Slf4j
import org.lappsgrid.gate.timeml.rest.Media
import org.lappsgrid.gate.timeml.rest.errors.NotFoundError
import org.lappsgrid.gate.timeml.rest.services.ManagerService
import org.lappsgrid.gate.timeml.rest.work.JobDescription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 */
@Slf4j("logger")
@RestController()
@RequestMapping("/job")
class JobController {

    @Autowired
    ManagerService manager

    @GetMapping(path = "/{id}", produces = Media.JSON)
    JobDescription getJobStatus(@PathVariable String id) {
        JobDescription job = manager.get(id)
        if (job == null) {
            throw new NotFoundError("No such job has been submitted.")
        }
        return job
    }
}

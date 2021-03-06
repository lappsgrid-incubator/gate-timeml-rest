package org.lappsgrid.gate.timeml.rest.work

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.lappsgrid.gate.timeml.rest.services.JobStatus
import org.lappsgrid.gate.timeml.rest.util.Time

import java.time.ZonedDateTime

/**
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class JobDescription {
    String id
    @JsonProperty('submitted_at')
    ZonedDateTime submittedAt
    @JsonProperty('started_at')
    ZonedDateTime startedAt
    @JsonProperty('stopped_at')
    ZonedDateTime stoppedAt
    @JsonProperty('finished_at')
    ZonedDateTime finishedAt
    @JsonProperty('ETA')
    ZonedDateTime eta
    Long elapsed
    @JsonProperty('result_URL')
    String resultUrl
    JobStatus status
    String message

    void calculateElapsed() {
        if (startedAt == null) {
            return
        }
        if (stoppedAt) {
            elapsed = Time.between(startedAt, stoppedAt)
        }
        else if (finishedAt) {
            elapsed = Time.between(startedAt, finishedAt)
        }
        else {
            elapsed = Time.since(startedAt)
        }

    }

}

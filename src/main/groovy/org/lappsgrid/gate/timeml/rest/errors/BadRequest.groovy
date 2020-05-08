package org.lappsgrid.gate.timeml.rest.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequest extends ServiceError {
    BadRequest() {
    }

    BadRequest(String message) {
        super(message)
    }

    BadRequest(String message, Throwable cause) {
        super(message, cause)
    }

    BadRequest(Throwable var1) {
        super(var1)
    }

    BadRequest(String message, Throwable cause, boolean supress, boolean trace) {
        super(message, cause, supress, trace)
    }
}

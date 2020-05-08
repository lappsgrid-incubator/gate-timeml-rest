package org.lappsgrid.gate.timeml.rest.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 *
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerError extends ServiceError {
    InternalServerError() {
    }

    InternalServerError(String message) {
        super(message)
    }

    InternalServerError(String message, Throwable cause) {
        super(message, cause)
    }

    InternalServerError(Throwable var1) {
        super(var1)
    }

    InternalServerError(String message, Throwable cause, boolean supress, boolean trace) {
        super(message, cause, supress, trace)
    }
}

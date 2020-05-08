package org.lappsgrid.gate.timeml.rest.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundError extends ServiceError {
    NotFoundError() {
    }

    NotFoundError(String message) {
        super(message)
    }

    NotFoundError(String message, Throwable cause) {
        super(message, cause)
    }

    NotFoundError(Throwable var1) {
        super(var1)
    }

    NotFoundError(String message, Throwable cause, boolean supress, boolean trace) {
        super(message, cause, supress, trace)
    }
}

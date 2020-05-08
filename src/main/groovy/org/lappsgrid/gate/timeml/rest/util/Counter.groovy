package org.lappsgrid.gate.timeml.rest.util

/**
 *
 */
class Counter {
    int count = 0

    Counter next() {
        ++count
        return this
    }

    String toString() {
        return count.toString()
    }
}

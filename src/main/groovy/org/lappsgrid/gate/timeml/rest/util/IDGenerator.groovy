package org.lappsgrid.gate.timeml.rest.util

/**
 *
 */
class IDGenerator {
    Map<String,Counter> counters = [:]

    void reset() {
        counters.clear()
    }

    String get(String type) {
        Counter counter = counters[type]
        if (counter == null) {
            counter = new Counter()
            counters[type] = counter
        }
        return "${type}${counter++}"
    }
}

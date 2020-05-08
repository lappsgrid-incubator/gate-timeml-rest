package org.lappsgrid.gate.timeml.rest.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

/**
 *
 */
//@Service
class Configuration {
//    @Value("file.age")
    private final int DEFAULT_FILE_AGE = 5
//    @Value("reaper.delay")
    private final int DEFAULT_REAPER_DELAY = 30
//    @Value("storage.directory")
    private final String DEFAULT_STORAGE_DIR = "/tmp/timeml"

    int fileAge
    int reaperDelay
    File storageDir

    Configuration() {
        fileAge = get ("FILE_AGE", DEFAULT_FILE_AGE)
        reaperDelay = get("REAPER_DELAY", DEFAULT_REAPER_DELAY)
        storageDir = get("STORAGE_PATH", DEFAULT_STORAGE_DIR)
    }

    private <T> T get(String name, T defaultValue) {
        String value = System.getenv(name)
        if (value) return value as T
        value = System.getProperty(name)
        if (value) return value as T
        return defaultValue
    }

}

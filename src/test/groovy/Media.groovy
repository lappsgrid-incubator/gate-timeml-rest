import org.junit.Test
import org.springframework.http.MediaType

/**
 *
 */
class Media {

    @Test
    void types() {
        println MediaType.TEXT_PLAIN.toString()
        println MediaType.APPLICATION_JSON.toString()
        println MediaType.APPLICATION_JSON_VALUE
    }
}

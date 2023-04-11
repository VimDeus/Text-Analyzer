import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;


/**
 * This class contains the unit tests for the TextAnalysisServer class (server-side).
 */
public class TextAnalysisServerTest {

    /**
     * Tests the analyzeText method with a positive input (numWords > 0).
     * @throws IOException if an I/O error occurs when reading from the URL.
     */
    @Test
    public void testAnalyzeTextPositive() throws IOException {
        String result = TextAnalysisServer.analyzeText(5);
        assertNotNull(result);
        assertTrue(result.contains("the"));
        assertTrue(result.contains("and"));
        assertTrue(result.contains("i"));
        assertTrue(result.contains("of"));
        assertTrue(result.contains("my"));
    }

    
    /**
     * Tests the analyzeText method with an empty input (numWords = 0).
     * @throws IOException if an I/O error occurs when reading from the URL.
     */
    @Test
    public void testAnalyzeTextEmptyInput() throws IOException {
        String result = TextAnalysisServer.analyzeText(0);
        assertNotNull(result);
        assertEquals("", result);
        assertTrue(result.isEmpty());
    }
}

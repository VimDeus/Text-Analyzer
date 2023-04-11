import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * This class contains the unit tests for the TextAnalyzer class (client-side).
 */
public class TextAnalyzerTest {

    private TextAnalyzer analyzer;

    
    /**
     * This method is called before the execution of each test case. It sets up the
     * necessary objects, resources, or configurations required for the test execution.
     * For example, it can initialize the TextAnalysisServer instance and configure a
     * test server socket.
     */
    @BeforeEach
    public void setUp() {
        analyzer = new TextAnalyzer();
    }
    
    
    /**
     * This method is called after the execution of each test case. It is responsible
     * for cleaning up any resources, objects, or configurations used during the test
     * execution. For example, it can close the test server socket and set the
     * TextAnalysisServer instance to null.
     */
    @AfterEach
    public void tearDown() {
        analyzer = null;
    }

    
    /**
     * Test the analyzeText() method with a normal input value.
     * @throws IOException if an I/O error occurs while testing
     */
    @Test
    public void testAnalyzeText() throws IOException {
        String result = analyzer.analyzeText(5);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    
    /**
     * Test the analyzeText() method with an empty input value.
     * @throws IOException if an I/O error occurs while testing
     */
    @Test
    public void testEmptyInput() throws IOException {
        String result = analyzer.analyzeText(0);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    
    /**
     * Test the analyzeText() method with an invalid negative input value.
     * @throws IOException if an I/O error occurs while testing
     */
    @Test
    public void testInvalidInput() throws IOException {
        String result = analyzer.analyzeText(-1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    
    /**
     * Test the analyzeText() method with a small input value to check if it returns the expected number of word occurrences.
     * @throws IOException if an I/O error occurs while testing
     */
    @Test
    public void testSmallInput() throws IOException {
        int numWords = 5;
        String result = analyzer.analyzeText(numWords);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.split("\n").length <= numWords);
    }

    
    /**
     * Test the analyzeText() method with a large input value to check if it returns the expected number of word occurrences.
     * @throws IOException if an I/O error occurs while testing
     */
    @Test
    public void testLargeInput() throws IOException {
        int numWords = 50;
        String result = analyzer.analyzeText(numWords);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.split("\n").length <= numWords);
    }

    
    /**
     * Test the analyzeText() method with the maximum input value.
     * @throws IOException if an I/O error occurs while testing
     */
    @Test
    public void testMaximumInput() throws IOException {
        String result = analyzer.analyzeText(Integer.MAX_VALUE);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}


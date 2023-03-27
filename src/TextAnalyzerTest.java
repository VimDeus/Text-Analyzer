import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextAnalyzerTest {
    @Test
    public void testAnalyzeText() {
        TextAnalyzer textAnalyzer = new TextAnalyzer();
        String result = textAnalyzer.analyzeText(5);
        String expected = "the: 59\n" +
                "i: 32\n" +
                "and: 30\n" +
                "my: 24\n" +
                "of: 22\n";
        assertEquals(expected, result);
        //Test should PASS
    }
    @Test
    public void testEmptyInput() {
        TextAnalyzer analyzer = new TextAnalyzer();
        String result = analyzer.analyzeText(0);
        assertEquals("", result);
        //Test should PASS
    }
    

    @Test
    public void testInvalidInput() {
        TextAnalyzer analyzer = new TextAnalyzer();
        String result = analyzer.analyzeText(-1);
        assertEquals("", result);
        //Test should PASS
    }

    @Test
    public void testSmallInput() {
        TextAnalyzer analyzer = new TextAnalyzer();
        String result = analyzer.analyzeText(5);
        assertTrue(result.contains("raven"));
        assertTrue(result.contains("nevermore"));
        assertTrue(result.contains("bird"));
        assertTrue(result.contains("bust"));
        assertTrue(result.contains("door"));
        //Test should FAIL
    }

    @Test
    public void testLargeInput() {
        TextAnalyzer analyzer = new TextAnalyzer();
        String result = analyzer.analyzeText(50);
        assertTrue(result.contains("raven"));
        assertTrue(result.contains("nevermore"));
        assertTrue(result.contains("bird"));
        assertTrue(result.contains("bust"));
        assertTrue(result.contains("door"));
        assertTrue(result.contains("said"));
        assertTrue(result.contains("lenore"));
        assertTrue(result.contains("perched"));
        assertTrue(result.contains("nameless"));
        assertTrue(result.contains("night"));
        assertTrue(result.contains("pondered"));
        assertTrue(result.contains("nothing"));
        assertTrue(result.contains("tapping"));
        //Test should FAIL
    }

    @Test
    public void testMaximumInput() {
        TextAnalyzer analyzer = new TextAnalyzer();
        String result = analyzer.analyzeText(Integer.MAX_VALUE);
        assertTrue(result.contains("raven"));
        assertTrue(result.contains("nevermore"));
        assertTrue(result.contains("bird"));
        assertTrue(result.contains("bust"));
        assertTrue(result.contains("door"));
        assertTrue(result.contains("said"));
        assertTrue(result.contains("lenore"));
        assertTrue(result.contains("perched"));
        assertTrue(result.contains("nameless"));
        assertTrue(result.contains("night"));
        assertTrue(result.contains("pondered"));
        assertTrue(result.contains("nothing"));
        assertTrue(result.contains("tapping"));
        //Test should PASS
    }

}

package de.dhbw.modellbahn.plugin.parser.lexer;

import de.dhbw.modellbahn.plugin.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
    }

    @Test
    void testKeywords() throws LexerException {
        lexer.init("ADD TO DRIVE");

        assertEquals(TokenType.ADD_KEYWORD, lexer.lookAhead().type());
        assertEquals("ADD", lexer.lookAhead().value());
        lexer.advance();

        assertEquals(TokenType.TO_KEYWORD, lexer.lookAhead().type());
        assertEquals("TO", lexer.lookAhead().value());
        lexer.advance();

        assertEquals(TokenType.DRIVE_COMMAND, lexer.lookAhead().type());
        assertEquals("DRIVE", lexer.lookAhead().value());
        lexer.advance();

        assertEquals(TokenType.EOF, lexer.lookAhead().type());
    }

    @Test
    void testCommentSkipping() throws LexerException {
        lexer.init("// This is a comment\nADD");

        assertEquals(TokenType.ADD_KEYWORD, lexer.lookAhead().type());
        assertEquals("ADD", lexer.lookAhead().value());
    }

    @Test
    void testMixedTokens() throws LexerException {
        lexer.init("DRIVE 1234 TO junction_5 USING TIME");

        assertEquals(TokenType.DRIVE_COMMAND, lexer.lookAhead().type());
        lexer.advance();

        assertEquals(TokenType.LOC_ID, lexer.lookAhead().type());
        assertEquals("1234", lexer.lookAhead().value());
        lexer.advance();

        assertEquals(TokenType.TO_KEYWORD, lexer.lookAhead().type());
        lexer.advance();

        assertEquals(TokenType.GRAPH_POINT, lexer.lookAhead().type());
        assertEquals("junction_5", lexer.lookAhead().value());
        lexer.advance();

        assertEquals(TokenType.USING_KEYWORD, lexer.lookAhead().type());
        lexer.advance();

        assertEquals(TokenType.OPTIMIZATION, lexer.lookAhead().type());
        assertEquals("TIME", lexer.lookAhead().value());
        lexer.advance();

        assertEquals(TokenType.EOF, lexer.lookAhead().type());
    }

    @Test
    void testLineAndColumnTracking() throws LexerException {
        lexer.init("ADD\nTO");

        assertEquals(1, lexer.lookAhead().line());
        assertEquals(1, lexer.lookAhead().column());
        lexer.advance();

        assertEquals(2, lexer.lookAhead().line());
        assertEquals(1, lexer.lookAhead().column());
    }

    @Test
    void testExpectMethod() throws LexerException, ParseException {
        lexer.init("ADD TO");

        // This should work fine
        lexer.expect(TokenType.ADD_KEYWORD);
        lexer.expect(TokenType.TO_KEYWORD);

        // Now at EOF
        assertEquals(TokenType.EOF, lexer.lookAhead().type());
    }

    @Test
    void testExpectMethodThrowsException() throws LexerException {
        lexer.init("ADD DRIVE");

        // First token is correct
        assertDoesNotThrow(() -> lexer.expect(TokenType.ADD_KEYWORD));

        // Next token is DRIVE_COMMAND, not TO_KEYWORD
        assertThrows(ParseException.class, () -> lexer.expect(TokenType.TO_KEYWORD));
    }

    @Test
    void testInvalidInput() throws LexerException {
        String invalidInput = "ADD @";
        lexer.init(invalidInput);

        assertThrows(LexerException.class, () -> lexer.advance());
    }

    @ParameterizedTest
    @ValueSource(strings = {"stationA", "W12", "A13", "junction1", "junction_5", "123A", "TIMO"})
    void testCorrectGraphPoint(String tokenValue) throws LexerException {
        lexer.init(tokenValue);
        assertEquals(TokenType.GRAPH_POINT, lexer.lookAhead().type());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADD", "1234"})
    void testNotRecognizeAsGraphPoint(String tokenValue) throws LexerException {
        lexer.init(tokenValue);
        assertNotEquals(TokenType.GRAPH_POINT, lexer.lookAhead().type());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "0", "9999", "1", "1000"})
    void testCorrectLocID(String tokenValue) throws LexerException {
        lexer.init(tokenValue);
        assertEquals(TokenType.LOC_ID, lexer.lookAhead().type());
    }

    @ParameterizedTest
    @ValueSource(strings = {"stationA", "W12", "A13", "junction1", "junction_5", "123A", "TIMO"})
    void testNotRecognizeAsLocID(String tokenValue) throws LexerException {
        lexer.init(tokenValue);
        assertNotEquals(TokenType.LOC_ID, lexer.lookAhead().type());
    }
}
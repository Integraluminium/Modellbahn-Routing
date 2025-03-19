package de.dhbw.modellbahn.plugin.parser.lexer;

import de.dhbw.modellbahn.plugin.parser.ParseException;
import de.dhbw.modellbahn.plugin.parser.lexer.machines.*;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final List<TokenMachine> tokenMachines;
    private Token currentToken;
    private CharSequence input;
    private int pos = 0;
    private int line = 1;
    private int col = 1;

    public Lexer() {
        tokenMachines = new ArrayList<>();
        addLexerMachines();
    }

    private void addLexerMachines() {
        addMachine(new LineCommentMachine());
        addKeywordMachine("ADD", TokenType.ADD_KEYWORD);
        addKeywordMachine("AT", TokenType.AT_KEYWORD);
        addKeywordMachine("TO", TokenType.TO_KEYWORD);
        addKeywordMachine("FACING", TokenType.FACING_KEYWORD);
        addKeywordMachine("USING", TokenType.USING_KEYWORD);
        addKeywordMachine("DRIVE", TokenType.DRIVE_COMMAND);
        addKeywordMachine("CONSIDER", TokenType.CONSIDER_KEYWORD);

        addKeywordMachine("HEIGHT", TokenType.HEIGHT_KEYWORD);
        addKeywordMachine("ELECTRIFICATION", TokenType.ELECTRIFICATION_KEYWORD);

        addKeywordMachine("MODIFY", TokenType.MODIFY_KEYWORD);
        addKeywordMachine("LIST", TokenType.LIST_KEYWORD);

        addKeywordMachine("TOGGLE", TokenType.TOGGLE_KEYWORD);
        addKeywordMachine("DIRECTION", TokenType.DIRECTION_KEYWORD);
        addKeywordMachine("POSITION", TokenType.POSITION_KEYWORD);
        addKeywordMachine("LOCOMOTIVES", TokenType.LOCOMOTIVES_KEYWORD);
        addKeywordMachine("GRAPHPOINTS", TokenType.GRAPHPOINTS_KEYWORD);


        addKeywordMachine("SYSTEM", TokenType.SYSTEM_KEYWORD);
        addKeywordMachine("START", TokenType.START_KEYWORD);
        addKeywordMachine("STOP", TokenType.STOP_KEYWORD);
        addKeywordMachine("SPEED", TokenType.SPEED_KEYWORD);
        addKeywordMachine("NEW", TokenType.NEW_KEYWORD);
        addKeywordMachine("ROUTE", TokenType.ROUTE_KEYWORD);
        addKeywordMachine("WITH", TokenType.WITH_KEYWORD);

        addMachine(new OptimizationMachine());
        addMachine(new AlgorithmMachine());
        addMachine(new NumberMachine());
        addMachine(new IdentifierMachine());
    }

    private void addKeywordMachine(String keyword, TokenType type) {
        addMachine(new KeywordMachine(keyword, type));

    }

    private void addMachine(TokenMachine machine) {
        tokenMachines.add(machine);
    }

    public void init(String input) throws LexerException {
        this.input = input;
        this.pos = 0;
        this.line = 1;
        this.col = 1;
        advance();
    }

    public Token lookAhead() {
        return currentToken;
    }

    public void advance() throws LexerException {
        currentToken = nextToken();
    }

    public Token nextToken() throws LexerException {
        Token token = nextWord();
        while (token.type() == TokenType.COMMENT) {
            token = nextWord();
        }
        return token;
    }

    private Token nextWord() throws LexerException {
        skipWhitespace();

        if (pos >= input.length()) {
            return new Token(TokenType.EOF, "EOF", line, col);
        }

        int longestMatch = 0;
        TokenMachine bestMachine = null;

        for (TokenMachine machine : tokenMachines) {
            int matchLength = machine.scan(input, pos);
            if (matchLength > longestMatch) {
                longestMatch = matchLength;
                bestMachine = machine;
            }
        }

        if (bestMachine == null) {
            throw new LexerException("No matching machine found at position " + pos + input.charAt(pos));
        }

        String matchedText = input.subSequence(pos, pos + longestMatch).toString();
        Token token = new Token(bestMachine.getTokenType(), matchedText, line, col);
        updatePosition(longestMatch);
        return token;

    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            updatePosition(1);
        }
    }

    private void updatePosition(final int length) {
        for (int i = 0; i < length; i++) {
            if (input.charAt(pos) == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
            pos++;
        }
    }


    public void expect(TokenType type) throws ParseException, LexerException {
        if (currentToken.type() != type) {
            throw new ParseException("Unexpected Token: Expected " + type + " but got " + currentToken.type());
        }
        advance();
    }
}

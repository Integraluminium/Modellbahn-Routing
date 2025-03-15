package de.dhbw.modellbahn.plugin.parser.lexer;

public enum TokenType {
    EOF,
    ADD_KEYWORD,
    AT_KEYWORD,
    TO_KEYWORD,
    FACING_KEYWORD,
    USING_KEYWORD,
    DRIVE_COMMAND,
    CONSIDER_KEYWORD,

    HEIGHT_KEYWORD,
    ELECTRIFICATION_KEYWORD,

    OPTIMIZATION,
    LOC_ID,
    ROUTING_OPTION,
    COMMENT, GRAPH_POINT
}

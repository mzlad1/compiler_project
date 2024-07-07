package com.example.ttt;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
class Lexer {
    private static final Pattern PATTERNS = Pattern.compile( // Token patterns
            "\\s*(?:(module)\\b|" +  // MODULE
                    "(begin)\\b|" +                  // BEGIN
                    "(end)\\b|" +                    // END
                    "(const)\\b|" +                  // CONST
                    "(var)\\b|" +                    // VAR
                    "(procedure)\\b|" +              // PROCEDURE
                    "(integer)\\b|" +                // INTEGER
                    "(real)\\b|" +                   // REAL
                    "(char)\\b|" +                   // CHAR
                    "(if)\\b|" +                     // IF
                    "(then)\\b|" +                   // THEN
                    "(else)\\b|" +                   // ELSE
                    "(while)\\b|" +                  // WHILE
                    "(do)\\b|" +                     // DO
                    "(loop)\\b|" +                   // LOOP
                    "(until)\\b|" +                  // UNTIL
                    "(exit)\\b|" +                   // EXIT
                    "(call)\\b|" +                   // CALL
                    "(readint)\\b|" +                // READINT
                    "(readreal)\\b|" +               // READREAL
                    "(readchar)\\b|" +               // READCHAR
                    "(readln)\\b|" +                 // READLN
                    "(writeint)\\b|" +               // WRITEINT
                    "(writereal)\\b|" +              // WRITEREAL
                    "(writechar)\\b|" +              // WRITECHAR
                    "(writeln)\\b|" +                // WRITELN
                    "(:=)|" +                        // ASSIGN
                    "(\\+)|" +                       // PLUS
                    "(-)|" +                         // MINUS
                    "(\\*)|" +                       // MULT
                    "(/)|" +                         // DIV
                    "(mod)\\b|" +                    // MOD
                    "(div)\\b|" +                    // DIV2
                    "(\\()|" +                       // LPAREN
                    "(\\))|" +                       // RPAREN
                    "(;)+|" +                        // SEMICOLON
                    "(:)|" +                         // COLON
                    "(,)|" +                         // COMMA
                    "(\\.)|" +                       // DOT
                    "(=)|" +                         // EQUAL
                    "(\\|=)|" +                      // NOTEQUAL
                    "(<=)|" +                        // LE
                    "(>=)|" +                        // GE
                    "(<)|" +                         // LT
                    "(>)|" +                         // GT
                    "([a-zA-Z][a-zA-Z0-9]*)\\b|" +   // NAME
                    "(\\d+\\.*\\d*)|" +              // REAL_VALUE
                    "(\\d+)|" +                      // INTEGER_VALUE
                    "(.)" +                           // CATCH-ALL FOR UNEXPECTED CHARACTERS
            ")"
    );
    private final Matcher matcher;
    private Tokenval tokenC;

    private int lineNumber = 1;  // Initialize line number

    public Lexer(String input) {
        matcher = PATTERNS.matcher(input);
        nextToken();
    }

    public Tokenval nextToken() { // Add line number to the token
        if (tokenC!=null) {
            if (tokenC.tokenType.equals("EOF")) {
                return tokenC;
            }
        }
        if (matcher.find()) { // Find the next token
            String patMatched = matcher.group();
            if (patMatched.contains("\n")) {
                lineNumber++;
            }

            for (int i = 1; i <= tokenTypes.length; i++) { // Find the token type
                if (matcher.group(i) != null) {
                    tokenC = new Tokenval(tokenTypes[i - 1], matcher.group(i), lineNumber);
                    break;
                }
            }
        }
        else{ // Handle unexpected characters
            if (matcher.hitEnd()) {
                tokenC = new Tokenval("EOF", "", lineNumber);
            } else {
                throw new RuntimeException("Error, wrong character " + matcher.regionStart() + " at " + lineNumber);
            }

        }
        return tokenC;
    }

    public Tokenval peek() {
        return tokenC;
    }

    private static final String[] tokenTypes = { // Token types
            "module", "begin", "end", "const", "var", "procedure", "integer", "real", "char",
            "if", "then", "else", "while", "do", "loop", "until", "exit", "call", "readint",
            "readreal", "readchar", "readln", "writeint", "writereal", "writechar", "writeln",
            "assign", "plus", "minus", "mult", "div", "mod", "div2", "LPAREN", "RPAREN", "SEMICOLON",
            "COLON", "COMMA", "doT", "EQUAL", "NOTEQUAL", "LE", "GE", "LT", "GT", "NAME",
            "real_VALUE", "integer_VALUE", "EOF"
    };
}
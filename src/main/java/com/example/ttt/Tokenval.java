package com.example.ttt;

public class Tokenval {
    public final String tokenType; // Add token type field
    public final String content;  // Add content field
    public final int line;  // Add line number field

    public Tokenval(String type, String value, int lineNumber) {
        this.tokenType = type;  // Store the token type
        this.content = value; // Store the content
        this.line = lineNumber;  // Store the line number
    }

    @Override
    public String toString() {
        return content + " " + tokenType + " " + line;
    }
}
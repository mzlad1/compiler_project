package com.example.ttt;
import java.text.ParseException;
import java.util.*;

class Parser {
    Lexer lexer;
    Stack<String> stack = new Stack<>();
    private static final Set<String> terminalSymbols = new HashSet<>(Arrays.asList(
            "module", "begin", "end", "const", "var", "procedure", "integer", "real", "char",
            "if", "then", "else", "while", "do", "loop", "until", "exit", "call", "readint",
            "readreal", "readchar", "readln", "writeint", "writereal", "writechar", "writeln",
            "assign", "plus", "minus", "mult", "div", "mod", "div2", "LPAREN", "RPAREN", "SEMICOLON",
            "COLON", "COMMA", "doT", "EQUAL", "NOTEQUAL", "LE", "GE", "LT", "GT", "NAME",
            "real_VALUE", "integer_VALUE", "EOF"
    ));
    private ArrayList<Object[]> table;
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.table = table = new ArrayList<>();
        populatetable();
    }

    private void populatetable() {
        table.add(new Object[]{"module-decl", "module", new String[]{"module-heading", "declarations", "block", "NAME", "doT"}});
        table.add(new Object[]{"module-heading", "module", new String[]{"module", "NAME", "SEMICOLON"}});
        table.add(new Object[]{"block", "begin", new String[]{"begin", "stmt-list", "end"}});
        table.add(new Object[]{"declarations", "const", new String[]{"const-decl", "var-decl", "procedure-decl"}});
        table.add(new Object[]{"declarations", "var", new String[]{"const-decl", "var-decl", "procedure-decl"}});
        table.add(new Object[]{"declarations", "procedure", new String[]{"const-decl", "var-decl", "procedure-decl"}});
        table.add(new Object[]{"declarations", "begin", new String[]{"const-decl", "var-decl", "procedure-decl"}});
        table.add(new Object[]{"const-decl", "const", new String[]{"const", "const-list"}});
        table.add(new Object[]{"const-decl", "var", new String[]{"λ"}});
        table.add(new Object[]{"const-decl", "procedure", new String[]{"λ"}});
        table.add(new Object[]{"const-decl", "begin", new String[]{"λ"}});
        table.add(new Object[]{"const-list", "NAME", new String[]{"NAME", "EQUAL", "value", "SEMICOLON", "const-list"}});
        table.add(new Object[]{"const-list", "var", new String[]{"λ"}});
        table.add(new Object[]{"const-list", "procedure", new String[]{"λ"}});
        table.add(new Object[]{"const-list", "begin", new String[]{"λ"}});
        table.add(new Object[]{"const-list", "doT", new String[]{"λ"}});
        table.add(new Object[]{"var-decl", "var", new String[]{"var", "var-list"}});
        table.add(new Object[]{"var-decl", "procedure", new String[]{"λ"}});
        table.add(new Object[]{"var-decl", "begin", new String[]{"λ"}});
        table.add(new Object[]{"var-list", "NAME", new String[]{"var-item", "SEMICOLON", "var-list"}});
        table.add(new Object[]{"var-list", "procedure", new String[]{"λ"}});
        table.add(new Object[]{"var-list", "begin", new String[]{"λ"}});
        table.add(new Object[]{"var-item", "NAME", new String[]{"name-list", "COLON", "data-type"}});
        table.add(new Object[]{"name-list", "NAME", new String[]{"NAME", "more-names"}});
        table.add(new Object[]{"more-names", "COMMA", new String[]{"COMMA", "NAME", "more-names"}});
        table.add(new Object[]{"more-names", "COLON", new String[]{"λ"}});
        table.add(new Object[]{"more-names", "RPAREN", new String[]{}});
        table.add(new Object[]{"data-type", "integer", new String[]{"integer"}});
        table.add(new Object[]{"data-type", "real", new String[]{"real"}});
        table.add(new Object[]{"data-type", "char", new String[]{"char"}});
        table.add(new Object[]{"procedure-decl", "procedure", new String[]{"procedure-heading", "declarations", "block", "NAME", "SEMICOLON", "procedure-decl"}});
        table.add(new Object[]{"procedure-decl", "begin", new String[]{"λ"}});
        table.add(new Object[]{"procedure-heading", "procedure", new String[]{"procedure", "NAME", "SEMICOLON"}});
        table.add(new Object[]{"stmt-list", "NAME", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "readint", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "readreal", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "readchar", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "readln", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "writeint", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "writereal", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "writechar", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "writeln", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "if", new String[]{"if-stmt", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "while", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "loop", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "exit", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "call", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "begin", new String[]{"statement", "SEMICOLON", "stmt-list"}});
        table.add(new Object[]{"stmt-list", "until", new String[]{}});
        table.add(new Object[]{"stmt-list", "else", new String[]{}});
        table.add(new Object[]{"stmt-list", "end", new String[]{}});
        table.add(new Object[]{"statement", "NAME", new String[]{"ass-stmt"}});
        table.add(new Object[]{"statement", "readint", new String[]{"read-stmt"}});
        table.add(new Object[]{"statement", "readreal", new String[]{"read-stmt"}});
        table.add(new Object[]{"statement", "readchar", new String[]{"read-stmt"}});
        table.add(new Object[]{"statement", "readln", new String[]{"read-stmt"}});
        table.add(new Object[]{"statement", "writeint", new String[]{"write-stmt"}});
        table.add(new Object[]{"statement", "writereal", new String[]{"write-stmt"}});
        table.add(new Object[]{"statement", "writechar", new String[]{"write-stmt"}});
        table.add(new Object[]{"statement", "writeln", new String[]{"write-stmt"}});
        table.add(new Object[]{"statement", "if", new String[]{"if-stmt"}});
        table.add(new Object[]{"statement", "while", new String[]{"while-stmt"}});
        table.add(new Object[]{"statement", "loop", new String[]{"loop-stmt"}});
        table.add(new Object[]{"statement", "exit", new String[]{"exit-stmt"}});
        table.add(new Object[]{"statement", "call", new String[]{"call-stmt"}});
        table.add(new Object[]{"statement", "begin", new String[]{"block"}});
        table.add(new Object[]{"ass-stmt", "NAME", new String[]{"NAME", "assign", "exp"}});
        table.add(new Object[]{"exp", "NAME", new String[]{"term", "exp-prime"}});
        table.add(new Object[]{"exp", "integer_VALUE", new String[]{"term", "exp-prime"}});
        table.add(new Object[]{"exp", "real_VALUE", new String[]{"term", "exp-prime"}});
        table.add(new Object[]{"exp", "LPAREN", new String[]{"term", "exp-prime"}});
        table.add(new Object[]{"exp-prime", "plus", new String[]{"add-oper", "term", "exp-prime"}});
        table.add(new Object[]{"exp-prime", "minus", new String[]{"add-oper", "term", "exp-prime"}});
        table.add(new Object[]{"exp-prime", "SEMICOLON", new String[]{}});
        table.add(new Object[]{"exp-prime", "RPAREN", new String[]{}});
        table.add(new Object[]{"term", "NAME", new String[]{"factor", "term-prime"}});
        table.add(new Object[]{"term", "integer_VALUE", new String[]{"factor", "term-prime"}});
        table.add(new Object[]{"term", "real_VALUE", new String[]{"factor", "term-prime"}});
        table.add(new Object[]{"term", "LPAREN", new String[]{"factor", "term-prime"}});
        table.add(new Object[]{"term-prime", "mult", new String[]{"mul-oper", "factor", "term-prime"}});
        table.add(new Object[]{"term-prime", "div", new String[]{"mul-oper", "factor", "term-prime"}});
        table.add(new Object[]{"term-prime", "div2", new String[]{"mul-oper", "factor", "term-prime"}});
        table.add(new Object[]{"term-prime", "mod", new String[]{"mul-oper", "factor", "term-prime"}});
        table.add(new Object[]{"term-prime", "SEMICOLON", new String[]{}});
        table.add(new Object[]{"term-prime", "RPAREN", new String[]{}});
        table.add(new Object[]{"term-prime", "plus", new String[]{}});
        table.add(new Object[]{"term-prime", "minus", new String[]{}});
        table.add(new Object[]{"factor", "LPAREN", new String[]{"LPAREN", "exp", "RPAREN"}});
        table.add(new Object[]{"factor", "NAME", new String[]{"name-value"}});
        table.add(new Object[]{"factor", "integer_VALUE", new String[]{"name-value"}});
        table.add(new Object[]{"factor", "real_VALUE", new String[]{"name-value"}});
        table.add(new Object[]{"name-value", "NAME", new String[]{"NAME"}});
        table.add(new Object[]{"name-value", "integer_VALUE", new String[]{"integer_VALUE"}});
        table.add(new Object[]{"name-value", "real_VALUE", new String[]{"real_VALUE"}});
        table.add(new Object[]{"add-oper", "plus", new String[]{"plus"}});
        table.add(new Object[]{"add-oper", "minus", new String[]{"minus"}});
        table.add(new Object[]{"mul-oper", "mult", new String[]{"mult"}});
        table.add(new Object[]{"mul-oper", "div", new String[]{"div"}});
        table.add(new Object[]{"mul-oper", "div2", new String[]{"div2"}});
        table.add(new Object[]{"mul-oper", "mod", new String[]{"mod"}});
        table.add(new Object[]{"read-stmt", "readint", new String[]{"readint", "LPAREN", "name-list", "RPAREN"}});
        table.add(new Object[]{"read-stmt", "readreal", new String[]{"readreal", "LPAREN", "name-list", "RPAREN"}});
        table.add(new Object[]{"read-stmt", "readchar", new String[]{"readchar", "LPAREN", "name-list", "RPAREN"}});
        table.add(new Object[]{"read-stmt", "readln", new String[]{"readln"}});
        table.add(new Object[]{"write-stmt", "writeint", new String[]{"writeint", "LPAREN", "write-list", "RPAREN"}});
        table.add(new Object[]{"write-stmt", "writereal", new String[]{"writereal", "LPAREN", "write-list", "RPAREN"}});
        table.add(new Object[]{"write-stmt", "writechar", new String[]{"writechar", "LPAREN", "write-list", "RPAREN"}});
        table.add(new Object[]{"write-stmt", "writeln", new String[]{"writeln"}});
        table.add(new Object[]{"write-list", "NAME", new String[]{"write-item", "more-write-value"}});
        table.add(new Object[]{"write-list", "integer_VALUE", new String[]{"write-item", "more-write-value"}});
        table.add(new Object[]{"write-list", "real_VALUE", new String[]{"write-item", "more-write-value"}});
        table.add(new Object[]{"more-write-value", "COMMA", new String[]{"COMMA", "write-list"}});
        table.add(new Object[]{"more-write-value", "RPAREN", new String[]{}});
        table.add(new Object[]{"write-item", "NAME", new String[]{"NAME"}});
        table.add(new Object[]{"write-item", "integer_VALUE", new String[]{"integer_VALUE"}});
        table.add(new Object[]{"write-item", "real_VALUE", new String[]{"real_VALUE"}});
        table.add(new Object[]{"if-stmt", "if", new String[]{"if", "condition", "then", "stmt-list", "else-part", "end", "SEMICOLON"}});
        table.add(new Object[]{"else-part", "else", new String[]{"else", "stmt-list"}});
        table.add(new Object[]{"else-part", "end", new String[]{}});
        table.add(new Object[]{"while-stmt", "while", new String[]{"while", "condition", "do", "stmt-list", "end"}});
        table.add(new Object[]{"loop-stmt", "loop", new String[]{"loop", "stmt-list", "until", "condition"}});
        table.add(new Object[]{"exit-stmt", "exit", new String[]{"exit"}});
        table.add(new Object[]{"call-stmt", "call", new String[]{"call", "NAME"}});
        table.add(new Object[]{"condition", "NAME", new String[]{"name-value", "relational-oper", "name-value"}});
        table.add(new Object[]{"condition", "integer_VALUE", new String[]{"name-value", "relational-oper", "name-value"}});
        table.add(new Object[]{"condition", "real_VALUE", new String[]{"name-value", "relational-oper", "name-value"}});
        table.add(new Object[]{"relational-oper", "LT", new String[]{"LT"}});
        table.add(new Object[]{"relational-oper", "LE", new String[]{"LE"}});
        table.add(new Object[]{"relational-oper", "GT", new String[]{"GT"}});
        table.add(new Object[]{"relational-oper", "GE", new String[]{"GE"}});
        table.add(new Object[]{"relational-oper", "EQUAL", new String[]{"EQUAL"}});
        table.add(new Object[]{"relational-oper", "NOTEQUAL", new String[]{"NOTEQUAL"}});
        table.add(new Object[]{"value", "integer_VALUE", new String[]{"integer_VALUE"}});
        table.add(new Object[]{"value", "real_VALUE", new String[]{"real_VALUE"}});
    }




    public void parse() throws ParseException {
        stack.push("module-decl");
        Tokenval token = lexer.peek(); // get the first token

        while (true) { // loop until the stack is empty
            String str = stack.pop();
            if (notTerminal(str)) { // if the string is a non terminal
                String[] production = getProduction(str, token.tokenType); // get the production
                if (production != null) {
                    if (production.length>0) {
                        for (int i = production.length - 1; i >= 0; i--) {
                            stack.push(production[i]); // push the production to the stack
                        }
                    }
                    else {
                        continue;
                    }
                } else if (hasLambda(str)) { // if the non terminal has lambda
                    continue;
                } else {
                    throw new ParseException("Unexpected " + token + " for the non terminal " + str + " at Line : " + token.line,0);
                }
            } else if (terminal(str)) { // if the string is a terminal
                if (str.equals(token.tokenType)) {
                    token = lexer.nextToken();
                } else {
                    throw new ParseException("Error: Expect " + str + " instead of " + token + " at Line : " + token.line,0);
                }
            }
            if (stack.isEmpty())
                break;
        }

        if (!"EOF".equals(token.tokenType)) { // if the token is not EOF
            throw new ParseException("Error at line " + token.line,0);
        }
    }

    private String[] getProduction(String nonTerminal, String tokenType) { // get the production of the non terminal
        for (int i =0;i<table.size();i++) {
            if (table.get(i)[0].equals(nonTerminal) && table.get(i)[1].equals(tokenType)) {
                return (String[]) table.get(i)[2];
            }
        }
        return null;
    }

    private boolean hasLambda(String nonTerminal) { // check if the non terminal has lambda
        for (int i =0;i<table.size();i++) {
            if (table.get(i)[0].equals(nonTerminal) && table.get(i)[1].equals("λ")) {
                return true;
            }
        }
        return false;
    }

    private boolean notTerminal(String symbol) { // check if the symbol is a non terminal
        return symbol.matches("module-decl|module-heading|block|declarations|const-decl|const-list|var-decl|var-list|var-item|name-list|more-names|data-type|procedure-decl|procedure-heading|stmt-list|statement|ass-stmt|exp|exp-prime|term|term-prime|factor|add-oper|mul-oper|read-stmt|write-stmt|write-list|more-write-value|write-item|if-stmt|else-part|while-stmt|loop-stmt|exit-stmt|call-stmt|condition|relational-oper|value|name-value");
    }
    private boolean terminal(String symbol) {
        return terminalSymbols.contains(symbol);
    }
}

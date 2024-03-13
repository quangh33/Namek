package com.quangh.namek;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        try {
            while (!isAtEnd()) {
                statements.add(statement());
            }
            return statements;
        } catch (ParseError err) {
            return List.of();
        }
    }

    private ParseError error(Token token, String message) {
        Namek.error(token, message);
        return new ParseError();
    }

    // statement -> printStatement | expressionStatement;
    private Stmt statement() {
        if (match(TokenType.PRINT)) {
            return printStatement();
        }
        return expressionStatement();
    }

    // expressionStatement -> expression ";";
    private Stmt expressionStatement() {
        Expr value = expression();
        if (!check(TokenType.SEMICOLON)) {
            throw error(peek(), "Expect ';' after expression.");
        }
        advance();
        return new Stmt.Expression(value);
    }

    // printStatement -> "print" expression ";";
    private Stmt printStatement() {
        Expr value = expression();
        if (!check(TokenType.SEMICOLON)) {
            throw error(peek(), "Expect ';' after expression.");
        }
        advance();
        return new Stmt.Print(value);
    }

    // primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")";
    private Expr primary() {
        if (match(TokenType.FALSE)) return new Expr.Literal(false);
        if (match(TokenType.TRUE)) return new Expr.Literal(true);
        if (match(TokenType.NIL)) return new Expr.Literal(null);
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expr.Literal(prev().literal);
        }

        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            if (!check(TokenType.RIGHT_PAREN)) {
                throw error(peek(), "Expect ')' after expression.");
            }
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    // unary -> ( "!" | "-" ) unary | primary ;
    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token op = prev();
            Expr right = unary();
            return new Expr.Unary(op, right);
        }

        return primary();
    }

    // factor -> unary ( ( "/" | "*" ) unary )* ;
    private Expr factor() {
        Expr left = unary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token op = prev();
            Expr right = unary();
            left = new Expr.Binary(left, op, right);
        }
        return left;
    }

    // term -> factor ( ( "+" | "-" ) factor )* ;
    private Expr term() {
        Expr left = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token op = prev();
            Expr right = factor();
            left = new Expr.Binary(left, op, right);
        }
        return left;
    }

    // comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    private Expr comparison() {
        Expr left = term();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token op = prev();
            Expr right = term();
            left = new Expr.Binary(left, op, right);
        }
        return left;
    }

    // equality -> comparison ( ( "!=" | "==" ) comparison )* ;
    private Expr equality() {
        Expr left = comparison();
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token op = prev();
            Expr right = term();
            left = new Expr.Binary(left, op, right);
        }
        return left;
    }

    private Expr expression() {
        return equality();
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return prev();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token prev() {
        return tokens.get(current - 1);
    }
}

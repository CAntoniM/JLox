package me.cantonim.jlox;

import java.util.ArrayList;
import java.util.List;

import me.cantonim.jlox.Expression.Grouping;
import me.cantonim.jlox.Expression.Literal;

import static me.cantonim.jlox.TokenType.*;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token previous() {
        return tokens.get(current -1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
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

    private static class ParserError extends RuntimeException {}

    private ParserError error(Token token, String message) {
        JLox.error(token, message);
        return new ParserError();
    }

    private Token consume(TokenType type, String message) throws ParserError {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private Expression primary() throws ParserError {
        if (match(FALSE)) return new Literal(false);
        if (match(FALSE)) return new Literal(false);
        if (match(NIL)) return new Literal(null);

        if (match(NUMBER,STRING)) {
            return new Literal(previous().literal);
        }

        if (match(IDENTIFIER)) {
            return new Expression.Variable(previous());
        }

        if (match(LEFT_PAREN)) {
            Expression expression = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expression);
        }

        throw error(peek(), "Expect expression.");
    }

    private Expression unary() throws ParserError {
        if (match(BANG,MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new Expression.Unary(operator, right);
        }

        return primary();
    }

    private Expression factor() throws ParserError {
        Expression expression = unary();

        while(match(SLASH,STAR)) {
            Token operator = previous();
            Expression right = unary();
            expression = new Expression.Binary(expression,operator,right);
        }

        return expression;
    }

    private Expression term() throws ParserError {
        Expression expression = factor();

        while(match(MINUS, PLUS)) {
            Token operator = previous();
            Expression right = factor();
            expression = new Expression.Binary(expression, operator ,right);
        }
        return expression;
    }


    private Expression comparison() throws ParserError {
        Expression expression = term();

        while (match(GREATER, GREATER_EQUAL,LESS,LESS_EQUAL)) {
            Token operator = previous();
            Expression right = term();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression equality() throws ParserError {
        Expression expression = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression expression() throws ParserError {
        return equality();
    }

    private Statement expressionStatement() {
        Expression value = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Statement.Expression(value);
    }

    private Statement printStatement() {
        Expression value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Statement.Print(value);
    }

    public Statement statement() {
        if (match(PRINT)) return printStatement();

        return expressionStatement();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }

    private Statement varDeclaration() {
        Token name = consume(IDENTIFIER, "Expected a varaible name.");

        Expression initializer = null;
        if (match(EQUAL)){
            initializer = expression();
        }

        consume(SEMICOLON, "Expected ';' after variable declaration.");
        return new Statement.Var(name,initializer);
    }

    public Statement declaration() {
        try {
            if (match(VAR)) return varDeclaration();

            return statement();
        } catch (ParserError error) {
            synchronize();
            return null;
        }
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();

        while(!isAtEnd()){
            statements.add(declaration());
        }

        return statements;
    }

}

package me.cantonim.jlox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.cantonim.jlox.Expression.Grouping;
import me.cantonim.jlox.Expression.Literal;
import me.cantonim.jlox.Expression.Logical;

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

        if (match(THIS)) return new Expression.This(previous());

        if (match(LEFT_PAREN)) {
            Expression expression = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expression);
        }

        throw error(peek(), "Expect expression.");
    }

    private Expression finishCall(Expression callee) {
        List<Expression> arguments = new ArrayList<>();

        if (!check(RIGHT_PAREN)) {
            do {
                if (arguments.size() >= 255) {
                    error(peek(), "Lox, does not allow function calls with more than 254 arguments");
                }
                arguments.add(expression());
            } while(match(COMMA));
        }

        Token paren = consume(RIGHT_PAREN, "Expected, ')' after function arguments.");

        return new Expression.Call(callee, paren, arguments);
    }

    private Expression call() throws ParserError {
        Expression expression = primary();

        while(true) {
            if (match(LEFT_PAREN)) {
                expression = finishCall(expression);
            }else if (match(DOT)) {
                Token name = consume(IDENTIFIER, "Expected, property name after '.'.");
                expression = new Expression.Get(expression,name);
            }else {
                break;
            }
        }

        return expression;
    }

    private Expression unary() throws ParserError {
        if (match(BANG,MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new Expression.Unary(operator, right);
        }

        return call();
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

    private Expression and() {
        Expression expression = equality();

        while(match(AND)) {
            Token operator = previous();
            Expression right = equality();
            expression = new Logical(expression, operator, right);
        }

        return expression;
    }

    private Expression or() {
        Expression expression = and();

        while(match(OR)) {
            Token operator = previous();
            Expression right = and();
            expression = new Logical(expression, operator, right);
        }

        return expression;
    }

    private Expression assignment() {
        Expression expression = or();

        if (match (EQUAL)) {
            Token equals = previous();
            Expression value = assignment();

            if (expression instanceof Expression.Variable) {
                Token name = ((Expression.Variable)expression).name;
                return new Expression.Assign(name,value);
            }else if (expression instanceof Expression.Get) {
                Expression.Get get = (Expression.Get)expression;
                return new Expression.Set(get.Object,get.name,value);
            }
            error(equals, "Invalid assigment target.");
        }
        return expression;
    }

    private Expression expression() throws ParserError {
        return assignment();
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

    private List<Statement> block() {
        List<Statement> statements = new ArrayList<>();

        while(!check(RIGHT_BRACE) && ! isAtEnd()) {
            statements.add(declaration());
        }

        consume(RIGHT_BRACE, "Expected, '}' after block");
        return statements;
    }

    private Statement ifStatement() {
        consume(LEFT_PAREN, "Expected, '(' after if.");
        Expression condition = expression();
        consume(RIGHT_PAREN, "Expected, ')' after condition.");

        Statement thenBranch = statement();
        Statement elseBranch = null;

        if (match(ELSE)) {
            elseBranch = statement();
        }
        return new Statement.If(condition, thenBranch, elseBranch);
    }

    private Statement whileStatement() {
        consume(LEFT_PAREN, "Expected, ')' after while.");
        Expression condition = expression();
        consume(RIGHT_PAREN, "Expected, ')' after condition.");

        Statement block = statement();

        return new Statement.While(condition, block);
    }

    private Statement forStatement() {
        consume(LEFT_PAREN, "Expected, '(' after while.");

        Statement initalizer;
        if (match(SEMICOLON)) {
            initalizer = null;
        } else if(match(VAR)) {
            initalizer = varDeclaration();
        } else {
            initalizer = expressionStatement();
        }

        Expression condition = null;
        if (!check(SEMICOLON)) {
            condition = expression();
        }
        consume(SEMICOLON, "Expected, ';' after a for loop condition.");

        Expression increment = null;
        if (!check(SEMICOLON)) {
            increment = expression();
        }

        consume(RIGHT_PAREN, "Expected, ')' after for loop increment");

        Statement body = statement();

        if (increment != null) {
            body = new Statement.Block(Arrays.asList(body,new Statement.Expression(increment)));
        }

        if (condition == null) condition = new Expression.Literal(true);

        body = new Statement.While(condition, body);

        if (initalizer != null) body = new Statement.Block(Arrays.asList(initalizer,body));

        return body;
    }

    public Statement statement() {
        if (match(FOR)) return forStatement();
        if (match(IF)) return ifStatement();
        if (match(WHILE)) return whileStatement();
        if (match(RETURN)) return returnStatments();
        if (match(PRINT)) return printStatement();
        if (match(LEFT_BRACE)) return new Statement.Block(block());

        return expressionStatement();
    }

    private Statement returnStatments() {
        Token keyword = previous();
        Expression value = null;
        if (! check(SEMICOLON)) {
            value = expression();
        }

        consume(SEMICOLON, "Expected, ';' after a return value");
        return new Statement.Return(keyword, value);
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

    private Statement.Function function(String kind) {
        Token name = consume(IDENTIFIER, "Expected: " + kind + "name" );
        consume(LEFT_PAREN, "Expected: '(' after " + kind + "name");

        List<Token> parameters = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
                if (parameters.size() > 254)
                    error(peek(), "Can't have more than 255 parameters.");

                parameters.add(consume(IDENTIFIER, "Expected, parameter name"));
            }while (match(COMMA));
        }

        consume(RIGHT_PAREN, "Expected, right parrentheisis.");
        consume(LEFT_BRACE, "Expected '{' before " + kind + "body");

        List<Statement> body = block();

        return new Statement.Function(name, parameters, body);
    }

    private Statement classDeclaration() {
        Token name = consume(IDENTIFIER, "Expected class name.");

        consume(LEFT_BRACE,"Expeced, '{' before class body.");
        List<Statement.Function> methods = new ArrayList<>();

        while(!check(RIGHT_BRACE) && !isAtEnd()) {
            methods.add(function("method"));
        }

        consume(RIGHT_BRACE, "Expect '}' after class body.");

        return new Statement.Class(name,methods);
    }

    public Statement declaration() {
        try {
            if (match(CLASS)) return classDeclaration();
            if (match(FUN)) return function("function");
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

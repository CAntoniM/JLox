package me.cantonim.jlox;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int line = 1;
    private int start = 0;
    private int current = 0;
    private static final int next=1;

    public Scanner(String source) {
        this.source = source;
    }

    private boolean isAtEnd () {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type, Object litteral) {
        tokens.add(new Token(type,source.substring(start,current), litteral, line));
    }

    private void addToken(TokenType type) {
        addToken(type,null);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek( int ahead_by) {
        if (isAtEnd() || current + ahead_by + 1 >= source.length()) return '\0';
        return source.charAt(current);
    }

    private char peek() {
        return peek(0);
    }

    private void string() {
        while(peek() != '"' && ! isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            JLox.error(line, "Unterminated String");
            return;
        }

        advance();
        String value = source.substring(start + 1, current -1 );
        addToken(TokenType.STRING, value);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while(isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peek(next))) {
            advance();
        }

        while(isDigit(peek())) advance();

        addToken(TokenType.NUMBER,source.substring(start, current));
    }

    private void scanToken() {
        char c = advance();

        switch (c) {
            //whitespace
            case '\n':
                line++;
            case '\r':
            case '\t':
            case ' ' :
            break;

            //single character tokens
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;

            //multi character tokens
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if (match('/')) {
                    while(peek() != '\n' && !isAtEnd()) advance();
                }else{
                    addToken(TokenType.SLASH);
                }
                break;

            case '"':
                string();
                break;
            //error
            default:
                if (isDigit(c)) {
                    number();
                }else {
                    JLox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }
}

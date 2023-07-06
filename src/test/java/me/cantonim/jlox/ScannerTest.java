package me.cantonim.jlox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ScannerTest {

    /**
     * A Test for the scanner to ensure that it can parse all of the relavent
     * digit forms and ignore the invalid forms.
     */
    @Test
    public void digitTest() {

        int number_of_cases = 4;

        Token results[][] = {
            {
                new Token(TokenType.NUMBER,"1", 1.0 ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.NUMBER,"999999", 999999,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.NUMBER,"0.0000001", 0.0000001,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.NUMBER,"1234.1234", 1234.1234,1),
                new Token(TokenType.EOF, "", null, 1)
            }
        };

        String cases[] = {
            "1",
            "999999",
            "0.0000001",
            "1234.1234",
        };

        for (int test_index = 0; test_index < number_of_cases; test_index++) {
            List<Token> expect_result = Arrays.asList(results[test_index]);

            Scanner scanner = new Scanner(cases[test_index]);
            List<Token> tokens = scanner.scanTokens();

            assertEquals(expect_result.size(), tokens.size());

            for (int index = 0; index < expect_result.size(); index ++) {
                Token expected = expect_result.get(index);
                Token test = tokens.get(index);

                assertTrue("Ensuring that neither of the test conditions are null", (expected != null && test != null));

                assertEquals(expected.type, test.type);
                assertEquals(expected.lexeme,test.lexeme );
            }
        }
    }

    /**
     * A Test for the scanner to ensure that it can parse all of the relavent single digit tokens.
     */
    @Test
    public void singleCharTest() {

        int number_of_cases = 1;

        Token results[][] = {
            {
                new Token(TokenType.LEFT_PAREN,"(", null ,1),
                new Token(TokenType.RIGHT_PAREN,")", null ,1),
                new Token(TokenType.LEFT_BRACE,"{", null ,1),
                new Token(TokenType.RIGHT_BRACE,"}", null ,1),
                new Token(TokenType.COMMA,",", null ,1),
                new Token(TokenType.DOT,".", null ,1),
                new Token(TokenType.MINUS,"-", null ,1),
                new Token(TokenType.PLUS,"+", null ,1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.STAR,"*", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
        };

        String cases[] = {
            "(){},.-+;*",
        };

        for (int test_index = 0; test_index < number_of_cases; test_index++) {
            List<Token> expect_result = Arrays.asList(results[test_index]);

            Scanner scanner = new Scanner(cases[test_index]);
            List<Token> tokens = scanner.scanTokens();

            assertEquals(expect_result.size(), tokens.size());

            for (int index = 0; index < expect_result.size(); index ++) {
                Token expected = expect_result.get(index);
                Token test = tokens.get(index);

                assertTrue("Ensuring that neither of the test conditions are null", (expected != null && test != null));

                assertEquals(expected.type, test.type);
                assertEquals(expected.lexeme,test.lexeme );
            }
        }

    }

    /**
     * A Test for the scanner to ensure that it can parse all of the tokens that can be a single or dual char tokens
     */
    @Test
    public void dualCharTest() {

        int number_of_cases = 4;

        Token results[][] = {
            {
                new Token(TokenType.EQUAL,"=", null ,1),
                new Token(TokenType.BANG,"!", null ,1),
                new Token(TokenType.BANG_EQUAL,"!=", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.EQUAL, "=", null, 1),
                new Token(TokenType.EQUAL_EQUAL, "==", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.LESS, "<", null, 1),
                new Token(TokenType.GREATER, ">", null, 1),
                new Token(TokenType.LESS_EQUAL, "<=", null, 1),
                new Token(TokenType.GREATER_EQUAL, ">=", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.SLASH, "/", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
        };

        String cases[] = {
            "=!!=",
            "= ==",
            "<> <= >=",
            "/"
        };

        for (int test_index = 0; test_index < number_of_cases; test_index++) {
            List<Token> expect_result = Arrays.asList(results[test_index]);

            Scanner scanner = new Scanner(cases[test_index]);
            List<Token> tokens = scanner.scanTokens();

            assertEquals(expect_result.size(), tokens.size());

            for (int index = 0; index < expect_result.size(); index ++) {
                Token expected = expect_result.get(index);
                Token test = tokens.get(index);

                assertTrue("Ensuring that neither of the test conditions are null", (expected != null && test != null));

                assertEquals(expected.type, test.type);
                assertEquals(expected.lexeme,test.lexeme );
            }
        }

    }

    @Test
    public void keywordsTest() {
        int number_of_cases = 4;

        Token results[][] = {
            {
                new Token(TokenType.AND, "and", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.CLASS, "class", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.ELSE, "else", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.FALSE, "false", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.FOR, "for", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.FUN, "fun", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.IF, "if", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.NIL, "nil", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.OR, "or", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.PRINT, "print", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.RETURN, "return", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.SUPER, "super", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.THIS, "this", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.TRUE, "true", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR, "var", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.WHILE, "while", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.WHILE, "while", null, 1),
                new Token(TokenType.VAR, "var", null, 1),
                new Token(TokenType.TRUE, "true", null, 1),
                new Token(TokenType.THIS, "this", null, 1),
                new Token(TokenType.RETURN, "return", null, 1),
                new Token(TokenType.PRINT, "print", null, 1),
                new Token(TokenType.OR, "or", null, 1),
                new Token(TokenType.IF, "if", null, 1),
                new Token(TokenType.NIL, "nil", null, 1),
                new Token(TokenType.FUN, "fun", null, 1),
                new Token(TokenType.FOR, "for", null, 1),
                new Token(TokenType.FALSE, "false", null, 1),
                new Token(TokenType.ELSE, "else", null, 1),
                new Token(TokenType.CLASS, "class", null, 1),
                new Token(TokenType.AND, "and", null, 1),
                new Token(TokenType.EOF, "", null, 1)
            },
        };

        String cases[] = {
            "and",
            "class",
            "else",
            "false",
            "for",
            "fun",
            "if",
            "nil",
            "or",
            "print",
            "return",
            "super",
            "this",
            "true",
            "var",
            "while",
            "while var true this super return print or nil if fun for false else class and"
        };

        for (int test_index = 0; test_index < number_of_cases; test_index++) {
            List<Token> expect_result = Arrays.asList(results[test_index]);

            Scanner scanner = new Scanner(cases[test_index]);
            List<Token> tokens = scanner.scanTokens();

            assertEquals(expect_result.size(), tokens.size());

            for (int index = 0; index < expect_result.size(); index ++) {
                Token expected = expect_result.get(index);
                Token test = tokens.get(index);

                assertTrue("Ensuring that neither of the test conditions are null", (expected != null && test != null));

                assertEquals(expected.type, test.type);
                assertEquals(expected.lexeme,test.lexeme );
            }
        }
    }
}

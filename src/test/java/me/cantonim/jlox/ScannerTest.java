package me.cantonim.jlox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        int number_of_cases = 17;

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
                new Token(TokenType.SUPER, "super", null, 1),
                new Token(TokenType.RETURN, "return", null, 1),
                new Token(TokenType.PRINT, "print", null, 1),
                new Token(TokenType.OR, "or", null, 1),
                new Token(TokenType.NIL, "nil", null, 1),
                new Token(TokenType.IF, "if", null, 1),
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

    @Test
    public void stringTest() {
        int number_of_cases = 3;

        Token results[][] = {
            {
                new Token(TokenType.STRING,"\"test\"", "test" ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.STRING,"\"This is a test\n foo bar\"","This is a test\n foo bar" ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.STRING,"\"test\"", "test" ,1),
                new Token(TokenType.STRING,"\"test\"", "test" ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
        };

        String cases[] = {
            "\"test\"",
            "\"This is a test\n foo bar\"",
            "\"test\" \"test\"",
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
                assertEquals(expected.lexeme,test.lexeme);
            }
        }
    }

    @Test
    public void declarionsTest() {
        int number_of_cases = 10;

        Token results[][] = {
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.STRING,"\"baz\"","baz",1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"bar", "bar" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.NUMBER,"12",12d,1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"baz", "baz" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.NUMBER,"12.12",12.12d,1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"boo", "boo" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.TRUE,"true",null,1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"boo", "boo" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.FALSE,"false",null,1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.LEFT_PAREN,"(",null,1),
                new Token(TokenType.NUMBER,"25",25d,1),
                new Token(TokenType.SLASH,"/",null,1),
                new Token(TokenType.NUMBER,"5",5d,1),
                new Token(TokenType.RIGHT_PAREN,")",null,1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.STRING,"\"this is a \"","this is a ",1),
                new Token(TokenType.PLUS,"+",null,1),
                new Token(TokenType.STRING,"\"test\"","test",1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.IDENTIFIER,"bar","bar",1),
                new Token(TokenType.STAR,"*",null,1),
                new Token(TokenType.IDENTIFIER,"baz","baz",1),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.EOF, "", null, 1)
            },
            {
                new Token(TokenType.VAR,"var", null ,1),
                new Token(TokenType.IDENTIFIER,"bar", "bar" ,1),
                new Token(TokenType.EQUAL,"=",null,1),
                new Token(TokenType.NUMBER,"2",2d,2),
                new Token(TokenType.SEMICOLON,";", null ,1),
                new Token(TokenType.VAR,"var", null ,2),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,2),
                new Token(TokenType.EQUAL,"=",null,2),
                new Token(TokenType.LEFT_PAREN,"(",null,2),
                new Token(TokenType.NUMBER,"3.12",3.12d,2),
                new Token(TokenType.STAR,"*",null,2),
                new Token(TokenType.NUMBER,"8",8d,2),
                new Token(TokenType.RIGHT_PAREN,")",null,2),
                new Token(TokenType.SLASH,"/",null,2),
                new Token(TokenType.IDENTIFIER,"bar", "bar" ,2),
                new Token(TokenType.SEMICOLON,";", null ,2),
                new Token(TokenType.EOF, "", null, 2)
            },
        };

        String cases[] = {
            "var foo;",
            "var foo = \"baz\";",
            "var bar = 12;",
            "var baz = 12.12;",
            "var boo = true;",
            "var boo = false;",
            "var foo = ( 25 / 5 );",
            "var foo = \"this is a \" + \"test\";",
            "var foo = bar * baz;",
            "var bar = 2;\nvar foo = (3.12 * 8) / bar;"
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
                assertEquals(expected.lexeme,test.lexeme);
            }
        }
    }


    /**
     * Adding a test for the scanning of the functions
     */
    @Test
    public void functionsTest() {

        int number_of_cases = 1;

        Token results[][] = {
            {
                new Token(TokenType.FUN,"fun", null ,1),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,1),
                new Token(TokenType.LEFT_PAREN,"(", null ,1),
                new Token(TokenType.IDENTIFIER,"bar", "bar" ,1),
                new Token(TokenType.COMMA,",", null ,1),
                new Token(TokenType.IDENTIFIER,"baz", "baz" ,1),
                new Token(TokenType.RIGHT_PAREN,")", null ,1),
                new Token(TokenType.LEFT_BRACE,"{", null ,1),
                new Token(TokenType.RETURN,"return", null ,2),
                new Token(TokenType.IDENTIFIER,"bar", "bar" ,2),
                new Token(TokenType.PLUS,"+", null ,2),
                new Token(TokenType.IDENTIFIER,"baz", "baz" ,2),
                new Token(TokenType.SEMICOLON,";",null ,2),
                new Token(TokenType.RIGHT_BRACE,"}", null ,3),
                new Token(TokenType.EOF, "", null, 1)
            },
        };

        String cases[] = {
            "fun foo(bar, baz) {\n\treturn bar + baz; \n}",
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
     * A Test for the scanning of classes to ensure that the tokens are correctly interpreted
     */
    @Test
    public void classesTest() {

        int number_of_cases = 1;

        Token results[][] = {
            {
                new Token(TokenType.CLASS,"class", null ,1),
                new Token(TokenType.IDENTIFIER,"foo", "foo" ,1),
                new Token(TokenType.LEFT_BRACE,"{", null ,1),
                new Token(TokenType.IDENTIFIER,"bar", "bar" ,2),
                new Token(TokenType.LEFT_PAREN,"(", null ,2),
                new Token(TokenType.IDENTIFIER,"a", "a" ,2),
                new Token(TokenType.RIGHT_PAREN,")", null ,2),
                new Token(TokenType.LEFT_BRACE,"{", null ,2),
                new Token(TokenType.PRINT,"print", null ,3),
                new Token(TokenType.IDENTIFIER,"a", "a" ,3),
                new Token(TokenType.SEMICOLON,";",null ,3),
                new Token(TokenType.RIGHT_BRACE,"}", null ,4),
                new Token(TokenType.RIGHT_BRACE,"}", null ,5),
                new Token(TokenType.EOF, "", null, 1)
            },
        };

        String cases[] = {
            "class foo {\n\tbar (a) {\n\t\tprint a;\n\t}\n}",
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

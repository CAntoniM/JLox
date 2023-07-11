package me.cantonim.jlox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void Parse() {
        String input[] = {"1 == 1", "2 <= 3", "3 + 2", "5 / 2", "-4", "43", "( 5 * 4 ) / 20"};
        String expected_output[] = {"(== 1.0 1.0)","(<= 2.0 3.0)", "(+ 3.0 2.0)", "(/ 5.0 2.0)", "(- 4.0)", "43.0", "(/ (group (* 5.0 4.0)) 20.0)"};

        assertEquals(input.length,expected_output.length);

        for (int index = 0; index < input.length; index++) {
            Scanner scanner = new Scanner(input[index]);
            List<Token> tokens = scanner.scanTokens();
            AstPrinter printer = new AstPrinter();


            Parser parser = new Parser(tokens);
            Expression expression = parser.parse();

            assertTrue(!JLox.hadError);

            assertEquals(expected_output[index], printer.print(expression));
        }
    }

}

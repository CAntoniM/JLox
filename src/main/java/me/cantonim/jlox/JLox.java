
package me.cantonim.jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class JLox {
    private static final Interpreter interpreter = new Interpreter();
    static public boolean hadError = false;
    static public boolean hadRuntimeError = false;

    private static void report (int line, String where, String message) {
        System.out.println(line + " | Error " + where + ": " + message);
        hadError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }


    public static void error(int line, String message) {
        report(line,"",message);
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
            "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    public static void run(String source) {

        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();

        if (hadError) return;

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        if (hadError) return;

        interpreter.interpret(statements);
    }

    public static void runFile(String file) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get(file));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    public static void runPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            try {
                System.out.print("-> ");

                String line = reader.readLine();

                if(line == null) break;

                run(line);

                hadError = false;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }

    public static void main(String[] args) throws IOException {
        CLI cli = new CLI(args);

        if (cli.file.isPresent()) {
            runFile(cli.file.get());
        }else {
            runPrompt();
        }

    }

}

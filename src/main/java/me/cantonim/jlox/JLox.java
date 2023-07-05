
package me.cantonim.jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class JLox {

    static private boolean hadError = false;

    private static void report (int line, String where, String message) {
        System.out.println(line + " | Error " + where + ": " + message);
        hadError = true;
    }

    public static void error(int line, String message) {
        report(line,"",message);
    }

    public static void run(String source) {

        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public static void runFile(String file) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get(file));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
    }

    public static void runPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {

            System.out.print("-> ");

            String line = reader.readLine();

            if(line == null) break;

            run(line);

            hadError = false;
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

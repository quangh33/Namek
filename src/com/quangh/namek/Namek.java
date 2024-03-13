package com.quangh.namek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Namek {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token t : tokens) {
            System.out.println(t);
        }

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();
        if (expression != null) {
            interpreter.interpret(expression);
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
            return;
        }
        report(token.line, " at '" + token.lexeme + "'", message);
    }

    private static void report(int line, String where, String message) {
        System.out.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void runtimeError(RuntimeErr err) {
        System.out.println("[line " + err.op.line + "] Error: " + err.getMessage());
        hadRuntimeError = true;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, this is Namek programming language interpreter!");
        if (args.length > 1) {
            System.out.println("Usage: Namek [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
}

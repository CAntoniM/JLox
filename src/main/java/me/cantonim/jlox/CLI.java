package me.cantonim.jlox;

import java.util.Optional;

import org.apache.logging.log4j.Level;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class CLI {
    public Level log_level = Level.WARN;
    public Optional<String> file = Optional.empty();

    public CLI(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("jlox").build();

        parser.addArgument("-v","--verbose")
            .choices("ALL","FINEST","FINER","FINE","CONFIG","INFO","WARNING","SEVERE","OFF")
            .setDefault("WARNING")
            .help("Set the level logging used by the application see log4j2 for explaination of log levels");
        parser.addArgument("file")
            .nargs("?")
            .help("The path to the source file to be interpreted");

        Namespace namespace = null;

        try {
            namespace = parser.parseArgs(args);
        } catch (ArgumentParserException exception) {
            parser.handleError(exception);
            System.exit(1);
        }

        switch (namespace.getString("verbose"))  {
            case "ALL":
                log_level = Level.ALL;
                break;
            case "DEBUG":
                log_level = Level.DEBUG;
                break;
            case "ERROR":
                log_level = Level.ERROR;
                break;
            case "FETAL":
                log_level = Level.FATAL;
                break;
            case "INFO":
                log_level = Level.INFO;
                break;
            case "OFF":
                log_level = Level.OFF;
                break;
            case "TRACE":
                log_level = Level.TRACE;
                break;
            case "WARN":
                log_level = Level.WARN;
                break;
        }

        String file_name = namespace.getString("file");

        if (file_name != null) {
            file = Optional.of(file_name);
        }

    }
}

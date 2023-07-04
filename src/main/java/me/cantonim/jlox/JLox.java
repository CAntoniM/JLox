
package me.cantonim.jlox;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


class JLox {

    private String _file;
    private Logger _logger;

    public JLox(String file, Level logLevel) {

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(logLevel);
        ctx.updateLoggers();

        _file = file;
        _logger = LogManager.getRootLogger();
    }

    public static JLox from(String[] args) {

        ArgumentParser parser = ArgumentParsers.newFor("jlox").build();

        parser.addArgument("-v","--verbose")
            .choices("ALL","FINEST","FINER","FINE","CONFIG","INFO","WARNING","SEVERE","OFF")
            .setDefault("WARNING")
            .help("Set the level logging used by the application see log4j2 for explaination of log levels");
        parser.addArgument("file")
            .nargs(1)
            .help("The path to the source file to be interpreted");

        Namespace namespace = null;

        try {
            namespace = parser.parseArgs(args);
        } catch (ArgumentParserException exception) {
            parser.handleError(exception);
            System.exit(1);
        }

        String file = namespace.getString("file");
        Level level = null;
        switch (namespace.getString("verbose"))  {
            case "ALL":
                level = Level.ALL;
                break;
            case "DEBUG":
                level = Level.DEBUG;
                break;
            case "ERROR":
                level = Level.ERROR;
                break;
            case "FETAL":
                level = Level.FATAL;
                break;
            case "INFO":
                level = Level.INFO;
                break;
            case "OFF":
                level = Level.OFF;
                break;
            case "TRACE":
                level = Level.TRACE;
                break;
            case "WARN":
                level = Level.WARN;
                break;
        }

        return new JLox(file,level);

    }

    public static void main(String[] args) {
        JLox interpreter = JLox.from(args);

    }

}

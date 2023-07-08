import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Generate {

    private static void defineType( PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    static class " + className + " extends " + baseName + " {");
        writer.println("        " + className + "(" + fieldList + ") {");

        String feilds[] = fieldList.split(", ");
        for (String feild : feilds) {
            System.out.println(feild);
            String name = feild.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }

        writer.println("        }");

        writer.println();

        for (String feild: feilds) {
            writer.println("        public final " + feild + ";");
        }

        writer.println("    }");
    }

    private static void defineAst( String outputDir, String baseName, List<String> types) throws IOException {

        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package me.cantonim.jlox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("public abstract class " + baseName + " {");

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String feilds = type.split(":")[1].trim();
            defineType(writer, baseName, className, feilds);
        }

        writer.println("}");
        writer.close();

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }

        String outputDir = args[0];

        defineAst(outputDir, "Expression", Arrays.asList(
            "Binary   : Expression left, Token operator, Expression right",
            "Grouping : Expression expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expression right"
        ));
    }
}

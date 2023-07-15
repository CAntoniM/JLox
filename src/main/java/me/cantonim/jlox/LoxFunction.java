package me.cantonim.jlox;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Statement.Function declaration;

    public LoxFunction(Statement.Function declaration) {
        this.declaration = declaration;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Enviroment env = new Enviroment(interpreter.globals);
        for(int i = 0; i < declaration.params.size(); i++) {
            env.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        interpreter.executeBlock(declaration.body,env);
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

}

package me.cantonim.jlox;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Statement.Function declaration;
    private final Enviroment closure;
    private final boolean isInitalizer;

    public LoxFunction(Statement.Function declaration, Enviroment closure, boolean is) {
        this.closure = closure;
        this.declaration = declaration;
        this.isInitalizer = is;
    }

    LoxFunction bind(LoxInstance instance) {
        Enviroment enviroment = new Enviroment(closure);
        enviroment.define("this",instance);
        return new LoxFunction(declaration,enviroment,isInitalizer);
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Enviroment env = new Enviroment(closure);
        for(int i = 0; i < declaration.params.size(); i++) {
            env.define(declaration.params.get(i).lexeme, arguments.get(i));
        }
        try {
            interpreter.executeBlock(declaration.body,env);
        } catch (Return returnValue) {
            if (isInitalizer) return closure.getAt(0, "this");

            return returnValue.value;
        }

        if (isInitalizer) return closure.getAt(0, "this");

        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

}

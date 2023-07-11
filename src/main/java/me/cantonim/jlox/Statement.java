package me.cantonim.jlox;

import java.util.List;

public abstract class Statement {

    interface Visitor<R> {
        R visitExpressionStatement(Expression statement);
        R visitPrintStatement(Print statement);
        R visitVarStatement(Variable statement);
    }

    static class Expression extends Statement {
        Expression(me.cantonim.jlox.Expression expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStatement(this);
        }
        public final me.cantonim.jlox.Expression expression;
    }

    static class Print extends Statement {
        Print(me.cantonim.jlox.Expression expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStatement(this);
        }
        public final me.cantonim.jlox.Expression expression;
    }

    static class Variable extends Statement {
        Variable(Token name, Expression initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStatement(this);
        }
        public final Token name;
        public final Expression initializer;
    }

    abstract <R> R accept( Visitor<R> visitor);
}

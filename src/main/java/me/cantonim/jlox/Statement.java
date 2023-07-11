package me.cantonim.jlox;

import java.util.List;

public abstract class Statement {

    interface Visitor<R> {
        R visitExpressionStatement(Expression statement);
        R visitPrintStatement(Print statement);
    }

    static class Expression extends Statement {
        Expression(me.cantonim.jlox.Expression value) {
            this.expression = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStatement(this);
        }
        public final me.cantonim.jlox.Expression expression;
    }

    static class Print extends Statement {
        Print(me.cantonim.jlox.Expression value) {
            this.expression = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStatement(this);
        }
        public final me.cantonim.jlox.Expression expression;
    }

    abstract <R> R accept( Visitor<R> visitor);
}

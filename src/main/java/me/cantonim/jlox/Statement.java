package me.cantonim.jlox;

import java.util.List;

public abstract class Statement {

    interface Visitor<R> {
        R visitBlockStatement(Block statement);
        R visitExpressionStatement(Expression statement);
        R visitIfStatement(If statement);
        R visitPrintStatement(Print statement);
        R visitVarStatement(Var statement);
    }
    static class Block extends Statement {
        Block(List<Statement> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStatement(this);
        }

        public final List<Statement> statements;
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

    static class If extends Statement {
        If(me.cantonim.jlox.Expression expression, Statement thenBranch, Statement elseBranch) {
            this.expression = expression;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStatement(this);
        }

        public final me.cantonim.jlox.Expression expression;
        public final Statement thenBranch;
        public final Statement elseBranch;
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

    static class Var extends Statement {
        Var(Token name, me.cantonim.jlox.Expression initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStatement(this);
        }

        public final Token name;
        public final me.cantonim.jlox.Expression initializer;
    }


    abstract <R> R accept( Visitor<R> visitor);

}

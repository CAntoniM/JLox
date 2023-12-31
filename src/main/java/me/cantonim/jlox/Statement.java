package me.cantonim.jlox;

import java.util.List;

// This file was generated by CAntoniM/JLox/tools/Generator.java
// if you are making changes to all classes in this file please do via that script
// otherwise please consider if you need to make the changes at all.


public abstract class Statement {

    interface Visitor<R> {
        R visitBlockStatement(Block statement);
        R visitClassStatement(Class statement);
        R visitExpressionStatement(Expression statement);
        R visitFunctionStatement(Function statement);
        R visitIfStatement(If statement);
        R visitPrintStatement(Print statement);
        R visitVarStatement(Var statement);
        R visitWhileStatement(While statement);
        R visitReturnStatement(Return statement);
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

    static class Class extends Statement {
        Class(Token name, List<Statement.Function> methods) {
            this.name = name;
            this.methods = methods;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassStatement(this);
        }

        public final Token name;
        public final List<Statement.Function> methods;
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

    static class Function extends Statement {
        Function(Token name, List<Token> params, List<Statement> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStatement(this);
        }

        public final Token name;
        public final List<Token> params;
        public final List<Statement> body;
    }

    static class If extends Statement {
        If(me.cantonim.jlox.Expression condition, Statement thenBranch, Statement elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStatement(this);
        }

        public final me.cantonim.jlox.Expression condition;
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

    static class While extends Statement {
        While(me.cantonim.jlox.Expression expression, Statement body) {
            this.expression = expression;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStatement(this);
        }

        public final me.cantonim.jlox.Expression expression;
        public final Statement body;
    }

    static class Return extends Statement {
        Return(Token keyword, me.cantonim.jlox.Expression value) {
            this.keyword = keyword;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStatement(this);
        }

        public final Token keyword;
        public final me.cantonim.jlox.Expression value;
    }


    abstract <R> R accept( Visitor<R> visitor);

}

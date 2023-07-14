package me.cantonim.jlox;

import java.util.List;

import me.cantonim.jlox.Expression.Assign;
import me.cantonim.jlox.Expression.Binary;
import me.cantonim.jlox.Expression.Grouping;
import me.cantonim.jlox.Expression.Literal;
import me.cantonim.jlox.Expression.Unary;
import me.cantonim.jlox.Expression.Visitor;
import me.cantonim.jlox.Statement.Block;
import me.cantonim.jlox.Statement.Print;
import me.cantonim.jlox.Statement.Var;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void>{

    private Enviroment environment = new Enviroment();

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    private void execute(Statement stmt) {
        stmt.accept(this);
    }

    void interpret(List<Statement> statements) {
        try {
            for(Statement statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            JLox.runtimeError(error);
        }
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;

        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    @Override
    public Object visitBinaryExpression(Binary expression) {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);

        switch (expression.operator.type) {
            case MINUS:
                checkNumberOperands(expression.operator, left, right);
                return (double)left - (double)right;
            case SLASH:
                checkNumberOperands(expression.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expression.operator, left, right);
                return (double) left * (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                throw new RuntimeError(expression.operator, "Operands must be two numbers or strings.");
            case GREATER:
                checkNumberOperands(expression.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expression.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expression.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expression.operator, left, right);
                return (double)left >= (double)right;
            case BANG_EQUAL: return !isEqual(left,right);
            case EQUAL_EQUAL: return isEqual(left,right);
            default:

        }
        return null;
    }

    @Override
    public Object visitGroupingExpression(Grouping expression) {
        return evaluate(expression.expression);
    }

    @Override
    public Object visitLiteralExpression(Literal expression) {
        return expression.value;
    }

    @Override
    public Object visitUnaryExpression(Unary expression) {
        Object right = evaluate(expression.right);

        switch (expression.operator.type) {
            case MINUS:
                return -(double) right;
            case BANG:
                return !isTruthy(right);
            default:
        }

        return null;
    }

    @Override
    public Void visitExpressionStatement(me.cantonim.jlox.Statement.Expression statement) {
        evaluate(statement.expression);
        return null;
    }

    @Override
    public Void visitPrintStatement(Print statement) {
        Object value = evaluate(statement.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStatement(Var statement) {
        Object value = null;

        if (statement.initializer != null) {
            value = evaluate(statement.initializer);
        }

        environment.define(statement.name.lexeme,value);
        return null;
    }

    @Override
    public Object visitVariableExpression(me.cantonim.jlox.Expression.Variable expression) {
        return environment.get(expression.name);
    }

    @Override
    public Object visitAssignExpression(Assign expression) {
        Object value = evaluate(expression.value);
        environment.assign(expression.name, value);
        return value;
    }

    public Void executeBlock(List<Statement> statements, Enviroment enviroment) {
        Enviroment previous = this.environment;

        try {
            this.environment = enviroment;

            for (Statement statment : statements) {
                execute(statment);
            }
        } finally {
            this.environment = previous;
        }
        return null;
    }

    @Override
    public Void visitBlockStatement(Block statement) {
        return executeBlock(statement.statements, new Enviroment(environment));
    }

}

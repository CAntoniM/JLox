package me.cantonim.jlox;

import me.cantonim.jlox.Expression.Binary;
import me.cantonim.jlox.Expression.Grouping;
import me.cantonim.jlox.Expression.Literal;
import me.cantonim.jlox.Expression.Unary;
import me.cantonim.jlox.Expression.Visitor;

public class Interpreter implements Visitor<Object> {

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

    void interpret(Expression expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
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

}
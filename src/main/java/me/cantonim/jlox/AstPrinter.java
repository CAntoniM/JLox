package me.cantonim.jlox;

import me.cantonim.jlox.Expression.Assign;
import me.cantonim.jlox.Expression.Binary;
import me.cantonim.jlox.Expression.Grouping;
import me.cantonim.jlox.Expression.Literal;
import me.cantonim.jlox.Expression.Logical;
import me.cantonim.jlox.Expression.Unary;
import me.cantonim.jlox.Expression.Variable;
import me.cantonim.jlox.Expression.Visitor;

public class AstPrinter implements Visitor<String>{

    String print(Expression expression) {
        return expression.accept(this);
    }

    private String parenthesize(String name, Expression... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);

        for (Expression expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    @Override
    public String visitBinaryExpression(Binary expression) {
        return parenthesize(expression.operator.lexeme, expression.left, expression.right);
    }

    @Override
    public String visitGroupingExpression(Grouping expression) {
        return parenthesize("group", expression.expression);
    }

    @Override
    public String visitLiteralExpression(Literal expression) {
        if (expression.value == null) return "nil";
        return expression.value.toString();
    }

    @Override
    public String visitUnaryExpression(Unary expression) {
        return parenthesize(expression.operator.lexeme, expression.right);
    }

    @Override
    public String visitVariableExpression(Variable expression) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitVariableExpression'");
    }

    @Override
    public String visitAssignExpression(Assign expression) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitAssignExpression'");
    }

    @Override
    public String visitLogicalExpression(Logical expression) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitLogicalExpression'");
    }
}

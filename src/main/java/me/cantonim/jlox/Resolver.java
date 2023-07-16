package me.cantonim.jlox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import me.cantonim.jlox.Expression.Assign;
import me.cantonim.jlox.Expression.Binary;
import me.cantonim.jlox.Expression.Call;
import me.cantonim.jlox.Expression.Grouping;
import me.cantonim.jlox.Expression.Literal;
import me.cantonim.jlox.Expression.Logical;
import me.cantonim.jlox.Expression.Unary;
import me.cantonim.jlox.Expression.Variable;
import me.cantonim.jlox.Statement.Block;
import me.cantonim.jlox.Statement.Function;
import me.cantonim.jlox.Statement.If;
import me.cantonim.jlox.Statement.Print;
import me.cantonim.jlox.Statement.Return;
import me.cantonim.jlox.Statement.Var;
import me.cantonim.jlox.Statement.While;

public class Resolver implements Expression.Visitor<Void>, Statement.Visitor<Void> {
    private final Interpreter interpreter;
    private final Stack<HashMap<String,Boolean>> scopes = new Stack<>();
    private FunctionType currentFunction = FunctionType.NONE;

    Resolver (Interpreter interpreter){
        this.interpreter = interpreter;
    }

    private void resolve(Statement statement) {
        statement.accept(this);
    }

    private void resolve(Expression expression) {
        expression.accept(this);
    }

    public void resolve(List<Statement> statements) {
        for (Statement statement : statements) {
            resolve(statement);
        }
    }

    private void beginScope() {
        scopes.push(new HashMap<String,Boolean>());
    }

    private void endScope() {
        scopes.pop();
    }

    private void declare(Token name) {
        if (scopes.isEmpty()) return;

        Map<String,Boolean> scope = scopes.peek();
        if (scope.containsKey(name.lexeme)) {
            JLox.error(name, "A Variable with that name is already in scope.");
        }
        scope.put(name.lexeme, false);
    }

    private void define(Token name) {
        if (!scopes.isEmpty()) scopes.peek().put(name.lexeme, true);
    }

    private void resolveLocal(Expression expression, Token name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name.lexeme)) {
                interpreter.resolve(expression, scopes.size());
                return;
            }
        }
    }

    private void resolveFunction(Statement.Function function, FunctionType type) {

        FunctionType enclosingFunction = currentFunction;
        currentFunction = type;

        beginScope();

        for (Token param : function.params) {
            declare(param);
            define(param);
        }

        resolve(function.body);
        endScope();

        currentFunction = enclosingFunction;

    }

    @Override
    public Void visitBlockStatement(Block statement) {
        beginScope();
        resolve(statement.statements);
        endScope();
        return null;
    }

    @Override
    public Void visitExpressionStatement(me.cantonim.jlox.Statement.Expression statement) {
        resolve(statement.expression);
        return null;
    }

    @Override
    public Void visitFunctionStatement(Function statement) {

        declare(statement.name);
        define(statement.name);

        resolveFunction(statement,FunctionType.FUNCTION);

        return null;
    }

    @Override
    public Void visitIfStatement(If statement) {
        resolve(statement.condition);
        resolve(statement.thenBranch);

        if (statement.elseBranch != null) resolve(statement.elseBranch);
        return null;
    }

    @Override
    public Void visitPrintStatement(Print statement) {
        resolve(statement.expression);
        return null;
    }

    @Override
    public Void visitVarStatement(Var statement) {
        declare(statement.name);

        if (statement.initializer != null) {
            resolve(statement.initializer);
        }
        define(statement.name);
        return null;
    }

    @Override
    public Void visitWhileStatement(While statement) {
        resolve(statement.expression);
        resolve(statement.body);

        return null;
    }

    @Override
    public Void visitReturnStatement(Return statement) {

        if (currentFunction == FunctionType.NONE) {
            JLox.error(statement.keyword, "Invalid return: can not return from the top level code");
        }

        if (statement.value != null)
            resolve(statement.value);
        return null;
    }

    @Override
    public Void visitAssignExpression(Assign expression) {
        resolve(expression.value);
        resolveLocal(expression, expression.name);
        return null;
    }

    @Override
    public Void visitBinaryExpression(Binary expression) {
        resolve(expression.left);
        resolve(expression.right);

        return null;
    }

    @Override
    public Void visitGroupingExpression(Grouping expression) {
        resolve(expression.expression);

        return null;
    }

    @Override
    public Void visitCallExpression(Call expression) {
        resolve(expression.callee);

        for(Expression argument : expression.arguments) {
            resolve(argument);
        }

        return null;
    }

    @Override
    public Void visitLiteralExpression(Literal expression) {
        return null;
    }

    @Override
    public Void visitLogicalExpression(Logical expression) {
        resolve(expression.left);
        resolve(expression.right);

        return null;
    }

    @Override
    public Void visitUnaryExpression(Unary expression) {
        resolve(expression.right);
        return null;
    }

    @Override
    public Void visitVariableExpression(Variable expression) {
        if (!scopes.isEmpty() && scopes.peek().get(expression.name.lexeme) == Boolean.FALSE) {
            JLox.error(expression.name, "Can not read local variables in its own initalizer");
        }

        resolveLocal(expression, expression.name);
        return null;
    }
}

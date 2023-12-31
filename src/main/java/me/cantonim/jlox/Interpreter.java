package me.cantonim.jlox;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.cantonim.jlox.Expression.Assign;
import me.cantonim.jlox.Expression.Binary;
import me.cantonim.jlox.Expression.Call;
import me.cantonim.jlox.Expression.Grouping;
import me.cantonim.jlox.Expression.Literal;
import me.cantonim.jlox.Expression.Logical;
import me.cantonim.jlox.Expression.Unary;
import me.cantonim.jlox.Expression.Visitor;
import me.cantonim.jlox.Statement.Block;
import me.cantonim.jlox.Statement.Function;
import me.cantonim.jlox.Statement.If;
import me.cantonim.jlox.Statement.Print;
import me.cantonim.jlox.Statement.Return;
import me.cantonim.jlox.Statement.Var;
import me.cantonim.jlox.Statement.While;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void>{

    final Enviroment globals = new Enviroment();
    private Enviroment environment = globals;
    private final Map<Expression,Integer> locals = new HashMap<>();

    public Interpreter() {
        globals.define("clock", new LoxCallable() {
            @Override
            public int arity() {return 0;}

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis()/1000.0;
            }

            @Override
            public String toString() {
                return "<native function>";
            }
        });
    }

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

    private Object lookUpVariable(Token name, Expression expression) {
        Integer distance = locals.get(expression);

        if (distance != null) {
            return environment.getAt(distance, name.lexeme);
        } else {
            return globals.get(name);
        }
    }

    @Override
    public Object visitVariableExpression(me.cantonim.jlox.Expression.Variable expression) {
        return lookUpVariable(expression.name, expression);
    }

    @Override
    public Object visitAssignExpression(Assign expression) {
        Object value = evaluate(expression.value);

        Integer distance = locals.get(expression);

        if (distance != null) {
            environment.assignAt(distance, expression.name, value);
        } else {
            globals.assign(expression.name, value);
        }

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

    @Override
    public Void visitIfStatement(If statement) {
        if(isTruthy(evaluate(statement.condition))) {
            execute(statement.thenBranch);
        }else {
            execute(statement.elseBranch);
        }
        return null;
    }

    @Override
    public Object visitLogicalExpression(Logical expression) {
        Object left = evaluate(expression);
        if (expression.operator.type == TokenType.OR) {
            if(isTruthy(left)) {
                return left;
            }else if (!isTruthy(left)) {
                return left;
            }
        }

        return evaluate(expression.right);
    }

    @Override
    public Void visitWhileStatement(While statement) {
        while(isTruthy(evaluate(statement.expression))) {
            execute(statement.body);
        }
        return null;
    }

    @Override
    public Object visitCallExpression(Call expression) {
        Object callee = evaluate(expression.callee);

        List<Object> arguments = new ArrayList<>();

        for (Expression argument : expression.arguments) {
            arguments.add(evaluate(argument));
        }

        if( !(callee instanceof LoxCallable)) {
            throw new RuntimeError(expression.Paren, "only functions and classes are callable.");
        }

        LoxCallable function = (LoxCallable)callee;

        if (arguments.size() != function.arity()) {
            throw new RuntimeError(expression.Paren, "Expected " +
                function.arity() + " arguments but got " +
                arguments.size() + ".");
    }
        return function.call(this, arguments);
    }

    @Override
    public Void visitFunctionStatement(Function statement) {
        LoxFunction function = new LoxFunction(statement,environment,false);

        environment.define(statement.name.lexeme, function);
        return null;
    }

    @Override
    public Void visitReturnStatement(Return statement) {
        Object value = null;
        if (statement.value != null) {
            value = evaluate(statement.value);
        }
        throw new me.cantonim.jlox.Return(value);
    }

    public void resolve(Expression expression, int depth) {

        locals.put(expression, depth);
    }

    @Override
    public Void visitClassStatement(Statement.Class statement) {
        environment.define(statement.name.lexeme, null);

        Map<String,LoxFunction> methods = new HashMap<>();

        for(Statement.Function method: statement.methods) {
            LoxFunction function = new LoxFunction(method, environment,method.name.lexeme.equals("init"));
            methods.put(method.name.lexeme,function);
        }

        LoxClass klass = new LoxClass(statement.name.lexeme,methods);
        environment.assign(statement.name, klass);

        return null;
    }


    @Override
    public Object visitGetExpression(Expression.Get expression) {
        Object object = evaluate(expression.Object);
        if (object instanceof LoxInstance) {
            return ((LoxInstance) object).get(expression.name);
        }

        throw new RuntimeError(expression.name,"Only class instances have properties");
    }

    @Override
    public Object visitSetExpression(Expression.Set expression) {
        Object object = evaluate(expression.object);

        if(!(object instanceof LoxInstance)) {
            throw new RuntimeError(expression.name, "Only class instances have properties");
        }

        Object value = evaluate(expression.value);
        ((LoxInstance)object).set(expression.name, value);
        return value;
    }

    public Object visitThisExpression(Expression.This expression) {
        return lookUpVariable(expression.keyword,expression);
    }

}

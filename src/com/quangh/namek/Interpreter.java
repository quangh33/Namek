package com.quangh.namek;

public class Interpreter implements Expr.Visitor<Object> {
    private Object eval(Expr e) {
        return e.accept(this);
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeErr(operator, "Operand must be a number");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeErr(operator, "Operands must be a number");
    }

    @Override
    public Object visitBinary(Expr.Binary b) {
        Object left = eval(b.left);
        Object right = eval(b.right);

        switch (b.op.type) {
            case SLASH:
                checkNumberOperands(b.op, left, right);
                if ((double) right == 0.0) {
                    throw new RuntimeErr(b.op, "Division by zero");
                }
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(b.op, left, right);
                return (double) left * (double) right;
            case MINUS:
                checkNumberOperands(b.op, left, right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }

                if ((left instanceof String && right instanceof Double) ||
                        (left instanceof Double && right instanceof String)) {
                    return stringify(left) + stringify(right);
                }

                throw new RuntimeErr(b.op, "Operands must be number or string");
            case GREATER:
                checkNumberOperands(b.op, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(b.op, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(b.op, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(b.op, left, right);
                return (double) left <= (double) right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
        }

        return null;
    }

    @Override
    public Object visitGrouping(Expr.Grouping g) {
        return eval(g.expr);
    }

    @Override
    public Object visitUnary(Expr.Unary u) {
        Object right = eval(u.expr);
        if (u.op.type == TokenType.MINUS) {
            checkNumberOperand(u.op, right);
            return -(double) right;
        }

        // false and nil are false, and everything else is true.
        if (u.op.type == TokenType.BANG) {
            if (right == null) return true;
            if (right instanceof Boolean) return !(boolean) right;
            return false;
        }
        return null;
    }

    @Override
    public Object visitLiteral(Expr.Literal l) {
        return l.value;
    }

    public void interpret(Expr expr) {
        try {
            Object value = eval(expr);
            String text = stringify(value);
            System.out.println(text);

        } catch (RuntimeErr err) {
            Namek.runtimeError(err);
        }
    }

    private String stringify(Object x) {
        String text = x.toString();
        if (x instanceof Double) {
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
        }
        return text;
    }
}

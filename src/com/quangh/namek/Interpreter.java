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

    @Override
    public Object visitBinary(Expr.Binary b) {
        Object left = eval(b.left);
        Object right = eval(b.right);

        switch (b.op.type) {
            case SLASH:
                return (double) left / (double) right;
            case STAR:
                return (double) left * (double) right;
            case MINUS:
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
            case GREATER:
                return (double) left > (double) right;
            case GREATER_EQUAL:
                return (double) left >= (double) right;
            case LESS:
                return (double) left < (double) right;
            case LESS_EQUAL:
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
}

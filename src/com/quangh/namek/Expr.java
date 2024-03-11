package com.quangh.namek;

public abstract class Expr {
    interface Visitor<R> {
        R visitBinary(Binary b);
        R visitGrouping(Grouping g);
        R visitUnary(Unary u);
        R visitLiteral(Literal b);
    }

    abstract <R> R accept(Visitor<R> visitor);

    static class Binary extends Expr {
        final Expr left;
        final Token op;
        final Expr right;

        Binary(Expr left, Token op, Expr right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinary(this);
        }
    }

    static class Grouping extends Expr {
        final Expr expr;

        Grouping(Expr expr) {
            this.expr = expr;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
        }
    }

    static class Literal extends Expr {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    static class Unary extends Expr {
        final Token op;
        final Expr expr;

        Unary(Token op, Expr expr) {
            this.op = op;
            this.expr = expr;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnary(this);
        }
    }
}

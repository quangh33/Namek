package com.quangh.namek;

public abstract class Stmt {
    interface Visitor<R> {
        R visitExpressionStmt(Stmt.Expression e);

        R visitPrintStmt(Stmt.Print p);
    }

    abstract <R> R accept(Visitor<R> visitor);

    static class Expression extends Stmt {
        final Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Print extends Stmt {
        final Expr expression;

        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }
}

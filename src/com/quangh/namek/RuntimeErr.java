package com.quangh.namek;

public class RuntimeErr extends RuntimeException {
    final Token op;

    public RuntimeErr(Token op, String mes) {
        super(mes);
        this.op = op;
    }
}

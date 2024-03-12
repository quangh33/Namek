# Namek - The programming language of Namekian

This project implements 3 main parts of an interpreter:
1. Scanner:
  - input: user code, i.e. Namek code
  - output: a list of tokens. Namek supports following tokens
  ```
    ( ) { }
    . , ; + - * /
    ! !=
    == =
    < <= > >=
    string: "abc"
    number
    identifier: and, class, else, false, true,
                for, fun, if, nil, or, print,
                super, return, this, var, while, eof
  ```

2. Parser
  - input: list of token (i.e. Scanner's output)
  - output: syntax tree
3. Interpreter:
  - input: syntax tree
  - output: code execution outcome

  In this first version, we implement a simple kind of interpreter named Tree-walk interpreters,
  it traverses the Syntax tree and evaluates as it goes.

## Grammar

```
expression     → equality ;
equality       → comparison ( ( "!=" | "==" ) comparison )* ;
comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term           → factor ( ( "-" | "+" ) factor )* ;
factor         → unary ( ( "/" | "*" ) unary )* ;
unary          → ( "!" | "-" ) unary
               | primary ;
primary        → NUMBER | STRING | "true" | "false" | "nil"
               | "(" expression ")" ;
```
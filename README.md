# Namek - The programming language of Namekian

This project implements 3 main part of an interpreter:
- Scanner:
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
- Parser
  - input: list of token (i.e. Scanner's output)
  - output: syntax tree
- Interpreter:
  - input: syntax tree
  - output: code execution outcome

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
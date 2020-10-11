package com.rox.expression

interface TermExpression : Expression {
    /** The value/term of this expression */
    val term: Expression
}

package com.rox.expression

interface CompoundExpression : Expression {
    /** The left hand sub expression */
    val left: Expression
    /** The right hand sub expression */
    val right: Expression
}

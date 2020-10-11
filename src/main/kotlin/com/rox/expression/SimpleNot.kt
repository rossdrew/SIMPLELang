package com.rox.expression

import java.lang.RuntimeException

class SimpleNot(override val term: Expression): TermExpression {
    override fun isReducible(): Boolean = true;
    override fun reduce(): Expression {
        return when {
            term.isReducible() -> SimpleNot(term.reduce())
            else -> SimpleBoolean(operate(term))
        }
    }

    private fun operate(term: Expression): Boolean {
        if (term is SimpleBoolean) {
            val leftBoolean: SimpleBoolean = term
            return !leftBoolean.value
        }
        throw RuntimeException("Cannot not expressions which are not boolean")
    }

    override fun toString(): String = "!$term"
}

package com.rox.expression

import java.lang.RuntimeException

class SimpleOr(override val left: Expression,
               override val right: Expression): CompoundExpression {
    override fun isReducible(): Boolean = true;
    override fun reduce(): Expression {
        return when {
            left.isReducible() -> SimpleOr(left.reduce(), right)
            right.isReducible() -> SimpleOr(left, right.reduce())
            else -> SimpleBoolean(operate(left, right))
        }
    }

    private fun operate(left: Expression, right: Expression): Boolean {
        if (left is SimpleBoolean) {
            val leftBoolean: SimpleBoolean = left
            if (right is SimpleBoolean) {
                val rightBoolean: SimpleBoolean = right
                return leftBoolean.value || rightBoolean.value
            }
        }
        throw RuntimeException("Cannot or expressions which are not boolean")
    }

    override fun toString(): String = "$left || $right"
}

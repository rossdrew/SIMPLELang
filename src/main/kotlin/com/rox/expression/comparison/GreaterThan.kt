package com.rox.expression.comparison

import com.rox.expression.CompoundExpression
import com.rox.expression.Expression
import com.rox.expression.SimpleBoolean
import com.rox.expression.SimpleNumber
import java.lang.RuntimeException

/**
 * Test if left expression is greater than right expression
 */
class GreaterThan(override val left: Expression,
                  override val right: Expression
): CompoundExpression {
    override fun isReducible(): Boolean = true;
    override fun reduce(): Expression {
        return when {
            left.isReducible() -> GreaterThan(left.reduce(), right)
            right.isReducible() -> GreaterThan(left, right.reduce())
            else -> SimpleBoolean(operate(left, right))
        }
    }

    private fun operate(left: Expression, right: Expression): Boolean {
        if (left is SimpleNumber) {
            if (right is SimpleNumber) {
                return left.value > right.value
            }
        }
        throw RuntimeException("Cannot compare '>' expressions which are not of the same type")
    }

    override fun toString(): String = "$left > $right"
}

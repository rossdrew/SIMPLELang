package com.rox.expression

import java.lang.RuntimeException

class Subtract(override val left: Expression, override val right: Expression): CompoundExpression {
    override fun isReducible(): Boolean = true;
    override fun reduce(): Expression {
        return when {
            left.isReducible() -> Subtract(left.reduce(), right)
            right.isReducible() -> Subtract(left, right.reduce())
            else -> SimpleNumber(operate(left, right))
        }
    }

    private fun operate(left: Expression, right: Expression): Int{
        if (left is SimpleNumber){
            val leftSimpleNumber: SimpleNumber = left
            if (right is SimpleNumber){
                val rightSimpleNumber: SimpleNumber = right
                return leftSimpleNumber.value - rightSimpleNumber.value
            }
        }
        throw RuntimeException("Cannot subtract expressions which are not Numbers")
    }
}

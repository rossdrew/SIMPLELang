package com.rox.expression

import java.lang.RuntimeException

class Divide(override val left: Expression,
             override val right: Expression): CompoundExpression {
    override fun isReducible(): Boolean = true;
    override fun reduce(): Expression {
        return when {
            left.isReducible() -> Divide(left.reduce(), right)
            right.isReducible() -> Divide(left, right.reduce())
            else -> SimpleNumber(operate(left, right))
        }
    }

    private fun operate(left: Expression, right: Expression): Int{
        if (left is SimpleNumber){
            val leftSimpleNumber: SimpleNumber = left
            if (right is SimpleNumber){
                val rightSimpleNumber: SimpleNumber = right
                return leftSimpleNumber.value / rightSimpleNumber.value
            }
        }
        throw RuntimeException("Cannot divide expressions which are not Numbers")
    }

    override fun toString(): String = "$left * $right"
}

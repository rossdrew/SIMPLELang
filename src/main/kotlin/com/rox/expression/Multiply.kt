package com.rox.expression

import java.lang.RuntimeException

class Multiply(val left: Expression,
               val right: Expression): Expression {
    override fun isReducible(): Boolean = true;
    override fun reduce(): Expression {
        return when {
            left.isReducible() -> Multiply(left.reduce(), right)
            right.isReducible() -> Multiply(left, right.reduce())
            else -> Number(operate(left, right))
        }
    }

    private fun operate(left: Expression, right: Expression): Int{
        if (left is Number){
            val leftNumber: Number = left
            if (right is Number){
                val rightNumber: Number = right
                return leftNumber.value * rightNumber.value
            }
        }
        throw RuntimeException("Cannot multiply expressions which are not Numbers")
    }

    override fun toString(): String = "$left * $right"
}

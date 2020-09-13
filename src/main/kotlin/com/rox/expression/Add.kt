package com.rox.expression

import java.lang.RuntimeException

data class Add(val left: Expression, val right: Expression): Expression {
    override fun isReducible(): Boolean = true;
    override fun reduce(): Expression {
        return when {
            left.isReducible() -> Add(left.reduce(), right)
            right.isReducible() -> Add(left, right.reduce())
            else -> Number(operate(left, right))
        }
    }

    private fun operate(left: Expression, right: Expression): Int{
        if (left is Number){
            val leftNumber: Number = left
            if (right is Number){
                val rightNumber: Number = right
                return leftNumber.value + rightNumber.value
            }
        }
        throw RuntimeException("Cannot add expressions which are not Numbers")
    }

    override fun toString(): String = "$left + $right"
}

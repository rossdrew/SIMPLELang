package com.rox.expression

interface Expression {
    /** Can this expression be reduced further */
    fun isReducible(): Boolean
    /** Reduce this expression further, throws RuntimeException if isReducable is false */
    fun reduce(): Expression
}

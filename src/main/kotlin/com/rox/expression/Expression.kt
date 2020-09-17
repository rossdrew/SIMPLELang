package com.rox.expression

interface Expression {
    /** Can this expression be reduced further */
    fun isReducible(): Boolean = false
    /** Reduce this expression further, throws RuntimeException if isReducible is false */
    fun reduce(): Expression
}

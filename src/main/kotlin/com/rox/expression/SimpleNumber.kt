package com.rox.expression

data class SimpleNumber(override val value: Int) : Expression, ValueProvider<Int> {
    override fun isReducible(): Boolean = false;
    override fun reduce(): Expression {
        throw RuntimeException("Number is not reducible")
    }

    override fun toString(): String{
        return "$value"
    }
}

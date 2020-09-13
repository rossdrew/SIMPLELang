package com.rox.expression

data class Number(val value: Int) : Expression {
    override fun isReducible(): Boolean = false;
    override fun reduce(): Expression {
        throw RuntimeException("Number is not reducible")
    }

    override fun toString(): String{
        return "$value"
    }
}

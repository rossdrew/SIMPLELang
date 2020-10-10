package com.rox.expression

data class SimpleBoolean(override val value: Boolean) : Expression, ValueProvider<Boolean> {
    override fun isReducible(): Boolean = false;
    override fun reduce(): Expression {
        throw RuntimeException("Boolean value is not reducible")
    }

    override fun toString(): String{
        return if (value) "TRUE" else "FALSE";
    }
}

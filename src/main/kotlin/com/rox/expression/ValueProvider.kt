package com.rox.expression

interface ValueProvider<T> : Expression {
    val value: T
}

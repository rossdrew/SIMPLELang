package com.rox.expression

import java.lang.RuntimeException
import org.junit.Test
import kotlin.test.*

class NumberTest {
    @Test
    fun `Value is as expected`(){
        assertEquals(0, Number(0).value)
        assertEquals(-1, Number(-1).value)
        assertEquals(1, Number(1).value)
        assertEquals(1243, Number(1243).value)
    }

    @Test
    fun `Not reducible`(){
        assertFalse(Number(0).isReducible())
    }

    @Test
    fun `Attempting to reduce throws an exception`(){
        assertFailsWith<RuntimeException>{Number(0).reduce()}
    }
}

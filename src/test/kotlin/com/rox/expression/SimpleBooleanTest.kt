package com.rox.expression

import org.junit.Test
import java.lang.RuntimeException
import kotlin.test.*

class SimpleBooleanTest {
    @Test
    fun `Equal values mean equal objects`(){
        assertEquals(SimpleBoolean(true), SimpleBoolean(true))
    }

    @Test
    fun `Unequal values mean unequal objects`(){
        assertNotEquals(SimpleBoolean(false), SimpleBoolean(true))
    }

    @Test
    fun `Not reducible`(){
        assertFalse(SimpleBoolean(false).isReducible())
    }

    @Test
    fun `Attempting to reduce throws an exception`(){
        assertFailsWith<RuntimeException>{SimpleBoolean(true).reduce()}
    }

    @Test
    fun `Value is as expected`(){
        assertTrue(SimpleBoolean(true).value)
        assertFalse(SimpleBoolean(false).value)
    }
}

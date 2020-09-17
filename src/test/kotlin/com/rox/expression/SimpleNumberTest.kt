package com.rox.expression

import java.lang.RuntimeException
import org.junit.Test
import kotlin.test.*

class SimpleNumberTest {
    @Test
    fun `Equal values mean equal objects`(){
        assertEquals(SimpleNumber(20), SimpleNumber(20))
    }

    @Test
    fun `Value is as expected`(){
        assertEquals(0, SimpleNumber(0).value)
        assertEquals(-1, SimpleNumber(-1).value)
        assertEquals(1, SimpleNumber(1).value)
        assertEquals(1243, SimpleNumber(1243).value)
    }

    @Test
    fun `Not reducible`(){
        assertFalse(SimpleNumber(0).isReducible())
    }

    @Test
    fun `Attempting to reduce throws an exception`(){
        assertFailsWith<RuntimeException>{SimpleNumber(0).reduce()}
    }
}

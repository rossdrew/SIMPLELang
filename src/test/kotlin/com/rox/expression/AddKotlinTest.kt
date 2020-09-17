package com.rox.expression

import io.mockk.every
import io.mockk.mockkClass
import org.junit.Test
import kotlin.test.*

/**
 * Add testing with kotlin.test
 */
class AddKotlinTest {
    @Test
    fun `Is reducible`(){
        val left = mockkClass(SimpleNumber::class)
        val right = mockkClass(SimpleNumber::class)

        val addition = Add(left, right)

        assertTrue(addition.isReducible())
    }

    @Test
    fun `Two numbers are reduced to one`(){
        val left = mockkClass(SimpleNumber::class)
        every { left.isReducible() } returns false
        every { left.value} returns 5

        val right = mockkClass(SimpleNumber::class)
        every { right.isReducible() } returns false
        every { right.value} returns 10

        val add = Add(left, right)
        val reduction = add.reduce()

        assertEquals(SimpleNumber::class, reduction::class)
        assertEquals(15, (reduction as SimpleNumber).value)
    }

    @Test
    fun `A left hand expression is further reduced`(){
        val left = mockkClass(Add::class)
        every { left.isReducible() } returns true
        every { left.reduce()} returns SimpleNumber(10)

        val right = mockkClass(SimpleNumber::class)
        every { right.isReducible() } returns false
        every { right.value} returns 10

        val add = Add(left, right)
        val firstReduction = add.reduce()
        val secondReduction = firstReduction.reduce()

        assertEquals(Add::class, firstReduction::class)
        assertEquals(SimpleNumber::class, (firstReduction as Add).left::class)

        assertEquals(SimpleNumber::class, secondReduction::class)
        assertEquals(20, (secondReduction as SimpleNumber).value)
    }

    @Test
    fun `A right hand expression is further reduced`(){
        val left = mockkClass(SimpleNumber::class)
        every { left.isReducible() } returns false
        every { left.value} returns 10

        val right = mockkClass(Add::class)
        every { right.isReducible() } returns true
        every { right.reduce()} returns SimpleNumber(15)

        val add = Add(left, right)
        val firstReduction = add.reduce()
        val secondReduction = firstReduction.reduce()

        assertEquals(Add::class, firstReduction::class)
        assertEquals(SimpleNumber::class, (firstReduction as Add).right::class)
        assertEquals(SimpleNumber::class, secondReduction::class)
        assertEquals(25, (secondReduction as SimpleNumber).value)
    }

    @Test
    fun `Left & right hand expressions are further reduced in left to right order`(){
        val left = mockkClass(Add::class)
        every { left.isReducible() } returns true
        every { left.reduce() } returns SimpleNumber(15)

        val right = mockkClass(Add::class)
        every { right.isReducible() } returns true
        every { right.reduce() } returns SimpleNumber(15)

        val add = Add(left, right)
        val firstReduction = add.reduce()
        val secondReduction = firstReduction.reduce()
        val thirdReduction = secondReduction.reduce()

        //First expression reduced to a number in the first step
        assertEquals(Add::class, firstReduction::class)
        assertEquals(SimpleNumber::class, (firstReduction as Add).left::class)
        assertEquals(Add::class, firstReduction.right::class)

        //Second expression reduced to a number in the second step
        assertEquals(Add::class, secondReduction::class)
        assertEquals(SimpleNumber::class, (secondReduction as Add).left::class)
        assertEquals(SimpleNumber::class, secondReduction.right::class)

        //Entire expression reduced to a number in the third step
        assertEquals(SimpleNumber::class, thirdReduction::class)
        assertEquals(30, (thirdReduction as SimpleNumber).value)
    }
}

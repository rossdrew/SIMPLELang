package com.rox.expression

import io.mockk.mockkClass
import org.junit.Test
import io.mockk.every
import kotlin.test.assertTrue
import kotlin.test.assertEquals as assertEquals1

class MultiplyTest {
    @Test
    fun `Is reducible`(){
        val left = mockkClass(Number::class)
        val right = mockkClass(Number::class)

        val addition = Multiply(left, right)

        assertTrue(addition.isReducible())
    }

    @Test
    fun `Two numbers are reduced to one`(){
        val left = mockkClass(Number::class)
        every { left.isReducible() } returns false
        every { left.value} returns 5

        val right = mockkClass(Number::class)
        every { right.isReducible() } returns false
        every { right.value} returns 10

        val multiply = Multiply(left, right)
        val reduction = multiply.reduce()

        assertEquals1(Number::class, reduction::class)
        assertEquals1(50, (reduction as Number).value)
    }

    @Test
    fun `A left hand expression is further reduced`(){
        val left = mockkClass(Add::class)
        every { left.isReducible() } returns true
        every { left.reduce()} returns Number(10)

        val right = mockkClass(Number::class)
        every { right.isReducible() } returns false
        every { right.value} returns 10

        val multiply = Multiply(left, right)
        val firstReduction = multiply.reduce()
        val secondReduction = firstReduction.reduce()

        kotlin.test.assertEquals(Multiply::class, firstReduction::class)
        kotlin.test.assertEquals(Number::class, (firstReduction as Multiply).left::class)

        kotlin.test.assertEquals(Number::class, secondReduction::class)
        kotlin.test.assertEquals(100, (secondReduction as Number).value)
    }

    @Test
    fun `A right hand expression is further reduced`(){
        val left = mockkClass(Number::class)
        every { left.isReducible() } returns false
        every { left.value} returns 10

        val right = mockkClass(Add::class)
        every { right.isReducible() } returns true
        every { right.reduce()} returns Number(15)

        val multiply = Multiply(left, right)
        val firstReduction = multiply.reduce()
        val secondReduction = firstReduction.reduce()

        kotlin.test.assertEquals(Multiply::class, firstReduction::class)
        kotlin.test.assertEquals(Number::class, (firstReduction as Multiply).right::class)
        kotlin.test.assertEquals(Number::class, secondReduction::class)
        kotlin.test.assertEquals(150, (secondReduction as Number).value)
    }

    @Test
    fun `Left & right hand expressions are further reduced in left to right order`(){
        val left = mockkClass(Add::class)
        every { left.isReducible() } returns true
        every { left.reduce() } returns Number(15)

        val right = mockkClass(Add::class)
        every { right.isReducible() } returns true
        every { right.reduce()} returns Number(15)

        val multiply = Multiply(left, right)
        val firstReduction = multiply.reduce()
        val secondReduction = firstReduction.reduce()
        val thirdReduction = secondReduction.reduce()

        //First expression reduced to a number in the first step
        kotlin.test.assertEquals(Multiply::class, firstReduction::class)
        kotlin.test.assertEquals(Number::class, (firstReduction as Multiply).left::class)
        kotlin.test.assertEquals(Add::class, firstReduction.right::class)

        //Second expression reduced to a number in the second step
        kotlin.test.assertEquals(Multiply::class, secondReduction::class)
        kotlin.test.assertEquals(Number::class, (secondReduction as Multiply).left::class)
        kotlin.test.assertEquals(Number::class, secondReduction.right::class)

        //Entire expression reduced to a number in the third step
        kotlin.test.assertEquals(Number::class, thirdReduction::class)
        kotlin.test.assertEquals(225, (thirdReduction as Number).value)
    }
}

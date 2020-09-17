package com.rox.expression

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkClass
import kotlin.test.assertEquals

/**
 * Add testing with Kotest
 */
class AddKoTest : ShouldSpec({
    should("be reducible") {
        val left = mockkClass(SimpleNumber::class)
        val right = mockkClass(SimpleNumber::class)

        Add(left, right).isReducible() shouldBe true
    }

    should("Add up to 15") {
        val left = mockkClass(SimpleNumber::class)
        every { left.isReducible() } returns false
        every { left.value} returns 5

        val right = mockkClass(SimpleNumber::class)
        every { right.isReducible() } returns false
        every { right.value} returns 10

        val add = Add(left, right)
        val reduction = add.reduce()

        SimpleNumber::class shouldBe reduction::class
        reduction shouldBe SimpleNumber(15)
    }

    should("Reduce left hand expression when present") {
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

        SimpleNumber::class shouldBe secondReduction::class
        secondReduction shouldBe SimpleNumber(20)
    }

    should("Reduce right hand expression when present") {
        val left = mockkClass(SimpleNumber::class)
        every { left.isReducible() } returns false
        every { left.value} returns 10

        val right = mockkClass(Add::class)
        every { right.isReducible() } returns true
        every { right.reduce()} returns SimpleNumber(15)

        val add = Add(left, right)
        val firstReduction = add.reduce()
        val secondReduction = firstReduction.reduce()

        firstReduction::class shouldBe Add::class
        (firstReduction as Add).right::class shouldBe SimpleNumber::class
        secondReduction shouldBe SimpleNumber(25)
    }

    should("Reduce right & left hand expressions when present") {
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
        firstReduction::class shouldBe Add::class
        (firstReduction as Add).left shouldBe SimpleNumber(15)
        firstReduction.right::class shouldBe Add::class

        //Second expression reduced to a number in the second step
        secondReduction::class shouldBe Add::class
        (secondReduction as Add).left shouldBe SimpleNumber(15)
        secondReduction.right shouldBe SimpleNumber(15)

        //Entire expression reduced to a number in the third step
        thirdReduction::class shouldBe SimpleNumber::class
        thirdReduction shouldBe SimpleNumber(30)
    }
})

package com.rox.expression

import com.rox.expression.comparison.EqualTo
import com.rox.expression.comparison.GreaterThan
import com.rox.expression.comparison.LessThan
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SimpleExpressionTest : StringSpec({
    "Should be reducible" {
        val left = mockk<SimpleNumber> {}
        val right = mockk<SimpleNumber> {}

        forAll(
            row("Addition", Add(left, right)),
            row("Multiplication", Multiply(left, right)),
            row("Subtraction", Subtract(left, right)),
            row("Division", Divide(left, right))
        ) { _, expression ->
            expression.isReducible() shouldBe true
        }
    }

    "Can compare two numbers" {
        val left = mockk<SimpleNumber> {
            every { isReducible() } returns false
        }

        val right = mockk<SimpleNumber> {
            every { isReducible() } returns false
        }

        forAll(
            row("Greater Than: 4>5=false", GreaterThan(left, right), 4, 5, SimpleBoolean(false)),
            row("Less Than: 4<5=true", LessThan(left, right), 4, 5, SimpleBoolean(true)),
            row("Equal To: 5==5=true", EqualTo(left, right), 5, 5, SimpleBoolean(true))
        ) { _, expression, leftValue, rightValue, expectedResult ->
            every { left.value } returns leftValue
            every { right.value} returns rightValue

            val reduction = expression.reduce()

            reduction::class shouldBe SimpleBoolean::class
            reduction shouldBe expectedResult
        }
    }

    "Can convert one boolean to another" {
        val term = mockk<SimpleBoolean> {
            every { isReducible() } returns false
        }

        forAll(
            row("And: !true=false", SimpleNot(term), true, SimpleBoolean(false))
        ) { _, expression, termValue, expectedResult ->
            every { term.value } returns termValue

            val reduction = expression.reduce()

            reduction::class shouldBe SimpleBoolean::class
            reduction shouldBe expectedResult
        }
    }

    "Can reduce two booleans to one" {
        val left = mockk<SimpleBoolean> {
            every { isReducible() } returns false
        }

        val right = mockk<SimpleBoolean> {
            every { isReducible() } returns false
        }

        forAll(
            row("And: true&&false=false", SimpleAnd(left, right), true, false, SimpleBoolean(false)),
            row("And: true||false=false", SimpleOr(left, right), true, false, SimpleBoolean(true)),
            row("And: true^false=false", SimpleXor(left, right), true, true, SimpleBoolean(false))
        ) { _, expression, leftValue, rightValue, expectedResult ->
            every { left.value } returns leftValue
            every { right.value} returns rightValue

            val reduction = expression.reduce()

            reduction::class shouldBe SimpleBoolean::class
            reduction shouldBe expectedResult
        }
    }

    "Can reduce two numbers to one" {
        val left = mockk<SimpleNumber> {
            every { isReducible() } returns false
        }

        val right = mockk<SimpleNumber> {
            every { isReducible() } returns false
        }

        forAll(
            row("Addition: 5+10=15", Add(left, right), 5, 10, SimpleNumber(15)),
            row("Multiplication: 2x13=26", Multiply(left, right), 2, 13, SimpleNumber(26)),
            row("Subtraction: 4-2=2", Subtract(left, right), 4, 2, SimpleNumber(2)),
            row("Division: 60/10=6", Divide(left, right), 60, 10, SimpleNumber(6))
        ) { _, expression, leftValue, rightValue, expectedResult ->
            every { left.value } returns leftValue
            every { right.value} returns rightValue

            val reduction = expression.reduce()

            reduction::class shouldBe SimpleNumber::class
            reduction shouldBe expectedResult
        }
    }

    "Can reduce a left hand expression" {
        val left = mockk<CompoundExpression> {
            every { isReducible() } returns true
        }

        val right = mockk<SimpleNumber> {
            every { isReducible() } returns false
        }

        forAll(
            row("Addition: 5+10=15", Add(left, right), 5, 10, SimpleNumber(15)),
            row("Multiplication: 2x13=26", Multiply(left, right), 2, 13, SimpleNumber(26)),
            row("Subtraction: 4-2=2", Subtract(left, right), 4, 2, SimpleNumber(2)),
            row("Division: 60/10=6", Divide(left, right), 60, 10, SimpleNumber(6))
        ) { _, expression, leftValue, rightValue, expectedResult ->
            every { left.reduce() } returns SimpleNumber(leftValue)
            every { right.value } returns rightValue

            val firstReduction = expression.reduce()
            val secondReduction = firstReduction.reduce()

            //Expression reduced to a number
            firstReduction::class shouldBe expression::class
            firstReduction as CompoundExpression
            firstReduction.left::class shouldBe SimpleNumber::class
            firstReduction.right::class shouldBe SimpleNumber::class

            secondReduction::class shouldBe SimpleNumber::class
            secondReduction shouldBe expectedResult
        }
    }

    "Can reduce a right hand expression"{
        val left = mockk<SimpleNumber> {
            every { isReducible() } returns false
        }

        val right = mockk<CompoundExpression> {
            every { isReducible() } returns true
        }

        forAll(
            row("Addition: 5+10=15", Add(left, right), 5, 10, SimpleNumber(15)),
            row("Multiplication: 2x13=26", Multiply(left, right), 2, 13, SimpleNumber(26)),
            row("Subtraction: 4-2=2", Subtract(left, right), 4, 2, SimpleNumber(2)),
            row("Division: 60/10=6", Divide(left, right), 60, 10, SimpleNumber(6))
        ) { _, expression, leftValue, rightValue, expectedResult ->
            every { left.value} returns leftValue
            every { right.reduce()} returns SimpleNumber(rightValue)

            val firstReduction = expression.reduce()
            val secondReduction = firstReduction.reduce()

            //Expression reduced to a number
            firstReduction::class shouldBe expression::class
            firstReduction as CompoundExpression
            firstReduction.left::class shouldBe SimpleNumber::class
            firstReduction.right::class shouldBe SimpleNumber::class

            secondReduction::class shouldBe SimpleNumber::class
            secondReduction shouldBe expectedResult
        }
    }

    "Can reduce left & right hand expressions"{
        val left = mockk<CompoundExpression> {
            every { isReducible() } returns true
        }

        val right = mockk<CompoundExpression> {
            every { isReducible() } returns true
        }

        forAll(
            row("Addition: 5+10=15", Add(left, right), 5, 10, SimpleNumber(15)),
            row("Multiplication: 2x13=26", Multiply(left, right), 2, 13, SimpleNumber(26)),
            row("Subtraction: 4-2=2", Subtract(left, right), 4, 2, SimpleNumber(2)),
            row("Division: 60/10=6", Divide(left, right), 60, 10, SimpleNumber(6))
        ) { _, expression, leftValue, rightValue, expectedResult ->
            every { left.reduce() } returns SimpleNumber(leftValue)
            every { right.reduce() } returns SimpleNumber(rightValue)

            val firstReduction = expression.reduce()
            val secondReduction = firstReduction.reduce()
            val thirdReduction = secondReduction.reduce()

            //First expression reduced to a number in the first step
            firstReduction::class shouldBe expression::class
            (firstReduction as CompoundExpression).left shouldBe SimpleNumber(leftValue)
            firstReduction.right shouldBe right

            //Second expression reduced to a number in the second step
            secondReduction::class shouldBe expression::class
            (secondReduction as CompoundExpression).left shouldBe SimpleNumber(leftValue)
            secondReduction.right shouldBe SimpleNumber(rightValue)

            //Entire expression reduced to a number in the third step
            thirdReduction::class shouldBe SimpleNumber::class
            thirdReduction shouldBe expectedResult
        }
    }
})

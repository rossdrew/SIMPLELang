package com.rox.expression

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll


class KotestProperties: StringSpec({
    val genSimpleNumber: Arb<SimpleNumber> = Arb.int().map(::SimpleNumber)
    val genSimpleBoolean: Arb<SimpleBoolean> = Arb.bool().map(::SimpleBoolean)

    "Zero friendly numerical operations (Division)" {
        checkAll(500, genSimpleNumber, genSimpleNumber) { a, b ->
            if (a.value != 0 || b.value != 0) {
                val result = Divide(a, b).reduce() as SimpleNumber
                val expected = SimpleNumber(a.value / b.value)
                result shouldBe expected
            }
        }
    }

    "Zero friendly numerical operations" {
        checkAll(500, genSimpleNumber, genSimpleNumber) { a, b ->
            io.kotest.data.forAll(
                row("Addition: ${a.value}+${b.value}=${a.value+b.value}", Add(a, b), SimpleNumber(a.value+b.value)),
                row("Multiplication: ${a.value}*${b.value}=${a.value+b.value}", Multiply(a, b), SimpleNumber(a.value*b.value)),
                row("Subtraction: ${a.value}-${b.value}=${a.value+b.value}", Subtract(a, b),SimpleNumber(a.value-b.value)),
            ) { _, expression, expectedResult ->
                val reduction = expression.reduce()

                reduction shouldBe expectedResult
            }
        }
    }

    "Boolean Operations"
})

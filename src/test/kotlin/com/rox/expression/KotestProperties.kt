package com.rox.expression

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.Sample
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll


class KotestProperties: StringSpec({
    "Addition" {
        val genSimpleNumber: Arb<SimpleNumber> = Arb.int().map(::SimpleNumber)

        checkAll(500, genSimpleNumber, genSimpleNumber) { a, b ->
            val result = Add(a, b).reduce() as SimpleNumber
            val expected = SimpleNumber(a.value + b.value)
            println("$a + $b = $expected")
            result shouldBe expected
        }
    }
})

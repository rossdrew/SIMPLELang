package com.rox.expression

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.Sample
import io.kotest.property.checkAll


class KotestProperties: StringSpec({
    "Addition" {
        val simpleNumberGenerator = SimpleNumberGenerator()

        checkAll(500, simpleNumberGenerator, simpleNumberGenerator) { a, b ->
            val result = Add(a, b).reduce() as SimpleNumber
            val expected = SimpleNumber(a.value + b.value)
            println("$a + $b = $expected")
            result shouldBe expected
        }
    }
})


class SimpleNumberGenerator : Arb<SimpleNumber>() {
    override fun edgecases(): List<SimpleNumber> {
        return listOf(
            SimpleNumber(0),
            SimpleNumber(-1),
            SimpleNumber(1)
        )
    }

    override fun values(rs: RandomSource): Sequence<Sample<SimpleNumber>> {
        return generateSequence {
            Sample(SimpleNumber(rs.random.nextInt()))
        }
    }
}

package com.rox

import com.rox.expression.Add
import com.rox.expression.Expression
import com.rox.expression.Multiply
import com.rox.expression.Number

class Machine(var expression: Expression) {
    /**
     * You can't assemble the project cause main method is not static. So you should define it in companion object.?!?!?
     */
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            println("Running...")
            Machine(
                Multiply(
                    Add(
                        Number(5),
                        Number(4)
                    ),
                    Add(
                        Number(9),
                        Number(1)
                    )
                )
            ).run()
        }
    }

    fun step() {
        expression = expression.reduce()
    }

    fun run() {
        println("$expression")
        while (expression.isReducible()){
            step()
            println("$expression")
        }
    }
}

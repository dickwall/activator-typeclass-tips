package com.example.save

/**
 * Created by Dick Wall on 7/25/14.
 * Demonstrate the conversion to string using the typeclasses, json to begin with
 * but switch to xml by following the activator instructions
 * Run to see the output
 */
object Demo extends App {
  val dottie = Dog("Dottie", 3, 62.0F)
  val harry = Person("Harry", "Potter", 21, "Goldenrod")
  val banana = Fruit("Banana")

  println(StringSaver.saveToString(dottie))
  println(StringSaver.saveToString(harry))
  println(StringSaver.saveToString(banana))
}

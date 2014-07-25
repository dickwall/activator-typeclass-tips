package com.example.save

// comment out these three lines to try xml instead of json
import com.example.save.json._
import spray.json._
import DefaultJsonProtocol._

// uncomment this line to try xml instead of json
//import com.example.save.xml._

/**
 * Created by Dick Wall on 7/25/14.
 * These are example domain objects that extend the savable typeclasses with their companions
 * providing the ability to provide a savable string form depending on the type class implementation
 * of savable provided. Json is the default, but comment out the json line and uncomment the
 * xml line, then recompile to see the XML version work instead.
 */
case class Dog(name: String, age: Int, weight: Float)
object Dog extends Savable3[String, Int, Float, Dog]

case class Person(first: String, last: String, age: Int, favoriteColor: String)
object Person extends Savable4[String, String, Int, String, Person]

case class Fruit(name: String)
object Fruit extends Savable1[String, Fruit]

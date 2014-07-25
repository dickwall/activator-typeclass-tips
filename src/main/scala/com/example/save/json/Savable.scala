package com.example.save.json

import spray.json.DefaultJsonProtocol._
import spray.json._


/**
 * Created by Dick Wall on 7/25/14.
 * An example typeclass that allows easy creation of a savable record with a minimum of boilerplate
 * in fact, Savables 1 to 4 are supplied, 5 to 22 (to take advantage of all case class sizes) are
 * left as an exercise for the developer.
 * Extending one of these typeclasses for the companion of any case class will endow that case class
 * with the ability to save itself in whatever form is defined here.
 */

abstract class Savable[RECORD <: Product : ClassManifest] {
  // aha! yes - this works!
  implicit val saveableAdapter: Savable[RECORD] = this

  // abstract jsonwriter that must be supplied by subclass
  implicit val jsonWriter: JsonWriter[RECORD]

  // pull out type name
  val itemName: String = implicitly[ClassManifest[RECORD]].runtimeClass.getSimpleName

  def saveToString(r: RECORD): String = s"""{ "classType":"$itemName","data":${r.toJson.compactPrint} }"""
}

abstract class Savable1[A : JsonFormat, RECORD <: Product : ClassManifest] extends Savable[RECORD] {
  def apply(f1: A): RECORD
  val jsonWriter: JsonWriter[RECORD] = jsonFormat1(this.apply)
}

abstract class Savable2[A : JsonFormat, B : JsonFormat, RECORD <: Product : ClassManifest] extends Savable[RECORD] {
  def apply(f1: A, f2: B): RECORD
  val jsonWriter: JsonWriter[RECORD] = jsonFormat2(this.apply)
}

abstract class Savable3[A : JsonFormat, B : JsonFormat, C : JsonFormat, RECORD <: Product : ClassManifest] extends Savable[RECORD] {
  def apply(f1: A, f2: B, f3: C): RECORD
  val jsonWriter: JsonWriter[RECORD] = jsonFormat3(this.apply)
}

abstract class Savable4[A : JsonFormat, B : JsonFormat, C : JsonFormat, D : JsonFormat, RECORD <: Product : ClassManifest] extends Savable[RECORD] {
  def apply(f1: A, f2: B, f3: C, f4: D): RECORD
  val jsonWriter: JsonWriter[RECORD] = jsonFormat4(this.apply)
}

// you get the idea - generate or use a macro if you like
package com.example.save.xml

import java.lang.reflect.Modifier

import scala.reflect.ClassTag
import scala.util.control.NonFatal

/**
 * Created by Dick Wall on 7/25/14.
 * An alternative implementation of Savable that targets XML instead of JSON, in order to use it
 * it is only necessary to change the imports, all of the existing case class and type class
 * companion definitions are unchanged.
 */
abstract class Savable[RECORD <: Product](implicit classTag: ClassTag[RECORD]) {
  // aha! yes - this works!
  implicit val saveableAdapter: Savable[RECORD] = this

  // abstract xml writer that must be supplied by subclass
  implicit def toXml(item: RECORD): String = {
    (for ((field, value) <- fieldsWithValues(item)) yield {
      s"<$field>$value</$field>"
    }).mkString
  }

  // pull out type name
  val itemName: String = classTag.runtimeClass.getSimpleName

  def saveToString(r: RECORD): String = s"""<Record><ClassType>$itemName<ClassType><Data>${toXml(r)}</Data></Record>"""

  // utility method to provide terms of a Product - kudos to the spray json project for the original
  protected val fieldNames: Seq[String] = {
    val clazz = classTag.runtimeClass
    try {
      // copy methods have the form copy$default$N(), we need to sort them in order, but must account for the fact
      // that lexical sorting of ...8(), ...9(), ...10() is not correct, so we extract N and sort by N.toInt
      val copyDefaultMethods = clazz.getMethods.filter(_.getName.startsWith("copy$default$")).sortBy(
        _.getName.drop("copy$default$".length).takeWhile(_ != '(').toInt)
      val fields = clazz.getDeclaredFields.filterNot { f =>
        f.getName.startsWith("$") || Modifier.isTransient(f.getModifiers) || Modifier.isStatic(f.getModifiers)
      }
      if (copyDefaultMethods.length != fields.length)
        sys.error("Case class " + clazz.getName + " declares additional fields")
      if (fields.zip(copyDefaultMethods).exists { case (f, m) => f.getType != m.getReturnType })
        sys.error("Cannot determine field order of case class " + clazz.getName)
      fields.map(_.getName)
    } catch {
      case NonFatal(ex) => throw new RuntimeException("Cannot automatically determine case class field names and order " +
        "for '" + clazz.getName + "', please use the 'jsonFormat' overload with explicit field name specification", ex)
    }
  }

  protected def fieldsWithValues(item: RECORD): Seq[(String, String)] =
    fieldNames zip item.productIterator.toSeq.map(_.toString)
}

// the SavableNs are trivial in this case, but still need to be there since they are used in
// the rest of the code - if we only had XML to support, everything could be a Savable

abstract class Savable1[A, RECORD <: Product : ClassTag] extends Savable[RECORD]
abstract class Savable2[A, B, RECORD <: Product : ClassTag] extends Savable[RECORD]
abstract class Savable3[A, B, C, RECORD <: Product : ClassTag] extends Savable[RECORD]
abstract class Savable4[A, B, C, D, RECORD <: Product : ClassTag] extends Savable[RECORD]

// you get the idea - generate or use a macro if you like
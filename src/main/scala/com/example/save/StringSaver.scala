package com.example.save

// comment out this line to try xml instead of json
import com.example.save.json._

// uncomment this line to try xml instead of json
//import com.example.save.xml._

/**
 * Created by Dick Wall on 7/25/14.
 * Provide a simple utility class to allow saving of anything savable into its string representation.
 */
object StringSaver {
  def saveToString[S <: Product : Savable](item: S): String = 
    implicitly[Savable[S]].saveToString(item)
}

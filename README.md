scala-sealed-class-utilities
============================

This feels like I may have solved an already solved problem, but whatever.

There's really just one method: enumerateCaseObjects in SealedClassUtilities.
This takes as its type parameter a sealed trait or abstract class, and it will
return all of the case objects which extend that trait or class (recall that
these are necessarily all defined in the same file). It will deal with
subtraits of the original trait, as long as they are also sealed (there's no
reason for them not to be, you wouldn't be able to extend them in another file
anyway).

There's an additional method that will produce a map of case objects based on
some string-returning method in the trait (or toString)

One possible use case for this is where you have an enumeration of things
represented as case objects extending from a sealed trait and you want to be
able to convert them to and from some kind of string representation, and you
might also want to produce a list of them, without having to maintain a list /
map manually in the code (and then forget to update it when you add another
item to the enumeration). It's a bit niche, admittedly. Look at the tests for
more guidance.

```scala

sealed trait OutputOptions {
  def extension: String
}

case object Word extends OutputOptions {
  val extension = "docx"
}

case object PDF extends OutputOptions {
  val extension = "pdf"
}

case object LibreOffice extends OutputOptions {
  val extension = "odt"
}

val allOptions = SealedClassUtilities.indexCaseObjects[OutputOptions](_.extension)

// Map(odt -> LibreOffice, pdf -> PDF, docx -> Word)
```


Licensing
---------

Any other added code is covered by MIT License. Copyright Daniel Rendall <drendall@gmail.com>

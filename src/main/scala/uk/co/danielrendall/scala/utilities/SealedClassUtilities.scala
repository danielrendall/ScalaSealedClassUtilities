package uk.co.danielrendall.scala.utilities

import scala.annotation.tailrec
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}


object SealedClassUtilities {

  /**
   * Given a sealed trait or class intended as an enumeration (with a bunch of case objects that inherit from it),
   * produce a set of all of the members of that sealed trait or class.
   *
   * @param ct
   * @param man
   * @tparam T A sealed trait or class
   * @return
   */
  def enumerateCaseObjects[T](implicit ct: ClassTag[T], man: Manifest[T]): Set[T] = {
    // Given a sealed thing, we can get all known subclasses which must all be in the same file, though conceivably
    // some may be inside objects
    ru.typeOf[T].typeSymbol.asClass.knownDirectSubclasses.map { clazz: ru.Symbol =>
      val classSymbol: ru.ClassSymbol = clazz.asClass
      val fullyQualifiedName = getFullyQualifiedName(classSymbol)
      val classInstance = Class.forName(fullyQualifiedName)
      // This is how we get the companion object, which is what we really want...
      val instance = classInstance.getField("MODULE$").get(classInstance).asInstanceOf[T]
      instance
    }
  }

  /**
   * Given a sealed trait or class intended as an enumeration (with a bunch of case objects that inherit from it), and
   * an optional way of getting a name for the members of that enumeration (if not toString), produce a map from name
   * to member.
   *
   * @param fn A function to extract a name from an instance of a class
   * @param ct
   * @param man
   * @tparam T A sealed trait or class
   * @return
   */
  def indexCaseObjects[T](fn: T => String = (s: T) => s.toString)(implicit ct: ClassTag[T], man: Manifest[T]): Map[String, T] =
    enumerateCaseObjects[T].map(t => fn(t) -> t).toMap

  private def getFullyQualifiedName(symbol: ru.Symbol): String = {
    // e.g. something like com.company.package.OuterObject$InnerObject$
    @tailrec
    def recurse(aSymbol: ru.Symbol, foundPackage: Boolean, accum: List[String]): String = {
      if (!aSymbol.isPackage && foundPackage) {
        accum.mkString
      } else {
        val newAccum =
          if (aSymbol.name.toString != "<root>") {
            if (aSymbol.isModuleClass && !aSymbol.isPackage)
              (aSymbol.name.toString + "$") :: accum
            else
              (aSymbol.name.toString + ".") :: accum
          } else accum

        recurse(aSymbol.owner, aSymbol.isPackage, newAccum)
      }
    }
    recurse(symbol, false, List.empty)
  }

}

package uk.co.danielrendall.scala.utilities

import scala.annotation.tailrec
import scala.collection.mutable
import scala.reflect.ClassTag
import scala.reflect.runtime.{universe => ru}


object SealedClassUtilities {

  private val enumerations = new mutable.HashMap[ClassTag[_], Set[_]]()

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
    enumerations.get(ct) match {
      case Some(x) => x.asInstanceOf[Set[T]]
      case None =>
        enumerations.synchronized {
          enumerations.getOrElseUpdate(ct, enumerateAllSubclasses(ru.typeOf[T].typeSymbol.asClass, ct.runtimeClass.getClassLoader)).asInstanceOf[Set[T]]
        }
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
  def indexCaseObjects[T](fn: T => String = (s: T) => s.toString)
                         (implicit ct: ClassTag[T], man: Manifest[T]): Map[String, T] =
    enumerateCaseObjects[T].map(t => fn(t) -> t).toMap

  private def getFullyQualifiedName(symbol: ru.Symbol): String = {
    // e.g. something like com.company.package.OuterObject$InnerObject$
    @tailrec
    def recurse(aSymbol: ru.Symbol, foundPackage: Boolean, accum: List[String]): String = {
      if (!aSymbol.isPackage && foundPackage) {
        accum.mkString
      } else {
        if (symbol.name.toString == "") {
          println("Bad name: " + symbol)
        }
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

  private def enumerateAllSubclasses[T](classSymbol: ru.ClassSymbol,
                                        classLoader: ClassLoader)
                                       (implicit ct: ClassTag[T], man: Manifest[T]): Set[T] = {
    // Given a sealed thing, we can get all known subclasses which must all be in the same file, though conceivably
    // some may be inside objects
    checkSealed(classSymbol)
    classSymbol.knownDirectSubclasses.flatMap { clazz: ru.Symbol =>
      val classSymbol: ru.ClassSymbol = clazz.asClass

      if (classSymbol.isTrait) {
        enumerateAllSubclasses(classSymbol, classLoader)
      } else {
        val fullyQualifiedName = getFullyQualifiedName(classSymbol)
        try {
          val classInstance = Class.forName(fullyQualifiedName, true, classLoader)
          // This is how we get the companion object, which is what we really want...
          val instance = classInstance.getField("MODULE$").get(classInstance).asInstanceOf[T]
          Set(instance)
        } catch {
          case _: ClassNotFoundException =>
            // This can happen if there's a case class among the case objects. For now, we will just ignore this.
            Set.empty
        }
      }
    }
  }

  private def checkSealed(symbol: ru.ClassSymbol): Unit =
    if (!symbol.isSealed) {
      throw NotSealedException(symbol)
    }

  case class NotSealedException(classSymbol: ru.ClassSymbol)
    extends ReflectiveOperationException("Can't enumerate non-sealed class: " + classSymbol.toString)
}

case class DanielTestClass(s: String)

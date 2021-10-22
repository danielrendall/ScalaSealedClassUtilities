package uk.co.danielrendall.scala.utilities

sealed class SimpleSealedAbstractClass(name: String, description: String) {
  def key: String = name + "_" + description
}

case object SimpleSealedAbstractClassFirst extends SimpleSealedAbstractClass("first", "alpha")

case object SimpleSealedAbstractClassSecond extends SimpleSealedAbstractClass("second", "bravo")

case object SimpleSealedAbstractClassThird extends SimpleSealedAbstractClass("third", "charlie")

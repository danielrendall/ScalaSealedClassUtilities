package uk.co.danielrendall.scala.utilities

sealed trait SimpleSealedTrait {
  def name: String
}

case object SimpleSealedTraitFirst extends SimpleSealedTrait {
  lazy val name = "first-option"
}

case object SimpleSealedTraitSecond extends SimpleSealedTrait {
  lazy val name = "second-option"
}

case object SimpleSealedTraitThird extends SimpleSealedTrait {
  lazy val name = "third-option"
}

package uk.co.danielrendall.scala.utilities

sealed trait ComplexSealedTrait {
  def name: String
  def description: String
  def key: String = name + "_" + description
}

case object ComplexSealedTraitFirst extends ComplexSealedTrait {
  val name = "first"
  val description = "alpha"
}

object ComplexSealedTrait {

  case object ComplexSealedTraitSecond extends ComplexSealedTrait {
    val name = "second"
    val description = "bravo"
  }

  case object ComplexSealedTraitThird extends ComplexSealedTrait {
    def name = "third"
    def description = "charlie"
  }

}

object UnrelatedNotEvenCompanion {

  case object ComplexSealedTraitFourth extends ComplexSealedTrait {
    val name = "fourth"
    val description = "delta"

    case object ComplexSealedTraitFifth extends ComplexSealedTrait {
      val name = "fifth"
      val description = "echo"
    }

  }

}

case object ComplexSealedTraitSixth extends ComplexSealedTrait {

  lazy val name = "sixth"
  lazy val description = "foxtrot"

  case object ComplexSealedTraitSeventh extends ComplexSealedTrait {
    val name = "seventh"
    def description = "golf"

    case object ComplexSealedTraitEighth extends ComplexSealedTrait {
      def name = "eighth"
      val description = "hotel"
    }

  }

}

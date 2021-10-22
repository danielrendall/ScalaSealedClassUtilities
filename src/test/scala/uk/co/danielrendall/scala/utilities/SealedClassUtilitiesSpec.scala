package uk.co.danielrendall.scala.utilities

import org.specs2.mutable.Specification
import uk.co.danielrendall.scala.utilities.ComplexSealedTrait.{ComplexSealedTraitSecond, ComplexSealedTraitThird}
import uk.co.danielrendall.scala.utilities.ComplexSealedTraitSixth.ComplexSealedTraitSeventh
import uk.co.danielrendall.scala.utilities.ComplexSealedTraitSixth.ComplexSealedTraitSeventh.ComplexSealedTraitEighth
import uk.co.danielrendall.scala.utilities.UnrelatedNotEvenCompanion.ComplexSealedTraitFourth
import uk.co.danielrendall.scala.utilities.UnrelatedNotEvenCompanion.ComplexSealedTraitFourth.ComplexSealedTraitFifth

class SealedClassUtilitiesSpec extends Specification {

  "With SimpleSealedTrait" should {
    "Enumerate case objects" in {
      SealedClassUtilities.enumerateCaseObjects[SimpleSealedTrait] must_===
        Set(SimpleSealedTraitFirst, SimpleSealedTraitSecond, SimpleSealedTraitThird)
    }
    "Map case objects by name" in {
      SealedClassUtilities.indexCaseObjects[SimpleSealedTrait](_.name) must_===
        Map("first-option" -> SimpleSealedTraitFirst,
          "second-option" -> SimpleSealedTraitSecond,
          "third-option" -> SimpleSealedTraitThird)
    }
  }

  "With SimpleSealedAbstractClass" should {
    "Enumerate case objects" in {
      SealedClassUtilities.enumerateCaseObjects[SimpleSealedAbstractClass] must_===
        Set(SimpleSealedAbstractClassFirst, SimpleSealedAbstractClassSecond, SimpleSealedAbstractClassThird)
    }
    "Map case objects by name" in {
      SealedClassUtilities.indexCaseObjects[SimpleSealedAbstractClass](_.key) must_===
        Map("first_alpha" -> SimpleSealedAbstractClassFirst,
          "second_bravo" -> SimpleSealedAbstractClassSecond,
          "third_charlie" -> SimpleSealedAbstractClassThird)
    }
  }


  "With ComplexSealedTrait" should {
    "Enumerate case objects" in {
      SealedClassUtilities.enumerateCaseObjects[ComplexSealedTrait] must_===
        Set(ComplexSealedTraitFirst, ComplexSealedTraitSecond, ComplexSealedTraitThird, ComplexSealedTraitFourth,
          ComplexSealedTraitFifth, ComplexSealedTraitSixth, ComplexSealedTraitSeventh, ComplexSealedTraitEighth)
    }
    "Map case objects by name" in {
      SealedClassUtilities.indexCaseObjects[ComplexSealedTrait](_.key) must_===
        Map("first_alpha" -> ComplexSealedTraitFirst,
          "second_bravo" -> ComplexSealedTraitSecond,
          "third_charlie" -> ComplexSealedTraitThird,
          "fourth_delta" -> ComplexSealedTraitFourth,
          "fifth_echo" -> ComplexSealedTraitFifth,
          "sixth_foxtrot" -> ComplexSealedTraitSixth,
          "seventh_golf" -> ComplexSealedTraitSeventh,
          "eighth_hotel" -> ComplexSealedTraitEighth)


    }
  }

}
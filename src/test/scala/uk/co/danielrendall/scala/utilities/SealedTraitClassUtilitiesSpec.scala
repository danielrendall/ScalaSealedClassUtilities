package uk.co.danielrendall.scala.utilities

import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification
import uk.co.danielrendall.scala.utilities.ComplexSealedTrait.{ComplexSealedTraitSecond, ComplexSealedTraitThird}
import uk.co.danielrendall.scala.utilities.ComplexSealedTraitSixth.ComplexSealedTraitSeventh
import uk.co.danielrendall.scala.utilities.ComplexSealedTraitSixth.ComplexSealedTraitSeventh.ComplexSealedTraitEighth
import uk.co.danielrendall.scala.utilities.SealedClassUtilities.NotSealedException
import uk.co.danielrendall.scala.utilities.UnrelatedNotEvenCompanion.ComplexSealedTraitFourth
import uk.co.danielrendall.scala.utilities.UnrelatedNotEvenCompanion.ComplexSealedTraitFourth.ComplexSealedTraitFifth

import java.util.concurrent.{Executors, TimeUnit}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}

class SealedTraitClassUtilitiesSpec extends Specification {

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

  "With NestedTrait" should {
    "Enumerate case objects of NestedTrait" in {
      SealedClassUtilities.enumerateCaseObjects[NestedTrait] must_===
        Set(DoNothing,
          Vague.DoSomething,
          Vague.DoSomethingElse,
          DoSomethingHelpful,
          GoodIdeas.DoSomethingHelpful,
          GoodIdeas.DoSomethingKind,
          GoodIdeas.DoSomethingWonderful,
          GoodIdeas.DoSomethingWonderful.DoSomethingSuperWonderful,
          DoSomethingEvil,
          BadIdeas.DoSomethingUnpleasant,
          BadIdeas.DoSomethingUnkind,
          BadIdeas.DoSomethingTerrible,
          BadIdeas.DoSomethingTerrible.DoSomethingSuperTerrible)
    }

    "Enumerate case objects of GoodIdeas" in {
      SealedClassUtilities.enumerateCaseObjects[GoodIdeas] must_===
        Set(DoSomethingHelpful,
          GoodIdeas.DoSomethingHelpful,
          GoodIdeas.DoSomethingKind,
          GoodIdeas.DoSomethingWonderful,
          GoodIdeas.DoSomethingWonderful.DoSomethingSuperWonderful)
    }

    "Enumerate case objects of BadIdeas" in {
      SealedClassUtilities.enumerateCaseObjects[BadIdeas] must_===
        Set(DoSomethingEvil,
          BadIdeas.DoSomethingUnpleasant,
          BadIdeas.DoSomethingUnkind,
          BadIdeas.DoSomethingTerrible,
          BadIdeas.DoSomethingTerrible.DoSomethingSuperTerrible)
    }

    "Enumerate case objects of Vague.Something" in {
      SealedClassUtilities.enumerateCaseObjects[Vague.Something] must_===
        Set(Vague.DoSomething,
          Vague.DoSomethingElse,
          GoodIdeas.DoSomethingHelpful,
          BadIdeas.DoSomethingUnpleasant)
    }
  }

  "With UnsealedTrait" should {
    "Throw exception" in {
      SealedClassUtilities.enumerateCaseObjects[UnsealedTrait] must
        throwA[NotSealedException]("Can't enumerate non-sealed class: trait UnsealedTrait")
    }
  }
  "With SealedTrait" should {
    "Throw exception" in {
      SealedClassUtilities.enumerateCaseObjects[SealedTrait] must
        throwA[NotSealedException]("Can't enumerate non-sealed class: trait UnsealedChildTrait")
    }
  }

  "Getting the answer from multiple threads" should {
    "Return the same object to each thread" in {
      val service = Executors.newFixedThreadPool(8)
      try {
        implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(service)

        val futures: Seq[Future[(Int, Set[NestedTrait])]] = (0 until 1000).map { id =>
          Future{
            (id, SealedClassUtilities.enumerateCaseObjects[NestedTrait])
          }
        }

        Await.result(Future.sequence(futures), Duration(10, TimeUnit.SECONDS)).toList match {
          case Nil => throw new Exception("Should have had some futures!")
          case head :: tail =>
            val headSet = head._2
            // IntelliJ complains about the next line
            tail.map(_._2) must contain(allOf(beEqualTo(headSet)))
        }

      } finally {
        service.shutdown()
      }

    }
  }
}

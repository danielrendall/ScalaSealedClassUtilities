package uk.co.danielrendall.scala.utilities

/**
 * This is intentionally a rats' nest of stupidity
 */
sealed trait NestedTrait

case object DoNothing extends NestedTrait

object Vague {

  sealed trait Something

  case object DoSomething extends NestedTrait with Something

  case object DoSomethingElse extends NestedTrait with Something
}

sealed trait GoodIdeas extends NestedTrait

case object DoSomethingHelpful extends GoodIdeas

object GoodIdeas {

  case object DoSomethingHelpful extends GoodIdeas with Vague.Something

  case object DoSomethingKind extends GoodIdeas

  class SuperThing

  case object DoSomethingWonderful extends GoodIdeas {

    case object DoSomethingSuperWonderful extends SuperThing with GoodIdeas
  }
}

sealed trait BadIdeas extends NestedTrait

case object DoSomethingEvil extends BadIdeas

object BadIdeas {
  case object DoSomethingUnpleasant extends BadIdeas with Vague.Something

  case object DoSomethingUnkind extends BadIdeas

  class SuperThing

  case object DoSomethingTerrible extends BadIdeas {

    case object DoSomethingSuperTerrible extends SuperThing with BadIdeas
  }

}

package uk.co.danielrendall.scala.utilities

trait UnsealedTrait

case object FirstUnsealedTrait extends UnsealedTrait


sealed trait SealedTrait

case object FirstSealedTrait extends SealedTrait

trait UnsealedChildTrait extends SealedTrait

case object FirstUnsealedChildTrait extends UnsealedChildTrait

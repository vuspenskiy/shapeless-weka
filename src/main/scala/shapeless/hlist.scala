package shapeless

import shapeless.ops.hlist.{Length, Zip}
import shapeless.extensions._

/**
 * Method on `HList` to zip with range of `Nat` from zero to `HList` length minus one
 *
 * @author Travis Brown
 */

object HListExtensions {

  def zipWithIndex[L <: HList, S <: Nat, R <: HList, Out <: HList](l: L)
    (implicit len: Length.Aux[L, S], range: Range.Aux[nat._0, S, R], zipper: Zip.Aux[L :: R :: HNil, Out]): Out =
    l.zip(range())
}

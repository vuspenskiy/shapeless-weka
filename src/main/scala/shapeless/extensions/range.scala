package shapeless.range

import shapeless._
import shapeless.ops.hlist.Prepend

/**
 * `Nat` range from A to B represented by HList
 *
 * @author Travis Brown
 */

trait Range[A <: Nat, B <: Nat] extends DepFn0 { type Out <: HList }

object Range {
  type Aux[A <: Nat, B <: Nat, Out0 <: HList] = Range[A, B] { type Out = Out0 }

  implicit def emptyRange[A <: Nat]: Aux[A, A, HNil] = new Range[A, A] {
    type Out = HNil
    def apply(): Out = HNil
  }

  implicit def slightlyBiggerRange[A <: Nat, B <: Nat, OutAB <: HList]
    (implicit rangeAB: Aux[A, B, OutAB],
              appender: Prepend[OutAB, B :: HNil],
              witnessB: Witness.Aux[B]): Aux[A, Succ[B], appender.Out] = new Range[A, Succ[B]] {
    type Out = appender.Out
    def apply(): Out = appender(rangeAB(), witnessB.value :: HNil)
  }

  def range[A <: Nat, B <: Nat](implicit r: Range[A, B]): r.Out = r()
}

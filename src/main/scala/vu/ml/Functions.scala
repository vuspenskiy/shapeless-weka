package vu.ml

import shapeless._
import shapeless.labelled._
import shapeless.nat._
import shapeless.ops.nat.ToInt
import vu.ml.Weka._
import weka.core.{Attribute, Instance}

/**
 * Polymorphic functions on Weka primitives for generalisation of the Weka algoritms
 * 
 * @author v.uspenskiy
 * @since 18/03/15
 */

object Functions {

  object getDistinctValues extends Poly1 {
    implicit def caseFieldDouble [K](implicit wk: Witness.Aux[K]) = at[FieldType[K, Option[Double]]]  { field => wk.value -> List[Double]() }
    implicit def caseFieldLong   [K](implicit wk: Witness.Aux[K]) = at[FieldType[K, Option[Long]]]    { field => wk.value -> List[Long]() }
    implicit def caseFieldBoolean[K](implicit wk: Witness.Aux[K]) = at[FieldType[K, Option[Boolean]]] { field => wk.value -> field.toList /* List[Boolean] */ }
    implicit def caseFieldString [K](implicit wk: Witness.Aux[K]) = at[FieldType[K, Option[String]]]  { field => wk.value -> field.toList /* List[String] */ }
  }

  object combineValues extends Poly1 {
    implicit def caseAny[K,V] = at[((K, List[V]), (K, List[V]))](fields => fields._1._1 -> (fields._1._2 ++ fields._2._2).distinct)
  }

  object size extends Poly1 {
    implicit def caseList[K,V]  = at[(K, List[V])](_._2.size)
  }

  object createAttribute extends Poly1 {
    implicit def caseDouble [K] = at[(K, List[Double])]  { values => new Attribute(values._1.toString) }
    implicit def caseLong   [K] = at[(K, List[Long])]    { values => new Attribute(values._1.toString) }
    implicit def caseBoolean[K] = at[(K, List[Boolean])] { values => new Attribute(values._1.toString, values._2.map(_.toString)) }
    implicit def caseString [K] = at[(K, List[String])]  { values => new Attribute(values._1.toString, values._2) }
  }

  object fillExample extends Poly1 {
    implicit def caseDouble [K] = at[((Attribute, FieldType[K, Option[Double]] ), Instance)] { fve => if(fve._1._2.isDefined) fve._2.setValue(fve._1._1, fve._1._2.get)          else fve._2.setMissing(fve._1._1); fve._2 }
    implicit def caseLong   [K] = at[((Attribute, FieldType[K, Option[Long]]   ), Instance)] { fve => if(fve._1._2.isDefined) fve._2.setValue(fve._1._1, fve._1._2.get.toDouble) else fve._2.setMissing(fve._1._1); fve._2 }
    implicit def caseBoolean[K] = at[((Attribute, FieldType[K, Option[Boolean]]), Instance)] { fve => if(fve._1._2.isDefined) fve._2.setValue(fve._1._1, fve._1._2.get.toString) else fve._2.setMissing(fve._1._1); fve._2 }
    implicit def caseString [K] = at[((Attribute, FieldType[K, Option[String]] ), Instance)] { fve => if(fve._1._2.isDefined) fve._2.setValue(fve._1._1, fve._1._2.get)          else fve._2.setMissing(fve._1._1); fve._2 }
  }

  object fitWithInstanceData extends Poly1 {

    implicit def caseDouble[K, N <: Nat: ToInt] =
      at[((FieldType[K, Option[Double]], N), Instance)] { fv => field[K](Option(fv._2.value(toInt[N]))) }

    implicit def caseBoolean[K, N <: Nat: ToInt] =
      at[((FieldType[K, Option[Boolean]], N), Instance)] { fv => field[K](Option("true".equals(fv._2.stringValue(toInt[N])))) }

    implicit def caseString[K, N <: Nat: ToInt] =
      at[((FieldType[K, Option[String]], N), Instance)] { fv => field[K](Option(fv._2.stringValue(toInt[N]))) }

    implicit def caseLong[K, N <: Nat: ToInt] =
      at[((FieldType[K, Option[Long]], N), Instance)] { fv => field[K](Option(fv._2.value(toInt[N]).toLong)) }
  }
}

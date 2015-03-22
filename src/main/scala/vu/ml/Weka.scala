package vu.ml

import weka.core.FastVector
import vu._

/**
 * Scala conversions for Weka library
 *
 * @author v.uspenskiy
 * @since 18/07/14 00:23
 */

object Weka {

  // Makes Weka `FastVector` from Scala `Iterable` implicitly
  implicit def iterableToFastVector[T](iterable: Iterable[T]): FastVector =
    make(new FastVector(iterable.size)) { vector => iterable.foreach(vector.addElement) }

  // Makes Scala `Iterator` from `java.util.Enumeration`
  def enumerate[T](enumeration: java.util.Enumeration[T]) = {
    Iterator.continually {
      enumeration.nextElement()
    } takeWhile { _ =>
      enumeration.hasMoreElements
    }
  }
}

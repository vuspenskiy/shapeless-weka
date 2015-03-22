package vu.ml

/**
 * @author v.uspenskiy
 * @since 30/01/14 01:56
 */

case class Id[+T](value: String)

case class Cluster[T](id: Id[Cluster[T]], objects: Array[T], typicalObject: T)

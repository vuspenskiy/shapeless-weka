package vu.ml

/**
 * @author v.uspenskiy
 * @since 30/01/14 01:56
 */

case class Id[+T](value: String)

// Fields' order sets importance for classifier (most important first)
case class Operation(brand: Option[String],
                     mcc: Option[String],
                     category: Option[String],
                     isCardNotPresent: Option[Boolean],
                     city: Option[String],
                     country: Option[String],
                     amount: Option[Double],
                     currency: Option[String],
                     group: Option[String],
                     isPaymentGate: Option[Boolean],
                     isRegular: Option[Boolean],
                     time: Option[Long],
                     timeOfDay: Option[String],
                     isWeekend: Option[Boolean],
                     isWorkingTime: Option[Boolean])

case class Cluster[T](id: Id[Cluster[T]], objects: Array[T], typicalObject: T)

package vu.ml

import shapeless._
import shapeless.ops.hlist._
import shapeless.extensions._
import shapeless.extensions.HListExtensions._
import HListExtensions._
import vu._
import weka.core.{Attribute, Instance, Instances}
import weka.clusterers.SimpleKMeans
import shapeless._
import scala.reflect.ClassTag
import vu.ml.Weka._
import Functions._

/**
 * Generalized with shapeless version of Weka `SimpleKMeans` 
 * independent of specific clustered objects
 * 
 * @author v.uspenskiy, Miles Sabin, Eugene Burmako
 * @since 30/01/14 01:56
 */

class KMeans[T : ClassTag, R <: HList, R2 <: HList, R3 <: HList, R4 <: HList, R5 <: HList, R6 <: HList,
             R7 <: HList, R8 <: HList, R9 <: HList, R10 <: HList, R11 <: HList, R12 <: HList, L <: Nat]
        (objectsList: List[T], clusterGrowthFactor: Int = 12)
        (implicit objectGeneric: LabelledGeneric.Aux[T, R],
           gdvm: Mapper.Aux[Functions.getDistinctValues.type, R, R2],
           dvz: Zip.Aux[R2 :: R2 :: HNil, R3],
           cvm: Mapper.Aux[Functions.combineValues.type, R3, R2],
           sm: Mapper.Aux[Functions.size.type, R2, R4],
           vtt: ToTraversable.Aux[R4, List, Int],
           cam: Mapper.Aux[Functions.createAttribute.type, R2, R5],
           fu: Unifier.Aux[R5, R6],
           uft: ToTraversable.Aux[R6, List, Attribute],
           len: Length.Aux[R, L],
           fz: Zip.Aux[R5 :: R :: HNil, R7],
           fgez: ZipConst.Aux[Instance, R7, R10],
           gem: Mapper.Aux[Functions.fillExample.type, R10, R11],
           rng: Range.Aux[nat._0, L, R8],
           rfz: Zip.Aux[R :: R8 :: HNil, R9],
           riz: ZipConst.Aux[Instance, R9, R12],
           rfm: Mapper.Aux[Functions.fitWithInstanceData.type, R12, R]) {

  def clusters: Seq[Cluster[T]] = objectsList match {
    case Nil => Nil
    case objects =>
      // Building features
      val generics = objects.map(objectGeneric.to)
      val values = generics.map(_.map(getDistinctValues)).reduce((dv1, dv2) => (dv1 zip dv2).map(combineValues))
      val cardinality = values.map(size).toList.sum
      val clusterAmount = scala.math.min(cardinality / clusterGrowthFactor, objects.size - 1)
      val features = values.map(createAttribute)

      // Building examples and creating classifier
      val kmeans = make(new SimpleKMeans()) { clusterer =>
        val instances = make(new Instances("objects", iterableToFastVector(features.unify.toList), objects.size)) { instances =>
          objects foreach { obj =>
            instances.add(make(new Instance(features.runtimeLength)) { example =>
              features.zip(objectGeneric.to(obj)).zipConst(example).map(fillExample)
            })
          }
        }

        clusterer.setNumClusters(clusterAmount)
        clusterer.setPreserveInstancesOrder(true)
        clusterer.buildClusterer(instances)
      }

      val centroids = enumerate(kmeans.getClusterCentroids.enumerateInstances())
      val objectsByCluster = kmeans.getAssignments.zip(objects).groupBy(_._1)

      val reference = objectGeneric.to(objects.head)

      val clusters = centroids.zipWithIndex map {
        case (instance, index) =>
          Cluster[T](
            Id(index.toString),
            objectsByCluster.getOrElse(index, Array[(Int, T)]()).map(_._2),
            objectGeneric.from(
              zipWithIndex(reference).zipConst(instance.asInstanceOf[Instance]).map(fitWithInstanceData)
            )
          )
      } toSeq

      Option(clusters).filterNot(_.isEmpty && objects.isEmpty) getOrElse {
        List(Cluster[T](Id("0"), objects.toArray, objects.head))
      }
  }
}

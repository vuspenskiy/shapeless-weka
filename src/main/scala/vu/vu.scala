
/**
 * Generic util functions
 */

package object vu {

  def make[T](p: => T)(s: T => Unit) = { val v = p; s(v); v }
}

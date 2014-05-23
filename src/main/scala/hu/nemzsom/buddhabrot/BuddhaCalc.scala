package hu.nemzsom.buddhabrot

import scala.util.Random
import scala.math._
import scala.annotation.tailrec

case class IterationSeq(seq: Seq[Complex], iter: Int, escaped: Boolean)

class BuddhaCalc(val reFrom: Double, val reTo: Double, val imFrom: Double, val imTo: Double) {

  import BuddhaCalc._

  require(reFrom < reTo)
  require(imFrom < imTo)
  require(
    !Complex(reFrom, imFrom).isInside ||
    !Complex(reFrom, imTo).isInside   ||
    !Complex(reTo, imFrom).isInside   ||
    !Complex(reTo, imTo).isInside
  )

  val rnd = new Random

  protected def nextComplex = {
    def between(from: Double, to: Double) =
      from + (rnd.nextDouble * (to - from))
    val re = between(reFrom, reTo)
    val im = between(imFrom, imTo)
    Complex(re, im)
  }

  def nextSeq(maxIter: Int): IterationSeq = {
    val c = nextComplex
    if (c.isInside) {
      // no need to iterate
      IterationSeq(Seq(c), 1, escaped = false)
    }
    else {
      @tailrec def iterate(iter: Int, z: Complex, seq: List[Complex]): IterationSeq = {
        val escaped = z.escaped
        if (iter >= maxIter || escaped)
          IterationSeq(seq, iter, escaped)
        else {
          val nextZ = z * z + c
          iterate(iter + 1, nextZ, nextZ :: seq)
        }
      }
      iterate(0, Complex.ZERO, List())
    }
  }

  override def toString =
    s"BuddhaCalc(reForm: $reFrom, reTo: $reTo, imFrom: $imFrom, imTo: $imTo)"
}

object BuddhaCalc {

  implicit class ComplexOps(c: Complex) {

    val bailOutRadius = 2

    def escaped: Boolean = c.re * c.re + c.im * c.im > bailOutRadius * bailOutRadius

    /** Optimization: Cardioid / bulb checking
      * from http://en.wikipedia.org/wiki/Mandelbrot_set#Cardioid_.2F_bulb_checking
      */
    def isInside: Boolean = {
      val q = pow(c.re - 0.25, 2) + pow(c.im, 2)
      q * (q + (c.re - 0.25)) < pow(c.im, 2) / 4 ||
        pow(c.re + 1, 2) + pow(c.im, 2) < 0.0625
    }
  }
}

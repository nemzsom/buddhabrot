package hu.nemzsom.buddhabrot

import scala.util.Random
import scala.math._
import scala.annotation.tailrec

sealed trait IterationSeq {
  val seq: Seq[Complex]
}
case class Escaped(seq: Seq[Complex]) extends IterationSeq
case class Stayed(seq: Seq[Complex]) extends IterationSeq

trait BuddhaCalc {

  import BuddhaCalc._

  val reFrom: Double
  val reTo:   Double
  val imFrom: Double
  val imTo:   Double

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

  protected def nextSeq(maxIter: Int): IterationSeq = {
    val c = nextComplex
    if (c.isInside) {
      Stayed(Seq(c))
    }
    else {
      @tailrec def loop(iter: Int, z: Complex, seq: List[Complex]): (Seq[Complex], Boolean) = {
        val escaped = z.escaped
        if (iter >= maxIter || escaped)
          (seq, escaped)
        else {
          val nextZ = z * z + c
          loop(iter + 1, nextZ, nextZ :: seq)
        }
      }
      val (seq, escaped) = loop(0, Complex.ZERO, List())
      if (escaped) Escaped(seq)
      else Stayed(seq)
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

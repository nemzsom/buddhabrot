package hu.nemzsom.buddhabrot

case class Complex(re: Double, im: Double) {

  def +(c: Complex) = new Complex(re + c.re, im + c.im)
  def *(c: Complex) = new Complex(re * c.re - im * c.im, im * c.re + re * c.im)
}

object Complex {

  val ZERO = Complex(0, 0)
}

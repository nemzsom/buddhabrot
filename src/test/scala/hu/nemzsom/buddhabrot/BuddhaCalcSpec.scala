package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.{Arbitrary, Gen}

class BuddhaCalcSpec extends FlatSpec with GeneratorDrivenPropertyChecks with Matchers {

  import BuddhaCalc._

  "BuddhaCalc" should "produce correct random complexes" in {
    forAll { (calc: TestCalc) =>
      val complexes = 1 to 20 map { _ =>
        calc.next
      }
      // all inside the range
      complexes forall { case Complex(re, im) =>
        re >= calc.reFrom && re <= calc.reTo &&
          im >= calc.imFrom && im <= calc.imTo
      } shouldBe true
      // all unique
      complexes.distinct shouldEqual complexes
    }
  }

  it should "produce iteration sequences" in {
    val maxIter = 10
    val calc = new TestCalc(-2.0, 2.0, -2.0, 2.0)
    val sequences: IndexedSeq[IterationSeq] = 1 to 100 map (_ => calc.seq(maxIter))
    // there should be escaped as well as stayed points
    Set(true, false) forall { escaped =>
      sequences exists (_.escaped == escaped)
    } shouldBe true
    // there should be at least 5 sequence with different size
    (sequences map (_.seq.size)).distinct.length should be > 5
    // there should be no sequence that size's greater than maxIter
    sequences foreach (_.seq.size should be <= maxIter)
  }

  it should "produce correct iteration numbers" in {
    val maxIter = 10
    forAll { (calc: TestCalc) =>
      val seq = calc.nextSeq(maxIter)
      seq.seq.length shouldEqual seq.iter
    }
  }


  implicit lazy val arbCalc: Arbitrary[TestCalc] = Arbitrary(
    for {
      reFrom <- Gen.choose(-100.0, 99.0)
      reTo <- Gen.choose(reFrom, 100.0)
      imFrom <- Gen.choose(-100.0, 99.0)
      imTo <- Gen.choose(imFrom, 100.0)
      if !Complex(reFrom, imFrom).isInside ||
         !Complex(reFrom, imTo).isInside   ||
         !Complex(reTo, imFrom).isInside   ||
         !Complex(reTo, imTo).isInside
    } yield new TestCalc(reFrom, reTo, imFrom, imTo)
  )
}

class TestCalc(reFrom: Double, reTo: Double, imFrom: Double, imTo: Double) extends BuddhaCalc(reFrom, reTo, imFrom, imTo) {

  def next = nextComplex

  def seq(maxIter: Int) = nextSeq(maxIter)
}

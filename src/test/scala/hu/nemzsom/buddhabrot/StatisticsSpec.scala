package hu.nemzsom.buddhabrot

import org.scalatest.{FlatSpec, Matchers}
import hu.nemzsom.buddhabrot.util.Statistics

class StatisticsSpec extends FlatSpec with Matchers {

  "Statistics" should "calculate sums correctly" in {
    val stats = new Statistics(3)
    stats.iters(10)
    stats.tick() shouldBe 30
    stats.iters(20)
    stats tick() shouldBe 45
    stats.iters(30)
    stats.tick() shouldBe 60
    stats.iters(30)
    stats.tick() shouldBe 80
    stats.iters(30)
    stats.tick() shouldBe 90
    stats.iters(10)
    stats.tick() shouldBe 70
  }

}

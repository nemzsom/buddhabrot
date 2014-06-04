package hu.nemzsom.buddhabrot.util

class Statistics(val ticksPerStat: Int) {

  var sampleCount = 0

  private val iterCounts = new Array[Long](ticksPerStat)
  private var allTicks = 0
  private var actIndex = 0

  def iters(count: Long): Unit = {
    val old = iterCounts(actIndex)
    iterCounts(actIndex) = old + count
  }

  def tick(): Long = {
    allTicks = allTicks + 1
    actIndex = allTicks % ticksPerStat
    val sum = {
      if (allTicks < ticksPerStat)
        iterCounts.sum * ticksPerStat / allTicks
      else
        iterCounts.sum
    }
    iterCounts(actIndex) = 0
    sum
  }

}

package hu.nemzsom.buddhabrot

class Statistics(val ticksPerStat: Int) {

  var pathCount = 0

  private val iterCounts = new Array[Int](ticksPerStat)
  private var allTicks = 0
  private var actIndex = 0

  def iters(count: Int): Unit = {
    val old = iterCounts(actIndex)
    iterCounts(actIndex) = old + count
  }

  def tick(): Int = {
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

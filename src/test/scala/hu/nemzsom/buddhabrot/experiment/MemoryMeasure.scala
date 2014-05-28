package hu.nemzsom.buddhabrot.experiment

import java.awt.image.BufferedImage
import hu.nemzsom.buddhabrot.Grid

object MemoryMeasure extends App {

  measureImg(1000, 1000)
  measureGrid(1000, 1000)
  measureImg(6000, 5000)
  measureGrid(6000, 5000)
  measureImg(6000, 15000)
  measureGrid(6000, 15000)
  measureImg(12000, 10000)
  measureGrid(12000, 10000)
  measureImg(15000, 15000)
  measureGrid(15000, 15000)

  def measureImg(width: Int, height: Int): Unit = {
    val before = getMemoryInBytes
    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val after = getMemoryInBytes
    val size = width * height
    val mem = after - before
    val memPer1000px = mem * 1000 / size
    println(s"Memory for $width×$height pixels: ${(mem) / 1024} KB (${mem / 1024 / 1024} MB). $memPer1000px byte/1000px")
  }

  def measureGrid(width: Int, height: Int): Unit = {
    val before = getMemoryInBytes
    val grid = new Grid(width, height, 0.0, height, 0.0, width)
    val after = getMemoryInBytes
    val size = width * height
    val mem = after - before
    val memPer1000point = mem * 1000 / size
    println(s"Memory for $width×$height grid: ${(mem) / 1024} KB (${mem / 1024 / 1024} MB). $memPer1000point byte/1000point")
  }

  def getMemoryInBytes: Long = {
    val runtime = Runtime.getRuntime
    for (x <- 1 to 16) System.gc()
    runtime.totalMemory - runtime.freeMemory
  }
}

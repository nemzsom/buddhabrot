package hu.nemzsom.buddhabrot

import java.awt.image.{DataBufferInt, BufferedImage}

object ImageBuilder {

  val scaler = Scale.linear(0, 255)_

  def build(grid: Grid): BufferedImage = {
    val img = new BufferedImage(grid.width, grid.height, BufferedImage.TYPE_INT_RGB)
    val pixels = img.pixels
    val (min, max) = minMax(grid)
    val scale = scaler(min, max)
    grid.zipWithIndex foreach { case (dense, i) =>
      val n = scale(dense)
      pixels(i) = n | n << 8 | n << 16
    }
    img
  }

  def preview(grid: Grid, outerWidth: Int, outerHeight: Int): BufferedImage = {
    val (width, height) = getTargetDimension(grid.width, grid.height, outerWidth, outerHeight)
    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val hScale = grid.width.toDouble / width
    val vScale = grid.height.toDouble / height
    val (min, max) = minMax(grid)
    val scale = scaler(min, max)
    for {
      y <- 0 to height - 1
      x <- 0 to width - 1
    } {
      val dense = grid.get((x * hScale).toInt, (y * vScale).toInt)
      val n = scale(dense)
      img.setRGB(x, y, n | n << 8 | n << 16)
    }
    img
  }

  def minMax(xs: Iterable[Int]): (Int, Int) =
    xs.foldLeft((Integer.MAX_VALUE, 0)) { case ((min, max), x) =>
      (
        if (x < min) x else min,
        if (x > max) x else max
      )
    }

  def getTargetDimension(sourceWidth: Int, sourceHeight: Int, outerWidth: Int, outerHeight: Int): (Int, Int) = {
    val sourceScale = sourceWidth.toDouble / sourceHeight
    val outerScale = outerWidth.toDouble / outerHeight
    if (outerScale < sourceScale) {
      val width = outerWidth
      val height = (width / sourceScale).toInt
      (width, height)
    }
    else {
      val height = outerHeight
      val width = (sourceScale * height).toInt
      (width, height)
    }
  }


  implicit class BufferedImageOps(img: BufferedImage) {

    def pixels: Array[Int] = {
        val raster = img.getRaster
        val databuffer: DataBufferInt = raster.getDataBuffer.asInstanceOf[DataBufferInt]
        databuffer.getData
    }
  }
}

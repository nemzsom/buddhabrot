package hu.nemzsom.buddhabrot

import java.awt.image.{DataBufferInt, BufferedImage}

case class RGBScales(rScale: Int => Int, gScale: Int => Int, bScale: Int => Int)

object ImageBuilder {

  def build(grids: Seq[(Grid, Instance)]): BufferedImage = {
    val firstGrid = grids.head._1
    val img = new BufferedImage(firstGrid.width, firstGrid.height, BufferedImage.TYPE_INT_RGB)
    val pixels = img.pixels
    val buddhas = getBuddhas(grids)
    pixels.indices foreach { i=>
      pixels(i) = getPixel(i, buddhas)
    }
    img
  }

  def preview(grids: Seq[(Grid, Instance)], outerWidth: Int, outerHeight: Int): BufferedImage = {
    val firstGrid = grids.head._1
    val (width, height) = getTargetDimension(firstGrid.width, firstGrid.height, outerWidth, outerHeight)
    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val hScale = firstGrid.width.toDouble / width
    val vScale = firstGrid.height.toDouble / height
    val buddhas = getBuddhas(grids)
    for {
      y <- 0 to height - 1
      x <- 0 to width - 1
    } {
      val gridIndex = firstGrid.indexFor((x * hScale).toInt, (y * vScale).toInt)
      getPixel(gridIndex, buddhas)
      img.setRGB(x, y, getPixel(gridIndex, buddhas))
    }
    img
  }

  def getBuddhas(grids: Seq[(Grid, Instance)]): Seq[(Array[Int], RGBScales, (Double, Double, Double))] =
    grids map { case (grid, instance) =>
      val (_, max) = minMax(grid)
      (
        grid.data,
        RGBScales(instance.rScaler(0, max), instance.gScaler(0, max), instance.bScaler(0, max)),
        instance.rgbWeights
        )
    }

  def getPixel(index: Int, buddhas: Seq[(Array[Int], RGBScales, (Double, Double, Double))]): Int ={
    var r, g, b = 0.0
    buddhas.foreach { case (data, RGBScales(rScale, gScale, bScale), (rWeight, gWeight, bWeight)) =>
      val dense = data(index)
      r = r + rScale(dense) * rWeight
      g = g + gScale(dense) * gWeight
      b = b + bScale(dense) * bWeight
    }
    r.round.toInt << 16 | g.round.toInt << 8 | b.round.toInt
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

package hu.nemzsom.buddhabrot

import java.awt.image.{DataBufferInt, BufferedImage}

object ImageUtil {

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

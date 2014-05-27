package hu.nemzsom.buddhabrot

object Scale {

  def linear(rangeFrom: Int, rangeTo: Int)(min: Int, max: Int)(x: Int): Int = {
    if (x < min) rangeFrom
    else if (x > max) rangeTo
    else {
      val targetSize = rangeTo - rangeFrom
      val sourceSize = max - min
      val scale = targetSize.toDouble / sourceSize
      val result = (scale * (x - min)).round.toInt + rangeFrom
      if (result < rangeFrom) rangeFrom
      else result
    }
  }

}

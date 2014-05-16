package hu.nemzsom.buddhabrot

case class Config(width: Int, height: Int, imFrom: Double, imTo: Double, reFrom: Double) {

  val reTo = (imTo - imFrom) * height / width + reFrom

}

object Config {

  val default = Config(
    width = 480,
    height = 640,
    imFrom = -2.0,
    imTo = 2.0,
    reFrom = -2.0
  )

}
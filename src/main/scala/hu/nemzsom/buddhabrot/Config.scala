package hu.nemzsom.buddhabrot

case class Config(width: Int, height: Int, reFrom: Double, reTo: Double, imFrom: Double) {

  val imTo = (reTo - reFrom) * width / height + imFrom

}

case object DefaultConfig extends Config(
  width = 480,
  height = 640,
  reFrom = -2.0,
  reTo = 2.0,
  imFrom = 2.0
)
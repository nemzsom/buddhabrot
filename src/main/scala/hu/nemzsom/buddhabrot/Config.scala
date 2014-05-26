package hu.nemzsom.buddhabrot

case class Config(width: Int, height: Int, imFrom: Double, imTo: Double, reFrom: Double, maxIter: Int, outDir: String, sampleFactor: Int) {

  val reTo = (imTo - imFrom) * height / width + reFrom
  val samples = width * height * sampleFactor

}

object Config {

  val default = Config(
    width = 1500,
    height = 1500,
    imFrom = -2.0,
    imTo = 2.0,
    reFrom = -2.0,
    maxIter = 2000,
    outDir = "/tmp/buddhabrot",
    sampleFactor = 100
  )
}
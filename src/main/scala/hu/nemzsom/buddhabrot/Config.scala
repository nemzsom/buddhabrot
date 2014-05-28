package hu.nemzsom.buddhabrot

import scala.swing.Color

case class Config(width: Int, height: Int, imFrom: Double, imTo: Double, reFrom: Double,
                  outDir: String,
                  scaler: (Int, Int) => ((Int, Int) => (Int => Int)),
                  instances: List[Instance]) {

  val reTo = (imTo - imFrom) * height / width + reFrom

}

case class Variation(maxIter: Int, sampleFactor: Int, color: Color, rgbWeights: (Double, Double, Double))

case class Instance(maxIter: Int, samples: Int, color: Color, rgbWeights: (Double, Double, Double), scaler: (Int, Int) => ((Int, Int) => (Int => Int))) {
  
  val rScaler = scaler(0, color.getRed)
  val gScaler = scaler(0, color.getGreen)
  val bScaler = scaler(0, color.getBlue)
}

object Config {
  
  def apply(width: Int, height: Int, imFrom: Double, imTo: Double, reFrom: Double,
            outDir: String,
            scaler: (Int, Int) => ((Int, Int) => (Int => Int)),
            variations: Variation*): Config = {
    val instances = variations.map { v =>
      Instance(v.maxIter, v.sampleFactor * width * height, v.color, v.rgbWeights, scaler)
    }
    val weightSums = instances.foldLeft(List(0.0, 0.0, 0.0)) { case (List(rSum, gSum, bSum), Instance(_, _, _, (r, g, b), _)) =>
      List(rSum + r, gSum + g, bSum + b)
    }
    require(weightSums.forall(_ ~= 1.0), s"rgbWeights should sum to 1.0 ($weightSums)")
    Config(width, height, imFrom, imTo, reFrom,
            outDir,
            scaler,
            instances.toList)
  }

  implicit class DoubleWithAlmostEquals(val d:Double) extends AnyVal {
    def ~=(d2:Double) = (d - d2).abs < 0.0000001
  }
}

object Configs {

  val defOutDir = "/tmp/buddhabrot"

  val simple = Config(
    width = 1000,
    height = 1000,
    imFrom = -2.0,
    imTo = 2.0,
    reFrom = -2.0,
    outDir = defOutDir,
    Scale.linear _,
    Variation(
      maxIter = 200,
      sampleFactor = 100,
      color = new Color(255, 255, 255),
      rgbWeights = (1, 1, 1)
    )
  )

  val nebulaBrot = Config(
    width = 1000,
    height = 1000,
    imFrom = -2.0,
    imTo = 2.0,
    reFrom = -2.0,
    outDir = defOutDir,
    Scale.linear _,
    Variation(
      maxIter = 200,
      sampleFactor = 100,
      color = new Color(255, 0, 0),
      rgbWeights = (1, 0, 0)
    ),
    Variation(
      maxIter = 2000,
      sampleFactor = 100,
      color = new Color(0, 255, 0),
      rgbWeights = (0, 1, 0)
    ),
    Variation(
      maxIter = 20000,
      sampleFactor = 100,
      color = new Color(0, 0, 255),
      rgbWeights = (0, 0, 1)
    )
  )
}
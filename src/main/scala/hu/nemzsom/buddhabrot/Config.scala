package hu.nemzsom.buddhabrot

case class Config(width: Int, height: Int, imFrom: Double, imTo: Double, reFrom: Double,
                  outRoot: String,
                  instances: Instance*) {

  val reTo = (imTo - imFrom) * height / width + reFrom
  val outDir = outRoot + s"/$width×$height/real[$reFrom - $reTo]_imag[$imFrom - $imTo]"

}

case class Instance(maxIter: Int, sampleFactor: Int) {

  val fileName = s"maxIter[$maxIter]_sampleFactor[$sampleFactor]"
  val gridFilename = fileName + ".grid"
  val stateFilename = fileName + ".state"
}

object Configs {

  val defOutDir = System.getProperty("user.home") + "/.buddhabrot"

  val simple = Config(
    width = 1000,
    height = 1000,
    imFrom = -2.0,
    imTo = 2.0,
    reFrom = -2.0,
    outRoot = defOutDir,
    Instance(
      maxIter = 20000,
      sampleFactor = 100
    )
  )

  val combined = Config(
    width = 1000,
    height = 1000,
    imFrom = -2.0,
    imTo = 2.0,
    reFrom = -2.0,
    outRoot = defOutDir,
    Instance(
      maxIter = 200,
      sampleFactor = 100
    ),
    Instance(
      maxIter = 2000,
      sampleFactor = 100
    ),
    Instance(
      maxIter = 20000,
      sampleFactor = 100
    ),
    Instance(
      maxIter = 20000,
      sampleFactor = 100
    )

  )
}
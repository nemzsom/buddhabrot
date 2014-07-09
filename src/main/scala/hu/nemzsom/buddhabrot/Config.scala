package hu.nemzsom.buddhabrot

import java.nio.file.Path
import java.util.UUID
import hu.nemzsom.buddhabrot.util.FileUtil

class Config(val name: String, val width: Int, val height: Int, val imFrom: Double, val imTo: Double, val reFrom: Double,
                  val outRoot: String) extends Ordered[Config] {

  val reTo = (imTo - imFrom) * height / width + reFrom
  val outDir = outRoot + s"/$width×$height/real[$reFrom - $reTo]_imag[$imFrom - $imTo]"

  var globalColorScheme = ColorScheme.BLACK_TO_WHITE
  var coloring = Coloring.Global
  var colorMixing = ColorMixing.Additive

  private var _instances = List.empty[Instance]

  def instances = _instances

  def addInstance(i: Instance) = {
    _instances = (i :: _instances).sorted
  }

  def removeInstance(i: Instance) = {
    _instances = _instances.filterNot(_.id == i.id)
  }

  override def toString: String =
    s"Config[width: $width, height: $height, reFrom: $reFrom, reTo: $reTo, imFrom: $imFrom, imTo: $imTo]"

  override def compare(that: Config): Int =
    if (width != that.width) width compareTo that.width
    else height compareTo that.height
}

case class Instance(maxIter: Int, sampleFactor: Int, id: UUID) extends Ordered[Instance]{

  val fileName = s"maxIter[$maxIter]_sampleFactor[$sampleFactor]"
  val gridFilename = fileName + ".grid"
  val stateFilename = fileName + ".state"

  var colorScheme = ColorScheme.BLACK_TO_WHITE

  override def compare(that: Instance): Int =
    if (maxIter != that.maxIter) maxIter compareTo that.maxIter
    else sampleFactor compareTo that.sampleFactor
}

object ColorMixing extends Enumeration {
  type ColorMixing = Value
  val Additive = Value("Additive")
  val Average = Value("Average")
}

object Coloring extends Enumeration {
  type Coloring = Value
  val Global = Value("Global")
  val Individual = Value("Individual")
}

object Config {

  import FileUtil.getSubDirs

  val defOutDir = System.getProperty("user.home") + "/.buddhabrot"

  def loadFrom(rootDir: Path): List[Config] = {
    getSubDirs(rootDir.toFile) flatMap { dir =>
      val dirName = dir.getName
      val (width, height) = extractDimension(dirName)
      getSubDirs(dir) map { subDir =>
        val (reFrom, _) = extractRange(subDir.getName, "real")
        val (imFrom, imTo) = extractRange(subDir.getName, "imag")
        new Config(width, height, imFrom, imTo, reFrom, dir.toPath.getParent.toAbsolutePath.toString)
      }
    }
  }

  def extractDimension(dirName: String): (Int, Int) =
    """^(\d*)×(\d*)""".r findFirstMatchIn dirName match {
      case Some(m) => (m.group(1).toInt, m.group(2).toInt)
      case None => throw new InvalidConfigException(s"Invalid directory name '$dirName': Dimension not found.")
    }

  def extractRange(dirName: String, rangeName: String): (Double, Double) =
    (rangeName + """\[([^\s]*)\s-\s([^\s]*)\]""").r findFirstMatchIn dirName match {
      case Some(m) =>
        (m.group(1).toDouble, m.group(2).toDouble)
      case None => throw new InvalidConfigException(s"Invalid directory name '$dirName': $rangeName range not found.")
    }

  val deprecated = new Config(
    name = deprecated,
    width = 1000,
    height = 1000,
    imFrom = -2.0,
    imTo = 2.0,
    reFrom = -2.0,
    outRoot = defOutDir
  )
}

class InvalidConfigException(msg: String) extends Exception(msg)
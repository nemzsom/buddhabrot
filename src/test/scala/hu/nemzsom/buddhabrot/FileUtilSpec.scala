package hu.nemzsom.buddhabrot

import org.scalatest.{Matchers, FlatSpec}
import java.nio.file.Files
import hu.nemzsom.buddhabrot.util.FileUtil

class FileUtilSpec extends FlatSpec with Matchers {

  import FileUtil._

  "FileUtil" should "save and restore objects" in {
    val o = List(1, 2)
    val path = Files.createTempFile("fileutil_", ".test")
    write(o, path)
    read(path) shouldEqual o
    Files.delete(path)
  }

}

package hu.nemzsom.buddhabrot

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ImageSaver(outDirStr: String) {
  
  val outDir = new File(outDirStr)
  
  if (outDir.exists()) require(outDir.isDirectory)
  else require(outDir.mkdirs())


  def saveImage(img: BufferedImage): String = {
    val file = currentFile
    ImageIO.write(img, "PNG", file)
    file.getAbsolutePath
  }

  def currentFile = {
    val dateStr = new SimpleDateFormat("YYYY.MM.dd_HH.mm").format(new Date)
    def newFile(i: Int): File = {
      val fileStr = dateStr + (if (i > 0) s"($i)" else "")
      val file = new File(outDir, fileStr + ".png")
      if (!file.exists) file
      else newFile(i + 1)
    }
    newFile(0)
  }

}

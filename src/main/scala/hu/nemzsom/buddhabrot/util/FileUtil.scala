package hu.nemzsom.buddhabrot.util

import java.nio.file.{Files, Path}
import java.io.{ObjectInputStream, ObjectOutputStream, File}

object FileUtil {

  def write(o: Any, path: Path): Any =
    using (new ObjectOutputStream(Files.newOutputStream(path))) { out =>
      out.writeObject(o)
    }

  def read(path: Path): Any = {
    using(new ObjectInputStream(Files.newInputStream(path))) { in =>
      in.readObject
    }
  }

  def getSubDirs(dir: File): List[File] =
    dir.listFiles.filter(_.isDirectory).toList

  def using[T <: {def close()}, R]
  (resource: T)
  (block: T => R): R  = {
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }

}

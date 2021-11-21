package ir.utils;

import java.io.File;

public class DeleteDir {

  /**
   * 删除指定文件夹的内容.
   * @param path 文件夹路径.
   * @return boolean. 0/文件夹不存在；1/删除成功.
   */
  public static Boolean DeleteDir(String path) {
    File file = new File(path);
    if (!file.exists()) {
      return false;
    }
    String[] contents = file.list();
    for (String fileName: contents) {
      File temp = new File(path, fileName);
      if (temp.isDirectory()) {
        DeleteDir(temp.getAbsolutePath());
      }
      temp.delete();
    }
    return true;
  }

}

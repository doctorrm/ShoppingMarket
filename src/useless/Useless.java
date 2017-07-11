package useless;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Useless {
	public static void main(String[] args) throws IOException {  
        // 需求：我要在e盘目录下创建一个文件夹demo  
        File file = new File("e:\\demo");  
        System.out.println("mkdir:" + file.mkdir());// mkdir没有异常  
  
        // 需求:我要在e盘目录demo下创建一个文件a.txt  
        File file2 = new File("e:\\demo\\a.txt");  
        System.out.println("createNewFile:" + file2.createNewFile());// createNewFile()有异常需要抛出或者捕获  
  
        // 需求：我要在e盘目录test下创建一个文件b.txt  
        // Exception in thread "main" java.io.IOException: 系统找不到指定的路径。  
        // 注意：要想在某个目录下创建内容，该目录首先必须存在。  
        // File file3 = new File("e:\\test\\b.txt");  
        // System.out.println("createNewFile:" + file3.createNewFile());  
  
        // 需求:我要在e盘目录test下创建aaa目录  
        // File file4 = new File("e:\\test\\aaa");  
        // System.out.println("mkdir:" + file4.mkdir());  
  
        // File file5 = new File("e:\\test");  
        // File file6 = new File("e:\\test\\aaa");  
        // System.out.println("mkdir:" + file5.mkdir());  
        // System.out.println("mkdir:" + file6.mkdir());  
  
        // 其实我们有更简单的方法  
        File file7 = new File("e:\\aaa\\bbb\\ccc\\ddd");  
        System.out.println("mkdirs:" + file7.mkdirs());  
  
        // 看下面的这个东西：  
        File file8 = new File("e:\\liuyi\\a.txt");  
        System.out.println("mkdirs:" + file8.mkdirs());// 创建liuyi父目录和a.txt子目录(注意a.txt不一定一定是文件,要看你调用的是什么方法,)  
    }  

}

package javanet.l02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <Strong>编写一个程序，使用文件输入输出流与多线程把一个文件复制到三个不同的文件夹中，请写出程序代码</Strong>
 * @author Kio
 */
public class Lab3 implements Runnable{
	
	private final String SOURCE_PATH;
	private final String DEST_PATH;
	
	/**
	 * @param source 源路径
	 * @param dest 目的路径
	 */
	private Lab3(String source, String dest) {
		this.SOURCE_PATH = source;
		this.DEST_PATH = dest;
	}
	
	public static void main(String[] args) {
		System.out.println("Copying Files");
		for (int i = 0; i < 3; i++) {
			new Thread(new Lab3("source","dest"+i)).start();
		}
	}

	@Override
	public void run() {
		try {
			copyDir(this.SOURCE_PATH,this.DEST_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("copy over!");
	}
	
	private static void copyDir(String sourcePath, String newPath) throws IOException {
		File file = new File(sourcePath);
		String[] filePath = file.list();

		if (!(new File(newPath)).exists()) {
			//noinspection ResultOfMethodCallIgnored
			(new File(newPath)).mkdir();
		}

		if (filePath==null)
			return;
		for (String s : filePath) {
			if ((new File(sourcePath + File.separator + s)).isDirectory()) {
				copyDir(sourcePath + File.separator + s, newPath + File.separator + s);
			}

			if (new File(sourcePath + File.separator + s).isFile()) {
				copyFile(sourcePath + File.separator + s, newPath + File.separator + s);
			}

		}
	}
	
	private static void copyFile(String oldPath, String newPath) throws IOException {
		File oldFile = new File(oldPath);
		File file = new File(newPath);
		FileInputStream in = new FileInputStream(oldFile);
		FileOutputStream out = new FileOutputStream(file);

		byte[] buffer = new byte[2097152];
		int readByte;
		while ((readByte = in.read(buffer)) != -1) {
			out.write(buffer, 0, readByte);
		}

		in.close();
		out.close();
	}
}

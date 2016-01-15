package com.api.manager.worker.tool.compress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩器
 *
 * @author ASLai
 */
public class Compressor {

	/** 不压缩 */
	public static final int C_NO = Deflater.NO_COMPRESSION;
	/** 快速压缩级别 */
	public static final int C_FAST = Deflater.BEST_SPEED;
	/** 默认压缩级别 */
	public static final int C_DEFAULT = Deflater.DEFAULT_COMPRESSION;
	/** 最大压缩级别 */
	public static final int C_MAX = Deflater.BEST_COMPRESSION;

	/**
	 * zip压缩并保存
	 * 
	 * @param zip 压缩文件
	 * @param name 文件名称
	 * @param bytes 待压缩字节数组
	 * @throws IOException io异常
	 */
	public static void zip(File zip, String name, byte[] bytes) throws IOException {

		zip(zip, name, bytes, C_DEFAULT);
	}

	/**
	 * zip压缩并保存
	 * 
	 * @param zip 压缩文件
	 * @param name 文件名称
	 * @param bytes 待压缩字节数组
	 * @param level 压缩级别
	 * @throws IOException io异常
	 */
	public static void zip(File zip, String name, byte[] bytes, int level) throws IOException {

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zip));
			// 设置压缩级别
			zos.setLevel(level);
			// 设置压缩文件
			zos.putNextEntry(new ZipEntry(name));
			// 写入字节
			zos.write(bytes);
		} catch (IOException e) {
			throw e;
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * zip解压获取字节数组
	 * 
	 * @param zip 压缩文件
	 * @param name 文件名称
	 * @return 字节数组
	 * @throws IOException IO异常
	 */
	public static byte[] unzip(File zip, String name) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipFile zipFile = null;
		InputStream is = null;
		try {
			// zip文件对象
			zipFile = new ZipFile(zip);
			ZipEntry entry = zipFile.getEntry(name);
			if (entry == null) {
				throw new FileNotFoundException(name);
			}
			// 获取输入流
			is = zipFile.getInputStream(entry);
			// 写入输出流
			byte[] buffer = new byte[1024];
			int len;
			while (-1 != (len = is.read(buffer))) {
				bos.write(buffer, 0, len);
			}
			// 返回字节数组
			return bos.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
}

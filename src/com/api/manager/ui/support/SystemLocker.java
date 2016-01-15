package com.api.manager.ui.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

/**
 * 系统锁定工具
 *
 * @author ASLai
 */
public class SystemLocker {

	/** 锁定文件 */
	private static String lock_file = ".lock";
	/** 锁定文件流 */
	private static FileOutputStream fos;
	/** 文件锁定去 */
	private static FileLock locker;

	/**
	 * 锁定系统
	 * 
	 * @return 是否成功
	 */
	public static boolean lock() {

		if (fos != null || locker != null) {
			return false;
		}
		// 创建文件夹
		File dir = UIConfig.getConfigDir();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 创建锁定去
		try {
			fos = new FileOutputStream(new File(dir, lock_file));
			locker = fos.getChannel().tryLock();
		} catch (IOException e) {
			locker = null;
		} finally {
		}
		return locker != null;
	}

	/**
	 * 解锁系统
	 */
	public static void unlock() {

		if (locker != null) {
			try {
				locker.release();
			} catch (IOException e) {
			}
			locker = null;
		}
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
			}
			fos = null;
		}
	}
}

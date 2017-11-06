package org.litepal.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

public class AppCacheDirUtil {
	
	/***
	 *得到系统缓存文件夹的路径 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi") 
	public static String getCacheDirPath(Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			return context.getApplicationContext().getExternalCacheDir()
					.getPath();
		} else {
			return context.getApplicationContext().getCacheDir().getPath();
		}
	}
	
	/***
	 * 创建存放在缓存目录下的/image/imageFileFolderName/imageName.jpg的file对象
	 * @param context
	 * @param imageFileFolderName 文件夹名字
	 * @param imageName 文件名字
	 * @return
	 */
	public static File creatImageFile(Context context, String imageFileFolderName, String imageName) {
		String path = getCacheDirPath(context);
		File imagePath = new File(path  +  "/image/"+imageFileFolderName + "/");
		if (!imagePath.exists()) {
			imagePath.mkdirs();
		}
		File imageFile = new File(imagePath, imageName+".jpg");
		if (imageFile.exists()) {
			imageFile.delete();
		}
		try {
			imageFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageFile;
	}

	
	/***
	 * 删除指定路径的文件
	 * @param _path
	 */
	public static void deleteFile(String _path){
		if (TextUtils.isEmpty(_path)) {
			return;
		}
		
		File file = new File(_path);
		if (file.exists()) {
			file.delete();
		}
	}
	
	/**
	 * 获取SD卡的地址
	 * 
	 * @return 若未取到则返回空字符串
	 */
	@SuppressLint("NewApi")
	public static String getSDDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			return Environment.getExternalStorageDirectory().getPath();
		} else {
			return "";
		}
	}
}

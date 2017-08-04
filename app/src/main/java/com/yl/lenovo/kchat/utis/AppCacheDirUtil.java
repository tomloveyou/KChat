package com.yl.lenovo.kchat.utis;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import com.yl.lenovo.kchat.KChatApp;

import javax.xml.validation.Validator;


public class AppCacheDirUtil {
	
	/**
	 * <b>方法描述： 删除numDays时间点前的指定路径下的缓存数据</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * 2016-6-16下午2:09:35
	 * @param dir
	 * @param numDays 删除某个毫秒值前的数据（ System.currentTimeMillis()删除当前时间前的所有数据）
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public static int clearCacheFolder(File dir, long numDays) {        
	    int deletedFiles = 0;       
	    if (dir!= null && dir.isDirectory()) {           
	        try {              
	            for (File child:dir.listFiles()) {  
	                if (child.isDirectory()) {            
	                    deletedFiles += clearCacheFolder(child, numDays);        
	                }  
	                else if (child.lastModified() < numDays) {   
	                    if (child.delete()) {                 
	                        deletedFiles++;         
	                    }  
	                }  
	            }           
	        } catch(Exception e) {     
	            e.printStackTrace();  
	        }   
	    }     
	    return deletedFiles;   
	}
	
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

	

	/***
	 * <b>方法描述：检查指定文件是否存在 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param fileName 要检查的文件名
	 * @param parentPath 文件所在目录
	 * @return true:存在该文件；false：不存在此文件
	 * @since Met 1.0
	 * @see
	 */
	public static boolean isFileExist(String fileName,String parentPath) {
		 File[] files=null;//声明一个File数组
		 File parentFile = new File(parentPath);
		 if (!parentFile.exists()) {
			 return false;
		 }
		 else {
			 files=parentFile.listFiles();
			 for (File file : files) {
				if (fileName.equals(file.getName())) {
					return true;
				}
			}
		 }
		   
		return false;
	}
}

package com.mfc.appframe.utils;

import android.content.Context;

/**
 * 根据资源的名字获取其ID值
 * 
 * @author mining
 * 
 */
public class ResourceUtils {
    
    public static int getIdByName(Context context,String resType,String resName) {
    	int id = 0;
    	id = context.getResources().getIdentifier(resName, resType, context.getPackageName());
    	return id;
    }
}

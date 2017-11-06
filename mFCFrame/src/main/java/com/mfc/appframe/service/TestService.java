/**
 * System_name：gsmobile
 *
 * 文件名：TestService.java
 *
 * 描述：
 * 
 * 日期：2016-4-26
 * 
 * 本系统是商用软件，未经授权擅自复制或传播本程序的部分或全部将是非法的
 *
 *
 *@version V1.0
 */
package com.mfc.appframe.service;

import com.mfc.appframe.utils.DebugUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：
 *  <dd> 类描述： 
 *  <dd> 创建时间：2016-4-26上午11:15:23 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version  
 */
public class TestService extends Service {

	private String tag="TestService";
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		DebugUtil.log(tag, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		DebugUtil.log(tag, "onStartCommand");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				while (true) {
					try {
						Thread.sleep(1000);
						DebugUtil.log(tag, "打印次数："+(i++));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DebugUtil.log(tag, "onDestroy");
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		DebugUtil.log(tag, "onLowMemory");
	}
	
	

}

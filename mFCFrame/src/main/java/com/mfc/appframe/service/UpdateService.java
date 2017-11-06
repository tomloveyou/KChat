package com.mfc.appframe.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.mfc.appframe.MainApplication;
import com.mfc.appframe.R;
import com.mfc.appframe.utils.AppCacheDirUtil;
import com.mfc.appframe.utils.AppUtils;

/***
 * 更新版本
 * 
 * @author lirj
 * 需要如下两个参数
 * app_name = intent.getStringExtra("app_name");
			down_url = intent.getStringExtra("updatepath");
 */
public class UpdateService extends Service {
	private static final int TIMEOUT = 10 * 10000;// 超时
	private static String down_url = "";
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;

	private String app_name;

	private NotificationManager notificationManager;
	private Notification notification;

	private Intent updateIntent;
	private PendingIntent pendingIntent;

	private int notification_id = 52012;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			down_url = intent.getStringExtra("updatepath");
			app_name = down_url.substring(down_url.lastIndexOf("/")+1);
			// 创建文件
			String sdcardDir = AppCacheDirUtil.getSDDir();
			if (!TextUtils.isEmpty(sdcardDir)) {
				File fileDictory = new File(sdcardDir+File.separator+AppUtils.getProjectName(getApplicationContext()));
				if (!fileDictory.exists()) {
					fileDictory.mkdirs();
				}
				apkFile = new File(fileDictory.getAbsolutePath()+File.separator+app_name);
				if (apkFile.exists()) {
					apkFile.delete();
				}
				apkFile.createNewFile();
			}
			else {//TODO sdcard不存在
				throw new Exception("sdcard不可用");
			}
			
			createNotification();

			createThread();
		} catch (Exception e) {
			e.printStackTrace();
			stopSelf();
		}

		return super.onStartCommand(intent, flags, startId);

	}

	/***
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					notificationManager.cancel(notification_id);
					/*// 下载完成，点击安装
					Uri uri = Uri.fromFile(apkFile);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");

					pendingIntent = PendingIntent.getActivity(
							UpdateService.this, 0, intent, 0);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后消失
					notification.setLatestEventInfo(UpdateService.this,
							app_name, "下载成功，点击安装", pendingIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后消失
					notificationManager.notify(notification_id, notification);*/
					stopService(updateIntent);
					stopSelf();
					break;
				case DOWN_ERROR:
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后消失
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后消失
					stopSelf();
					break;

				default:
					stopService(updateIntent);
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateFile(down_url,
							apkFile.getAbsolutePath());
					if (downloadSize > 0) {
						// 下载成功
						message.what = DOWN_OK;
						handler.sendMessage(message);
						AppUtils.installApk(apkFile);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}

			}
		}).start();
	}

	/***
	 * 创建通知栏
	 */
	RemoteViews contentView;
	private File apkFile;

	public void createNotification() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		// // 这个参数是通知提示闪出来的值.
		notification.tickerText = "开始下载";

		/***
		 * 在这里我们用自定的view来显示Notification
		 */
		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_update_layout);
		contentView.setTextViewText(R.id.tv_notification_percent, "正在下载0%");
		contentView.setTextViewText(R.id.tv_notification_title, app_name);
		contentView.setProgressBar(R.id.pb_notification, 100, 0, false);

		notification.contentView = contentView;

		//TODO 若无法下载更新需改这里
		updateIntent = new Intent(this, ((Activity)MainApplication.currentActivity).getClass());
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		notification.contentIntent = pendingIntent;

		notificationManager.notify(notification_id, notification);

	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;// 提示step
		int totalSize;// 文件总大小
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小
			/**
			 * 每次增张5%
			 */
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				// 改变通知栏
				// notification.setLatestEventInfo(this, "正在下载...", updateCount
				// + "%" + "", pendingIntent);
				contentView.setTextViewText(R.id.tv_notification_percent,"正在下载"+updateCount + "%");
				contentView.setProgressBar(R.id.pb_notification, 100,updateCount, true);
				// show_view
				notificationManager.notify(notification_id, notification);
			}

		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();

		return downloadCount;

	}

}

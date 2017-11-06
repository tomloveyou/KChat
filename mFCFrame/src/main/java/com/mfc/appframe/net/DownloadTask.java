package com.mfc.appframe.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.mfc.appframe.MainApplication;
import com.mfc.appframe.components.NormalAlertDialog;
import com.mfc.appframe.components.ProgressCircleNumberDialog;
import com.mfc.appframe.utils.AppCacheDirUtil;
import com.mfc.appframe.utils.CallOtherOpenFileUtil;
import com.mfc.appframe.utils.DebugUtil;
import com.mfc.appframe.utils.ToastUtil;

/**
 * <dl>  下载任务，需要在excute方法调用时传递 url地址和文件名称。
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：
 *  <dd> 类描述： 
 *  <dd> 创建时间：2016-4-6上午9:26:17 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version  
 */
public class DownloadTask extends AsyncTask<Void, Long, Boolean> {

	private ProgressCircleNumberDialog dialog;
	private long totalSize;
	private String path;//目标目录路径
	private String mFileName;//下载文件的文件名
	private String url;//下载文件的链接
	private File downloadedFile;//已下载文件的file
	
	public DownloadTask(Context context,String url, String fileName) {
		this.url = url;
		this.mFileName = fileName;
		dialog = new ProgressCircleNumberDialog(context, "正在下载..."+fileName);
		dialog.setTitle("下载提示");
		dialog.setCancelable(false);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		//下载
		return downloadFile(url,mFileName);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	@Override
	protected void onPostExecute(Boolean resultState) {
		super.onPostExecute(resultState);
		dialog.dismiss();
		if (resultState) {//下载成功
			//弹出打开对话框
			alertDownloadFinishDilog();
		}
		else {//下载失败
			ToastUtil.showToast("下载失败，请重试。");
		}
		//下载结束
	}

	/**
	 * <b>方法描述：下载完成提示框 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @since Met 1.0
	 * @see 
	 */
	 
	private void alertDownloadFinishDilog() {
		new NormalAlertDialog(MainApplication.currentActivity)
		.setTitle("下载提示")
		.setContentMsg(mFileName+"已下载至："+path+"。\n是否现在打开？")
		.setNegativeButton("关闭", null)
		.setPositiveButton("打开", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//打开附件
				CallOtherOpenFileUtil.getInstance().openFile(MainApplication.getContext(), downloadedFile);
			}
		}).show();
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate(values);
		//进度更新
		if (dialog != null && totalSize != 0) {
			DebugUtil.log("当前下载百分百："+values[0]*100/totalSize+"；当前下载字节数："+values[0]);
			dialog.setProgress((int)(values[0]*100/totalSize));
		}
	}
	
	/***
	 * <b>方法描述： 下载附件方法</b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param down_url 下载地址
	 * @param fileName
	 * @return
	 * @since Met 1.0
	 * @see
	 */
	public boolean downloadFile(String down_url, String fileName){
		boolean resultState = false;
		InputStream is = null;
		FileOutputStream fos = null;
		long downloadedCount=0;
		try {
			path = AppCacheDirUtil.getDownloadPath();
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet get =new HttpGet(down_url);
			HttpResponse res = httpClient.execute(get);
			Header[] headers = res.getAllHeaders();
			String disposition = "";
			for (Header header : headers) {
				if ("Content-Disposition".equals(header.getName())) {
					disposition = header.getValue();
				}
			}
			if (!TextUtils.isEmpty(disposition)) {
				fileName = disposition.substring(disposition.lastIndexOf("filename=")+9);
				fileName = new String(fileName.getBytes("ISO-8859-1"), "GBK");
				mFileName = fileName;
			}
			fos = new FileOutputStream(path+File.separator+fileName);
			// 获取下载文件的size
			HttpEntity entity = res.getEntity();
			is = entity.getContent();
			if (res.getStatusLine().getStatusCode() == 200 && is != null) {
				totalSize = res.getEntity().getContentLength();
				DebugUtil.log("附件大小："+totalSize);
				int len = 0;
				byte[] bytes = new byte[1024];
				int i = 0;
				while ((len = is.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
					downloadedCount+=len;
					i++;
					if (i >= 5) {//每5kb更新一次进度条
						onProgressUpdate(downloadedCount);
						i = 0;
					}
				}
				downloadedFile = new File(path+File.separator+fileName);//下载成功后给文件赋值
				resultState = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			DebugUtil.err(e.getMessage());
		} 
		finally{
				try {
					if (fos != null) {
						fos.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return resultState;
	}

}

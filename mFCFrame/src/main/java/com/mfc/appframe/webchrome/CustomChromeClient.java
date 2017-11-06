package com.mfc.appframe.webchrome;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.mfc.appframe.MainApplication;
import com.mfc.appframe.components.ProgressCircleNumberDialog;
import com.mfc.appframe.js_java.InjectedChromeClient;

public class CustomChromeClient extends InjectedChromeClient {
	private ProgressCircleNumberDialog dialog;
	
	 public CustomChromeClient (String injectedName, Class<?> injectedCls) {
         super(injectedName, injectedCls);
     }
     
     @Override
     public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
         // to do your work
         // ...
         return super.onJsAlert(view, url, message, result);
     }

     @Override
     public void onProgressChanged (WebView view, int newProgress) {
         super.onProgressChanged(view, newProgress);
         //显示进度条
         if (dialog == null ) {
        	 dialog = new ProgressCircleNumberDialog(MainApplication.currentActivity,"正在加载...");
        	 dialog.show();
         }
         else if (!dialog.isShowing()) {
        	 dialog.show();
         }
         dialog.setProgress(newProgress);
			if (newProgress == 100) {//加载完成隐藏进度条
				if (dialog != null) {
					dialog.dismiss();
				}
			}
     }

     @Override
     public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
         // to do your work
         // ...
         return super.onJsPrompt(view, url, message, defaultValue, result);
     }
     
     /***
      * <b>方法描述： 清除dialog</b>
      * <dd>方法作用： 
      * <dd>适用条件： 
      * <dd>执行流程： 
      * <dd>使用方法： 
      * <dd>注意事项： 
      * @since Met 1.0
      * @see
      */
     public void clearDialog () {
    	 if (dialog != null && dialog.isShowing()) {
    		 dialog.dismiss();
    	 }
    	 dialog = null;
     }
}

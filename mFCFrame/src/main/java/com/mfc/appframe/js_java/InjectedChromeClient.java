/**
 * Summary: 应用中使用的WebChromeClient基类
 * Version 1.0
 * Date: 13-11-8
 * Time: 下午2:31
 * Copyright: Copyright (c) 2013
 * 
 * 这个项目的原理就是使用 WebChromeClient.onJsPrompt 方法来进行交互，
 * 本质上都是js调用 prompt 函数，传输一些参数， 
 * onJsPrompt 方法拦截到prompt动作，
 * 然后解析数据，最后调用相应的Native方法
 * 
 * 那么这个库的整个执行流程是这样的：
	1.JsCallJava 类解析了 HostJsScope 类中所有的静态方法，
		将它们放到一个Map中，并且生成一段js代码
	2.向WebView设置 InjectedChromeClient ，在 onProgressChanged 方法中将那段js代码注入到Html5页面中，
		这个过程通俗点讲就是， Native告诉Html 5页面，我开放了什么功能给你，你就来调用我
	3.这样js就可以调用Native提供的这些方法，那段js代码还会将js执行的方法转换成一段json字符串，
		通过js的prompt方法传到 onJsPrompt 方法中， JsCallJava 调用 call(WebView view, String msg) 解析json字符串，
		包括要执行的 方法名字 ， 参数类型 和 方法参数 ，
		其中还会验证json中的方法参数类型和HostJsScope 中同名方法参数类型是否一致等等。
	4.最后，如果方法正确执行， call 方法就返回一个json字符串code=200，否则就传code=500，
		这个信息会通过 prompt 方法的返回值传给js，
		这样Html 5 代码就能知道有没有正确执行了
		原地址：http://www.apkbus.com/android-246234-1-1.html
 */

package com.mfc.appframe.js_java;

import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class InjectedChromeClient extends WebChromeClient {
    private final String TAG = "InjectedChromeClient";
    private JsCallJava mJsCallJava;
    private boolean mIsInjectedJS;

    public InjectedChromeClient (String injectedName, Class injectedCls) {
        mJsCallJava = new JsCallJava(injectedName, injectedCls);
    }

    public InjectedChromeClient (JsCallJava jsCallJava) {
        mJsCallJava = jsCallJava;
    }
    
    // 处理Alert事件
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        result.confirm();
        return true;
    }

    @Override
    public void onProgressChanged (WebView view, int newProgress) {
        //为什么要在这里注入JS
        //1 OnPageStarted中注入有可能全局注入不成功，导致页面脚本上所有接口任何时候都不可用
        //2 OnPageFinished中注入，虽然最后都会全局注入成功，但是完成时间有可能太晚，当页面在初始化调用接口函数时会等待时间过长
        //3 在进度变化时注入，刚好可以在上面两个问题中得到一个折中处理
        //为什么是进度大于25%才进行注入，因为从测试看来只有进度大于这个数字页面才真正得到框架刷新加载，保证100%注入成功
        if (newProgress <= 25) {
            mIsInjectedJS = false;
        } else if (!mIsInjectedJS) {
            view.loadUrl(mJsCallJava.getPreloadInterfaceJS());
            mIsInjectedJS = true;
            Log.d(TAG, " inject js interface completely on progress " + newProgress);

        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm(mJsCallJava.call(view, message));
        return true;
    }
}

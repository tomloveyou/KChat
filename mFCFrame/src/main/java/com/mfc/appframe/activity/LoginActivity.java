package com.mfc.appframe.activity;
/*package com.mfc.appframe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.litepal.crud.DataSupport;

import com.hstc.frame.MyApplication;
import com.hstc.frame.bean.UnitGrid;
import com.hstc.frame.bean.WorkGrid;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mfc.appframe.R;
import com.mfc.appframe.base.BaseActivity;
import com.mfc.appframe.bean.User;
import com.mfc.appframe.components.ProgressSpinnerDialog.DialogDismissListener;
import com.mfc.appframe.constant.Constant;
import com.mfc.appframe.net.NetWorkTask;
import com.mfc.appframe.net.NetWorkTask.OnResultListener;
import com.mfc.appframe.utils.AppUtils;
import com.mfc.appframe.utils.DebugUtil;
import com.mfc.appframe.utils.DialogUtils;
import com.mfc.appframe.utils.HrefUtils;
import com.mfc.appframe.utils.JsonUtil;
import com.mfc.appframe.utils.LitepalXmlUtils;
import com.mfc.appframe.utils.ToastUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

*//***
 * 登录页面
 * @author lirj
 *
 *//*
public class LoginActivity extends BaseActivity implements OnClickListener,
		OnResultListener {

	private Button btn_login;
	private EditText et_name;
	private EditText et_pwd;
	private TextView tv_copyRight;
	private String mUname;
	private String mPwd;

	private NetWorkTask netWorkTask = new NetWorkTask(
			Constant.TYPE_REQUEST_LOGIN, Constant.METHOD_LOGIN, this);
	private DialogDismissListener dialogDismissListener = new DialogDismissListener() {

		@Override
		public void onDialogDismissListener() {
			if (netWorkTask != null) {
				netWorkTask.cancel(true);
				netWorkTask = null;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		setListener();
	}

	private void setListener() {
		btn_login.setOnClickListener(this);
	}

	private void initView() {
		btn_login = (Button) findViewById(R.id.btn_login);
		et_name = (EditText) findViewById(R.id.et_name);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		tv_copyRight = (TextView) findViewById(R.id.tv_copyRight);
		String versionName;
		try {
			versionName = AppUtils.getVersionName(this);
		} catch (Exception e) {
			versionName = "";
			e.printStackTrace();
		}
		tv_copyRight.setText(String.format(
				getResources().getString(R.string.copyright), versionName));
	}

	private boolean verify() {
		mUname = et_name.getText().toString();
		mPwd = et_pwd.getText().toString();
		if (TextUtils.isEmpty(mUname)) {
			ToastUtil.showToast(R.string.uname_cannot_null);
			return false;
		}
		if (TextUtils.isEmpty(mPwd)) {
			ToastUtil.showToast(R.string.pwd_cannot_null);
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:// 登录
			if (verify()) {
				// webview登录（登录金信系统，用于保持session）
				// 首先使用webview登录金信系统，以保证金信系统登录成功，然后通过广播通知loginActivity登录188服务器
				DialogUtils.showProgressDialog(FreeAnchoredManageactivity.this,
						getResources().getString(R.string.logining),
						dialogDismissListener);
				// MyApplication.wv_web.loadUrl(Constant.JINXIN_LOGIN_URL+"&userID="+mUname+"&password="+mPwd);
				// 请求金信系统，获取sessionid
				doLogin();
			}
			break;

		default:
			break;
		}
	}

	*//**
	 * <b>方法描述：此登录用于返回用户信息 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @since Met 1.0
	 * @see
	 *//*
	@SuppressWarnings("unchecked")
	private void doLogin() {
		Map<String, String> keyMap = new HashMap<String, String>();
		Map<String, String> valueMap = new HashMap<String, String>();
		keyMap.put("name", mUname);
		valueMap.put("pwd", mPwd);
		netWorkTask = new NetWorkTask(Constant.TYPE_REQUEST_LOGIN,
				Constant.METHOD_LOGIN, this);
		netWorkTask.execute(keyMap, valueMap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DialogUtils.isShowing()) {DialogUtils.dismiss();}

	}

	// 登录187服务器成功回调
	@Override
	public void onSuccess(String result, String method) {
		if (DialogUtils.isShowing()) {DialogUtils.dismiss();}
		DebugUtil.log(result);
		Map<String, Object> resultMap = JsonUtil.json2Map(result);
		if (resultMap != null
				&& (resultMap.get("state").toString()).equals("0")) {
			DebugUtil.err(resultMap.get("state").toString());
			ToastUtil.showToast(resultMap.get("errMsg").toString());
		} else if (resultMap != null
				&& (resultMap.get("state").toString()).equals("1")) {
			ToastUtil.showToast(R.string.login_success);
			// 解析用户信息，保存到user表和WorkGrid及UnitGrid中 TODO 此过程可能耗时，后期建议新开线程
			parseAndSaveUserInfo(result);

			// 设置
			// 跳转主页
			HrefUtils.hrefActivity(LoginActivity.this, MainActivity.class, 0);
		} else {
			ToastUtil.showToast(result);
		}
	}

	*//**
	 * <b>方法描述：解析登录成功返回的json数据，存入对应表中 </b>
	 * <dd>方法作用： 解析登录成功返回的json数据，存入对应表中
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param result 登录后返回的json数据
	 * @since Met 1.0
	 * @see 
	 *//*

	@SuppressWarnings("unchecked")
	private void parseAndSaveUserInfo(String result) {
		Map<String, Object> resultMap = JsonUtil.json2Map(result);
		String cookie = resultMap.get("Cookie").toString();
		// 解析用户信息
		Map<String, Object> userInfo = (Map<String, Object>) resultMap
				.get("UserInfo");
		MyApplication.TailorID = cookie;
		DebugUtil.log("服务器返回cookie：" + cookie);
		LitepalXmlUtils.getInstance().updateDBname(
				"gsmobile_" + userInfo.get("USERNO").toString());// 为当前用户创建数据库
		// 首先清除现有数据（每个用户对应一个数据库，所以相互不影响）
		DataSupport.deleteAll(User.class);
		DataSupport.deleteAll(WorkGrid.class);
		DataSupport.deleteAll(UnitGrid.class);
		
		MyApplication.user = new User(userInfo.get("OPERATORID").toString(), userInfo
				.get("ORGID").toString(), userInfo.get("USERNO").toString(),
				userInfo.get("USERNAME").toString(), userInfo.get("ORGCODE")
						.toString(), userInfo.get("ORGNAME").toString());

		// 极光别名设置
		JPushInterface.setAlias(getApplicationContext(), userInfo.get("USERNO")
				.toString() + "_android", new TagAliasCallback() {

			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 6004://alias超长。最多 40个字节

					break;
				case 6002://设置超时

					break;
				case 6011://10s内设置tag或alias大于10次	短时间内操作过于频繁

					break;

				default:
					break;
				}
			}
		});
		
		MyApplication.user.save();// 保存用户信息
		// 解析工作网格
		List<Map<String, Object>> workGridlists = (List<Map<String, Object>>) resultMap
				.get("GridInfo");
		// 解析单元网格
		List<Map<String, Object>> unitGridlists = (List<Map<String, Object>>) resultMap
				.get("UnitGridInfo");

		for (Map<String, Object> map : workGridlists) {// 遍历工作网格
			WorkGrid wg = new WorkGrid(map.get("USER_NO").toString(), map.get(
					"GRID_NAME").toString(), map.get("GRID_SN").toString());
			// 设置工作网格中包含的单元网格
			List<UnitGrid> uGrids = new ArrayList<UnitGrid>();
			// 遍历单元网格
			for (Map<String, Object> map2 : unitGridlists) {
				UnitGrid ud = new UnitGrid(map2.get("GRID_SN").toString(), map2
						.get("UNITGRID_NAME").toString(), map2.get(
						"UNITGRID_SN").toString());
				ud.save();
				if (map2.get("GRID_SN").toString().equals(wg.getGRID_SN())) {
					uGrids.add(ud);// 单元网格信息保存
				}
			}
			wg.setUnitGridList(uGrids);
			wg.save();// 工作网格信息保存
		}

		// TODO 测试数据，请发布前删除====start
		WorkGrid wg = new WorkGrid(userInfo.get("USERNO").toString(), "东区一号网格",
				"5566");
		UnitGrid ud1 = new UnitGrid("5566", "东区一号网格附一号单元网格", "1121");
		UnitGrid ud2 = new UnitGrid("5566", "东区一号网格附二号单元网格", "1122");
		ud1.save();
		ud2.save();
		List<UnitGrid> listTest = new ArrayList<UnitGrid>();
		listTest.add(ud1);
		listTest.add(ud2);
		wg.setUnitGridList(listTest);
		wg.save();
		// TODO 测试数据，请发布前删除===end
	}

	// 登录187服务器失败回调
	@Override
	public void onFail(int Code, String errMsg, String method) {
		if (DialogUtils.isShowing()) {DialogUtils.dismiss();}
		DebugUtil.err(errMsg);
		ToastUtil.showToast("登录失败:" + errMsg);
	}

	*//** 
	 * 同步一下cookie 
	 *//*
	public String getCookies(String url) {
		CookieManager cookieManager = CookieManager.getInstance();
		if (cookieManager.acceptCookie()) {
			return cookieManager.getCookie(url);
		}
		return null;
	}

}
*/
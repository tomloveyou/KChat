package com.mfc.appframe.activity;
/*package com.mfc.appframe.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.hstc.frame.activity.data.UpdateDataActivity;
import com.hstc.frame.activity.data.UploadDataActivity;
import com.hstc.frame.activity.econaccounts.AccountClaimListActivity;
import com.hstc.frame.activity.econaccounts.AnnualSurveyList;
import com.hstc.frame.activity.econaccounts.LimitedCompanyListActivity;
import com.hstc.frame.activity.econaccounts.NotLocalAccountListActivity;
import com.hstc.frame.activity.regulation.InspListActivity;
import com.hstc.frame.activity.rights.RightsListActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.mfc.appframe.R;
import com.mfc.appframe.adapter.CommonAdapterForArray;
import com.mfc.appframe.adapter.ViewHolder;
import com.mfc.appframe.base.BaseActivity;
import com.mfc.appframe.components.MenuPopupUtil;
import com.mfc.appframe.components.MenuPopupUtil.OnMenuItemClickListener;
import com.mfc.appframe.components.ViewPagerScroller;
import com.mfc.appframe.components.transformer.CubeTransformer;
import com.mfc.appframe.constant.Constant;
import com.mfc.appframe.fragment.GSOAFragment;
import com.mfc.appframe.fragment.QueryFragment;
import com.mfc.appframe.fragment.SuperviseFragment;
import com.mfc.appframe.net.NetWorkTask;
import com.mfc.appframe.net.NetWorkTask.OnResultListener;
import com.mfc.appframe.receiver.NetBroadCastReceiver;
import com.mfc.appframe.utils.AppUtils;
import com.mfc.appframe.utils.DeviceUtil;
import com.mfc.appframe.utils.DisplayUtils;
import com.mfc.appframe.utils.HrefUtils;
import com.mfc.appframe.utils.ToastUtil;

*//**
 * 主界面
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：MainActivity
 *  <dd> 类描述： 主界面
 *  <dd> 创建时间：2016-3-28下午3:37:23 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version
 *//*
public class MainActivity extends BaseActivity {

	private long firstClickTime;
	private ViewPager vp_main;// viewpager
	private List<Fragment> fragments = new ArrayList<Fragment>();// fragment的集合
	private RadioGroup rg_tab;
	// ==========对话框=========
	private AlertDialog dialog;
	private View dialog_content;
	private ListView lv_jump_list;
	private MyNetBroadCastReceiver netReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContent(R.layout.activity_main);
		setTitle(getString(R.string.title_OA), false, "注销",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						 * //注销 AppUtils.clearActivitys();
						 * HrefUtils.hrefActivity(MainActivity.this,
						 * LoginActivity.class, 0); finish();
						 
						menu.showDropDown(v);
					}
				});
		initMenuData();
		initView();
		setListener();
		registeReceiver();
	}

	*//**
	 * 
	 * <b>方法描述：设置监听事件 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @since Met 1.0
	 * @see
	 *//*
	private void setListener() {
		vp_main.setPageTransformer(true, new CubeTransformer());
		// 设置viewpager翻页监听
		vp_main.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:// 工商OA
					setTitleText(R.string.title_OA);
					rg_tab.check(R.id.rb_oa);
					break;
				case 1:// 现场监管
					setTitleText(R.string.title_supervise);
					rg_tab.check(R.id.rb_supervise);
					break;

				case 2:// 查询
					setTitleText(R.string.title_query);
					rg_tab.check(R.id.rb_query);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 设置下方按钮点击翻页
		rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_oa:// 点击工商oa按钮
					vp_main.setCurrentItem(0, true);
					break;
				case R.id.rb_supervise:// 现场监管按钮
					vp_main.setCurrentItem(1, true);
					break;
				case R.id.rb_query:// 现场监管按钮
					vp_main.setCurrentItem(2, true);
					break;

				default:
					break;
				}
			}
		});
	}

	// 初始化控件
	private void initView() {
		vp_main = (ViewPager) findViewById(R.id.vp_main);
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		fragments.add(new GSOAFragment());
		fragments.add(new SuperviseFragment());
		fragments.add(new QueryFragment());
		vp_main.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
				fragments));
		initViewPagerScroll();
	}

	public void yidongOA(View view) {
		HrefUtils.hrefActivity(this, OAWebActivity.class, 0);
	}

	public void yidongZF(View view) {
		ToastUtil.showToast(R.string.in_construction);
		// HrefUtils.hrefActivity(this, WebActivity.class, 1);
	}

	public void qiyeGS(View view) {
		ToastUtil.showToast(R.string.in_construction);
		// HrefUtils.hrefActivity(this, WebActivity.class, 1);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (System.currentTimeMillis() - firstClickTime > 2000) {
				firstClickTime = System.currentTimeMillis();
				Toast.makeText(this, "再点一次退出", 0).show();
			} else {
				AppUtils.exitApp();
			}
		}
		return true;
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPagerAdapter(FragmentManager fm, List<Fragment> frags) {
			super(fm);
			this.fragments = frags;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

	public void dismissListDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void showListDialog(final String[] texts, final int type) {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(this).create();
			dialog_content = View
					.inflate(this, R.layout.dialog_jump_list, null);
			lv_jump_list = (ListView) dialog_content
					.findViewById(R.id.lv_jump_list);
		}
		dialog.show();
		dialog.getWindow().setContentView(dialog_content);
		lv_jump_list.setAdapter(new CommonAdapterForArray<String>(this, texts,
				R.layout.item_jump_list) {

			@Override
			public void convert(ViewHolder holder, String t) {
				holder.setText(R.id.tv_name, texts[holder.getPosition()]);
			}
		});
		lv_jump_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dismissListDialog();
				switch (type) {// 点击每个gridview item弹出的对话框列表的点击事件
				case 0:// 经济户口管理
					onEncoListJump(position);
					break;
				case 1:// 监管查询
					onInspListJump(position);
					break;
				case 2:// 工商监管
					HrefUtils.hrefActivity(MainActivity.this,
							InspListActivity.class, 0);
					break;
				case 4:// 12315

					switch (position) {
					case 0:
						HrefUtils.hrefActivity(MainActivity.this,
								RightsListActivity.class, 0);
						break;
					case 1:
						ToastUtil.showToast("建设中...");
						break;

					default:
						break;
					}

					break;
				case 5:// 数据管理
					onDataManagerListJump(position);
					break;

				default:

					break;
				}

			}
		});
	}

	*//***
	 * 数据管理对话框列表跳转控制
	 * <b>方法描述： </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @param position
	 * @since Met 1.0
	 * @see
	 *//*
	protected void onDataManagerListJump(int position) {
		switch (position) {
		case 0:// 数据更新
			HrefUtils.hrefActivity(MainActivity.this, UpdateDataActivity.class,
					0);
			break;
		case 1:// 数据上传
			HrefUtils.hrefActivity(MainActivity.this, UploadDataActivity.class,
					0);
			break;

		default:
			break;
		}
	}

	*//***
	 * 经济户口管理列表点击
	 * @param position
	 *//*
	protected void onEncoListJump(int position) {
		Bundle bundle = new Bundle();
		switch (position) {
		case 0:// 警示处理(受限企业列表)
			
			 * bundle.putInt(Constant.LIST_TYPE_KEY,
			 * Constant.TYPE_LIST_CAUTION);
			 
			HrefUtils.hrefActivity(MainActivity.this,
					LimitedCompanyListActivity.class, 0);
			break;
		case 1:// 户口认领
			
			 * bundle.putInt(Constant.LIST_TYPE_KEY,
			 * Constant.TYPE_LIST_ACCOUNT);
			 
			HrefUtils.hrefActivity(MainActivity.this,
					AccountClaimListActivity.class, 0);
			break;
		case 2:// 非本辖区户口管理
			HrefUtils.hrefActivity(MainActivity.this,
					NotLocalAccountListActivity.class, 0);
			break;
		case 3:// 个体验照
			HrefUtils
					.hrefActivity(MainActivity.this, AnnualSurveyList.class, 0);

			break;

		default:
			break;
		}
	}

	*//**
	 * 
	 * <b>方法描述： 监管查询列表点击</b>
	 *  <dd>方法作用： 
	 *  <dd>适用条件： 
	 *  <dd>执行流程： 
	 *  <dd>使用方法： 
	 *  <dd>注意事项： 
	 *  <dd>日期：2016-4-26下午4:50:56
	 * @param position
	 * @since Met 1.0
	 * @see
	 *//*
	protected void onInspListJump(int position) {
		Bundle bundle = new Bundle();
		switch (position) {
		case 0:// 商品质量监测信息查询
			bundle.putString(Constant.ENTER_URL_KEY,
					Constant.ENTER_URL_SUPERVISE_QUERY);
			HrefUtils.hrefActivity(this, OAWebActivity.class, 0, bundle);
			break;
		case 1://食品流通许可证查询
			bundle.putString(Constant.ENTER_URL_KEY,
					Constant.ENTER_URL_SUPERVISE_FOOD_PERMIT);
			HrefUtils.hrefActivity(this, OAWebActivity.class, 0, bundle);
			break;
		case 2:// 商标注册信息查询
			bundle.putString(Constant.ENTER_URL_KEY,
					Constant.ENTER_URL_SUPERVISE_TRADEMARK);
			HrefUtils.hrefActivity(this, OAWebActivity.class, 0, bundle);
			break;
		case 3:// 户外广告登记信息查询
			bundle.putString(Constant.ENTER_URL_KEY,
					Constant.ENTER_URL_OUTDOOR_ADVERTISING);
			HrefUtils.hrefActivity(this, OAWebActivity.class, 0, bundle);

			break;

		default:
			break;
		}
	}

	private void initViewPagerScroll() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			ViewPagerScroller scroller = new ViewPagerScroller(
					vp_main.getContext());
			mScroller.set(vp_main, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}
	
	*//***
	 * 注册网络监听
	 *//*
	private void registeReceiver() {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		netReceiver = new MyNetBroadCastReceiver();
		registerReceiver(netReceiver, filter);
	}
	
	*//**
	 * <dl>  Class Description
	 *  <dd> 项目名称：gsmobile
	 *  <dd> 类名称：用于监听网络变化的广播，如果网络改变则检查是否需要上传操作日志
	 *  <dd> 类描述： 
	 *  <dd> 创建时间：2016-4-26下午3:52:10 2016
	 *  <dd> 修改人：无
	 *  <dd> 修改时间：无
	 *  <dd> 修改备注：无
	 * </dl>
	 * @author lirj
	 * @see
	 * @version
	 *//*
	public class MyNetBroadCastReceiver extends BroadcastReceiver {

		 (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				//网络变化时，检查网络是否可用，不可用提示
				if (DeviceUtil.isNetConnected(context) && DeviceUtil.isWifiActive(context)) {//网络可用，且是wifi环境
					//TODO 检查上传日志
				}
				else {//网络可用
				}
			}
		}
		
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (netReceiver != null) {//销毁广播
			unregisterReceiver(netReceiver);
			netReceiver = null;
		}
	}
	//======================测试用TODO===================
	private MenuPopupUtil menu;
	private List<String> menuTexts = new ArrayList<String>();

	*//***
	 * 初始化菜单项
	 *//*
	private void initMenuData() {
		menuTexts.add("注销");
		menuTexts.add("推送");
		menu = new MenuPopupUtil(DisplayUtils.dip2px(this, 170), -2, this);
		menu.setItemTextsAndImgs(menuTexts, null);
		menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void OnMenuItemClick(View view, int position) {
				switch (position) {
				case 0:// 注销
						// 注销
					AppUtils.clearActivitys();
					HrefUtils.hrefActivity(MainActivity.this,
							LoginActivity.class, 0);
					finish();
					break;
				case 1:// 推送
					new NetWorkTask(0, "testPush", new OnResultListener() {

						@Override
						public void onSuccess(String result, String method) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFail(int Code, String errMsg,
								String method) {
							// TODO Auto-generated method stub

						}
					}).execute();
					break;

				default:
					break;
				}
			}
		});
	}
}
*/
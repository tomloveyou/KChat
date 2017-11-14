package com.yl.lenovo.kchat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;


import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.yl.lenovo.kchat.bean.Comment;
import com.yl.lenovo.kchat.bean.Dynamic;
import com.yl.lenovo.kchat.bean.MyUser;
import com.yl.lenovo.kchat.stick.TravelActivity;
import com.yl.lenovo.kchat.stick.util.ToastUtil;
import com.yl.lenovo.kchat.stick.view.SmoothListView.SmoothListView;
import com.yl.lenovo.kchat.utis.DateUtil;
import com.yl.lenovo.kchat.utis.SPUtils;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SmoothListView.ISmoothListViewListener, BGANinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks {
    private TextView tv_username, tv_email;
    private SimpleDraweeView iv_avator;
    private SmoothListView mMomentRv;

    NavigationView navigationView;

    private List<Dynamic> data = new ArrayList<>();
    private CommonAdapter<Dynamic> adapter;
    private Dialog dialog;
    FloatingActionButton fab;
    private List<String> item_data = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PREVIEW = 1;
    private static final int REQUEST_CODE_ADD_MOMENT = 1;


    private BGANinePhotoLayout mCurrentClickNpl;

    private int limit = 10; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始

    boolean isFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mMomentRv = (SmoothListView) findViewById(R.id.rv_moment_list_moments);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tv_email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        iv_avator = (SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.iv_avator);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        item_data.add("点钟");
        item_data.add("点钟");
        item_data.add("点钟");
        item_data.add("点钟");

        iv_avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // selectImageAndCrop();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DynamicReleaseActivity.class));
            }
        });
        mMomentRv.setSmoothListViewListener(this);
        mMomentRv.setLoadMoreEnable(true);

        adapter = new CommonAdapter<Dynamic>(MainActivity.this, R.layout.item_dynamic_layout, data) {
            @Override
            protected void convert(ViewHolder viewHolder, final Dynamic item, int position) {
                if (TextUtils.isEmpty(item.getContent())) {

                    viewHolder.setVisible(R.id.item_dynamic_content, false);
                } else {
                    viewHolder.setVisible(R.id.item_dynamic_content, true);


                }
                viewHolder.setText(R.id.item_dynamic_title, item.getTitle());
                viewHolder.setText(R.id.item_dynamic_content, item.getContent());
                viewHolder.setText(R.id.item_dynamic_time, item.getCreatedAt());
                SimpleDraweeView imageView = viewHolder.getView(R.id.iv_item_moment_avatar);
                imageView.setImageURI(Uri.parse(item.getAuthor()!=null?item.getAuthor().getUser_avator():"https://www.baidu.com/s?wd=%E9%83%AB%E5%8E%BF&rsv_idx=2&tn=98012088_5_dg&ch=12&usm=1&ie=utf-8&rsv_cq=%E6%88%90%E9%83%BD%E8%8D%89%E5%A0%82%E5%8C%97%E8%B7%AF%E9%82%AE%E7%BC%96&rsv_dl=0_right_recommends_merge_28335&euri=244256"));
                viewHolder.setOnClickListener(R.id.iv_comment, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NiceDialog.init()
                                .setLayoutId(R.layout.commit_layout)
                                .setConvertListener(new ViewConvertListener() {

                                    @Override
                                    public void convertView(com.othershe.nicedialog.ViewHolder holder, final BaseNiceDialog dialog) {
                                        final EditText editText = holder.getView(R.id.edit_input);
                                        editText.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.showSoftInput(editText, 0);
                                            }
                                        });
                                        holder.getView(R.id.tv_comment_publish).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                pubComment(dialog,item, editText.getText().toString());
                                            }
                                        });
                                    }
                                })
                                .setShowBottom(true)
                                .show(getSupportFragmentManager());
                    }
                });


                BGANinePhotoLayout ninePhotoLayout = viewHolder.getView(R.id.npl_item_moment_photos);
                ninePhotoLayout.setDelegate(MainActivity.this);
                ninePhotoLayout.setData((ArrayList<String>) item.getImg_list());
            }
        };
        mMomentRv.setAdapter(adapter);

        navigationView.setNavigationItemSelectedListener(this);

//        if (KChatApp.getInstance().getBmobUser() != null) {
//            tv_email.setText(KChatApp.getInstance().getBmobUser().getEmail());
//            tv_username.setText(KChatApp.getInstance().getBmobUser().getUsername());
//
//            iv_avator.setImageURI(Uri.parse(KChatApp.getInstance().getBmobUser().getUser_avator() == null ? "http://ossweb-img.qq.com/upload/apps/ishow/176/thumb_1316413425_-1719592020_13323_sProdImgNo_1.jpg" : KChatApp.getInstance().getBmobUser().getUser_avator()));
//        }
        onRefresh();

    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }


    private void onClickLove(final Dynamic dynamic) {
        // TODO Auto-generated method stub
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user == null) {
            // 前往登录注册界面
            ToastUtil.show(this, "请先登录。");
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (dynamic.isMyLove()) {
            ToastUtil.show(this, "您已经赞过啦");
            return;
        }
        isFav = dynamic.isMyFav();
        if (isFav) {
            dynamic.setMyFav(false);
        }
        dynamic.setLove(dynamic.getLove() + 1);
//        love.setTextColor(Color.parseColor("#D95555"));
//        love.setText(dynamic.getLove() + "");
        dynamic.increment("love", 1);
        dynamic.update(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                dynamic.setMyLove(true);
                dynamic.setMyFav(isFav);


                ToastUtil.show(MainActivity.this, "点赞成功~");
            }


        });
    }


    @Override
    public void onRefresh() {
        curPage = 1;
        data.clear();
        adapter.notifyDataSetChanged();
        BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();
        query.setLimit(limit);
        query.order("createdAt");
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        query.addWhereEqualTo("ower", user.getObjectId());
        query.setSkip(curPage);
        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> object, BmobException e) {
                if (e == null) {
                    data.addAll(object);
                    adapter.notifyDataSetChanged();
                }
                mMomentRv.setRefreshTime(DateUtil.getRefreshTime(MainActivity.this));
                mMomentRv.stopRefresh();
            }
        });

    }

    @Override
    public void onLoadMore() {

        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();
        query.setLimit(limit);
        query.order("createdAt");
        query.addWhereEqualTo("ower", user.getObjectId());//查询当前登录用户的动态
        query.setSkip(curPage);
        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> object, BmobException e) {
                if (e == null) {
                    curPage += 10;
                    data.addAll(object);
                    adapter.notifyDataSetChanged();
                    if (object.size() == 0) {
                        //mMomentRv.setLoadEnd();
                    } else {
                        mMomentRv.stopLoadMore();
                    }
                } else {
                    mMomentRv.stopLoadMore();
                }

            }
        });
    }

    private void pubComment(final BaseNiceDialog dialog, final Dynamic dynamic, String content) {

        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        final Comment comment = new Comment();
        comment.setCommentContent(content);
        comment.setUser(user);
        comment.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {

                if (e == null) {
                    Log.i("bmob", "评论发表成功");
                    BmobRelation relation = new BmobRelation();
                    relation.add(comment);
                    dynamic.setRelation(relation);
                    dynamic.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }

        });
    }

    protected Dialog createLoadingDialog(String msg, boolean isAnimation, int imageId) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.loading_layout, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img_loading);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

        if (imageId != 0) {
            spaceshipImage.setImageResource(imageId);
        }
        if (isAnimation) {
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    this, R.anim.loading_animation);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        }

        if (TextUtils.isEmpty(msg)) {
            tipTextView.setVisibility(View.GONE);
        } else {
            tipTextView.setVisibility(View.VISIBLE);
            tipTextView.setText(msg);// 设置加载信息
        }

        Dialog loadingDialog = new Dialog(this, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, MessageActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this, TravelActivity.class));
        } else if (id == R.id.nav_manage) {
            // startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return;
        }

        // 保存图片的目录，改成你自己要保存图片的目录。如果不传递该参数的话就不会显示右上角的保存按钮
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (mCurrentClickNpl.getItemCount() == 1) {
                // 预览单张图片

                startActivity(BGAPhotoPreviewActivity.newIntent(this, downloadDir, mCurrentClickNpl.getCurrentClickItem()));
            } else if (mCurrentClickNpl.getItemCount() > 1) {
                // 预览多张图片

                startActivity(BGAPhotoPreviewActivity.newIntent(this, downloadDir, mCurrentClickNpl.getData(), mCurrentClickNpl.getCurrentClickItemPosition()));
            }
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", REQUEST_CODE_PERMISSION_PHOTO_PREVIEW, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

}

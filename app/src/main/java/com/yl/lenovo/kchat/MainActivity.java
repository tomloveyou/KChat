package com.yl.lenovo.kchat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.squareup.picasso.Picasso;
import com.yl.lenovo.kchat.bean.Dynamic;
import com.yl.lenovo.kchat.bean.MyUser;
import com.yl.lenovo.kchat.bean.leadimg;
import com.yl.lenovo.kchat.mvp.contract.FileContract;
import com.yl.lenovo.kchat.mvp.contract.UserContract;
import com.yl.lenovo.kchat.mvp.presenter.FilePresenterImpl;
import com.yl.lenovo.kchat.mvp.presenter.UserPresenter;
import com.yl.lenovo.kchat.utis.AppCacheDirUtil;
import com.yl.lenovo.kchat.utis.SPUtils;
import com.yl.lenovo.kchat.widget.CustomOperationPopWindow;
import com.yl.lenovo.kchat.xlistview.XListView;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPPToolbarActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadBatchListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FileContract.FileSingleUploadView, BGANinePhotoLayout.Delegate, UserContract.UserDataUpdateView, EasyPermissions.PermissionCallbacks {
    private TextView tv_username, tv_email;
    private ImageView iv_avator;
    private RecyclerView mMomentRv;
    private MomentAdapter mMomentAdapter;
    NavigationView navigationView;

    private List<Dynamic> data = new ArrayList<>();

    FloatingActionButton fab;
    private List<String> item_data = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PREVIEW = 1;
    private static final int REQUEST_CODE_ADD_MOMENT = 1;

    private BGANinePhotoLayout mCurrentClickNpl;
    private FileContract.FileUploadPresenter presenter = new FilePresenterImpl(this);
    private UserContract.UserPresenter userPresenter = new UserPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mMomentRv = (RecyclerView) findViewById(R.id.rv_moment_list_moments);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tv_email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        iv_avator = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_avator);
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
                selectImageAndCrop();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReleaseActivity.class));
            }
        });
        mMomentAdapter = new MomentAdapter(mMomentRv);

        mMomentRv.addOnScrollListener(new BGARVOnScrollListener(this));
        navigationView.setNavigationItemSelectedListener(this);

        if (KChatApp.getInstance().getBmobUser() != null) {
            tv_email.setText(KChatApp.getInstance().getBmobUser().getEmail());
            tv_username.setText(KChatApp.getInstance().getBmobUser().getUsername());
            Picasso.with(MainActivity.this).load(KChatApp.getInstance().getBmobUser().getUser_avator() == null ? "http://ossweb-img.qq.com/upload/apps/ishow/176/thumb_1316413425_-1719592020_13323_sProdImgNo_1.jpg" : KChatApp.getInstance().getBmobUser().getUser_avator()).into(iv_avator);

        }
        processLogic(savedInstanceState);

    }


    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }


    private class MomentAdapter extends BGARecyclerViewAdapter<Dynamic> {

        public MomentAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_dynamic_layout);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, Dynamic moment) {
            if (TextUtils.isEmpty(moment.getContent())) {
                helper.setVisibility(R.id.item_dynamic_content, View.GONE);
            } else {
                helper.setVisibility(R.id.item_dynamic_content, View.VISIBLE);
                helper.setText(R.id.item_dynamic_title, moment.getTitle());
                helper.setText(R.id.item_dynamic_content, moment.getContent());
                helper.setText(R.id.item_dynamic_time, moment.getCreatedAt());
                Picasso.with(MainActivity.this).load(moment.getUser_avator()).into(helper.getImageView(R.id.iv_item_moment_avatar));
            }

            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(MainActivity.this);
            ninePhotoLayout.setData((ArrayList<String>) moment.getImg_list());
        }
    }

    protected void processLogic(Bundle savedInstanceState) {
        setTitle("朋友圈列表");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mMomentRv.setLayoutManager(manager);
        mMomentRv.setAdapter(mMomentAdapter);
        BmobQuery<Dynamic> query = new BmobQuery<Dynamic>();

        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> object, BmobException e) {
                if (e == null) {
                    data.addAll(object);
                    mMomentAdapter.setData(data);
                }

            }
        });

    }

    public void selectImageAndCrop() {
        // 来自相册
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         */
        albumIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, 123);
    }

    /**
     * 将图片Bitmap对象保存为File文件
     *
     * @param bitmap 传入的图片Bitmap对象
     */
    public File saveBitmapFile(Bitmap bitmap) {

        File file = AppCacheDirUtil.creatImageFile(this, "img",
                System.currentTimeMillis() + ".jpeg");// 将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Bitmap img_front = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    presenter.singleupload(saveBitmapFile(img_front).getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
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

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            SPUtils.remove("userinfo");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void uploadsuccess(String url) {
        KChatApp.getInstance().getBmobUser().setUser_avator(url);
        userPresenter.update(KChatApp.getInstance().bmobUser);
    }

    @Override
    public void updatesuccess() {
        SPUtils.save("userinfo", new Gson().toJson(KChatApp.getInstance().getBmobUser()));
        Picasso.with(MainActivity.this).load(KChatApp.getInstance().getBmobUser().getUser_avator() == null ? "http://ossweb-img.qq.com/upload/apps/ishow/176/thumb_1316413425_-1719592020_13323_sProdImgNo_1.jpg" : KChatApp.getInstance().getBmobUser().getUser_avator()).into(iv_avator);
        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void error(String msg) {

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

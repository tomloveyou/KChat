package com.yl.lenovo.kchat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.smile.filechoose.api.ChosenFile;
import com.smile.filechoose.api.ChosenImage;
import com.smile.filechoose.api.FileChooserListener;
import com.smile.filechoose.api.FileChooserManager;
import com.smile.filechoose.api.ImageChooserListener;
import com.smile.filechoose.api.ImageChooserManager;
import com.smile.filechoose.api.utils.ImageChooserBuilder;
import com.smile.filechoose.api.utils.VideoChooserBuilder;
import com.yl.lenovo.kchat.bean.Dynamic;
import com.yl.lenovo.kchat.mvp.contract.FileContract;
import com.yl.lenovo.kchat.mvp.presenter.FilePresenterImpl;
import com.yl.lenovo.kchat.widget.dialog.DialogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPPToolbarActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ReleaseActivity extends BGAPPToolbarActivity implements FileContract.FileMultiUploadView, EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate {
private FileContract.FileUploadPresenter presenter=new FilePresenterImpl(this);
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";

    /**
     * 拖拽排序九宫格控件
     */
    private BGASortableNinePhotoLayout mPhotosSnpl;

    private EditText mContentEt;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release);
        mContentEt = getViewById(R.id.et_moment_add_content);
        mPhotosSnpl = getViewById(R.id.snpl_moment_add_photos);
        setTitle("发布动态");



    }

    @Override
    protected void setListener() {

        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_release) {
                    DialogUtils.showProgressDialog(ReleaseActivity.this,"发布中，请稍后……");
                    presenter.multiupload(mPhotosSnpl.getData().toArray(new String[mPhotosSnpl.getData().size()]));
                }
                return true;
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void uploadsuccess(List<String> urls) {
       Dynamic dynamic=new Dynamic();
        dynamic.setImg_list(urls);
        dynamic.setTitle(KChatApp.getInstance().getBmobUser().getNickname());
        dynamic.setContent(mContentEt.getText().toString());
        dynamic.setUser_avator(KChatApp.getInstance().getBmobUser().getUser_avator());
        dynamic.setOwer(KChatApp.getInstance().getBmobUser().getObjectId());
        dynamic.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                DialogUtils.dismiss();
                error("成功发布");
                finish();
            }
        });
    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dynamic_release_manu, menu);//加载menu文件到布局

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));

        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }
    }
}

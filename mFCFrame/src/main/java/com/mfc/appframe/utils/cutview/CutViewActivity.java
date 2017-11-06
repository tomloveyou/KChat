package com.mfc.appframe.utils.cutview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;

import com.mfc.appframe.R;
import com.mfc.appframe.base.BaseActivity;
import com.mfc.appframe.utils.AppCacheDirUtil;
import com.mfc.appframe.utils.HrefUtils;

import com.mfc.appframe.utils.ToastUtil;
import com.mfc.appframe.utils.cutview.ClipImageLayout;


public class CutViewActivity extends BaseActivity {
	private ClipImageLayout clipImageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContent(R.layout.activity_cut_view);
		setTitle("图片裁剪", true, "裁剪", new OnClickListener() {

			@Override
			public void onClick(View v) {
			Uri uri=Uri.fromFile(saveBitmapFile(clipImageLayout.clip()));
			if (uri!=null) {
				setResult(Activity.RESULT_OK, new Intent().setDataAndType(
						uri,
						"image/*"));
				finish();
			}else {
				ToastUtil.showToast("裁剪失败");
			}
			

			}
		});
		
		clipImageLayout = (ClipImageLayout) findViewById(R.id.activity_cut_view);
		
		clipImageLayout.setImageBitmap(decodeUriAsBitmap(Uri.parse(getIntent().getExtras().get("uri").toString())));
		
	}
	
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri ) {
	    if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}
	/**
	 * 将图片Bitmap对象保存为File文件
	 * 
	 * @param bitmap传入的图片Bitmap对象
	 */
	public File saveBitmapFile(Bitmap bitmap) {

		File file = AppCacheDirUtil.creatImageFile(this, "pilot_certify",
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

}

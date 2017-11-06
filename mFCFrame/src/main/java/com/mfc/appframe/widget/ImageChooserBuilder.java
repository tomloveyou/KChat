package com.mfc.appframe.widget;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
public class ImageChooserBuilder  extends Builder {
    private OnClickListener listener;
    private Context context;
    private String title;
    private String titleGalleryOption;
    private String titleTakePictureOption;

    @SuppressLint({"NewApi"})
    public ImageChooserBuilder(Context context, int theme, OnClickListener listener) {
        super(context, theme);
        this.listener = listener;
        this.context = context;
        this.setupDefaultData();
    }

    @TargetApi(11)
    public ImageChooserBuilder(Context context, OnClickListener listener) {
        super(context);
        this.listener = listener;
        this.context = context;
        this.setupDefaultData();
    }

    public Builder setDialogTitle(String title) {
        this.title = title;
        return this;
    }

    public Builder setDialogTitle(int resId) {
        this.title = this.context.getString(resId);
        return this;
    }

    public Builder setTitleGalleryOption(String titleGalleryOption) {
        this.titleGalleryOption = titleGalleryOption;
        return this;
    }

    public Builder setTitleGalleryOption(int resId) {
        this.titleGalleryOption = this.context.getString(resId);
        return this;
    }

    public Builder setTitleTakePictureOption(String titleTakePictureOption) {
        this.titleTakePictureOption = titleTakePictureOption;
        return this;
    }

    public Builder setTitleTakePictureOption(int resId) {
        this.titleTakePictureOption = this.context.getString(resId);
        return this;
    }

    private void setupDefaultData() {
        this.title = "操作方式";
        this.titleGalleryOption = "编辑";
        this.titleTakePictureOption = "查看";
    }

    @NonNull
    public AlertDialog create() {
        this.setTitle(this.title);
        CharSequence[] titles = new CharSequence[]{this.titleGalleryOption, this.titleTakePictureOption};
        this.setItems(titles, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    ImageChooserBuilder.this.listener.onClick(dialog, 291);
                } else if(which == 1) {
                    ImageChooserBuilder.this.listener.onClick(dialog, 294);
                }

            }
        });
        AlertDialog d = super.create();
        return d;
    }
}

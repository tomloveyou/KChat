package com.yl.lenovo.kchat.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;


/**
 * <dl>  Class Description
 * <dd> 项目名称：gsmobile
 * <dd> 类名称：提示文字加旋转动画的提示框工具类
 * <dd> 类描述：
 * <dd> 创建时间：2016-4-25下午4:34:49 2016
 * <dd> 修改人：无
 * <dd> 修改时间：无
 * <dd> 修改备注：无
 * </dl>
 *
 * @author lirj
 * @see
 */
public class DialogUtils {
    private static ProgressSpinnerDialog dialog;

    public static synchronized void showProgressDialog(Context context, String text) {
        if (context == null) {
            dialog = null;
            return;// 如果所在的activity已经关闭则关闭对话框
        }
        dismiss();
        dialog = new ProgressSpinnerDialog(context);

        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                DialogUtils.dismiss();
            }
        });
        dialog.show(-1, text, null);
    }

    public static synchronized void showProgressDialog(Context context, String text,
                                                       ProgressSpinnerDialog.DialogDismissListener dismissListener) {
        if (context == null) {
            dialog = null;
            return;
        }// 如果所在的activity已经关闭则关闭对话框
        dismiss();
        dialog = new ProgressSpinnerDialog(context);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                DialogUtils.dismiss();
            }
        });
        dialog.show(-1, text, dismissListener);
    }

    public static synchronized void showProgressDialog(Context context, String text,
                                                       int countDownTime, ProgressSpinnerDialog.DialogDismissListener dismissListener) {
        if (context == null) {
            dialog = null;
            return;// 如果所载的activity已经关闭则关闭对话框
        }
        dismiss();
        dialog = new ProgressSpinnerDialog(context);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                DialogUtils.dismiss();
            }
        });
        dialog.show(countDownTime, text, dismissListener);
    }

    public static boolean isShowing() {
        if (dialog != null && dialog.isShowing()) {
            return true;
        }
        return false;
    }

    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * <b>方法描述：设置显示的提示文本信息 </b> <dd>方法作用： <dd>适用条件： <dd>执行流程： <dd>使用方法： <dd>
     * 注意事项： 2016-4-25下午4:37:58
     *
     * @param message
     * @see
     * @since Met 1.0
     */
    public static void setMessage(String message) {
        if (dialog != null) {
            dialog.setMessage(message);
        }
    }
}

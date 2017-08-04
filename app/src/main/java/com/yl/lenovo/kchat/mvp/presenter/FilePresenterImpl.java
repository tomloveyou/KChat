package com.yl.lenovo.kchat.mvp.presenter;

import com.yl.lenovo.kchat.mvp.BasePresenterImpl;
import com.yl.lenovo.kchat.mvp.contract.FileContract;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by lenovo on 2017/7/6.
 */

public class FilePresenterImpl extends BasePresenterImpl implements FileContract.FileUploadPresenter{
private FileContract.FileSingleUploadView fileSingleUploadView;
    private FileContract.FileMultiUploadView fileMultiUploadView;

    public FilePresenterImpl(FileContract.FileSingleUploadView fileSingleUploadView) {
        this.fileSingleUploadView = fileSingleUploadView;
    }

    public FilePresenterImpl(FileContract.FileMultiUploadView fileMultiUploadView) {
        this.fileMultiUploadView = fileMultiUploadView;
    }

    @Override
    public void singleupload(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    fileSingleUploadView.uploadsuccess(bmobFile.getFileUrl());
                }else{
                    fileSingleUploadView.error("上传文件失败：" + e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    @Override
    public void multiupload(final String[] filePaths) {
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                fileMultiUploadView.uploadsuccess(urls);
                if(urls.size()!=filePaths.length){//如果数量相等，则代表文件全部上传完成
                    //do something
                    fileMultiUploadView.error("部分文件上传失败");
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                fileMultiUploadView.error("错误码"+statuscode +",错误描述："+errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }
}

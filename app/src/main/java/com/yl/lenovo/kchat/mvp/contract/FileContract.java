package com.yl.lenovo.kchat.mvp.contract;

import com.yl.lenovo.kchat.mvp.BasePresenter;
import com.yl.lenovo.kchat.mvp.BasePresenterImpl;

import java.util.List;

/**
 * Created by lenovo on 2017/7/6.
 */

public interface FileContract {
    interface FileSingleUploadView{
        void uploadsuccess(String url);
        void error(String msg);
    }
    interface FileMultiUploadView{
        void uploadsuccess(List<String>urls);
        void error(String msg);
    }

    interface FileUploadPresenter extends BasePresenter{
        void singleupload(String path);
        void multiupload(String[] filePaths);
    }
}

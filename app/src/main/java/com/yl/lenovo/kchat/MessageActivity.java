package com.yl.lenovo.kchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yl.lenovo.kchat.bean.Dynamic;
import com.yl.lenovo.kchat.bean.Message;
import com.yl.lenovo.kchat.xlistview.XListView;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPPToolbarActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MessageActivity extends BGAPPToolbarActivity implements XListView.IXListViewListener {
    private XListView pub_listview;
    private List<Message> data = new ArrayList<>();
    private CommonAdapter<Message> messageCommonAdapter;
    private int pagenumber=0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.xlistview);
        setTitle("消息列表");
        pub_listview = (XListView) findViewById(R.id.pub_listview);
        messageCommonAdapter = new CommonAdapter<Message>(MessageActivity.this, android.R.layout.simple_list_item_activated_2, data) {
            @Override
            protected void convert(ViewHolder viewHolder, Message item, int position) {
                viewHolder.setText(android.R.id.text1, "标题：" + item.getAlert());
                viewHolder.setText(android.R.id.text2, "内容：" + item.getContent());
            }
        };
        pub_listview.setAdapter(messageCommonAdapter);
        onRefresh();
    }

    private void load() {

        BmobQuery<Message> query = new BmobQuery<Message>();
        query.setLimit(10);
        query.setSkip(pagenumber);
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                pagenumber+=10;
                pub_listview.stopRefresh();
                pub_listview.stopLoadMore();
                if (e == null) {
                    data.addAll(list);
                    messageCommonAdapter.notifyDataSetChanged();
                }
                if (list.size()==0){
                    pub_listview.setLoadEnd();
                }

            }
        });
    }

    @Override
    protected void setListener() {
        pub_listview.setPullLoadEnable(true);
        pub_listview.setPullRefreshEnable(true);
        pub_listview.setXListViewListener(this);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onRefresh() {
        pagenumber=0;
        data.clear();

        load();
    }

    @Override
    public void onLoadMore() {
        load();
    }
}

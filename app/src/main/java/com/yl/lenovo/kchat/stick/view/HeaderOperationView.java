package com.yl.lenovo.kchat.stick.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yl.lenovo.kchat.ItemDetailActivity;
import com.yl.lenovo.kchat.ItemDetailFragment;
import com.yl.lenovo.kchat.R;
import com.yl.lenovo.kchat.stick.adapter.HeaderOperationAdapter;
import com.yl.lenovo.kchat.stick.model.OperationEntity;
import com.yl.lenovo.kchat.stick.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class HeaderOperationView extends AbsHeaderView<List<OperationEntity>> {

    @BindView(R.id.gv_operation)
    FixedGridView gvOperation;

    public HeaderOperationView(Activity context) {
        super(context);
    }

    @Override
    protected void getView(List<OperationEntity> list, ListView listView) {
        View view = mInflate.inflate(R.layout.header_operation_layout, listView, false);
        ButterKnife.bind(this, view);

        dealWithTheView(list);
        listView.addHeaderView(view);
    }

    private void dealWithTheView(List<OperationEntity> list) {
        if (list == null || list.size() < 2 || list.size() > 6) return;
        if (list.size()%2 != 0) return;

        final HeaderOperationAdapter adapter = new HeaderOperationAdapter(mActivity, list);
        gvOperation.setAdapter(adapter);

        gvOperation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.show(mActivity, adapter.getItem(position).getTitle());
                Context context=view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, position);

                context.startActivity(intent);
            }
        });
    }

}

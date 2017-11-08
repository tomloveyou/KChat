package com.yl.lenovo.kchat.stick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.lenovo.kchat.R;
import com.yl.lenovo.kchat.bean.SplashAndLogin;
import com.yl.lenovo.kchat.stick.adapter.TravelingAdapter;
import com.yl.lenovo.kchat.stick.model.ChannelEntity;
import com.yl.lenovo.kchat.stick.model.FilterData;
import com.yl.lenovo.kchat.stick.model.FilterEntity;
import com.yl.lenovo.kchat.stick.model.FilterTwoEntity;
import com.yl.lenovo.kchat.stick.model.OperationEntity;
import com.yl.lenovo.kchat.stick.model.TravelingEntity;
import com.yl.lenovo.kchat.stick.util.ColorUtil;
import com.yl.lenovo.kchat.stick.util.DensityUtil;
import com.yl.lenovo.kchat.stick.util.ModelUtil;
import com.yl.lenovo.kchat.stick.util.StatusBarUtil;
import com.yl.lenovo.kchat.stick.view.FilterView;
import com.yl.lenovo.kchat.stick.view.HeaderBannerView;
import com.yl.lenovo.kchat.stick.view.HeaderChannelView;
import com.yl.lenovo.kchat.stick.view.HeaderDividerView;
import com.yl.lenovo.kchat.stick.view.HeaderFilterView;
import com.yl.lenovo.kchat.stick.view.HeaderOperationView;
import com.yl.lenovo.kchat.stick.view.SmoothListView.SmoothListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者：孙福生
 * <p>
 * 个人博客：sunfusheng.com
 */
public class TravelActivity extends AppCompatActivity implements SmoothListView.ISmoothListViewListener {

    @BindView(R.id.listView)
    SmoothListView smoothListView;
    @BindView(R.id.real_filterView)
    FilterView realFilterView;
    @BindView(R.id.rl_bar)
    RelativeLayout rlBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_title_bg)
    View viewTitleBg;
    @BindView(R.id.view_action_more_bg)
    View viewActionMoreBg;
    @BindView(R.id.fl_action_more)
    FrameLayout flActionMore;

    private Context mContext;
    private Activity mActivity;
    private int mScreenHeight; // 屏幕高度

    private List<String> bannerList = new ArrayList<>(); // 广告数据
    private List<ChannelEntity> channelList = new ArrayList<>(); // 频道数据
    private List<OperationEntity> operationList = new ArrayList<>(); // 运营数据
    private List<TravelingEntity> travelingList = new ArrayList<>(); // ListView数据

    private HeaderBannerView headerBannerView; // 广告视图
    private HeaderChannelView headerChannelView; // 频道视图
    private HeaderOperationView headerOperationView; // 运营视图
    private HeaderDividerView headerDividerView; // 分割线占位图
    private HeaderFilterView headerFilterView; // 分类筛选视图
    private FilterData filterData; // 筛选数据
    private TravelingAdapter mAdapter;

    private int titleViewHeight = 65; // 标题栏的高度

    private View itemHeaderBannerView; // 从ListView获取的广告子View
    private int bannerViewHeight = 180; // 广告视图的高度
    private int bannerViewTopMargin; // 广告视图距离顶部的距离

    private View itemHeaderFilterView; // 从ListView获取的筛选子View
    private int filterViewPosition = 4; // 筛选视图的位置
    private int filterViewTopMargin; // 筛选视图距离顶部的距离
    private boolean isScrollIdle = true; // ListView是否在滑动
    private boolean isStickyTop = false; // 是否吸附在顶部
    private boolean isSmooth = false; // 没有吸附的前提下，是否在滑动
    private int filterPosition = -1; // 点击FilterView的位置：分类(0)、排序(1)、筛选(2)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_layout);
        ButterKnife.bind(this);
        StatusBarUtil.setStatusBarTranslucent(this, false);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        mContext = this;
        mActivity = this;
        mScreenHeight = DensityUtil.getWindowHeight(this);

        // 筛选数据
        filterData = new FilterData();
        filterData.setCategory(ModelUtil.getCategoryData());
        filterData.setSorts(ModelUtil.getSortData());
        filterData.setFilters(ModelUtil.getFilterData());
        // 广告数据
        bannerList = ModelUtil.getBannerData();
        // 频道数据
        //BmobQuery<ChannelEntity> query=new BmobQuery<>();

        //channelList = ModelUtil.getChannelData();
        getChannelData();
        // 运营数据
        operationList = ModelUtil.getOperationData();
        // ListView数据
         travelingList = ModelUtil.getTravelingData();
        getTravelingData();
    }

    public void getChannelData() {
        BmobQuery<ChannelEntity> query = new BmobQuery<ChannelEntity>();
        query.findObjects(new FindListener<ChannelEntity>() {
            @Override
            public void done(List<ChannelEntity> list, BmobException e) {
                channelList = list;

            }
        });

    }

    private void getTravelingData() {
        BmobQuery<TravelingEntity> query = new BmobQuery<TravelingEntity>();
        query.findObjects(new FindListener<TravelingEntity>() {
            @Override
            public void done(List<TravelingEntity> list, BmobException e) {
                travelingList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        // 设置广告数据
        headerBannerView = new HeaderBannerView(this);
        headerBannerView.fillView(bannerList, smoothListView);

        // 设置频道数据
        headerChannelView = new HeaderChannelView(this);
        headerChannelView.fillView(channelList, smoothListView);

        // 设置运营数据
        headerOperationView = new HeaderOperationView(this);
        headerOperationView.fillView(operationList, smoothListView);

        // 设置分割线
        headerDividerView = new HeaderDividerView(this);
        headerDividerView.fillView("", smoothListView);

        // 设置假FilterView数据
        headerFilterView = new HeaderFilterView(this);
        headerFilterView.fillView(new Object(), smoothListView);

        // 设置真FilterView数据
        realFilterView.setFilterData(mActivity, filterData);
        realFilterView.setVisibility(View.GONE);

        // 设置ListView数据
        mAdapter = new TravelingAdapter(this, travelingList);
        smoothListView.setAdapter(mAdapter);

        filterViewPosition = smoothListView.getHeaderViewsCount() - 1;
    }

    private void initListener() {
        // 关于
        flActionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AboutActivity.class));
            }
        });

        // (假的ListView头部展示的)筛选视图点击
        headerFilterView.getFilterView().setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                isSmooth = true;
                smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, titleViewHeight));
            }
        });

        // (真正的)筛选视图点击
        realFilterView.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                realFilterView.show(position);
                smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, titleViewHeight));
            }
        });

        // 分类Item点击
        realFilterView.setOnItemCategoryClickListener(new FilterView.OnItemCategoryClickListener() {
            @Override
            public void onItemCategoryClick(FilterTwoEntity leftEntity, FilterEntity rightEntity) {
                fillAdapter(ModelUtil.getCategoryTravelingData(leftEntity, rightEntity));
            }
        });

        // 排序Item点击
        realFilterView.setOnItemSortClickListener(new FilterView.OnItemSortClickListener() {
            @Override
            public void onItemSortClick(FilterEntity entity) {
                fillAdapter(ModelUtil.getSortTravelingData(entity));
            }
        });

        // 筛选Item点击
        realFilterView.setOnItemFilterClickListener(new FilterView.OnItemFilterClickListener() {
            @Override
            public void onItemFilterClick(FilterEntity entity) {
                fillAdapter(ModelUtil.getFilterTravelingData(entity));
            }
        });

        smoothListView.setRefreshEnable(true);
        smoothListView.setLoadMoreEnable(true);
        smoothListView.setSmoothListViewListener(this);
        smoothListView.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
            @Override
            public void onSmoothScrolling(View view) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScrollIdle && bannerViewTopMargin < 0) return;

                // 获取广告头部View、自身的高度、距离顶部的高度
                if (itemHeaderBannerView == null) {
                    itemHeaderBannerView = smoothListView.getChildAt(1);
                }
                if (itemHeaderBannerView != null) {
                    bannerViewTopMargin = DensityUtil.px2dip(mContext, itemHeaderBannerView.getTop());
                    bannerViewHeight = DensityUtil.px2dip(mContext, itemHeaderBannerView.getHeight());
                }

                // 获取筛选View、距离顶部的高度
                if (itemHeaderFilterView == null) {
                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderFilterView != null) {
                    filterViewTopMargin = DensityUtil.px2dip(mContext, itemHeaderFilterView.getTop());
                }

                // 处理筛选是否吸附在顶部
                if (filterViewTopMargin <= titleViewHeight || firstVisibleItem > filterViewPosition) {
                    isStickyTop = true; // 吸附在顶部
                    realFilterView.setVisibility(View.VISIBLE);
                } else {
                    isStickyTop = false; // 没有吸附在顶部
                    realFilterView.setVisibility(View.GONE);
                }

                if (isSmooth && isStickyTop) {
                    isSmooth = false;
                    realFilterView.show(filterPosition);
                }

                // 处理标题栏颜色渐变
                handleTitleBarColorEvaluate();
            }
        });
    }

    // 填充数据
    private void fillAdapter(List<TravelingEntity> list) {
        if (list == null || list.size() == 0) {
            int height = mScreenHeight - DensityUtil.dip2px(mContext, 95); // 95 = 标题栏高度 ＋ FilterView的高度
            mAdapter.setData(ModelUtil.getNoDataEntity(height));
        } else {
            mAdapter.setData(list);
        }
    }

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        if (bannerViewTopMargin > 0) {
            fraction = 1f - bannerViewTopMargin * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            rlBar.setAlpha(fraction);
            return;
        }

        float space = Math.abs(bannerViewTopMargin) * 1f;
        fraction = space / (bannerViewHeight - titleViewHeight);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        rlBar.setAlpha(1f);

        if (fraction >= 1f || isStickyTop) {
            isStickyTop = true;
            viewTitleBg.setAlpha(0f);
            viewActionMoreBg.setAlpha(0f);
            rlBar.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            viewTitleBg.setAlpha(1f - fraction);
            viewActionMoreBg.setAlpha(1f - fraction);
            rlBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(mContext, fraction, R.color.transparent, R.color.colorPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        headerBannerView.enqueueBannerLoopMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        headerBannerView.removeBannerLoopMessage();
    }

    @Override
    public void onBackPressed() {
        if (realFilterView.isShowing()) {
            realFilterView.resetAllStatus();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothListView.stopRefresh();
                smoothListView.setRefreshTime("刚刚");
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothListView.stopLoadMore();
            }
        }, 2000);
    }

}

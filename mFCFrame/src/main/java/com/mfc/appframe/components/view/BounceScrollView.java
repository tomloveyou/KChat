package com.mfc.appframe.components.view;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/*  
 *@author luoxiaohui
 *@date 2016年4月18日
 *@description 具有弹性的ScrollView
 **/

public class BounceScrollView extends ScrollView {

	 // 保存ScrollView中子控件
    private View contentView = null;
    // 用来保存唯一子控件的布局信息
    private Rect contentViewRect = new Rect();
    // 移动开始时候的Y坐标
    private float startY;
    // 线性阻尼 缓冲过量移动的移动速度
    private static float MOVE_FACTOR = 0.5f;
    //过度位移恢复的动画持续时间
    private static long DURATION_MILLIS = 280;
 
    public BounceScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
 
    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public BounceScrollView(Context context) {
        super(context);
    }
 
    /**
     * 在布局完成后得到ScrollView的唯一子View，并存在contentView中
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
    }
 
    /**
     * 在事件分发其中处理触摸事件
     * 根据android中事件分发的机制判断，个人觉得把事件处理逻辑写在分发器中比写在onTouchEvent中好些，
     * 因为在其子View没有接收到该触摸事件之前自己就处理了触摸事件。
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
 
        if (contentView != null)
            switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    playAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int detailY = (int) (nowY - startY);
                if (isNeedMove(detailY)) {
                    // 超出屏幕后滚动的View移动的距离为滑动位移的MOVE_FACTOR倍
                    detailY = (int) (detailY * MOVE_FACTOR);
                    //重新布局子View，并且只修改顶部与底部的位置
                    contentView.layout(contentViewRect.left, contentViewRect.top + detailY, contentViewRect.right,
                            contentViewRect.bottom + detailY);
                }
                break;
            default:
                break;
            }
 
        return super.dispatchTouchEvent(ev);
    }
 
     
    /**
     * 在布局都完成后contentView的布局也就确定了
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //在未超出移动前contentView的布局没有发生变化 即全局中contentView的布局不变
        if(contentView != null){
            contentViewRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(),
                    contentView.getBottom());
            }
    }
 
    /**
     * 判断是否需要超出屏幕移动
     * 
     * 通过三个量来判断是否需要移动及如何移动，这三个量分别为scrollY、
     * contentViewHeight和scrollViewHeight外加辅助detailY手指移动的位移。分三种情况：
     * 
     * 其中两种均为contentViewHeight>scrollViewHeight：
     * 1、当contentView的顶部处于ScrollView顶部且向下滑动手指时候需要超出屏幕移动条件为：
     * scrollY == 0 && detailY > 0, 如图：
     * |-----scrollViewHeight-----|
     * |----------contentViewHeight--------|
     *  -----detailY---->
     *  
     * 2、当contentView的底部处于ScrollView底部且向上滑动手指时候需要超出屏幕移动条件为：
     * scrollY + scrollViewHeight >= contentViewHeight && detailY < 0, 如图：
     * |--scrollY--|
     *             |-----scrollViewHeight-----|
     * |-----------contentViewHeight----------|
     *                       <-----detailY----
     *                       
     * 另外一种情况是contentViewHeight<=scrollViewHeight上下滑动都需要做超出屏幕移动
     * 3、当contentView的本身处于ScrollView内部时候无论向上或向下滑动手指时候都需要超出屏幕移动条件为：
     * contentViewHeight <= scrollViewHeight，如图：
     * |-----scrollViewHeight-----|
     * |---contentViewHeight---|
     *  <-----detailY---->
     * 
     * @param detailY
     *            手指移动的位移（向下或向右滑动为正方向）
     * @return 是否需要移动
     */
    private boolean isNeedMove(int detailY) {
        int scrollY = getScrollY();
        int contentViewHeight = contentView.getHeight();
        int scrollViewHeight = getHeight();
 
        return (scrollY == 0 && detailY > 0)|| (scrollY + scrollViewHeight >= contentViewHeight && detailY < 0)
                || (contentViewHeight <= scrollViewHeight);
    }
 
    /**
     * 播放contentView复位的动画并将contentView复位
     * 动画可以自定义
     * 动画执行时间随拉伸的距离增加而减少
     */
    private void playAnimation() {
        int contentViewTop = contentView.getTop();
        int scrollViewHeight = this.getHeight();
        float factor = 1-Math.abs(contentViewTop - contentViewRect.top)/(scrollViewHeight*1.0f);
        TranslateAnimation ta = new TranslateAnimation(0,0,contentViewTop,contentViewRect.top);
        ta.setDuration((long) (DURATION_MILLIS*factor));
        contentView.startAnimation(ta);
        contentView.layout(contentViewRect.left, contentViewRect.top
                ,contentViewRect.right,contentViewRect.bottom);
    }
 
    /**
     * 判断是否需要动画效果
     * @return
     */
    private boolean isNeedAnimation() {
        return contentView.getTop() != contentViewRect.top;
    }
}

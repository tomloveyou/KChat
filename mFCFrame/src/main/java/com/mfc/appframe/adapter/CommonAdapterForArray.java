package com.mfc.appframe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapterForArray<T> extends BaseAdapter
{
	protected Context mContext;
	protected T[] mDatas;
	protected LayoutInflater mInflater;
	private int layoutId;

	/***
	 * 实例commonadapter
	 * @param context
	 * @param datas 数据源
	 * @param layoutId item布局资源id
	 */
	public CommonAdapterForArray(Context context, T[] datas, int layoutId)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}
	
	public void setData(T[] datas) {
		this.mDatas = datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return mDatas.length;
	}

	@Override
	public T getItem(int position)
	{
		return mDatas[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t);

}

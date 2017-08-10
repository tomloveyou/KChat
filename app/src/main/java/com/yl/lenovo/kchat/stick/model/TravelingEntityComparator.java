package com.yl.lenovo.kchat.stick.model;

import java.util.Comparator;

import cn.bmob.v3.BmobObject;

/**
 * Created by sunfusheng on 16/4/25.
 */
public class TravelingEntityComparator extends BmobObject implements Comparator<TravelingEntity> {

    @Override
    public int compare(TravelingEntity lhs, TravelingEntity rhs) {
        return rhs.getRank() - lhs.getRank();
    }
}

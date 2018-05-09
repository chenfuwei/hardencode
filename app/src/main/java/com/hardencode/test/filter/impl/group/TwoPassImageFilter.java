package com.hardencode.test.filter.impl.group;

import android.content.Context;

import com.hardencode.test.filter.impl.BaseImageFilter;

public class TwoPassImageFilter extends BaseFilterGroup{
    public TwoPassImageFilter(Context mContext, String firstVertexShader, String firstFragShader,
                              String secondVertexShader, String secondFragShader) {
        super(mContext);
        addFilter(new BaseImageFilter(mContext, firstVertexShader, firstFragShader));
        addFilter(new BaseImageFilter(mContext, secondVertexShader, secondFragShader));
    }
}

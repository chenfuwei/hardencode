package com.hardencode.test.filter.impl.group;

import android.content.Context;

import com.hardencode.test.filter.impl.BaseImageFilter;

import java.util.ArrayList;
import java.util.List;

public class BaseFilterGroup extends BaseImageFilter{
    private List<BaseImageFilter> filters;

    public BaseFilterGroup(Context mContext) {
        super(mContext);
    }

    public void setFilters(List<BaseImageFilter> filters)
    {
        if(null == this.filters)
        {
            this.filters = new ArrayList<>();
        }
        this.filters.clear();
        this.filters.addAll(filters);
    }

    public void addFilter(BaseImageFilter filter)
    {
        if(null == this.filters)
        {
            filters = new ArrayList<>();
        }
        filters.add(filter);
    }

    @Override
    public void onInputImageSizeChange(int nWidth, int nHeight) {
        super.onInputImageSizeChange(nWidth, nHeight);

        if(null != filters && filters.size() > 0)
        {
            for(BaseImageFilter filter : filters)
            {
                filter.onInputImageSizeChange(nWidth, nHeight);
            }
        }
    }

    @Override
    public void onOutputSizeChange(int nWidth, int nHeight) {
        super.onOutputSizeChange(nWidth, nHeight);
        if(null != filters && filters.size() > 0)
        {
            for(BaseImageFilter filter : filters)
            {
                filter.onOutputSizeChange(nWidth, nHeight);
            }
        }
    }


    @Override
    public int onDrawFrameToTexture(int glTexture) {
        if(null != filters && filters.size() > 0)
        {
            for(BaseImageFilter filter : filters)
            {
                glTexture = filter.onDrawFrameToTexture(glTexture);
            }
        }
        return glTexture;
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
        if(null != filters && filters.size() > 0)
        {
            for(BaseImageFilter filter : filters)
            {
                filter.setProgressValue(percent);
            }
        }
    }
}

package com.hardencode.test.filter;

import android.content.Context;

import com.hardencode.test.filter.impl.BaseImageFilter;
import com.hardencode.test.filter.impl.BrightnessImageFilter;
import com.hardencode.test.filter.impl.ContrastImageFilter;

public class ImageFilterFactory {
    public BaseImageFilter getImageFilter(ImageFilter imageFilter, Context mContext)
    {
        if(imageFilter == ImageFilter.BRIGHTNESS)
        {
            return new BrightnessImageFilter(mContext);
        }else if(imageFilter == ImageFilter.CONTRAST){
            return new ContrastImageFilter(mContext);
        }else
        {
            return new BaseImageFilter(mContext);
        }
    }
}

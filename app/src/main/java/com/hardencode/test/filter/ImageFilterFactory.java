package com.hardencode.test.filter;

import android.content.Context;

import com.hardencode.test.filter.impl.BaseImageFilter;
import com.hardencode.test.filter.impl.BrightnessImageFilter;
import com.hardencode.test.filter.impl.GrayScaleImageFilter;
import com.hardencode.test.filter.impl.colormatrix.ColorMatrixImageFilter;
import com.hardencode.test.filter.impl.ContrastImageFilter;
import com.hardencode.test.filter.impl.GammaImageFilter;
import com.hardencode.test.filter.impl.HueImageFilter;
import com.hardencode.test.filter.impl.colormatrix.SepiaImageFilter;

public class ImageFilterFactory {
    public BaseImageFilter getImageFilter(ImageFilter imageFilter, Context mContext)
    {
        if(imageFilter == ImageFilter.BRIGHTNESS)
        {
            return new BrightnessImageFilter(mContext);
        }else if(imageFilter == ImageFilter.CONTRAST){
            return new ContrastImageFilter(mContext);
        }else if(imageFilter == ImageFilter.GAMMA){
            return new GammaImageFilter(mContext);
        }else if(imageFilter == ImageFilter.HUE){
            return new HueImageFilter(mContext);
        }else if(imageFilter == ImageFilter.COLORMATRIX){
            return new ColorMatrixImageFilter(mContext);
        }else if(imageFilter == ImageFilter.SEPIAL){
            return new SepiaImageFilter(mContext);
        }else if(imageFilter == ImageFilter.GRAYSCALE){
            return new GrayScaleImageFilter(mContext);
        }else
        {
            return new BaseImageFilter(mContext);
        }
    }
}

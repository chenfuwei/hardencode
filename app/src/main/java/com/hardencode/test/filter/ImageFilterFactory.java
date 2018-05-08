package com.hardencode.test.filter;

import android.content.Context;

import com.hardencode.test.filter.impl.BaseImageFilter;
import com.hardencode.test.filter.impl.BrightnessImageFilter;
import com.hardencode.test.filter.impl.BulgeDistortionImageFilter;
import com.hardencode.test.filter.impl.CGAColorSpaceImageFilter;
import com.hardencode.test.filter.impl.twoinput.blend.ColorDodgeImageFilter;
import com.hardencode.test.filter.impl.GrayScaleImageFilter;
import com.hardencode.test.filter.impl.KuwaharaImageFilter;
import com.hardencode.test.filter.impl.OpacityImageFilter;
import com.hardencode.test.filter.impl.PosterizeImageFilter;
import com.hardencode.test.filter.impl.SharpnessImageFilter;
import com.hardencode.test.filter.impl.SphereRefractionImageFilter;
import com.hardencode.test.filter.impl.SwirlImageFilter;
import com.hardencode.test.filter.impl.TransformImageFilter;
import com.hardencode.test.filter.impl.VignetteImageFilter;
import com.hardencode.test.filter.impl.WhiteBalanceImageFilter;
import com.hardencode.test.filter.impl.colormatrix.ColorMatrixImageFilter;
import com.hardencode.test.filter.impl.ContrastImageFilter;
import com.hardencode.test.filter.impl.GammaImageFilter;
import com.hardencode.test.filter.impl.HueImageFilter;
import com.hardencode.test.filter.impl.colormatrix.SepiaImageFilter;
import com.hardencode.test.filter.impl.twoinput.blend.AddBlendImageFilter;
import com.hardencode.test.filter.impl.twoinput.blend.AlphaBlendImageFilter;
import com.hardencode.test.filter.impl.twoinput.blend.ColorBlendImageFilter;
import com.hardencode.test.filter.impl.twoinput.blend.ColorBurnImageFilter;
import com.hardencode.test.filter.impl.twoinput.blend.DarkenBlendImageFilter;

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
        }else if(imageFilter == ImageFilter.SWIRL){
            return new SwirlImageFilter(mContext);
        }else if(imageFilter == ImageFilter.WHITEBALANCE){
            return new WhiteBalanceImageFilter(mContext);
        }else if(imageFilter == ImageFilter.BULGE) {
            return new BulgeDistortionImageFilter(mContext);
        }else if(imageFilter == ImageFilter.CGACOLOR){
            return new CGAColorSpaceImageFilter(mContext);
        }else if(imageFilter == ImageFilter.VIGNETTE){
            return new VignetteImageFilter(mContext);
        }else if(imageFilter == ImageFilter.TRANSFORM){
            return new TransformImageFilter(mContext);
        }else if(imageFilter == ImageFilter.SPHEREFRACTION){
            return new SphereRefractionImageFilter(mContext);
        }else if(imageFilter == ImageFilter.SHARPNESS){
            return new SharpnessImageFilter(mContext);
        }else if(imageFilter == ImageFilter.POSTERIZE){
            return new PosterizeImageFilter(mContext);
        }else if(imageFilter == ImageFilter.OPACITY){
            return new OpacityImageFilter(mContext);
        }else if(imageFilter == ImageFilter.KUWAHARA){
            return new KuwaharaImageFilter(mContext);
        }else if(imageFilter == ImageFilter.ALPHABLEND){
            return new AlphaBlendImageFilter(mContext);
        }else if(imageFilter == ImageFilter.ADDBLEND){
            return new AddBlendImageFilter(mContext);
        }else if(imageFilter == ImageFilter.COLORBLEND){
            return new ColorBlendImageFilter(mContext);
        }else if(imageFilter == ImageFilter.COLORBURN){
            return new ColorBurnImageFilter(mContext);
        }else if(imageFilter == ImageFilter.COLORDODGE)
        {
            return new ColorDodgeImageFilter(mContext);
        }else if(imageFilter == ImageFilter.DARKEN){
            return new DarkenBlendImageFilter(mContext);
        }else

        {
            return new BaseImageFilter(mContext);
        }
    }
}

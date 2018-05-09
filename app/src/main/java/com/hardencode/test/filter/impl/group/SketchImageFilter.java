package com.hardencode.test.filter.impl.group;

import android.content.Context;

import com.hardencode.test.filter.impl.GrayScaleImageFilter;
import com.hardencode.test.filter.impl.sample.Image3x3TextureSampingFilter;

public class SketchImageFilter extends BaseFilterGroup{
    public SketchImageFilter(Context mContext) {
        super(mContext);

        addFilter(new GrayScaleImageFilter(mContext));
        addFilter(new Image3x3TextureSampingFilter(mContext, "image_3x3_vertex_shader.sh", "sketch_frag_shader.sh"));
    }
}

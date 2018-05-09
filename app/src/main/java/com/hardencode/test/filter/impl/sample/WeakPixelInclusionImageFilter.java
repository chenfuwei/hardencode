package com.hardencode.test.filter.impl.sample;

import android.content.Context;

public class WeakPixelInclusionImageFilter extends Image3x3TextureSampingFilter{
    public WeakPixelInclusionImageFilter(Context mContext) {
        super(mContext, "image_3x3_vertex_shader.sh", "weak_pixel_inclusion_frag_shader.sh");
    }
}

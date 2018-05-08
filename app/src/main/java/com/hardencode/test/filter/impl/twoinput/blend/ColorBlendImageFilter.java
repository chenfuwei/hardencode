package com.hardencode.test.filter.impl.twoinput.blend;

import android.content.Context;

import com.hardencode.test.filter.impl.twoinput.TwoInputImageFilter;

public class ColorBlendImageFilter extends TwoInputImageFilter{
    public ColorBlendImageFilter(Context mContext) {
        super(mContext, "default_twoinput_vertex_shader.sh", "color_blend_frag_shader.sh");
    }
}

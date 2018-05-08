package com.hardencode.test.filter.impl.twoinput.blend;

import android.content.Context;

import com.hardencode.test.filter.impl.twoinput.TwoInputImageFilter;

public class DarkenBlendImageFilter extends TwoInputImageFilter{
    public DarkenBlendImageFilter(Context mContext) {
        super(mContext, "default_twoinput_vertex_shader.sh", "darken_frag_shader.sh");
    }
}

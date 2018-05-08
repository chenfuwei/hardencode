package com.hardencode.test.filter.impl.twoinput.blend;

import android.content.Context;

public class AlphaBlendImageFilter extends MixBlendImageFilter{
    public AlphaBlendImageFilter(Context mContext) {
        super(mContext, "default_twoinput_vertex_shader.sh", "alpha_blend_frag_shader.sh");
    }
}

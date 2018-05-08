package com.hardencode.test.filter.impl.twoinput.blend;

import android.content.Context;

import com.hardencode.test.filter.impl.twoinput.TwoInputImageFilter;

public class AddBlendImageFilter extends TwoInputImageFilter{
    public AddBlendImageFilter(Context mContext) {
        super(mContext,"default_twoinput_vertex_shader.sh", "add_blend_frag_shader.sh");
    }


}

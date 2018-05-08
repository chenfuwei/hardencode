package com.hardencode.test.filter.impl.twoinput.blend;

import android.content.Context;

import com.hardencode.test.filter.impl.twoinput.TwoInputImageFilter;

public class ColorBurnImageFilter extends TwoInputImageFilter{
    public ColorBurnImageFilter(Context mContext) {
        super(mContext,"default_twoinput_vertex_shader.sh", "color_burn_frag_shader.sh");
    }
}

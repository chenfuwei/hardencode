package com.hardencode.test.filter.impl.twoinput.blend;

import android.content.Context;

import com.hardencode.test.filter.impl.BaseImageFilter;
import com.hardencode.test.filter.impl.twoinput.TwoInputImageFilter;

public class ColorDodgeImageFilter extends TwoInputImageFilter {
    public ColorDodgeImageFilter(Context mContext) {
        super(mContext, "default_twoinput_vertex_shader.sh", "color_dodge_frag_shader.sh");
    }
}

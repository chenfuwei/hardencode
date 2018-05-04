package com.hardencode.test.filter.impl;

import android.content.Context;

public class CGAColorSpaceImageFilter extends BaseImageFilter{
    public CGAColorSpaceImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "cga_color_space_frag_shader.sh");
    }
}

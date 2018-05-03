package com.hardencode.test.filter.impl;

import android.content.Context;

/*
灰度化，在RGB模型中，如果R=G=B时，则彩色表示一种灰度颜色，其中R=G=B的值叫灰度值，因此，灰度图像每个像素只需一个字节存放灰度值（又称强度值、亮度值），灰度范围为0-255。
一般有分量法 最大值法平均值法加权平均法四种方法对彩色图像进行灰度化。
 */
public class GrayScaleImageFilter extends BaseImageFilter{
    public GrayScaleImageFilter(Context mContext) {
        super(mContext, "default_vertex_shader.sh", "grayscale_frag_shader.sh");
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void onDrawFramePre() {
        super.onDrawFramePre();
    }

    @Override
    public void setProgressValue(float percent) {
        super.setProgressValue(percent);
    }
}

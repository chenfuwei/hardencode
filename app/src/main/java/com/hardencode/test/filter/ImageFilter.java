package com.hardencode.test.filter;

public enum ImageFilter {
    DEFAULT("default"), BRIGHTNESS("brightness"), CONTRAST("contrast"), GAMMA("gamma"),
    HUE("hue"), COLORMATRIX("colormatrix"), SEPIAL("sepial"), GRAYSCALE("grayscale"),
    SWIRL("Swirl"), WHITEBALANCE("wihtebalance"), BULGE("bulge distortion"), CGACOLOR("CGA color"),
    VIGNETTE("vignette"), TRANSFORM("transform"), SPHEREFRACTION("shpere refraction"),
    SHARPNESS("sharpness"), POSTERIZE("posterize"),OPACITY("opacity"), KUWAHARA("kuwahara"),
    ALPHABLEND("alpha blend"), ADDBLEND("add blend"), COLORBLEND("color blend"), COLORBURN("color burn"),
    COLORDODGE("color dodge"), DARKEN("darken"), WEAKPIXEL("weak pixel"), TONE("tone"), LAPULACIAN("lapulacian"),
    CONVOLUTION("convolution"), EMBOSS("emboss");

    private String value;

    ImageFilter(String value) {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

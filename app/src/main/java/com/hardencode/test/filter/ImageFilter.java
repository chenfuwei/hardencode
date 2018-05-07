package com.hardencode.test.filter;

public enum ImageFilter {
    DEFAULT("default"), BRIGHTNESS("brightness"), CONTRAST("contrast"), GAMMA("gamma"),
    HUE("hue"), COLORMATRIX("colormatrix"), SEPIAL("sepial"), GRAYSCALE("grayscale"),
    SWIRL("Swirl"), WHITEBALANCE("wihtebalance"), BULGE("bulge distortion"), CGACOLOR("CGA color"),
    VIGNETTE("vignette"), TRANSFORM("transform"), SPHEREFRACTION("shpere refraction"),
    SHARPNESS("sharpness"), POSTERIZE("posterize"),OPACITY("opacity"), KUWAHARA("kuwahara");

    private String value;

    ImageFilter(String value) {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

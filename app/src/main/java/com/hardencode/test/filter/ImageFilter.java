package com.hardencode.test.filter;

public enum ImageFilter {
    DEFAULT("default"), BRIGHTNESS("brightness"), CONTRAST("contrast"), GAMMA("gamma"),
    HUE("hue"), COLORMATRIX("colormatrix"), SEPIAL("sepial"), GRAYSCALE("grayscale"),
    SWIRL("Swirl");

    private String value;

    ImageFilter(String value) {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

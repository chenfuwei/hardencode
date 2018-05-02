package com.hardencode.test.filter;

public enum ImageFilter {
    DEFAULT(0), BRIGHTNESS(1), CONTRAST(2), GAMMA(3);

    private int value;

    ImageFilter(int value) {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}

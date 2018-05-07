attribute vec3 aPosition;
attribute vec2 aCoordinate;
varying vec2 aVaryCoordinate;

uniform float widthFactor;
uniform float heightFactor;

varying vec2 center;
varying vec2 left;
varying vec2 right;
varying vec2 top;
varying vec2 bottom;
varying vec2 lefttop;
varying vec2 righttop;
varying vec2 leftbottom;
varying vec2 rightbottom;

void main()
{
    vec2 widthVec = vec2(widthFactor, 0.0);
    vec2 heightVec = vec2(0.0, heightFactor);

    center = aCoordinate;
    left = center - widthVec;
    right = center + widthVec;
    top = center + heightVec;
    bottom = center - heightVec;
    lefttop = left + heightVec;
    righttop = right + heightVec;
    leftbottom = left - heightVec;
    rightbottom = right - heightVec;

    gl_Position = vec4(aPosition, 1.0);
}
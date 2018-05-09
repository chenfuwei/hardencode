attribute vec3 aPosition;
attribute vec2 aCoordinate;
varying vec2 aVaryCoordinate;

uniform float widthTexel;
uniform float heightTexel;

varying vec2 textureCoord;
varying vec2 leftTextureCoord;
varying vec2 rightTextureCoord;

varying vec2 topTextureCoord;
varying vec2 topLeftTextureCoord;
varying vec2 topRightTextureCoord;

varying vec2 bottomTextureCoord;
varying vec2 bottomLeftTextureCoord;
varying vec2 bottomRightTextureCoord;

void main()
{
    gl_Position = vec4(aPosition, 1.0);

    vec2 widthStep = vec2(widthTexel, 0.0);
    vec2 heightStep = vec2(0.0, heightTexel);
    vec2 widthHeightStep = vec2(widthTexel, heightTexel);
    vec2 widthNegativeHeightStep = vec2(widthTexel, -heightTexel);
    vec2 neagativeWidthHeightStep = vec2(-widthTexel, heightTexel);

    textureCoord = aCoordinate;
    leftTextureCoord = textureCoord - widthStep;
    rightTextureCoord = textureCoord + widthStep;

    topTextureCoord = textureCoord - heightStep;
    topLeftTextureCoord = textureCoord - widthHeightStep;
    topRightTextureCoord = textureCoord + widthNegativeHeightStep;

    bottomTextureCoord = textureCoord + heightStep;
    bottomLeftTextureCoord = textureCoord + neagativeWidthHeightStep;
    bottomRightTextureCoord = textureCoord + widthHeightStep;
}
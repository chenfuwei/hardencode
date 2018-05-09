precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

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
    float topLeftIndensity = texture2D(aInputImageTexture, topLeftTextureCoord).r;
    float topRightIndensity = texture2D(aInputImageTexture, topRightTextureCoord).r;
    float topIndensity = texture2D(aInputImageTexture, topTextureCoord).r;

    float bottomLeftIndensity = texture2D(aInputImageTexture, bottomLeftTextureCoord).r;
    float bottomRightIndensity = texture2D(aInputImageTexture, bottomRightTextureCoord).r;
    float bottomIndensity = texture2D(aInputImageTexture, bottomTextureCoord).r;

    float leftIndensity = texture2D(aInputImageTexture, leftTextureCoord).r;
    float rightIndensity = texture2D(aInputImageTexture, rightTextureCoord).r;
    float centerIndensity = texture2D(aInputImageTexture, textureCoord).r;

    vec4 textureColor = texture2D(aInputImageTexture, aVaryCoordinate);

    float h = -topLeftIndensity - 2.0 * topIndensity - topRightIndensity + bottomLeftIndensity + 2.0 * bottomIndensity + bottomRightIndensity;
    float v = -bottomLeftIndensity - 2.0 * leftIndensity - topLeftIndensity + bottomRightIndensity + 2.0 * rightIndensity + topRightIndensity;

    float mag = 1.0 - length(vec2(h, v));

    gl_FragColor = vec4(vec3(mag), 1.0);
}
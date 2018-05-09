precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;
uniform mat3 coordFactor;


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
    vec3 center = texture2D(aInputImageTexture, textureCoord).rgb;
    vec3 left = texture2D(aInputImageTexture, leftTextureCoord).rgb;
    vec3 right = texture2D(aInputImageTexture, rightTextureCoord).rgb;

    vec3 top = texture2D(aInputImageTexture, topTextureCoord).rgb;
    vec3 topLeft = texture2D(aInputImageTexture, topLeftTextureCoord).rgb;
    vec3 topRight = texture2D(aInputImageTexture, topRightTextureCoord).rgb;

    vec3 bottom = texture2D(aInputImageTexture, bottomTextureCoord).rgb;
    vec3 bottomLeft = texture2D(aInputImageTexture, bottomLeftTextureCoord).rgb;
    vec3 bottomRight = texture2D(aInputImageTexture, bottomRightTextureCoord).rgb;

    vec3 result = topLeft * coordFactor[0][0] + top * coordFactor[0][1] + topRight * coordFactor[0][2];
    result += left * coordFactor[1][0] + center * coordFactor[1][1] + right * coordFactor[1][2];
    result += bottomLeft * coordFactor[2][0] + bottom * coordFactor[2][1] + bottomRight * coordFactor[2][2];

    result += 0.5;

    gl_FragColor = vec4(result, 1.0);
}
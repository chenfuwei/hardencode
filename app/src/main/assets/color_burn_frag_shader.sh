precision highp float;

uniform sampler2D aInputImageTexture;
uniform sampler2D aInputImageTexture2;
varying vec2 aVaryCoordinate;
varying vec2 aVaryCoordinate2;

void main()
{
    vec4 sourceColor = texture2D(aInputImageTexture, aVaryCoordinate);
    vec4 blendColor = texture2D(aInputImageTexture2, aVaryCoordinate2);
    vec4 whiteColor = vec4(1.0);
    gl_FragColor = whiteColor - (whiteColor - sourceColor) / (blendColor);
}
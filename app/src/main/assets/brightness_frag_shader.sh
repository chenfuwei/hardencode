precision mediump float;

uniform float aBrightness;
uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

void main()
{
    vec4 textureColor = texture2D(aInputImageTexture, aVaryCoordinate);
    gl_FragColor = vec4(textureColor.rgb * aBrightness, textureColor.a);
}

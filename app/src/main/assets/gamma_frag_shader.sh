precision mediump float;

uniform float aGamma;
uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

void main()
{
    vec4 textureColor = texture2D(aInputImageTexture, aVaryCoordinate);
    gl_FragColor = vec4(pow(textureColor.rgb, vec3(aGamma)), textureColor.a);
}
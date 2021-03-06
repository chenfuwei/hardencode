precision mediump float;

uniform sampler2D aInputImageTexture;
uniform float aContrast;
varying vec2 aVaryCoordinate;

void main()
{
    vec4 textureColor = texture2D(aInputImageTexture, aVaryCoordinate);
    gl_FragColor = vec4((textureColor.rgb - vec3(0.5)) * aContrast + vec3(0.5), textureColor.a);
}
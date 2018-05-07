precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;
uniform float colorLevel;

void main()
{
    vec4 color = texture2D(aInputImageTexture, aVaryCoordinate);
    gl_FragColor = floor(color * colorLevel + vec4(0.5)) / colorLevel;
}
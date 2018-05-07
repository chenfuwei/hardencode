precision highp float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;
uniform float opacity;

void main()
{
    vec4 color = texture2D(aInputImageTexture, aVaryCoordinate);
    gl_FragColor = vec4(color.rgb, color.a * opacity);
}
precision highp float;

uniform sampler2D aInputImageTexture;
uniform sampler2D aInputImageTexture2;
varying vec2 aVaryCoordinate;
varying vec2 aVaryCoordinate2;

void main()
{
    vec4 base = texture2D(aInputImageTexture, aVaryCoordinate);
    vec4 overlay = texture2D(aInputImageTexture2, aVaryCoordinate2);

    gl_FragColor = vec4(min(base.rgb * overlay.a, overlay.rgb * base.a) + base.rgb * (1.0 - overlay.a) + overlay.rgb * (1.0 - base.a), 1.0);
}
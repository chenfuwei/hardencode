precision mediump float;

uniform sampler2D aInputImageTexture;
uniform sampler2D aInputImageTexture2;
varying vec2 aVaryCoordinate;
varying vec2 aVaryCoordinate2;
uniform float mixpercent;

void main()
{
    vec4 sourceColor = texture2D(aInputImageTexture, aVaryCoordinate);
    vec4 sourceColor2 = texture2D(aInputImageTexture2, aVaryCoordinate2);

    gl_FragColor = mix(sourceColor, sourceColor2, sourceColor2.a * mixpercent);
}
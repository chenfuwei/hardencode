precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;
uniform mat4 aColorMatrix;
uniform float indensity;

void main()
{
    vec4 textureColor = texture2D(aInputImageTexture, aVaryCoordinate);
    vec4 outputColor = textureColor * aColorMatrix;

    gl_FragColor = outputColor * indensity + textureColor * (1.0 - indensity);
}

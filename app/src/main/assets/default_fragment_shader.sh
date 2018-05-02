precision mediump float;

varying vec2 aVaryCoordinate;
uniform sampler2D aInputImageTexture;

void main()
{
    vec4 textColor = texture2D(aInputImageTexture, aVaryCoordinate);
    gl_FragColor = textColor;
}

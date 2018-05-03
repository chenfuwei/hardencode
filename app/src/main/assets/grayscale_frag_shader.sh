precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

void main()
{
    vec4 textureColor = texture2D(aInputImageTexture, aVaryCoordinate);

    vec3 W = vec3(0.2125, 0.7154, 0.0721);

    float lumance = dot(textureColor.rgb, W);

    gl_FragColor = vec4(vec3(lumance), textureColor.a);
}

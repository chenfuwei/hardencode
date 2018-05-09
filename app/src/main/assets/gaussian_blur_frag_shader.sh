precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

const int GAUSSIAN_SAMPLES = 9;
varying vec2 coords[GAUSSIAN_SAMPLES];

void main()
{
    vec3 sum = vec3(0.0);
    vec4 fragColor=texture2D(aInputImageTexture,aVaryCoordinate);
    sum += texture2D(aInputImageTexture, coords[0]).rgb * 0.05;
    sum += texture2D(aInputImageTexture, coords[1]).rgb * 0.09;
    sum += texture2D(aInputImageTexture, coords[2]).rgb * 0.12;
    sum += texture2D(aInputImageTexture, coords[3]).rgb * 0.15;
    sum += texture2D(aInputImageTexture, coords[4]).rgb * 0.18;
    sum += texture2D(aInputImageTexture, coords[5]).rgb * 0.15;
    sum += texture2D(aInputImageTexture, coords[6]).rgb * 0.12;
    sum += texture2D(aInputImageTexture, coords[7]).rgb * 0.09;
    sum += texture2D(aInputImageTexture, coords[8]).rgb * 0.05;
    gl_FragColor = vec4(sum,fragColor.a);
}

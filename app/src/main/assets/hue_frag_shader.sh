//YIQ ,YIQ，是NTSC（National Television Standards Committee）电视系统标准。
//Y是提供黑白电视及彩色电视的亮度信号（Luminance），即亮度（Brightness），
//I代表In-phase，色彩从橙色到青色，Q代表Quadrature-phase，色彩从紫色到黄绿色。

precision highp float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;
uniform float aHueAdjust;

const highp vec4 kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);
const highp vec4 kRGBToI = vec4 (0.595716, -0.274453, -0.321263, 0.0);
const highp vec4 kRGBToQ = vec4 (0.211456, -0.522591, 0.31135, 0.0);
const highp vec4 kYIQToR = vec4 (1.0, 0.9563, 0.6210, 0.0);
const highp vec4 kYIQToG = vec4 (1.0, -0.2721, -0.6474, 0.0);
const highp vec4 kYIQToB = vec4 (1.0, -1.1070, 1.7046, 0.0);

void main()
{
    vec4 textureColor = texture2D(aInputImageTexture, aVaryCoordinate);

    float YPrime = dot(kRGBToYPrime, textureColor);
    float I = dot(kRGBToI, textureColor);
    float Q = dot(kRGBToQ, textureColor);

    float IQDegree = atan(Q / I);
    float chroma = sqrt(I * I + Q * Q);

    IQDegree += (-aHueAdjust);

    I = chroma * cos(IQDegree);
    Q = chroma * sin(IQDegree);

    vec4 YIQ = vec4(YPrime, I, Q ,0.0);

    float R = dot(kYIQToR, YIQ);
    float G = dot(kYIQToG, YIQ);
    float B = dot(kYIQToB, YIQ);

    gl_FragColor = vec4(R, G, B, textureColor.a);

}
precision mediump float;

uniform sampler2D aInputImageTexture;
uniform sampler2D aInputImageTexture2;
varying vec2 aVaryCoordinate;
varying vec2 aVaryCoordinate2;

void main()
{
    vec4 sourceColor = texture2D(aInputImageTexture, aVaryCoordinate);
    vec4 blendColor = texture2D(aInputImageTexture2, aVaryCoordinate2);

    float r;
    if(sourceColor.r * blendColor.a + blendColor.r * sourceColor.a >= sourceColor.a * blendColor.a)
    {
        r = sourceColor.a * blendColor.a + sourceColor.r * (1.0 - blendColor.a) + blendColor.r * (1.0 - sourceColor.a);
    }else
    {
        r = sourceColor.r + blendColor.r;
    }

    float g;
    if(sourceColor.g * blendColor.a + blendColor.g * sourceColor.a >= sourceColor.a * blendColor.a)
    {
        g = sourceColor.a * blendColor.a + sourceColor.g * ( 1.0 - blendColor.a) + blendColor.g * (1.0 - sourceColor.a);
    }else
    {
        g = sourceColor.g + blendColor.g;
    }

    float b;
    if(sourceColor.b * blendColor.a + blendColor.b * sourceColor.a >= sourceColor.a * blendColor.a)
    {
        b = sourceColor.a * blendColor.a + sourceColor.b * (1.0 - blendColor.a) + blendColor.g * (1.0 - sourceColor.a);
    }else
    {
        b = sourceColor.b + blendColor.b;
    }

    float a = sourceColor.a + blendColor.a - sourceColor.a * blendColor.a;

    gl_FragColor = vec4(r, b, b ,a);
}
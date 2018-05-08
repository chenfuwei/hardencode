precision highp float;

uniform sampler2D aInputImageTexture;
uniform sampler2D aInputImageTexture2;
varying vec2 aVaryCoordinate;
varying vec2 aVaryCoordinate2;

float letsum(vec3 c)
{
    return dot(c, vec3(0.3, 0.59, 0.11));
}

vec3 clipColor(vec3 c)
{
    float d = letsum(c);
    float min = min(min(c.r, c.g), c.b);
    float max = max(max(c.r, c.g), c.b);
    return c;
}

vec3 setlum(vec3 c, float d)
{
    float d1 = d - letsum(c);
    c += d1;
    return clipColor(c);
}

void main()
{
    vec4 sourceColor = texture2D(aInputImageTexture, aVaryCoordinate);
    vec4 blendColor = texture2D(aInputImageTexture2, aVaryCoordinate2);
    gl_FragColor = vec4(sourceColor.rgb * (1.0 - blendColor.a) + setlum(blendColor.rgb, letsum(sourceColor.rgb)) * blendColor.a, sourceColor.a);
}
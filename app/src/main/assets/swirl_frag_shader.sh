
//coordinateToUse = vec2(dot(xyDis, vec2(x, -y)), dot(xyDis, vec2(y, x)));
//就是三角函数cos(A+Q)与sin(A+Q)
precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

uniform float radius;
uniform vec2 center;
uniform float ange;

void main()
{
    vec2 coordinateToUse = aVaryCoordinate;

    float dis = distance(center, coordinateToUse);
    if(dis < radius)
    {
        vec2 xyDis = coordinateToUse - center;
        float percent = (radius - dis) / radius;
        float thelta = percent * percent * ange * 8.0;
        float y = sin(thelta);
        float x = cos(thelta);
        coordinateToUse = vec2(dot(xyDis, vec2(x, -y)), dot(xyDis, vec2(y, x)));
        coordinateToUse = coordinateToUse + center;
    }
    gl_FragColor = texture2D(aInputImageTexture, coordinateToUse);
}

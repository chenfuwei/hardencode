precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

uniform float aspectRatio;
uniform float scale;
uniform float radius;
uniform vec2 center;

void main()
{
    vec2 coordToUse = vec2(aVaryCoordinate.x, aVaryCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio);
    float dis = distance(center, coordToUse);
    coordToUse = aVaryCoordinate;

    if(dis < radius){
        coordToUse -= center;
        float percent = 1.0 - ((radius - dis) / radius) * scale;
        percent = percent * percent;
        coordToUse = coordToUse * percent;
        coordToUse += center;
    }

    gl_FragColor = texture2D(aInputImageTexture, coordToUse);
}
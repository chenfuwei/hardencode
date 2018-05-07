precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

uniform vec2 center;
uniform float radius;
uniform float aspectRatio;
uniform float refractiveIndex;

void main()
{
    vec2 coordinateToUse = vec2(aVaryCoordinate.x, aVaryCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio);
    float distanceCenter = distance(center, coordinateToUse);
    float distanceCenterFactor = step(distanceCenter, radius);

    float distanceCenterX = coordinateToUse.x - center.x;
    float distanceCenterY = coordinateToUse.y - center.y;

    float distanceCenterRate = distanceCenter / radius;
    float depth = radius *(sqrt(1.0 - distanceCenterRate * distanceCenterRate));
    vec3 refractVec3 = refract(vec3(0.0, 0.0, -1.0), normalize(vec3(distanceCenterX, distanceCenterY, depth)), refractiveIndex);

    gl_FragColor = texture2D(aInputImageTexture, (refractVec3.xy + 1.0) * 0.5) * distanceCenterFactor;
}
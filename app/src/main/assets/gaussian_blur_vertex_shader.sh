attribute vec3 aPosition;
attribute vec2 aCoordinate;
varying vec2 aVaryCoordinate;

uniform float texelWidthOffset;
uniform float texelHeightOffset;
const int GAUSSIAN_SAMPLES = 9;
varying vec2 coords[GAUSSIAN_SAMPLES];

void main(){
    gl_Position = vec4(aPosition, 1.0);
    aVaryCoordinate = aCoordinate;

    vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);
    vec2 blur;
    for(int i = 1; i <= GAUSSIAN_SAMPLES; i++)
    {
        float multiply = float(i) - ((float(GAUSSIAN_SAMPLES) - 1.0) / 2.0);
        blur = multiply * singleStepOffset;
        coords[i] = aCoordinate + blur;
    }
}
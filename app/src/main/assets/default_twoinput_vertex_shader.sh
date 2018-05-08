attribute vec3 aPosition;
attribute vec2 aCoordinate;
attribute vec2 aCoordinate2;
varying vec2 aVaryCoordinate;
varying vec2 aVaryCoordinate2;

void main(){
    gl_Position = vec4(aPosition, 1.0);
    aVaryCoordinate = aCoordinate;
    aVaryCoordinate2 = aCoordinate2;
}
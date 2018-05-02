attribute vec3 aPosition;
attribute vec2 aCoordinate;
varying vec2 aVaryCoordinate;

void main(){
    gl_Position = vec4(aPosition, 1.0);
    aVaryCoordinate = aCoordinate;
}
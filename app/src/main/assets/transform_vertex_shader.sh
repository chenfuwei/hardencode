attribute vec3 aPosition;
attribute vec2 aCoordinate;
varying vec2 aVaryCoordinate;

uniform mat4 transformMatrix;
uniform mat4 orthoMatrix;

void main(){
    gl_Position = orthoMatrix * transformMatrix * vec4(aPosition, 1.0);
    aVaryCoordinate = aCoordinate;
}
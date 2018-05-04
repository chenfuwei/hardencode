precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

uniform vec2 vignetteCenter;
uniform vec3 vignetteColor;
uniform float vignetteStart;
uniform float vignetteEnd;

void main(){
    vec4 color = texture2D(aInputImageTexture, aVaryCoordinate);
    float coordDistance = 1.0 - distance(aVaryCoordinate, vignetteCenter);
    float percent = smoothstep(vignetteStart, vignetteEnd, coordDistance);

    vec4 finalColor = color;
    finalColor.r = mix(color.r, vignetteColor.r, percent);
    finalColor.g = mix(color.g, vignetteColor.g, percent);
    finalColor.b = mix(color.b, vignetteColor.b, percent);

    gl_FragColor = finalColor;
}
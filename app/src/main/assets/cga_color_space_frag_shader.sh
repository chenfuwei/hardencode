precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;

void main(){
    vec4 color = texture2D(aInputImageTexture, aVaryCoordinate);
    vec4 colorCyan = vec4(85.0 / 255.0, 1.0, 1.0, 1.0);
    vec4 colorMagenta = vec4(1.0, 85.0 / 255.0, 1.0, 1.0);
    vec4 colorWhite = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 colorBlack = vec4(0.0, 0.0, 0.0, 1.0);

    float cyanDistance = distance(colorCyan, color);
    float magentaDistance = distance(colorMagenta, color);
    float whiteDistance = distance(colorWhite, color);
    float blackDistance = distance(colorBlack, color);

    float minDistance = min(cyanDistance, magentaDistance);
    minDistance = min(minDistance, whiteDistance);
    minDistance = min(minDistance, blackDistance);

    vec4 finalColor = color;
    if(minDistance == cyanDistance)
    {
        finalColor = colorCyan;
    }else if(minDistance == magentaDistance)
    {
        finalColor = colorMagenta;
    }else if(minDistance == whiteDistance)
    {
        finalColor = colorWhite;
    }else
    {
        finalColor = colorBlack;
    }

    gl_FragColor = finalColor;
}
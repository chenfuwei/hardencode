precision mediump float;

uniform sampler2D aInputImageTexture;
varying vec2 aVaryCoordinate;
uniform float temperature;
uniform float tiny;

void main()
{
    vec4 source = texture2D(aInputImageTexture, aVaryCoordinate);
    const vec3 warmFilter = vec3(0.93, 0.54, 0.0);
    const mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311);
    const mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702);

    vec3 YIQ = RGBtoYIQ * source.rgb;
    YIQ.b = clamp(YIQ.b + tiny*0.5226*0.1, -0.5226, 0.5226);

    vec3 rgb = YIQtoRGB * YIQ;

    vec3 processed = vec3(0.0);
    if(rgb.r < 0.5){
        processed.r =  2.0 * rgb.r * warmFilter.r;
    }else{
        processed.r = 1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r);
    }

    if(rgb.g < 0.5){
        processed .g = 2.0 * rgb.g * warmFilter.g;
    }else{
        processed .g = 1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g);
    }

    if(rgb.b < 0.5){
        processed.b = 2.0 * rgb.b * warmFilter.b;
    }else
   {
        processed.b = 1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b);
   }
    gl_FragColor = vec4(mix(rgb, processed, temperature), source.a);
}
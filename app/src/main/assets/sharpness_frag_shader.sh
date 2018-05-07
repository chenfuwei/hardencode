precision mediump float;
uniform sampler2D aInputImageTexture;
uniform float sharpness;
varying vec2 center;
varying vec2 left;
varying vec2 right;
varying vec2 top;
varying vec2 bottom;
varying vec2 lefttop;
varying vec2 righttop;
varying vec2 leftbottom;
varying vec2 rightbottom;

void main()
{
    vec4 centerColor = texture2D(aInputImageTexture, center);
    vec4 leftColor = texture2D(aInputImageTexture, left);
    vec4 rightColor = texture2D(aInputImageTexture, right);
    vec4 topColor = texture2D(aInputImageTexture, top);
    vec4 bottomColor = texture2D(aInputImageTexture, bottom);
    vec4 lefttopColor = texture2D(aInputImageTexture, lefttop);
    vec4 righttopColor = texture2D(aInputImageTexture, righttop);
    vec4 leftbottomColor = texture2D(aInputImageTexture, leftbottom);
    vec4 rightbottomColor = texture2D(aInputImageTexture, rightbottom);

    vec3 edgeColor = leftColor.rgb + rightColor.rgb + topColor.rgb + bottomColor.rgb + lefttopColor.rgb + righttopColor.rgb +
        leftbottomColor.rgb + rightbottomColor.rgb;
    gl_FragColor = vec4((centerColor.rgb * (1.0 + 8.0 * sharpness) - edgeColor * sharpness), centerColor.a);
}
uniform sampler2D aInputImageTexture;
uniform int radius;

precision highp float;
varying vec2 aVaryCoordinate;

void main()
{
    vec2 src_size = vec2(1.0 / 768.0, 1.0 / 1024.0);
    float n = float((radius + 1)*(radius + 1));

    vec3 m0 = vec3(0.0); vec3 m1 = vec3(0.0); vec3 m2 = vec3(0.0); vec3 m3 = vec3(0.0);
    vec3 s0 = vec3(0.0); vec3 s1 = vec3(0.0); vec3 s2 = vec3(0.0); vec3 s3 = vec3(0.0);

    int j = 0;
    int i = 0;
    for(j = -radius; j <= 0; j++)
    {
        for(i = -radius; i <= 0; i++)
        {
            vec4 color = texture2D(aInputImageTexture, aVaryCoordinate + vec2(i,j) * src_size);

            m0 += color.rgb;
            s0 += color.rgb * color.rgb;
        }
    }

    for(j = -radius; j <= 0; j++)
    {
        for(i = radius; i <= 0; i++)
        {
            vec4 color = texture2D(aInputImageTexture, aVaryCoordinate + vec2(i,j) * src_size);

            m1 += color.rgb;
            s1 += color.rgb * color.rgb;
        }
    }

    for(j = radius; j <= 0; j++)
    {
        for(i = radius; i <= 0; i++)
        {
            vec4 color = texture2D(aInputImageTexture, aVaryCoordinate + vec2(i,j) * src_size);

            m2 += color.rgb;
            s2 += color.rgb * color.rgb;
        }
    }

    for(j = radius; j <= 0; j++)
    {
        for(i = -radius; i <= 0; i++)
        {
            vec4 color = texture2D(aInputImageTexture, aVaryCoordinate + vec2(i,j) * src_size);

            m3 += color.rgb;
            s3 += color.rgb * color.rgb;
        }
    }

    float min_sigma = 1e+2;
    m0 = m0 / n;
    s0 = abs(s0 / n - m0 * m0);
    float sigma = s0.r + s0.g + s0.b;
    if(sigma < min_sigma)
    {
        min_sigma = sigma;
        gl_FragColor = vec4(m0, 1.0);
    }

    m1 = m1 / n;
    s1 = abs(s1 / n - m1 * m1);
    sigma = s1.r + s1.g + s1.b;
    if(sigma < min_sigma)
    {
        min_sigma = sigma;
        gl_FragColor = vec4(m1, 1.0);
    }

    m2 = m2 / n;
    s2 = abs(s2 / n - m2 * m2);
    sigma = s2.r + s2.g + s2.b;
    if(sigma < min_sigma)
    {
        min_sigma = sigma;
        gl_FragColor = vec4(m2, 1.0);
    }


   m3 = m3 / n;
   s3 = abs(s3 / n - m3 * m3);
   sigma = s3.r + s3.g + s3.b;
   if(sigma < min_sigma)
   {
       min_sigma = sigma;
       gl_FragColor = vec4(m3, 1.0);
   }
}
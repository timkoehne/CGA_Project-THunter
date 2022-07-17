#version 330 core

uniform float shininess, cosInnen, cosAussen;
uniform vec3 fragColor, lightColorSpot, spotlightDir;
uniform int anzLichter;

uniform sampler2D emit, diff, specular;

in struct VertexData
{
    vec3 position;
    vec2 textureCords;
    vec3 normal;
} vertexData;

in struct Light
{
    vec3 toCamera;
    vec3 toLight;
    vec3 lightColor;
} lights[5];
in vec3 toSpotlight;

out vec4 color;

vec3 gamma(vec3 c_linear){
    return vec3(pow(c_linear.x, 1/2.2), pow(c_linear.y, 1/2.2), pow(c_linear.z, 1/2.2));
}

vec3 invgamma(vec3 c_gamma){
    return vec3(pow(c_gamma.x, 2.2), pow(c_gamma.y, 2.2), pow(c_gamma.z, 2.2));
}

float attenuation(vec3 toLight){
    float distance = length(toLight);
    return (1.0f / pow(distance, 2));
}

vec3 emmisivBerechnen(){
    return gamma(texture(emit, vertexData.textureCords).rgb);
}

vec3 ambientBerechnen(){
    return gamma(texture(diff, vertexData.textureCords).rgb) * 0.1;
}

vec3 diffuseBerechnen(vec3 normal, vec3 toLight, vec3 color){
    vec3 matDiffuse = gamma(texture(diff, vertexData.textureCords).rgb);

    float cosa = max(0.0, dot(normal, toLight));
    vec3 diffuseTerm = matDiffuse * color;
    return diffuseTerm*cosa;
}

vec3 specularBerechnen(vec3 normal, vec3 toLight, vec3 color){
    vec3 matSpecular = gamma(texture(specular, vertexData.textureCords).rgb);
    vec3 toCamera = normalize(lights[0].toCamera);

    //Blinn-Phong-Modell
    vec3 halfway = normalize(toLight + toCamera);
    float spec = pow(max(0.0, dot(normal, halfway)), shininess);
    vec3 specular = matSpecular * spec * color;
    return specular;

    //Phong-Modell
//    vec3 reflection = normalize(reflect(-toLight, normal));
//    float cosBeta = max(0.0, dot(reflection, toCamera));
//    float cosBetak = pow(cosBeta, shininess);
//    vec3 specularTerm = matSpecular * color;
//    return specularTerm * cosBetak;
}

float angleIntensity(){
    float theta = dot(normalize(toSpotlight), normalize(-spotlightDir));
    if (theta > cosInnen){
        return 1;
    } else if (theta > cosAussen){
        return clamp((theta - cosAussen) / (cosInnen - cosAussen), 0, 1);
    }
    return 0;
}

vec3 angleIntensityColorTest(){
    float theta = dot(normalize(toSpotlight), normalize(-spotlightDir));
    if (theta > cosInnen){
        return vec3(1, 0, 0);
    } else if (theta > cosAussen){
        return vec3(0, 1, 0);
    }
    return vec3(0, 0, 1);
}

vec3 diffuseAndSpecularBerechnen(vec3 toLight, vec3 color){
    return (diffuseBerechnen(normalize(vertexData.normal), normalize(toLight), color) +
    specularBerechnen(normalize(vertexData.normal), normalize(toLight), color)) * attenuation(toLight);//
}

void main(){

    color = vec4(emmisivBerechnen() * fragColor, 1.0);
    color += vec4(ambientBerechnen(), 0.4);

    for (int i = 0; i < anzLichter; i++){
        color += vec4(diffuseAndSpecularBerechnen(lights[i].toLight, lights[i].lightColor), 0.0);
    }

    color += vec4(diffuseAndSpecularBerechnen(toSpotlight, lightColorSpot) * angleIntensity(), 0.0);

//    if(vertexData.position.y > 0.4){
//        color = vec4(0, 1, 0, 1);
//    }else{
//        color = vec4(1, 0, 0, 1);
//    }


}


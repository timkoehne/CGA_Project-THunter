#version 330 core

uniform float shininess, cosInnen, cosAussen;
//uniform vec3 fragColor, lightColorSpot, spotlightDir;
//uniform int anzLichter;

uniform float ambientTag;
uniform float ambientNacht;

uniform float ingameTime;
uniform float sonnenaufgangUhrzeit;
uniform float sonnenuntergangUhrzeit;
uniform float fadeDauerIngameStunden;

uniform sampler2D emit, diff, spec, depthMap;

uniform vec3 sunPos;
uniform vec3 viewPos;

in struct VertexData
{
    vec3 position;
    vec2 textureCords;
    vec3 normal;
    vec4 fragPosLightSpace;
} vertexData;

//in struct Light
//{
//    vec3 toCamera;
//    vec3 toLight;
//    vec3 lightColor;
//} lights[5];
//in vec3 toSpotlight;

out vec4 FragColor;

float mixFactor(float startzeit){
    return (ingameTime - startzeit)/fadeDauerIngameStunden;
}

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
    return texture(emit, vertexData.textureCords).rgb;
}

vec3 ambientBerechnen(){
    float ambient;
    if (ingameTime >= sonnenaufgangUhrzeit  && ingameTime < (sonnenaufgangUhrzeit + fadeDauerIngameStunden)){ // Morning
        ambient = mix(ambientNacht, ambientTag, mixFactor(sonnenaufgangUhrzeit));

    } else if (ingameTime >= sonnenaufgangUhrzeit + fadeDauerIngameStunden && ingameTime <  sonnenuntergangUhrzeit){ // Day
        ambient = ambientTag;

    } else if (ingameTime >= sonnenuntergangUhrzeit  && ingameTime < (sonnenuntergangUhrzeit + fadeDauerIngameStunden)){ // Evening
        ambient = mix(ambientTag, ambientNacht, mixFactor(sonnenuntergangUhrzeit));

    } else { // Night
        ambient = ambientNacht;
    }
    return ambient * texture(diff, vertexData.textureCords).rgb;
}

vec3 diffuseBerechnen(vec3 normal, vec3 lightPos){
    vec3 matDiffuse = texture(diff, vertexData.textureCords).rgb;
    vec3 lightDir = normalize(lightPos - vertexData.position);

    float cosa = max(dot(lightDir, normal), 0.0);
    return matDiffuse * cosa;
}

vec3 specularBerechnen(vec3 normal, vec3 lightPos){
    vec3 matSpecular = texture(spec, vertexData.textureCords).rgb;

    vec3 lightDir = normalize(lightPos - vertexData.position);
    vec3 viewDir = normalize(viewPos - vertexData.position);
    float cosb = 0.0;
    vec3 halfwayDir = normalize(lightDir + viewDir);
    cosb = pow(max(dot(normal, halfwayDir), 0.0), shininess);
    vec3 specular = matSpecular * cosb;
    return specular;
}

//float angleIntensity(){
//    float theta = dot(normalize(toSpotlight), normalize(-spotlightDir));
//    if (theta > cosInnen){
//        return 1;
//    } else if (theta > cosAussen){
//        return clamp((theta - cosAussen) / (cosInnen - cosAussen), 0, 1);
//    }
//    return 0;
//}
//
//vec3 angleIntensityColorTest(){
//    float theta = dot(normalize(toSpotlight), normalize(-spotlightDir));
//    if (theta > cosInnen){
//        return vec3(1, 0, 0);
//    } else if (theta > cosAussen){
//        return vec3(0, 1, 0);
//    }
//    return vec3(0, 0, 1);
//}

float shadowCalculation(vec4 fragPosLightSpace){
    vec3 projCoords = (fragPosLightSpace.xyz * 0.5) + 0.5;
    float lightSpaceDepth = texture(depthMap, projCoords.xy).r;
    float cameraDepth = projCoords.z;
    float shadow = cameraDepth > lightSpaceDepth  ? 1.0 : 0.0;
    return shadow;
}


void main(){
    vec3 normal = normalize(vertexData.normal);

    vec3 lightColor = vec3(1);


    float shadow = shadowCalculation(vertexData.fragPosLightSpace);

//    FragColor = vec4(emmisivBerechnen() * lightColor, 1.0);
    FragColor = vec4(ambientBerechnen(), 1.0);

    FragColor += (1-shadow) * (vec4(diffuseBerechnen(normal, sunPos), 0.0));
    FragColor += (1-shadow) * (vec4(specularBerechnen(normal, sunPos), 0.0));

    FragColor = vec4(FragColor.rgb, 1);


}
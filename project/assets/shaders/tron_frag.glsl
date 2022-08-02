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

uniform int celShadingLevels;

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

vec3 diffuseBerechnen(vec3 normal, vec3 lightDir){
    vec3 matDiffuse = texture(diff, vertexData.textureCords).rgb;

    float cosa = max(dot(lightDir, normal), 0.0);

    if (celShadingLevels != 0){
        cosa = floor(cosa * celShadingLevels)/celShadingLevels;
    }

    return matDiffuse * cosa;
}

vec3 specularBerechnen(vec3 normal, vec3 lightDir){
    vec3 matSpecular = texture(spec, vertexData.textureCords).rgb;

    vec3 viewDir = normalize(viewPos - vertexData.position);
    float cosb = 0.0;
    vec3 halfwayDir = normalize(lightDir + viewDir);
    cosb = pow(max(dot(normal, halfwayDir), 0.0), shininess);

    if (celShadingLevels != 0){
        cosb = floor(cosb * celShadingLevels)/celShadingLevels;
    }
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

float shadowCalculation(vec3 normal, vec3 lightDir, vec4 fragPosLightSpace){
    vec2 texelSize = 1.0 / textureSize(depthMap, 0);
    float bias = max(0.008 * (1.0 - dot(normal, lightDir)), 0.005);

    vec3 projCoords = (fragPosLightSpace.xyz * 0.5) + 0.5;
    float lightSpaceDepth = texture(depthMap, projCoords.xy).r;
    float cameraDepth = projCoords.z;

    float shadow = 0.0;
    if (cameraDepth > 1.0){
        return shadow;
    }

    //percentage closer filtering
    int filterSize = 5;
    for (int x = -(filterSize / 2); x <= (filterSize / 2); x++) {
        for (int y = -(filterSize / 2); y <= (filterSize / 2); y++){
            float pcfShadow = texture(depthMap, projCoords.xy + vec2(x, y) * texelSize).r;
            shadow += (cameraDepth - bias) > pcfShadow ? 1.0 : 0.0;
        }
    }

    return shadow / pow(filterSize, 2);
}




void main(){
    vec3 normal = normalize(vertexData.normal);
    vec3 lightDir = normalize(sunPos - vertexData.position);

    vec3 lightColor = vec3(1);

    float shadow = shadowCalculation(normal, lightDir, vertexData.fragPosLightSpace);

    //    FragColor = vec4(emmisivBerechnen() * lightColor, 1.0);
    FragColor = vec4(ambientBerechnen(), 1.0);



    FragColor += (1-shadow) * (vec4(diffuseBerechnen(normal, lightDir), 0.0));
    FragColor += (1-shadow) * (vec4(specularBerechnen(normal, lightDir), 0.0));

        FragColor = vec4(FragColor.rgb, 1);
//    FragColor = vec4(1, 0, 0, 0);


}
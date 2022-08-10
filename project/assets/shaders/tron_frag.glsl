#version 330 core

uniform float shininess;
uniform vec3 emmisivColor, lightColorSpot;
uniform int anzLichter;

uniform float ambient;
uniform float maxSunIntensity;

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

uniform struct Light
{
    vec3 lightPos;
    vec3 lightColor;
} lights[5];

uniform struct Spotlight
{
    vec3 lightPos;
    vec3 lightColor;
    vec3 lightDir;
    float cosInnen;
    float cosAussen;
} spotlight;

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
    return ambient * texture(diff, vertexData.textureCords).rgb;
}

float sunIntensity(){
    float intensity;
    float minIntensity = 0.1f;

    if (ingameTime >= sonnenaufgangUhrzeit  && ingameTime < (sonnenaufgangUhrzeit + fadeDauerIngameStunden)){ // Morning
        intensity = mix(minIntensity, maxSunIntensity, mixFactor(sonnenaufgangUhrzeit));

    } else if (ingameTime >= sonnenaufgangUhrzeit + fadeDauerIngameStunden && ingameTime <  sonnenuntergangUhrzeit){ // Day
        intensity = maxSunIntensity;

    } else if (ingameTime >= sonnenuntergangUhrzeit  && ingameTime < (sonnenuntergangUhrzeit + fadeDauerIngameStunden)){ // Evening
        intensity = mix(maxSunIntensity, minIntensity, mixFactor(sonnenuntergangUhrzeit));

    } else { // Night
        intensity = minIntensity;
    }
    return intensity;
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

float angleIntensity(){
    vec3 lightDir = normalize(spotlight.lightPos - vertexData.position);
    float theta = dot(lightDir, normalize(-spotlight.lightDir));
    if (theta > spotlight.cosInnen){
        return 1;
    } else if (theta > spotlight.cosAussen){
        return clamp((theta - spotlight.cosAussen) / (spotlight.cosInnen - spotlight.cosAussen), 0, 1);
    }
    return 0;
}

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
            float pcf = texture(depthMap, projCoords.xy + vec2(x, y) * texelSize).r;
            shadow += (cameraDepth - bias) > pcf ? 1.0 : 0.0;
        }
    }

    return shadow / pow(filterSize, 2);
}




void main(){

    vec3 normal = normalize(vertexData.normal);
    FragColor = vec4(0, 0, 0, 1);

    //Sun
    vec3 lightDir = normalize(sunPos - vertexData.position);
    float shadow = shadowCalculation(normal, lightDir, vertexData.fragPosLightSpace);
//    FragColor += vec4(emmisivBerechnen(), 0.0);
    FragColor += vec4(ambientBerechnen(), 0.0);
    FragColor += (1-shadow) * (vec4(diffuseBerechnen(normal, lightDir), 0.0)) * sunIntensity();
    FragColor += (1-shadow) * (vec4(specularBerechnen(normal, lightDir), 0.0)) * sunIntensity();


    //spotlight
    lightDir = spotlight.lightPos - vertexData.position;
    FragColor += (vec4(diffuseBerechnen(normal, normalize(lightDir)) * attenuation(lightDir) * angleIntensity(), 0.0));
    FragColor += (vec4(specularBerechnen(normal, normalize(lightDir)) * attenuation(lightDir) * angleIntensity(), 0.0));

    //einfache lightquellen
    for (int i = 0; i < anzLichter; i++){
        lightDir = lights[i].lightPos - vertexData.position;
        FragColor += (vec4(diffuseBerechnen(normal, normalize(lightDir)) * lights[i].lightColor * attenuation(lightDir), 0.0));
        FragColor += (vec4(specularBerechnen(normal, normalize(lightDir)) * lights[i].lightColor * attenuation(lightDir), 0.0));
    }


}
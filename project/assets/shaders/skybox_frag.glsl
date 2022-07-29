#version 400

in vec3 textureCoords;
out vec4 out_Color;

uniform float ambientTag;
uniform float ambientNacht;

uniform float ingameTime;
uniform samplerCube cubeMapDay;
uniform samplerCube cubeMapNight;

uniform float sonnenaufgangUhrzeit;
uniform float sonnenuntergangUhrzeit;
uniform float fadeDauerIngameStunden;


vec4 dayTexture(){
    return texture(cubeMapDay, textureCoords);
}
vec4 nightTexture(){
    return texture(cubeMapNight, textureCoords);
}

float mixFactor(float startzeit){
    return (ingameTime - startzeit)/fadeDauerIngameStunden;
}

void main(void){

    float ambient;

    if (ingameTime >= sonnenaufgangUhrzeit  && ingameTime < (sonnenaufgangUhrzeit + fadeDauerIngameStunden)){ // Morning
        out_Color = mix(nightTexture(), dayTexture(), mixFactor(sonnenaufgangUhrzeit));
        ambient = mix(ambientNacht, ambientTag, mixFactor(sonnenaufgangUhrzeit));

    } else if (ingameTime >= sonnenaufgangUhrzeit + fadeDauerIngameStunden && ingameTime <  sonnenuntergangUhrzeit){ // Day
        out_Color = dayTexture();
        ambient = ambientTag;

    } else if (ingameTime >= sonnenuntergangUhrzeit  && ingameTime < (sonnenuntergangUhrzeit + fadeDauerIngameStunden)){ // Evening
        out_Color = mix(dayTexture(), nightTexture(), mixFactor(sonnenuntergangUhrzeit));
        ambient = mix(ambientTag, ambientNacht, mixFactor(sonnenuntergangUhrzeit));

    } else { // Night
        out_Color = nightTexture();
        ambient = ambientNacht;
    }

    out_Color *= ambient;

    if(out_Color.a < 0.5){
        discard;
    }
}
#version 400

in vec3 textureCoords;
out vec4 out_Color;

uniform float ingameTime;
uniform samplerCube cubeMapDay;
uniform samplerCube cubeMapNight;

uniform float sonnenaufgangUhrzeit;
uniform float sonnenuntergangUhrzeit;
uniform float fadeDauerIngameStunden;

void main(void){

    if (ingameTime >= sonnenaufgangUhrzeit  && ingameTime < (sonnenaufgangUhrzeit + fadeDauerIngameStunden)){ // Morning
        out_Color = mix(texture(cubeMapNight, textureCoords), texture(cubeMapDay, textureCoords), (ingameTime - sonnenaufgangUhrzeit)/fadeDauerIngameStunden);

    } else if (ingameTime >= sonnenaufgangUhrzeit + fadeDauerIngameStunden && ingameTime <  sonnenuntergangUhrzeit){ // Day
        out_Color = texture(cubeMapDay, textureCoords);

    } else if (ingameTime >= sonnenuntergangUhrzeit  && ingameTime < (sonnenuntergangUhrzeit + fadeDauerIngameStunden)){ // Evening
        out_Color = mix(texture(cubeMapDay, textureCoords), texture(cubeMapNight, textureCoords), (ingameTime - sonnenuntergangUhrzeit)/fadeDauerIngameStunden);

    } else { // Night
        out_Color = texture(cubeMapNight, textureCoords);
    }

}
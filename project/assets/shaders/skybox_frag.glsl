#version 400

in vec3 textureCoords;
out vec4 out_Color;

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

    if (ingameTime >= sonnenaufgangUhrzeit  && ingameTime < (sonnenaufgangUhrzeit + fadeDauerIngameStunden)){ // Morning
        out_Color = mix(nightTexture(), dayTexture(), mixFactor(sonnenaufgangUhrzeit));

    } else if (ingameTime >= sonnenaufgangUhrzeit + fadeDauerIngameStunden && ingameTime <  sonnenuntergangUhrzeit){ // Day
        out_Color = dayTexture();

    } else if (ingameTime >= sonnenuntergangUhrzeit  && ingameTime < (sonnenuntergangUhrzeit + fadeDauerIngameStunden)){ // Evening
        out_Color = mix(dayTexture(), nightTexture(), mixFactor(sonnenuntergangUhrzeit));

    } else { // Night
        out_Color = nightTexture();
    }

    if(out_Color.a < 0.1){
        discard;
    }

//    if (out_Color.xyz == vec3(1, 1, 1))
//    out_Color = vec4(0, 1, 0, 0);


}
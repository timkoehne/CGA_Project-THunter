#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCords;
layout(location = 2) in vec3 normal;

uniform mat4 model_matrix;
uniform mat4 view;
uniform mat4 projection;
uniform vec2 tcMultiplier;
uniform int anzLichter;

uniform struct LightInput
{
    vec3 lightPos;
    vec3 lightColor;
} lightInput[5];

out struct VertexData
{
    vec3 position;
    vec2 textureCords;
    vec3 normal;
} vertexData;

out struct Light
{
    vec3 toCamera;
    vec3 toLight;
    vec3 lightColor;
} lights[5];

uniform vec3 spotlightPos;
out vec3 toSpotlight;

void main(){

    vec4 pos = projection * view * model_matrix * vec4(position, 1.0f);
    gl_Position = pos;

    vertexData.position = pos.xyz;
    vertexData.textureCords = vec2(textureCords.x * tcMultiplier.x, textureCords.y * tcMultiplier.y);
    vertexData.normal = transpose(inverse(mat3(view * model_matrix))) * normal;

    //diffuse
    vec4 spotlightPosition = view * vec4(spotlightPos, 1.0);

    vec4 viewPos = (view * model_matrix * vec4(position, 1.0));
    toSpotlight = (spotlightPosition-viewPos).xyz;

    vec4 lightPosition;
    for(int i = 0; i < anzLichter; i++){
        lightPosition = view * vec4(lightInput[i].lightPos, 1.0);
        lights[i].toLight = (lightPosition-viewPos).xyz;
        lights[i].toCamera = -viewPos.xyz;
        lights[i].lightColor = lightInput[i].lightColor;
    }


}

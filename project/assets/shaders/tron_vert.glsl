#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCords;
layout(location = 2) in vec3 normal;

uniform mat4 model_matrix;
uniform mat4 view;
uniform mat4 projection;
uniform vec2 tcMultiplier;
uniform int anzLichter;

uniform mat4 sunSpaceMatrix;

struct PointLight
{
    vec3 lightPos;
    vec3 lightColor;
};
uniform PointLight lightInput[10];

out struct VertexData
{
    vec3 position;
    vec2 textureCords;
    vec3 normal;
    vec4 fragPosLightSpace;
} vertexData;

struct Light
{
    vec3 toCamera;
    vec3 toLight;
    vec3 lightColor;
};
out Light lights[10];

uniform vec3 spotlightPos;
out vec3 toSpotlight;


void main(){

    vertexData.position = vec3(model_matrix * vec4(position, 1.0f));
    vertexData.normal = transpose(inverse(mat3(model_matrix))) * normal;//hier war die view matrix mit multipliziert
    vertexData.textureCords = vec2(textureCords.x * tcMultiplier.x, textureCords.y * tcMultiplier.y);
    vertexData.fragPosLightSpace = sunSpaceMatrix * vec4(vertexData.position, 1.0);
    gl_Position = projection * view * vec4(vertexData.position, 1.0);


    //lighting
    vec4 spotlightPosition = view * vec4(spotlightPos, 1.0);

    vec4 viewPos = (view * model_matrix * vec4(position, 1.0));
    toSpotlight = (spotlightPosition-viewPos).xyz;

    vec4 lightPosition;

    for (int i = 0; i < 4; i++){


        if (i == 0){
            lightPosition = view * vec4(lightInput[0].lightPos, 1.0);
            lights[0].toLight = (lightPosition-viewPos).xyz;
            lights[0].toCamera = -viewPos.xyz;
            lights[0].lightColor = lightInput[0].lightColor;
        } else if (i == 1){
            lightPosition = view * vec4(lightInput[1].lightPos, 1.0);
            lights[1].toLight = (lightPosition-viewPos).xyz;
            lights[1].toCamera = -viewPos.xyz;
            lights[1].lightColor = lightInput[1].lightColor;
        } else if (i == 2){
            lightPosition = view * vec4(lightInput[2].lightPos, 1.0);
            lights[2].toLight = (lightPosition-viewPos).xyz;
            lights[2].toCamera = -viewPos.xyz;
            lights[2].lightColor = lightInput[2].lightColor;
        }
    }


}

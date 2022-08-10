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

out struct VertexData
{
    vec3 position;
    vec2 textureCords;
    vec3 normal;
    vec4 fragPosLightSpace;
} vertexData;


void main(){

    vertexData.position = vec3(model_matrix * vec4(position, 1.0f));
    vertexData.normal = transpose(inverse(mat3(model_matrix))) * normal;
    vertexData.textureCords = vec2(textureCords.x * tcMultiplier.x, textureCords.y * tcMultiplier.y);
    vertexData.fragPosLightSpace = sunSpaceMatrix * vec4(vertexData.position, 1.0);
    gl_Position = projection * view * vec4(vertexData.position, 1.0);
}

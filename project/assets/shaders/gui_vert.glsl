#version 330 core
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCords;

out vec2 out_TextureCords;

uniform mat4 model_matrix;

void main(void){
    gl_Position = model_matrix * vec4(position, 1.0);
    out_TextureCords = textureCords;
}
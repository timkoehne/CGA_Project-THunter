#version 330 core
//layout (location = 0) in vec3 aPos;
//layout (location = 1) in vec2 aTexCords;
layout (location = 0) in mat4 model_matrix;

//out vec2 texCords;

uniform mat4 projection;
uniform mat4 view;

void main(){

    gl_Position = projection * view * model_matrix * vec4(1.0);
//    texCords = aTexCords;
}
#version 400

in vec3 position;
out vec3 textureCoords;

uniform mat4 model_matrix;
uniform mat4 view;
uniform mat4 projection;

void main(void){

    //ignore transformation from view matrix to always center skybox on camera
    gl_Position = projection * mat4(mat3(view)) * model_matrix * vec4(position, 1.0);
    textureCoords = position;

}
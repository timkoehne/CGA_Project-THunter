#version 330 core
in vec2 out_TextureCords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main(void){
    out_Color = texture(guiTexture, out_TextureCords);
}
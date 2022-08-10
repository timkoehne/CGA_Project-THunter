#version 330 core
in vec2 out_TextureCords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main(void){

    vec4 tex = texture(guiTexture, out_TextureCords);
    if (tex.a < 0.5){
        discard;
    }
    out_Color = tex;

}
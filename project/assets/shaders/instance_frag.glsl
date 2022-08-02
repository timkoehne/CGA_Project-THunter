#version 330 core
out vec4 fragColor;

//in vec2 texCords;

uniform sampler2D image;

void main(){
//        fragColor = texture(image, texCords);
        fragColor = vec4(1f, 0f, 0f, 1f);
}


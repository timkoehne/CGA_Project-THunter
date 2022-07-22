#version 330 core

out vec4 FragColor;

in vec3 TexCoords;

//uniform samplerCube skybox;
//uniform samplerCube skybox;
uniform sampler2D skybox;

void main()
{

//    if (TexCoords.x > 0){
//        FragColor = vec4(1,0,0,0);
//    }else{
//        FragColor = vec4(0,1,0,0);
//    }


   FragColor = texture(skybox, TexCoords.xy);
    FragColor += vec4(0.2);

}
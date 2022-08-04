package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20

class Material(
    var diff: Texture2D,
    var emit: Texture2D,
    var specular: Texture2D,
    var shininess: Float = 50.0f,
    var tcMultiplier: Vector2f = Vector2f(1.0f)
) {


    fun bind(shaderProgram: ShaderProgram) {

        emit.bind(GL13.GL_TEXTURE0)
        shaderProgram.setUniform("emit", 0)

        specular.bind(GL13.GL_TEXTURE1)
        shaderProgram.setUniform("spec", 1)

        diff.bind(GL13.GL_TEXTURE2)
        shaderProgram.setUniform("diff", 2)


        shaderProgram.setUniform("shininess", shininess)
        shaderProgram.setUniform("tcMultiplier", tcMultiplier)
    }
}
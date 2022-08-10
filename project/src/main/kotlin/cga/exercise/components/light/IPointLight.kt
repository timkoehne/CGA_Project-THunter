package cga.exercise.components.light

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.shader.ShaderProgram

interface IPointLight {
    fun bind(shaderProgram: ShaderProgram)
}
package cga.exercise.components.shadows

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glTexImage2D
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30

class ShadowRenderer(val scene: Scene) {

    val depthMapFBO: Int //TODO ggf Unsigned
    val depthMap: Int
    var sunPos = Vector3f(0f, 20f, 0f)
    val simpleDepthShader =
        ShaderProgram("project/assets/shaders/simpleDepth_vert.glsl", "project/assets/shaders/simpleDepth_frag.glsl")
    var sunSpaceMatrix = Matrix4f()

    val near_plane = scene.camera.nPlane
    val far_plane = scene.camera.fPlane

    companion object {
        const val SHADOW_WIDTH = 1024
        const val SHADOW_HEIGHT = 1024
    }

    init {
        depthMapFBO = GL30.glGenFramebuffers()
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, depthMapFBO)

        depthMap = GL11.glGenTextures()
        println(depthMap)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthMap)
        glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_DEPTH_COMPONENT,
            SHADOW_WIDTH,
            SHADOW_HEIGHT,
            0,
            GL11.GL_DEPTH_COMPONENT,
            GL11.GL_FLOAT,
            0
        )
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL13.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_EDGE)

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_TEXTURE_2D, depthMap, 0)
        GL30.glDrawBuffer(GL30.GL_NONE)
        GL30.glReadBuffer(GL30.GL_NONE)
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)
    }

    fun render(dt: Float, time: Float) {
        //first render pass
        val depthProjection = Matrix4f().ortho(-10f, 10f, -10f, 10f, near_plane, far_plane)
        val depthView = Matrix4f().lookAt(sunPos, Vector3f(1f, 10f, 1f), Vector3f(0f, 1f, 0f))
        sunSpaceMatrix = depthProjection.mul(depthView)

        simpleDepthShader.use()
        simpleDepthShader.setUniform("lightSpaceMatrix", sunSpaceMatrix, false)

        GL30.glViewport(0, 0, ShadowRenderer.SHADOW_WIDTH, ShadowRenderer.SHADOW_HEIGHT)
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, depthMapFBO)

        GL30.glClear(GL30.GL_DEPTH_BUFFER_BIT)
        GL30.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)

//        scene.myMap.render(simpleDepthShader)
        scene.renderEntities(dt, time, simpleDepthShader)


        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)

        //reset viewport
        GL30.glViewport(0, 0, scene.window.windowWidth, scene.window.windowHeight)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
    }


}
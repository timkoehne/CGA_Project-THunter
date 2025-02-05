package cga.exercise.components.shadows

import cga.exercise.components.entities.Cube
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.Scene
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.*

class ShadowRenderer(val scene: Scene) {
    companion object {
        const val SHADOW_WIDTH = 1024 * 16//TODO bessere loesung finden
        const val SHADOW_HEIGHT = 1024 * 16//TODO bessere loesung finden
    }

    var sun: Cube = Cube(scene.myMap)


    val depthMapFBO: Int
    val depthMap: Int
    val simpleDepthShader =
        ShaderProgram("project/assets/shaders/simpleDepth_vert.glsl", "project/assets/shaders/simpleDepth_frag.glsl")
    var sunSpaceMatrix = Matrix4f()

    val near_plane = scene.camera.nPlane
    val far_plane = scene.camera.fPlane

    init {
        sun.translate(Vector3f(0f, 20f, 0f))

        depthMapFBO = GL30.glGenFramebuffers()
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, depthMapFBO)

        depthMap = GL11.glGenTextures()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthMap)
        GL11.glTexImage2D(
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
        GL13.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER)

        val borderColor = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glTexParameterfv(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, borderColor)

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_TEXTURE_2D, depthMap, 0)
        GL30.glDrawBuffer(GL30.GL_NONE)
        GL30.glReadBuffer(GL30.GL_NONE)
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)

    }

    fun render(dt: Float, time: Float) {

        val depthProjection =
            Matrix4f().ortho(
                -200f,
                200f,
                -200f,
                200f,
                near_plane,
                far_plane
            ) //TODO bessere loesung finden. debugshader hilft vermutlich
        val depthView = Matrix4f().lookAt(sun.getPosition(), Vector3f(2f, 16f, 2f), Vector3f(0f, 1f, 0f))
        sunSpaceMatrix = depthProjection.mul(depthView)

        simpleDepthShader.use()
        simpleDepthShader.setUniform("lightSpaceMatrix", sunSpaceMatrix, false)

        GL30.glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT)
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, depthMapFBO)

        GL30.glClear(GL30.GL_DEPTH_BUFFER_BIT)
        GL30.glActiveTexture(GL13.GL_TEXTURE10)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 10)

        GL11.glCullFace(GL11.GL_FRONT)

        //TODO was soll alles hier gerendert werden?? evtl uebergeben?
        scene.myMap.renderWithoutGroundShader(simpleDepthShader)
        scene.entityManager.render(dt, time, simpleDepthShader)

        GL11.glCullFace(GL11.GL_BACK)

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)

        //reset viewport
        GL30.glViewport(0, 0, scene.window.windowWidth, scene.window.windowHeight)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)

        GL13.glActiveTexture(GL13.GL_TEXTURE10)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthMap)
    }


    fun cleanUp() {
        simpleDepthShader.cleanUp()
        GL11.glBindTexture(GL20.GL_TEXTURE_2D, 0)
        GL15.glDeleteBuffers(depthMapFBO)
        GL11.glDeleteTextures(depthMap)
    }


}
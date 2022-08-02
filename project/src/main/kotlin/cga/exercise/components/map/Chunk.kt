package cga.exercise.components.map

import cga.exercise.components.TerrainGenerator
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11

class Chunk(val abstand: Float, var positionXZ: Vector3f) {

    companion object {
        val heightGenerator = TerrainGenerator()
        const val chunkSize = 16
        private const val numVerticesPerSide = chunkSize + 1
        val diff = Texture2D.invoke("project/assets/textures/grass.png", true)
        val emit = Texture2D.invoke("project/assets/textures/black.png", true)
        val spec = Texture2D.invoke("project/assets/textures/white.png", true)
        val material: Material

        init {
            //diff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            //emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            //spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

            material = Material(diff, emit, spec)
        }
    }

    private val vertexdata = FloatArray((numVerticesPerSide * numVerticesPerSide) * 8)
    private val indexdata = IntArray((numVerticesPerSide - 1) * (numVerticesPerSide - 1) * 6)

    private val grid: Array<Array<Vector3f>> = Array(numVerticesPerSide) { Array(numVerticesPerSide) { Vector3f() } }

    val mesh: Renderable

    init {
        positionXZ.add(Vector3f(numVerticesPerSide / 2f, 0f, numVerticesPerSide / 2f))
        var gridVertCounter = 0
        for (z in 0 until numVerticesPerSide) {
            for (x in 0 until numVerticesPerSide) {

                vertexInGridErstellen(x, z)
                vertexdataAusGridErstellen(x, z)

                //Vertices zu Grid kombinieren
                if (x < numVerticesPerSide - 1 && z < numVerticesPerSide - 1) {
                    //CCW um die Vertices
                    //Dreieck1
                    indexdata[gridVertCounter] = ((z * numVerticesPerSide) + x)//oben links anfangen
                    indexdata[gridVertCounter + 1] = (((z + 1) * numVerticesPerSide) + x) //einen nach unten
                    indexdata[gridVertCounter + 2] = (((z + 1) * numVerticesPerSide) + x) + 1 //einen nach rechts
                    //Dreieck2
                    indexdata[gridVertCounter + 3] = ((z * numVerticesPerSide) + x) //oben links anfangen
                    indexdata[gridVertCounter + 4] = (((z + 1) * numVerticesPerSide) + x) + 1 //einen diagonal nach unten rechts
                    indexdata[gridVertCounter + 5] = (((z) * numVerticesPerSide) + x) + 1 //einen nach oben
                    gridVertCounter += 6
                }
            }
        }

        //TODO in zukunft vllt andere vertex attribute wenn ein eigener shader existiert
        mesh = Renderable(mutableListOf(Mesh(vertexdata, indexdata, Mesh.vertexAttributes, material)))
    }

    fun vertexdataAusGridErstellen(x: Int, z: Int) {
        //pos
        val pos = ((z * numVerticesPerSide) + x) * 8
        vertexdata[pos] = grid[x][z].x
        vertexdata[pos + 1] = grid[x][z].y
        vertexdata[pos + 2] = grid[x][z].z

        //texture coords

        val worldZ = ((((grid[x][z].z - positionXZ.z) / abstand) + (numVerticesPerSide / 2))).toInt()
        val worldX = ((((grid[x][z].x - positionXZ.x) / abstand) + (numVerticesPerSide / 2))).toInt()

        when (worldZ % 2) {
            0 -> when (worldX % 2) {
                0 -> {
                    vertexdata[pos + 3] = 0f
                    vertexdata[pos + 4] = 1f
                }

                1 -> {
                    vertexdata[pos + 3] = 1f
                    vertexdata[pos + 4] = 1f
                }
            }

            1 -> when (worldX % 2) {
                0 -> {
                    vertexdata[pos + 3] = 0f
                    vertexdata[pos + 4] = 0f
                }

                1 -> {
                    vertexdata[pos + 3] = 1f
                    vertexdata[pos + 4] = 0f
                }
            }
        }

        //normal
        //TODO normalen vom ground sind einfach grade nach oben
        vertexdata[pos + 5] = 0f
        vertexdata[pos + 6] = 1f
        vertexdata[pos + 7] = 0f
    }

    fun vertexInGridErstellen(x: Int, z: Int) {
        grid[x][z] = Vector3f(
            positionXZ.x + ((x - numVerticesPerSide / 2) * abstand),
            heightGenerator.getTerrainHeight(
                positionXZ.x + ((x - numVerticesPerSide / 2) * abstand),
                positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
            ),
            positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
        )
    }

    fun render(camera: TronCamera, shaderProgram: ShaderProgram) {
        camera.bind(shaderProgram)
        mesh.render(shaderProgram)
    }

    fun cleanUp(){
        mesh.cleanUp()
    }

}
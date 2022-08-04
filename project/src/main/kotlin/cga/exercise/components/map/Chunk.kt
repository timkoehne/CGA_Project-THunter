package cga.exercise.components.map

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector3f
import org.lwjgl.opengl.GL13

class Chunk(val abstand: Float, var positionXZ: Vector3f) {

    companion object {
        val heightGenerator = TerrainGenerator()
        const val chunkSize = 16
        private const val numVerticesPerSide = chunkSize + 1
        val diff = Texture2D.invoke("project/assets/textures/texture pack/grass (4).png", true)
        val emit = Texture2D.invoke("project/assets/textures/black.png", true)
        val spec = Texture2D.invoke("project/assets/textures/black.png", true)
        val material = Material(diff, emit, spec, 100f)


        val dirt = Texture2D.invoke("project/assets/textures/texture pack/dirt (1).png", true)
        val snow = Texture2D.invoke("project/assets/textures/texture pack/snow.png", true)
        val sand = Texture2D.invoke("project/assets/textures/texture pack/sand.png", true)
//        val grass = Texture2D.invoke("project/assets/textures/texture pack/", true)


        init {
            //diff.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            //emit.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)
            //spec.setTexParams(GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR)

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
                    indexdata[gridVertCounter + 4] =
                        (((z + 1) * numVerticesPerSide) + x) + 1 //einen diagonal nach unten rechts
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
        val normal = calculateNormal(grid[x][z].x, grid[x][z].z)
        vertexdata[pos + 5] = normal.x
        vertexdata[pos + 6] = normal.y
        vertexdata[pos + 7] = normal.z
    }

    private fun calculateNormal(x: Float, z: Float): Vector3f{
        val heightLeft = heightGenerator.getTerrainHeight(x*abstand-1, z*abstand)
        val heightRight = heightGenerator.getTerrainHeight(x*abstand+1, z*abstand)
        val heightBelow = heightGenerator.getTerrainHeight(x*abstand, z*abstand-1)
        val heightAbove = heightGenerator.getTerrainHeight(x*abstand, z*abstand+1)
        val normal = Vector3f(heightLeft-heightRight, 2f, heightBelow-heightAbove)
        normal.normalize()
        return normal


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
        shaderProgram.use()
        camera.bind(shaderProgram)

        dirt.bind(GL13.GL_TEXTURE3)
        shaderProgram.setUniform("dirtDiff", 3)
        snow.bind(GL13.GL_TEXTURE4)
        shaderProgram.setUniform("snowDiff", 4)
        sand.bind(GL13.GL_TEXTURE5)
        shaderProgram.setUniform("sandDiff", 5)

        mesh.render(shaderProgram)
    }

    fun cleanUp() {
        mesh.cleanUp()
    }

}
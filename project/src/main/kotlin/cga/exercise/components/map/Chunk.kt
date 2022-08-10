package cga.exercise.components.map

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.entities.Entity
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader
import com.sun.source.tree.ForLoopTree
import com.sun.source.tree.Tree
import org.joml.Random
import org.joml.Vector3f
import org.lwjgl.opengl.GL13


class Models {
    companion object {
        val trees = mutableListOf(
            "project/assets/trees/tree1.1.obj",
            "project/assets/trees/tree1.1rot.obj",
            "project/assets/trees/tree1.obj",
            "project/assets/trees/tree1rot.obj",
            "project/assets/trees/tree5.obj",
            "project/assets/trees/tree6.obj",
            "project/assets/trees/tree9.obj",
            "project/assets/trees/tree10.obj",
            "project/assets/trees/tree11.obj"
        )

        val grasses = mutableListOf(
            "project/assets/deko/grassBueschel.obj"
        )

        val stones = mutableListOf(
            "project/assets/deko/bigStone.obj",
            "project/assets/deko/etwasKleinererStein.obj",
            "project/assets/deko/stock.obj"
        )

        val accessories = mutableListOf<String>(
            "project/assets/accessories/brownMushroomPile.obj",
            "project/assets/accessories/redMushroomPile.obj"
        )
    }
}

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

        private val possibleTreePositions = mutableListOf(
            Vector3f(14f, 0f, 2f),
            Vector3f(8f, 0f, 3f),
            Vector3f(3f, 0f, 5f),
            Vector3f(12f, 0f, 6f),
            Vector3f(6f, 0f, 8f),
            Vector3f(11f, 0f, 10f),
            Vector3f(3f, 0f, 12f),
            Vector3f(9f, 0f, 14f),
            Vector3f(14f, 0f, 14f)
        )

        private val possibleStonePositions = mutableListOf(
            Vector3f(5f, 0f, 3f),
            Vector3f(9f, 0f, 6f),
            Vector3f(7f, 0f, 11f),
            Vector3f(14f, 0f, 11f)
        )

        private val possibleAccessoryPositions = mutableListOf(
            Vector3f(5f, 0f, 2f),
            Vector3f(15f, 0f, 2f),
            Vector3f(8f, 0f, 6f),
            Vector3f(8f, 0f, 11f),
            Vector3f(14f, 0f, 12f),
            Vector3f(9f, 0f, 15f)
        )


    }


    val random: Random = Random()

    private val grassTiles = mutableListOf<Foliage>()
    private val treeTiles = mutableListOf<Foliage>()
    private val stoneTiles = mutableListOf<Foliage>()
    private val accessoryTiles = mutableListOf<Foliage>()

    private val treePositions = mutableListOf<Vector3f>()

    private val vertexdata = FloatArray((numVerticesPerSide * numVerticesPerSide) * 8)
    private val indexdata = IntArray((numVerticesPerSide - 1) * (numVerticesPerSide - 1) * 6)

    private val grid: Array<Array<Vector3f>> = Array(numVerticesPerSide) { Array(numVerticesPerSide) { Vector3f() } }

    private val mesh: Renderable

    init {

        mesh = groundMeshGenerieren()

        val anzGrass: Int = (random.nextFloat() * 100).toInt()
        grassTilesGenerieren(anzGrass)

        val anzTrees: Int = random.nextInt(3) + 6
        tilesGenerieren(anzTrees, Models.trees, possibleTreePositions, treePositions, treeTiles, -1f)

        val anzStones: Int = random.nextInt(2)
        tilesGenerieren(anzStones, Models.stones, possibleStonePositions, null, stoneTiles, -1f)

        val anzAccessories: Int = random.nextInt(2) + 3
        tilesGenerieren(anzAccessories, Models.accessories, possibleAccessoryPositions, null, accessoryTiles, 0f)

    }


    fun groundMeshGenerieren(): Renderable {

        //ground generieren
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
        return Renderable(mutableListOf(Mesh(vertexdata, indexdata, Mesh.vertexAttributes, material)))
    }

    fun tilesGenerieren(
        num: Int,
        models: List<String>,
        possiblePositions: List<Vector3f>,
        occupiedPositions: MutableList<Vector3f>?,
        foliageList: MutableList<Foliage>,
        positionOffset: Float
    ) {
        var foliage: Foliage
        var posIndex: Int
        var x: Float
        var z: Float
        for (i in 0 until num) {
            foliage = Foliage(ModelLoader.loadModel(models[random.nextInt(models.size)]))



            if (occupiedPositions == null) {
                posIndex = random.nextInt(possiblePositions.size)
            } else {
                do {
                    posIndex = random.nextInt(possiblePositions.size)
                } while (occupiedPositions.contains(possiblePositions[posIndex]))
                occupiedPositions.add(possiblePositions[posIndex])
            }

            x = possiblePositions[posIndex].x
            z = possiblePositions[posIndex].z

            foliageList.add(foliage)
            foliage.translate(
                Vector3f(
                    positionXZ.x + ((x - numVerticesPerSide / 2) * abstand), heightGenerator.getTerrainHeight(
                        positionXZ.x + ((x - numVerticesPerSide / 2) * abstand),
                        positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
                    ) + positionOffset, positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
                )
            )
            foliage.rotate(0f, random.nextFloat() * Math.toRadians(360.0).toFloat(), 0f)
        }
    }

    fun grassTilesGenerieren(anzGrass: Int) {
        var grass: Foliage
        var x: Float
        var z: Float
        for (i in 0..anzGrass) {
            grass = Foliage(ModelLoader.loadModel("project/assets/deko/grassBueschel.obj", 0f, 0f, 0f))
            x = (random.nextFloat() * numVerticesPerSide)
            z = (random.nextFloat() * numVerticesPerSide)

            grassTiles.add(grass)
            grass.translate(
                Vector3f(
                    positionXZ.x + ((x - numVerticesPerSide / 2) * abstand), heightGenerator.getTerrainHeight(
                        positionXZ.x + ((x - numVerticesPerSide / 2) * abstand),
                        positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
                    ), positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
                )
            )
            grass.scale(Vector3f(random.nextFloat() * 0.2f + 0.5f))
        }
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

    private fun calculateNormal(x: Float, z: Float): Vector3f {
        val heightLeft = heightGenerator.getTerrainHeight(x * abstand - 1, z * abstand)
        val heightRight = heightGenerator.getTerrainHeight(x * abstand + 1, z * abstand)
        val heightBelow = heightGenerator.getTerrainHeight(x * abstand, z * abstand - 1)
        val heightAbove = heightGenerator.getTerrainHeight(x * abstand, z * abstand + 1)
        val normal = Vector3f(heightLeft - heightRight, 2f, heightBelow - heightAbove)
        normal.normalize()
        return normal
    }

    fun vertexInGridErstellen(x: Int, z: Int) {
        grid[x][z] = Vector3f(
            positionXZ.x + ((x - numVerticesPerSide / 2) * abstand), heightGenerator.getTerrainHeight(
                positionXZ.x + ((x - numVerticesPerSide / 2) * abstand),
                positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
            ), positionXZ.z + ((z - numVerticesPerSide / 2) * abstand)
        )
    }

    fun render(camera: TronCamera, shaderProgram: ShaderProgram) {
        shaderProgram.use()
        camera.bind(shaderProgram)

        grassTiles.forEach { it.render(shaderProgram) }

        dirt.bind(GL13.GL_TEXTURE3)
        shaderProgram.setUniform("dirtDiff", 3)
        snow.bind(GL13.GL_TEXTURE4)
        shaderProgram.setUniform("snowDiff", 4)
        sand.bind(GL13.GL_TEXTURE5)
        shaderProgram.setUniform("sandDiff", 5)

        mesh.render(shaderProgram)
    }

    fun renderEntities(camera: TronCamera, shaderProgram: ShaderProgram) {
        shaderProgram.use()
        camera.bind(shaderProgram)

        treeTiles.forEach { it.render(shaderProgram) }
        accessoryTiles.forEach { it.render(shaderProgram) }
        stoneTiles.forEach { it.render(shaderProgram) }
    }


    fun cleanUp() {
        mesh.cleanUp()
    }

}
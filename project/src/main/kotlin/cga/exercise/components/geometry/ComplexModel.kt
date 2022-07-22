package cga.exercise.components.geometry

import cga.exercise.components.geometry.Mesh.Companion.vertexAttributes
import cga.framework.OBJLoader

open class ComplexModel(val objFile: String) : Renderable(mutableListOf<Mesh>()) {

    init {
        val res = OBJLoader.loadOBJ(objFile)

        for (obj in res.objects) {
            for (mesh in obj.meshes) {
                super.meshes.add(Mesh(mesh.vertexData, mesh.indexData, vertexAttributes))
            }
        }
    }


}
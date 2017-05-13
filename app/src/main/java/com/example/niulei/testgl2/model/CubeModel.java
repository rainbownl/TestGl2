package com.example.niulei.testgl2.model;

/**
 * Created by niulei on 2017/4/28.
 */

public class CubeModel extends Model {
	private float defaultVertexes[] = {
			-1f, 1f, 1f,
			1f, 1f, 1f,
			1f, -1f, 1f,
			-1f, -1f, 1f,
			-1f, 1f, -1f,
			1f, 1f, -1f,
			1f, -1f, -1f,
			-1f, -1f, -1f,
	};
	private float defaultColors[] = {
			0f, 0f, 0f, 1f,
			0f, 0f, 1f, 1f,
			0f, 1f, 0f, 1f,
			0f, 1f, 1f, 1f,
			1f, 0f, 0f, 1f,
			1f, 0f, 1f, 1f,
			1f, 1f, 0f, 1f,
			1f, 1f, 1f, 1f,
	};

	private short defaultIndices[] = {
			2, 6, 7, 2, 7, 3,
			5, 4, 7, 5, 7, 6,
			1, 5, 6, 1, 6, 2,
			0, 1, 2, 0, 2, 3,
			0, 4, 5, 0, 5, 1,
			3, 7, 4, 3, 4, 0,
	};
	/*private float defaultNormals[] = {
			0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,
			1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f,
			0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f,
			0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f,
			-1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f,
			0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f,
	};*/
	private float defaultNormals[] = {
			-1f, 1f, 1f,
			1f, 1f, 1f,
			1f, -1f, 1f,
			-1f, -1f, 1f,
			-1f, 1f, -1f,
			1f, 1f, -1f,
			1f, -1f, -1f,
			-1f, -1f, -1f,
	};

	private float defaultMaterialAmbient[] = {0.5f, 0.5f, 0.5f, 1.0f};
	private float defaultMaterialDiffuse[] = {0.5f, 0.5f, 0.5f, 1.0f};
	private float defaultMaterialSpecular[] = {0.9f, 0.9f, 0.9f, 1.0f};
	public CubeModel(){
		vertexes = defaultVertexes;
		colors = defaultColors;
		indices = defaultIndices;

		material_ambient = defaultMaterialAmbient;
		material_diffuse = defaultMaterialDiffuse;
		material_specular = defaultMaterialSpecular;

		normals = defaultNormals;
	}

	public CubeModel(float[] in_vertexes, float[] in_colors, short[] in_indices, float[] in_material_ambient,
	                 float[] in_material_diffuse, float[] in_material_specular){
		super(in_vertexes, in_colors, in_indices, in_material_ambient, in_material_diffuse, in_material_specular);
	}
}

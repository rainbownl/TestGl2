package com.example.niulei.testgl2.model;

/**
 * Created by niulei on 2017/4/28.
 */

public class TrangleModel extends Model {
	private float defaultVertexes[] = {
			0f, 1f, 0f,
			-1f, -1f, 0f,
			1f, -1f, 0f,
	};
	private float defaultNormals[] = {
			0f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f
	};
	private float defaultColors[] = {
			1f, 0f, 0f, 1f,
			0f, 1f, 0f, 1f,
			0f, 0f, 1f, 1f
	};
	private short defaultIndices[] = {0, 1, 2};

	public TrangleModel(){
		super();
		vertexes = defaultVertexes;
		colors = defaultColors;
		indices = defaultIndices;

		normals = defaultNormals;
	}

	public TrangleModel(float[] vertexes, float[] colors, short[] indices, float[] in_material_ambient,
	                    float[] in_material_diffuse, float[] in_material_specular){
		super(vertexes, colors, indices, in_material_ambient, in_material_diffuse, in_material_specular);
	}
}

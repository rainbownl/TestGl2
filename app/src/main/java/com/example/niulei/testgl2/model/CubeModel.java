package com.example.niulei.testgl2.model;

/**
 * Created by niulei on 2017/4/28.
 */

public class CubeModel extends Model {
	private float defaultVertexes[] = {
			-1f, 1f, 1f, 1f,
			1f, 1f, 1f, 1f,
			1f, -1f, 1f, 1f,
			-1f, -1f, 1f, 1f,
			-1f, 1f, -1f, 1f,
			1f, 1f, -1f, 1f,
			1f, -1f, 1f, 1f,
			-1f, -1f, -1f, 1f
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
			0, 1, 2, 0, 2, 3,
			1, 5, 6, 1, 6, 2,
			0, 4, 5, 0, 5, 1,
			2, 6, 7, 2, 7, 3,
			3, 7, 4, 3, 4, 0,
			5, 4, 7, 5, 7, 6
	};

	public CubeModel(){
		vertexes = defaultVertexes;
		colors = defaultColors;
		indices = defaultIndices;
	}

	public CubeModel(float[] in_vertexes, float[] in_colors, short[] in_indices){
		super(in_vertexes, in_colors, in_indices);
	}
}

package com.example.niulei.testgl2.model;

/**
 * Created by niulei on 2017/4/28.
 */

public class TrangleModel extends Model {
	private float defaultVertexes[] = {
			0f, 1f, 0f, 1f,
			-1f, -1f, 0f, 1f,
			1f, -1f, 0f, 1f
	};
	private float defaultColors[] = {
			1f, 0f, 0f, 1f,
			0f, 1f, 0f, 1f,
			0f, 0f, 1f, 1f
	};
	private short defaultIndices[] = {0, 1, 2};

	public TrangleModel(){
		vertexes = defaultVertexes;
		colors = defaultColors;
		indices = defaultIndices;
	}

	public TrangleModel(float[] vertexes, float[] colors, short[] indices){
		super(vertexes, colors, indices);
	}
}

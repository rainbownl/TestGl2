package com.example.niulei.testgl2.model;

/**
 * Created by niulei on 2017/4/28.
 */

public class Model {
	protected float[] vertexes;
	protected float[] colors;
	protected short[] indices;

	public Model(){
		vertexes = null;
		colors = null;
		indices = null;
	};
	public Model(float[] in_vertexes, float[] in_colors, short[] in_indices){
		vertexes = in_vertexes;
		colors = in_colors;
		indices = in_indices;
	}

	public float[] getVertexes(){
		return vertexes;
	}

	public float[] getColors(){
		return colors;
	}

	public short[] getIndices() {
		return indices;
	}
}

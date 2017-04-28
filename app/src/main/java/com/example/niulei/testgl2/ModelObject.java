package com.example.niulei.testgl2;

/**
 * Created by niulei on 2017/4/26.
 */

public class ModelObject {
	public static final int VERTEX_ARRAY = 0;
	public static final int VERTEX_ELEMENTS = 1;
	public int vertexType;
	public float[] vertexes;
	public float[] colors;
	public float[] indices;

	public ModelObject(float[] in_vertexes, float[] in_colors){
		vertexType = VERTEX_ARRAY;
		vertexes = in_vertexes;
		colors = in_colors;
	}

	public ModelObject(float[] in_vertexes, float[] in_indices, float[] in_colors){
		vertexType = VERTEX_ELEMENTS;
		vertexes = in_vertexes;
		colors = in_colors;
		indices = in_indices;
	}
}

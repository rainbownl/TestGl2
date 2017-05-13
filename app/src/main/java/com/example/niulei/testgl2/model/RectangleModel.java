package com.example.niulei.testgl2.model;

/**
 * Created by niulei on 2017/5/4.
 */

public class RectangleModel extends Model {
	private float[] defaultVertexes = {
			-1f, 0.5f, 0f,
			-1f, -0.5f, 0f,
			1f, -0.5f, 0f,
			1f, 0.5f, 0f
	};
	private float[] defaultColors = {
			1f, 0f, 0f, 1f,
			0f, 1f, 0f, 1f,
			0f, 0f, 1f, 1f,
			0f, 1f, 1f, 1f,
	};
	private short[] defaultIndices = {0, 1, 2, 0, 2, 3};
}

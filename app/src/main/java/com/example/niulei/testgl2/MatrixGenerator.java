package com.example.niulei.testgl2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by niulei on 2017/4/25.
 */

public class MatrixGenerator {
	public static FloatBuffer arrayToBuffer(float[] array){
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * Float.SIZE/8);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);

		return floatBuffer;
	}
	public static FloatBuffer makeScaleMatrix(float x, float y, float z){
		float matrix[] = {
				x, 0.0f, 0.0f, 0.0f,
				0.0f, y, 0.0f, 0.0f,
				0.0f, 0.0f, z, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		};

		FloatBuffer matrixFloatBuffer = arrayToBuffer(matrix);

		return matrixFloatBuffer;
	}

	public static FloatBuffer makeTransformMatrix(float x, float y, float z){
		float matrix[] ={
				1f, 0f, 0f, 0f,
				0f, 1f, 0f, 0f,
				0f, 0f, 1f, 0f,
				x,  y,  z,  1f
		};

		return  arrayToBuffer(matrix);
	}

	public static FloatBuffer makeRotateXMatrix(double angle){
		float matrix[] = {
				1f, 0f, 0f, 0f,
				0f, (float) Math.cos(angle), (float) Math.sin(angle), 0f,
				0f, -(float) Math.sin(angle), (float) Math.cos(angle), 0f,
				0f, 0f, 0f, 1f
		};

		return arrayToBuffer(matrix);
	}

	public static FloatBuffer makeRotateYMatrix(double angle){
		float matrix[] = {
				(float)Math.cos(angle), 0f, (float)Math.sin(angle), 0f,
				0f, 1f, 0f, 0f,
				-(float)Math.sin(angle), 0f, (float)Math.cos(angle), 0f,
				0f, 0f, 0f, 1f
		};

		return arrayToBuffer(matrix);
	}

	public static FloatBuffer makeRotateZMatrix(double angle){
		float matrix[] = {
				(float)Math.cos(angle), -(float)Math.sin(angle), 0f, 0f,
				(float)Math.sin(angle), (float)Math.cos(angle), 0f, 0f,
				0f, 0f, 1f, 0f,
				0f, 0f, 0f, 2f
		};

		return arrayToBuffer(matrix);
	}
}

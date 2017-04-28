package com.example.niulei.testgl2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by niulei on 2017/4/25.
 */

public class MatrixCalculator {
	public static FloatBuffer plus(FloatBuffer buffer1, FloatBuffer buffer2){
		if (buffer1.remaining() != 16 || buffer2.remaining() != 16){
			return null;
		}
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(buffer1.array().length * Float.SIZE/8);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		for (int i = 0; i < buffer1.array().length; i++){
			floatBuffer.put(buffer1.array()[i] + buffer2.array()[i]);
		}

		return floatBuffer;
	}

	public static FloatBuffer minus(FloatBuffer buffer1, FloatBuffer buffer2){
		if (buffer1.remaining() != 16 || buffer2.remaining() != 16){
			return null;
		}
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(buffer1.array().length * Float.SIZE/8);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		for (int i = 0; i < buffer1.array().length; i++){
			floatBuffer.put(buffer1.array()[i] - buffer2.array()[i]);
		}

		return floatBuffer;
	}

	public static FloatBuffer multiplyMM(FloatBuffer buffer1, FloatBuffer buffer2){
		if (buffer1.remaining() != 16 || buffer2.remaining() != 16){
			return null;
		}

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16 * Float.SIZE/8);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		for (int j = 0; j < 4; j++){
			for (int i = 0; i < 4; i++){
				floatBuffer.put(buffer1.get(0+i)*buffer2.get(j*4) + buffer1.get(4+i)*buffer2.get(j*4+1) +
						buffer1.get(8+i)*buffer2.get(j*4+2) + buffer1.get(12+i)*buffer2.get(j*4+3));
			}
		}

		floatBuffer.position(0);
		return floatBuffer;

	}
}

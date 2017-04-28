package com.example.niulei.testgl2;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.niulei.testgl2.model.Model;
import com.example.niulei.testgl2.model.TrangleModel;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
	private int PrintLog = 1;
	private String LogTag = "CldNavi";
	private int program = 0;
	private int ScreenWidth = 0;
	private int ScreenHeight = 0;
	FloatBuffer vertexFloatBuffer = null;
	FloatBuffer colorFloatBuffer = null;
	FloatBuffer mvp_matrixFloatBuffer = null;
	private int nStep = 0;
	private float[] viewMatrix = null;
	private float[] projectionMatrix = null;
	private float[] mvMatrix = null;
	private float[] mvpMatrix = null;

	private String vertexShaderSrc;
	private String fragmentShaderSrc;
	private Model model;

	public void setVertexShaderSrc(String inSource){
		vertexShaderSrc = inSource;
	}
	public void setFragmentShaderSrc(String inSource){
		fragmentShaderSrc = inSource;
	}


	public void setnStep(int step){
		nStep = step;
	}

	public int getnStep(){
		return nStep;
	}
	public MyRenderer(int screenWidth, int screenHeight){
		ScreenWidth = screenWidth;
		ScreenHeight = screenHeight;

		float vertexes[] =
				{0.0f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f};
		float colors[] = {
				1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f
		};

		model = new TrangleModel();
		vertexFloatBuffer = MatrixGenerator.arrayToBuffer(vertexes);
		colorFloatBuffer = MatrixGenerator.arrayToBuffer(colors);
	}

	private void ShowProgramErrorLog(){
		if (PrintLog == 1) {
			Log.d(LogTag, GLES20.glGetProgramInfoLog(program));
		}
	}

	private int LoadShader(int shaderType, String shaderSrc){
		int shader = GLES20.glCreateShader(shaderType);
		GLES20.glShaderSource(shader, shaderSrc);
		GLES20.glCompileShader(shader);
		ShowProgramErrorLog();
		return shader;
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		int vertexShader = LoadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSrc);
		int fragmentShader = LoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSrc);

		program = GLES20.glCreateProgram();
		if (program == 0){
			Log.d(LogTag, "glCreateProgram failed!!");
			return;
		}
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);

		//GLES20.glBindAttribLocation(program, 0, "vPosition");
		GLES20.glLinkProgram(program);

		viewMatrix = new float[16];
		projectionMatrix = new float[16];
		mvMatrix = new float[16];
		mvpMatrix = new float[16];
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		ScreenWidth = width;
		ScreenHeight = height;
		GLES20.glViewport(0, 0, ScreenWidth, ScreenHeight);
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

		Matrix.setLookAtM(viewMatrix, 0, -2f, 2f, 2f, 0f, 0f, 0f, 0f, 1f, 0f);
		Matrix.perspectiveM(projectionMatrix, 0, 45, 1f*ScreenWidth/ScreenHeight, 0.1f, 100f);
		Matrix.multiplyMM(mvMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
	}

	private void drawAxis(int vertexLocation, int colorLocation, int mvpLocation){
		float axiXVertex[] = {
				-1.0f, 0.02f, 0.0f,
				-1.0f, -0.02f, 0.0f,
				1.0f, -0.02f, 0.0f,
				1.0f, 0.02f, 0.0f
		};
		float axiYVertex[] = {
				-0.02f, 1.0f, 0.0f,
				-0.02f, -1.0f, 0.0f,
				0.02f, -1.0f, 0.0f,
				0.02f, 1.0f, 0.0f
		};
		float axiZVertex[] = {
				-0.02f, 0.0f, -1.0f,
				-0.02f, 0.0f, 1.0f,
				0.02f, 0.0f, 1.0f,
				0.02f, 0.0f, -1.0f
		};
		float colors[] = {
				0.8f, 0.3f, 0.8f, 0.5f,
		};
		short indices[] = {0, 1, 2, 0, 2, 3};
		FloatBuffer colorBuffer = MatrixGenerator.arrayToBuffer(colors);
		GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(axiXVertex));
		GLES20.glEnableVertexAttribArray(vertexLocation);
		//GLES20.glVertexAttribPointer(colorLocation, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
		GLES20.glVertexAttrib4f(colorLocation, colors[0], colors[1], colors[2], colors[3]);
		//GLES20.glEnableVertexAttribArray(colorLocation);
		GLES20.glUniformMatrix4fv(mvpLocation, 1, false, MatrixGenerator.arrayToBuffer(mvMatrix));

		ShortBuffer indicesShortBuffer =
				ByteBuffer.allocateDirect(indices.length * Short.SIZE/8)
				.order(ByteOrder.nativeOrder())
				.asShortBuffer()
				.put(indices);
		indicesShortBuffer.position(0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);

		GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(axiYVertex));
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);

		GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(axiZVertex));
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);

	}
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glUseProgram(program);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		int mPosition = GLES20.glGetAttribLocation(program, "vPosition");
		int mColorPosition = GLES20.glGetAttribLocation(program, "vColor");
		int mvpMatrixLocation = GLES20.glGetUniformLocation(program, "u_mvp_matrix");

		drawAxis(mPosition, mColorPosition, mvpMatrixLocation);

		GLES20.glVertexAttribPointer(mPosition, 4, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(model.getVertexes()));
		GLES20.glEnableVertexAttribArray(mPosition);

		if (mColorPosition >= 0) {
			GLES20.glVertexAttribPointer(mColorPosition, 4, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(model.getColors()));
			GLES20.glEnableVertexAttribArray(mColorPosition);
		}

		float rotateMatrix[] = new float[16];
		Matrix.setRotateM(rotateMatrix, 0, (float)180/250*nStep, 0f, 0f, 1f);
		Matrix.multiplyMM(mvpMatrix, 0, mvMatrix, 0, rotateMatrix, 0);

		GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, MatrixGenerator.arrayToBuffer(mvpMatrix));

		short indices[] = model.getIndices();
		ShortBuffer indicesShortBuffer =
				ByteBuffer.allocateDirect(indices.length * Short.SIZE/8)
				.order(ByteOrder.nativeOrder())
				.asShortBuffer()
				.put(indices);
		indicesShortBuffer.position(0);
		//GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		GLES20.glEnable();
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);
	}
}
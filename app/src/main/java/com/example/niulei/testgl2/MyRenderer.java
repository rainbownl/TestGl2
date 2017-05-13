package com.example.niulei.testgl2;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES31Ext;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.niulei.testgl2.loader3ds.Object3DS;
import com.example.niulei.testgl2.loader3ds.Parser3ds;
import com.example.niulei.testgl2.model.CubeModel;
import com.example.niulei.testgl2.model.Model;
import com.example.niulei.testgl2.model.TrangleModel;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
	private int renderStatus = 0;
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
	private ArrayList<Object3DS> modelObjects;

	public void setModelObjects(ArrayList<Object3DS> objects){ modelObjects = objects;}
	public void setModel(Model in_model){model = in_model;}
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

		vertexFloatBuffer = MatrixGenerator.arrayToBuffer(vertexes);
		colorFloatBuffer = MatrixGenerator.arrayToBuffer(colors);
	}

	private void ShowProgramErrorLog(){
		if (PrintLog == 1) {
			Log.d(LogTag, GLES20.glGetProgramInfoLog(program));
		}
	}

	private void ShowShaderErrorLog(int shader){
		if (PrintLog == 1){
			Log.d(LogTag, "Shader error info: " + GLES20.glGetShaderInfoLog(shader));
		}
	}

	private int LoadShader(int shaderType, String shaderSrc){
		int shader = GLES20.glCreateShader(shaderType);
		GLES20.glShaderSource(shader, shaderSrc);
		GLES20.glCompileShader(shader);
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
		GLES20.glUseProgram(program);

		viewMatrix = new float[16];
		projectionMatrix = new float[16];
		mvMatrix = new float[16];
		mvpMatrix = new float[16];
		setEnableLight(program, 1);
		setWorldLight(program, 0.8f, 0.5f, 0.5f, 1.0f);
		setDirectionalLight(program);
		renderStatus = 1;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		ScreenWidth = width;
		ScreenHeight = height;
		GLES20.glViewport(0, 0, ScreenWidth, ScreenHeight);
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

		Matrix.setLookAtM(viewMatrix, 0, -4f, 4f, 4f, 0f, 0f, 0f, 0f, 1f, 0f);
		Matrix.perspectiveM(projectionMatrix, 0, 45, 1f*ScreenWidth/ScreenHeight, 0.1f, 100f);
		Matrix.multiplyMM(mvMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
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
		//GLES20.glUniformMatrix4fv(mvpLocation, 1, false, MatrixGenerator.arrayToBuffer(mvMatrix));

		ShortBuffer indicesShortBuffer =
				ByteBuffer.allocateDirect(indices.length * Short.SIZE/8)
				.order(ByteOrder.nativeOrder())
				.asShortBuffer()
				.put(indices);
		indicesShortBuffer.position(0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);

		GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(axiYVertex));
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);

		GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(axiZVertex));
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);

	}
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glClearDepthf(1.0f);

		int mPosition = GLES20.glGetAttribLocation(program, "vPosition");
		int mColorPosition = GLES20.glGetAttribLocation(program, "vColor");
		int mvpMatrixLocation = GLES20.glGetUniformLocation(program, "u_mvp_matrix");
		int normalLocation = GLES20.glGetAttribLocation(program, "normal");
		int texCoordLocation = GLES20.glGetAttribLocation(program, "tex_coord");
		int texSamplerLocation = GLES20.glGetUniformLocation(program, "s_texture");

		/*float rotateMatrix[] = new float[16];
		Matrix.setRotateM(rotateMatrix, 0, (float)180/250*nStep, 0f, 0f, 1f);
		Matrix.multiplyMM(mvpMatrix, 0, mvMatrix, 0, rotateMatrix, 0);
		GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, MatrixGenerator.arrayToBuffer(mvMatrix));*/

		drawAxis(mPosition, mColorPosition, mvpMatrixLocation);
		//drawModel(program, model, mPosition, mColorPosition);

		int material_ambient_location = GLES20.glGetUniformLocation(program, "material_ambient");
		int material_diffuse_location = GLES20.glGetAttribLocation(program, "material_diffuse");
		GLES20.glUniform4f(material_ambient_location, 0.8f, 0.3f, 0.5f, 1f);
		GLES20.glUniform4f(material_diffuse_location, 0.8f, 0.3f, 0.5f, 1f);
		Matrix.setRotateM(mvpMatrix, 0, 90, -1, 0, 0);
		Matrix.multiplyMM(mvpMatrix, 0, mvMatrix, 0, mvpMatrix, 0);
		if (modelObjects != null){
			for (Object3DS modelObject:modelObjects) {
				modelObject.draw(mvpMatrix, mvpMatrixLocation, mPosition, normalLocation, texCoordLocation, texSamplerLocation, -1);
			}
		}
	}

	private void setWorldLight(int program, float r, float g, float b, float a){
		int light_world_location = GLES20.glGetUniformLocation(program, "light_world");
		if (light_world_location >= 0){
			GLES20.glUniform4f(light_world_location, r, g, b, a);
		}
	}

	private void setDirectionalLight(int program){
		//float[] light_ambient = {0.2f, 0.85f, 0.2f, 1.0f};
		//float[] light_diffuse = {0.2f, 0.85f, 0.2f, 1.0f};
		float[] light_ambient = {1f, 1f, 1f, 1.0f};
		float[] light_diffuse = {1f, 1f, 1f, 1.0f};
		float[] light_direction = {-1f, 2f, 1f};

		int light_direction_location = GLES20.glGetUniformLocation(program, "light0_direction");
		if (light_direction_location < 0){
			Log.d("CldNavi", "[ERROR]light_direction_location :" + light_direction_location);
		}
		else {
			GLES20.glUniform3f(light_direction_location, light_direction[0], light_direction[1], light_direction[2]);
		}

		int light_ambient_location = GLES20.glGetUniformLocation(program, "light0_ambient");
		if (light_ambient_location < 0){
			Log.d("CldNavi", "[ERROR]light_ambient_location :" + light_ambient_location);
		}
		else {
			GLES20.glUniform4f(light_ambient_location, light_ambient[0], light_ambient[1], light_ambient[2], light_ambient[3]);
		}

		int light_diffuse_location = GLES20.glGetUniformLocation(program, "light0_diffuse");
		if (light_diffuse_location < 0){
			Log.d("CldNavi", "[ERROR]light_diffuse_location :" + light_diffuse_location);
		}
		else {
			GLES20.glUniform4f(light_diffuse_location, light_diffuse[0], light_diffuse[1], light_diffuse[2], light_diffuse[3]);
		}
	}

	private void setModelMaterial(int program, Model model){
		float[] material_ambient = model.getMaterial_ambient();
		float[] material_diffuse = model.getMaterial_diffuse();

		if (material_ambient != null) {
			int material_ambient_location = GLES20.glGetUniformLocation(program, "material_ambient");
			if (material_ambient_location < 0) {
				Log.d("CldNavi", "[ERROR]material_ambient_location :" + material_ambient_location);
			}
			GLES20.glUniform4f(material_ambient_location, material_ambient[0], material_ambient[1], material_ambient[2], material_ambient[3]);
		}

		if (material_diffuse != null){
			int material_diffuse_location = GLES20.glGetUniformLocation(program, "material_diffuse");
			if (material_diffuse_location < 0){
				Log.d("CldNavi", "[ERROR]material_diffuse_location :" + material_diffuse_location);
			}
			GLES20.glUniform4f(material_diffuse_location, material_diffuse[0], material_diffuse[1], material_diffuse[2], material_diffuse[3]);
		}
	}

	private void setModelAttribute(int program, Model model, int vertexLocation, int colorLocation){
		if (vertexLocation >= 0) {
			GLES20.glVertexAttribPointer(vertexLocation, 3, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(model.getVertexes()));
			GLES20.glEnableVertexAttribArray(vertexLocation);
		}

		if (colorLocation >= 0) {
			GLES20.glVertexAttribPointer(colorLocation, 4, GLES20.GL_FLOAT, false, 0, MatrixGenerator.arrayToBuffer(model.getColors()));
			GLES20.glEnableVertexAttribArray(colorLocation);
		}

		int normal_location = GLES20.glGetAttribLocation(program, "normal");
		if (normal_location < 0){
			Log.d("CldNavi", "[ERROR]normal_location :" + normal_location);
		}
		else {
			GLES20.glVertexAttribPointer(normal_location, 3, GLES20.GL_FLOAT, true, 0, MatrixGenerator.arrayToBuffer(model.getNormals()));
			GLES20.glEnableVertexAttribArray(normal_location);
		}
	}

	private void drawModel(int program, Model model, int vertexLocation, int colorLocation){
		setModelAttribute(program, model, vertexLocation, colorLocation);
		setModelMaterial(program, model);

		short indices[] = model.getIndices();
		ShortBuffer indicesShortBuffer =
				ByteBuffer.allocateDirect(indices.length * Short.SIZE/8)
						.order(ByteOrder.nativeOrder())
						.asShortBuffer()
						.put(indices);
		indicesShortBuffer.position(0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indicesShortBuffer);
	}

	private void setEnableLight(int program, int enable){
		int lightEnableLocation = GLES20.glGetUniformLocation(program, "light_enable");
		if (lightEnableLocation < 0){
			Log.d("CldNavi", "[ERROR]lightEnable :" + lightEnableLocation);
		}
		else {
			GLES20.glUniform1i(lightEnableLocation, enable);
		}
	}
}
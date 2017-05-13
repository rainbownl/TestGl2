package com.example.niulei.testgl2.model;

/**
 * Created by niulei on 2017/4/28.
 */

public class Model {
	protected int materialCount = 0;
	protected float[] vertexes;
	protected float[] colors;
	protected short[] indices;

	protected float[] material_ambient;
	protected float[] material_diffuse;
	protected float[] material_specular;

	protected float[] normals;

	private float defaultMaterialAmbient[] = {0.5f, 0.5f, 0.5f, 1.0f};
	private float defaultMaterialDiffuse[] = {0.5f, 0.5f, 0.5f, 1.0f};
	private float defaultMaterialSpecular[] = {0.9f, 0.9f, 0.9f, 1.0f};

	public float[] getMaterial_ambient() {
		return material_ambient;
	}

	public float[] getMaterial_diffuse() {
		return material_diffuse;
	}

	public float[] getMaterial_specular() {
		return material_specular;
	}


	public Model(){
		vertexes = null;
		colors = null;
		indices = null;
		material_ambient = defaultMaterialAmbient;
		material_diffuse = defaultMaterialDiffuse;
		material_specular = defaultMaterialSpecular;
	};

	public Model(float[] in_vertexes, float[] in_colors, short[] in_indices, float[] in_material_ambient,
	             float[] in_material_diffuse, float[] in_material_specular){
		vertexes = in_vertexes;
		colors = in_colors;
		indices = in_indices;

		material_ambient = in_material_ambient;
		material_diffuse = in_material_diffuse;
		material_specular = in_material_specular;
	}

	public void addMaterial(){
		materialCount ++;
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

	public float[] getNormals() {
		return normals;
	}
}

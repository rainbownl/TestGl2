package com.example.niulei.testgl2.loader3ds;

/**
 * Created by niulei on 2017/5/7.
 */
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by andrisgauracs on 23/10/2016.
 */
public abstract class Parser3ds {

	private BufferedInputStream stream;
	private int pos;
	private int limit;
	private ArrayList<Float> scales = new ArrayList<>();
	private float scaleFactor = 0.0f;
	private float initialScaleFactor = 0.0f;

	float[] vertices;
	ArrayList<Object3DS> models = new ArrayList<Object3DS>();
	int[] faces;
	int numFaces;
	ArrayList<String[]> materials = new ArrayList<String[]>();

	/** Size of the texture coordinate data in elements. */

	public boolean objReady = false;

	public abstract InputStream getInputStreamByName(String name);
	/**
	 * This is the constructor, when a texture is specified, or no texture is specified, in which case, we use our gray "default_texture"
	 * @param file - the 3ds object from "raw" resource folder
	 */
	public Parser3ds(InputStream file) {
		stream = new BufferedInputStream(file);
		pos = 0;

		try {
			limit = readChunk();
			while (pos < limit) {
				readChunk();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Object3DS model : models) {
			if (model.getVertices() != null) {
				model.prepareModel();
				scales.add(model.getScaleFactor());
			}
		}
		for (int i =0; i < scales.size(); i++) {
			if (scales.get(i) > scaleFactor) { scaleFactor = scales.get(i); initialScaleFactor = scales.get(i); }
		}
		objReady = true;
	}

	/**
	 *
	 * @param file - the 3ds object from "raw" resource folder
	 * @param model_texture - Since the original file has not specified a texture filename, we will "force" it to use this texture
	 */
	public Parser3ds(InputStream file, String model_texture) {
		stream = new BufferedInputStream(file);
		pos = 0;

		//Read through the file
		try {
			limit = readChunk();
			while (pos < limit) {
				readChunk();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Object3DS model : models) {
			if (model.getVertices() != null) {
				model.prepareModel();
				//Get each object's scale factor
				scales.add(model.getScaleFactor());
			}
		}
		//We must choose one scale factor (if there is more than one object)
		for (int i =0; i < scales.size(); i++) {
			if (scales.get(i) > scaleFactor) { scaleFactor = scales.get(i); initialScaleFactor = scales.get(i); }
		}
		//Object is ready for drawing
		objReady = true;
	}

	private byte getByte() throws IOException {
		int read = stream.read();
		if (read == -1) {
			throw new EOFException();
		}
		pos++;
		return (byte) read;
	}

	public short getShort() throws IOException {
		byte b0 = getByte();
		byte b1 = getByte();
		return makeShort(b1, b0);
	}

	public int getInt() throws IOException {
		byte b0 = getByte();
		byte b1 = getByte();
		byte b2 = getByte();
		byte b3 = getByte();
		return makeInt(b3, b2, b1, b0);
	}

	public float getFloat() throws IOException {
		return Float.intBitsToFloat(getInt());
	}

	static private short makeShort(byte b1, byte b0) {
		return (short)((b1 << 8) | (b0 & 0xff));
	}

	static private int makeInt(byte b3, byte b2, byte b1, byte b0) {
		return (((b3       ) << 24) |
				((b2 & 0xff) << 16) |
				((b1 & 0xff) <<  8) |
				((b0 & 0xff)      ));
	}

	public String readString() throws IOException {
		StringBuilder sb = new StringBuilder(64);
		byte ch = getByte();
		while (ch != 0) {
			sb.append((char)ch);
			ch = getByte();
		}
		return sb.toString();
	}

	public void skip(int i) throws IOException {
		int skipped = 0;
		do {
			skipped += stream.skip(i - skipped);
		} while (skipped < i);

		pos += i;
	}

	private int readChunk() throws IOException {
		short type = getShort();
		int size = getInt(); // this is probably unsigned but for convenience we use signed int
		parseChunk(type, size);
		return size;
	}

	/**
	 * Most of the byte reading functions are implemented from <a href="https://github.com/kjetilos/3ds-parser">this java 3ds parser</a>
	 * @param type
	 * @param size
	 * @throws IOException
	 */
	private void parseChunk(short type, int size) throws IOException {
		switch (type) {
			case 0x3d3d: //3D Editor Chunk
				break;
			case 0x4000:
				parseObjectChunk();
				break;
			case 0x4100: // Triangular Mesh
				break;
			case 0x4110:
				parseVerticesList();
				break;
			case 0x4120:
				parseFaces();
				break;
			case 0x4130:
				parseFaceMaterial();
				break;
			case 0x4140:
				parseUVTexture();
				break;
			case 0x4d4d:
				parseMainChunk();
				break;
			case (short)0xafff: // Material block
				break;
			case (short)0xa000: // Material name
				parseTextureName();
				break;
			case (short)0xa200: // Texture map 1
				break;
			case (short)0xa300: // Mapping filename
				parseTextureFilename();
				break;
			default:
				skipChunk(type, size);
		}
	}

	private void skipChunk(int type, int size) throws IOException {
		move(size - 6); // size includes headers. header is 6 bytes
	}

	private void move(int i) throws IOException {
		skip(i);
	}

	private void parseMainChunk() throws IOException {

		Log.v("Status", "Found Main object");
	}

	private void parseObjectChunk() throws IOException {
		String name = readString();
		models.add(models.size(), new Object3DS());

	}

	private void parseVerticesList() throws IOException {
		short numVertices = getShort();
		vertices = new float[numVertices * 3];
		for (int i=0; i<vertices.length; i++) {
			//getFloat();
			vertices[i] = getFloat();
		}
		for (int i = 0; i < vertices.length; i++){
			vertices[i] /= 206.0f;
		}
		models.get(models.size()-1).setVertices(vertices);
	}

	private void parseFaces() throws IOException {
		numFaces = getShort();
		faces = new int[numFaces * 3];
		for (int i=0; i<numFaces; i++) {
			faces[i*3] = getShort();
			faces[i*3 + 1] = getShort();
			faces[i*3 + 2] = getShort();
			getShort(); // Discard face flag
		}
		models.get(models.size()-1).setFaces(faces);
		models.get(models.size()-1).setNumFaces(numFaces);
	}

	private void parseFaceMaterial() throws IOException {
		String name = readString();
		for (int i=0; i<materials.size(); i++) {
			if (materials.get(i)[0].equals(name) && materials.get(i)[1] != null) {
				InputStream inputStream = getInputStreamByName(name);
				if (inputStream != null) {
					models.get(models.size() - 1).setTextureHandle(TextureLoader.loadTexture(inputStream));
					models.get(models.size() - 1).setHasTexture();
				}
				break;
			}
		}
		if (!models.get(models.size()-1).hasTexture()) {
			InputStream inputStream = getInputStreamByName("defaultTexture");

			models.get(models.size()-1).setTextureHandle(TextureLoader.loadTexture(inputStream));
		}
		int size = getShort();
		for (int i = 0; i < size; i++) {
			getByte(); getByte(); //Just skipping these chunks
		}
	}

	private void parseUVTexture() throws IOException {
		short numVertices = getShort();
		float[] uv = new float[numVertices * 2];
		for (int i=0; i<numVertices; i++) {
			uv[i*2] = getFloat();
			uv[i *2+1] = getFloat();
		}
		models.get(models.size()-1).setTextures(uv);
	}

	private void parseTextureName() throws IOException {
		String materialName = readString();
		materials.add(materials.size(), new String[2]);
		materials.get(materials.size()-1)[0] = materialName;
	}

	private void parseTextureFilename() throws IOException {
		String mappingFile = readString();
		mappingFile = mappingFile.substring(0, mappingFile.lastIndexOf('.'));
		materials.get(materials.size()-1)[1] = mappingFile;

	}

	/**
	 * This function is executed, when the seek bar value is changed.
	 */
	public void changeScale(float val) {
		scaleFactor = initialScaleFactor + initialScaleFactor * val;
	}

	public ArrayList<Object3DS> getObjects(){
		return models;
	}

}
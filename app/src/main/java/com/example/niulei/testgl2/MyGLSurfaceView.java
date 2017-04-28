package com.example.niulei.testgl2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceView;

/**
 * Created by niulei on 2017/4/17.
 */

public class MyGLSurfaceView extends GLSurfaceView {

	public MyGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
	}
}

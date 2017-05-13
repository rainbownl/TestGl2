package com.example.niulei.testgl2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Created by niulei on 2017/4/17.
 */

public class MyGLSurfaceView extends GLSurfaceView {

	public MyGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		setEGLConfigChooser(true);
		setEGLConfigChooser(8, 8, 8, 8, 16, 8);
	}

}

package com.example.niulei.testgl2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.niulei.testgl2.loader3ds.Parser3ds;
import com.example.niulei.testgl2.model.Model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    public static final int MSG_TIMER = 0;
    Handler mHandler = null;
    MyRenderer myRenderer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MyGLSurfaceView myGLSurfaceView = new MyGLSurfaceView(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        myRenderer = new MyRenderer(displayMetrics.widthPixels, displayMetrics.heightPixels);
        Parser3ds parser3ds = new Parser3ds(getResources().openRawResource(R.raw.car)){
            @Override
            public InputStream getInputStreamByName(String name) {
                return getResources().openRawResource(R.raw.tex1030);
            }
        };
        myRenderer.setModelObjects(parser3ds.getObjects());
        myRenderer.setVertexShaderSrc(getVertexShader());
        myRenderer.setFragmentShaderSrc(getFragmentShader());
        myGLSurfaceView.setRenderer(myRenderer);
        myGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        initHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mHandler != null){
                        Message msg = new Message();
                        msg.what = MSG_TIMER;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        }).start();
        setContentView(myGLSurfaceView);

        /*setContentView(R.layout.activity_main);
        String cpuInfo = getCpuInfo();
        String memInfo = getMemInfo();
        TextView textView = (TextView)findViewById(R.id.textView);
        if (textView != null){
            textView.setText(cpuInfo);
            //textView.setText(memInfo);
        }*/
    }

    private String getCpuInfo(){
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            String info = new String(new char[1024]);
            while ((line = bufferedReader.readLine()) != null) {
                info = info + line + "\n";
            }
            bufferedReader.close();
            return  info;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private String getMemInfo(){
        try{
            FileReader fileReader = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String memInfo = new String(new char[1024]);
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                memInfo = memInfo + line + "\n";
            }
            bufferedReader.close();

            return memInfo;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_TIMER:
                        if (myRenderer != null){
                            int nStep = myRenderer.getnStep();
                            if (nStep < 250){
                                myRenderer.setnStep(nStep + 1);
                            }
                            else{
                                myRenderer.setnStep(0);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }
    private String readInputstream(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String out = "";
        try {
            String line = null;
            while ((line = br.readLine()) != null){
                out += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
    public String getVertexShader(){
        InputStream is = getResources().openRawResource(R.raw.vertexshader);
        return readInputstream(is);
    }

    public String getFragmentShader(){
        InputStream is = getResources().openRawResource(R.raw.fragmentshader);
        return readInputstream(is);
    }
}

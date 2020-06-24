package com.example.jogodabolinha;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jogodabolinha.Task.ThreadJogo;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements SensorEventListener, SurfaceHolder.Callback {
    private SurfaceView mSurfuceView;
    private SurfaceHolder mHolder;
    private SensorManager mSensorManager;
    private ThreadJogo mThreadJogo;
    private Bolinha mBolinha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "bacon1", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSurfuceView = new SurfaceView(this);
        mSurfuceView.setKeepScreenOn(true);
        mHolder = mSurfuceView.getHolder();
        mHolder.addCallback(this);
        setContentView(mSurfuceView);
        mBolinha = new Bolinha(this);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mBolinha.setTamanhoDeTela(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThreadJogo = new ThreadJogo(this, mBolinha, holder);
        mThreadJogo.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            mBolinha.setTamanhoDeTela(0, 0);
            mThreadJogo.parar();
        } finally {
            mThreadJogo = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mBolinha.setAceleracao(this, event.values[0], event.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
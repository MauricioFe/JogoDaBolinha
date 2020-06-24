package com.example.jogodabolinha.Task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.example.jogodabolinha.Bolinha;
import com.example.jogodabolinha.R;

import java.util.concurrent.TimeUnit;

public class ThreadJogo extends Thread {
    private boolean mExecutando;
    private Bolinha mBolinha;
    private SurfaceHolder mHolder;
    private Bitmap mBackground;

    public ThreadJogo(Context context, Bolinha bolinha, SurfaceHolder holder) {
        mBolinha = bolinha;
        mHolder = holder;
        mBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
    }

    @Override
    public void run() {
        mExecutando = true;
        while (mExecutando) {
            try {
                mBolinha.calcularFisica();
                pintar();
                TimeUnit.MILLISECONDS.sleep(16);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    private void pintar() {
        Canvas canvas = null;
        try {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                Rect rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.drawBitmap(mBackground, null, rect, null);
                canvas.drawBitmap(mBolinha.getmImgBolinha(), mBolinha.getX(), mBolinha.getY(), null);
            }
        } finally {
            if (canvas != null) mHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void para() {
        mExecutando = false;
        interrupt();
    }
}

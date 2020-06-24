package com.example.jogodabolinha.Task;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.jogodabolinha.Bolinha;
import com.example.jogodabolinha.R;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/*Criada para realizar a renderizaçã do que será exibido na tela. O Atributo mEcexutando que determina se a Thread será executada*/
public class ThreadJogo extends Thread {
    private boolean mExecutando;
    /*Possui mais três atributos. Uma instancia da classe bolinha. o Segundo é uma instancia da classe SurfaceHolder, que trabalha
    * em conjunto a classe SurfaceView. O SurfaceHolder obtem o objeto canvas para desenha o conteúdo na surfaceView
    *
    * SurfaceView é Fornece uma superfície de desenho dedicada incorporada dentro de uma hierarquia de views. Você pode controlar o
    * formato dessa superfície e, se desejar, seu tamanho; o SurfaceView cuida de colocar a superfície no local correto na tela*/
    private Bolinha mBolinha;
    private SurfaceHolder mHolder;
    private Bitmap mBackground;
    /*Inicialização dos atributos*/
    public ThreadJogo(Context context, Bolinha bolinha, SurfaceHolder holder) {
        mBolinha = bolinha;
        mHolder = holder;
        Resources res = context.getResources();
        Drawable drawable = res.getDrawable(R.drawable.ic_grass);
        mBackground = Bolinha.drawableToBitmap(drawable);
    }
    /*No método run tem um laço controlado pelo mExecutando, e dentro dele calculamos a fisica da bolinha.
    * Em seguida, requisitamos que a tela seja pintada pelo método pintar.Por fim fazemos nossa Thread dormir por um tempo para
    * que ela possa ser desenhada*/
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
    /* Esse método obtem uma instancia do canvas por meio do método lockCanvas() da instancia SurfaceHolder. Depois pintamos
    * o background e finalmente pintamos a bolinha. O processo de pitura  termina quando o que foi pintado no canvas é enviado para
    * o SurfaceView por meio do método unlockCanvasAndPost()*/
    private void pintar() {
        Canvas canvas = null;
        try {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                Rect rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.drawBitmap(mBackground, null, rect, null);
                canvas.drawBitmap(mBolinha.getImgBolinha(), mBolinha.getX(), mBolinha.getY(), null);
            }
        } finally {
            if (canvas != null) mHolder.unlockCanvasAndPost(canvas);
        }
    }
/*Método interrompe a Thread*/
    public void parar() {
        mExecutando = false;
        interrupt();
    }
}

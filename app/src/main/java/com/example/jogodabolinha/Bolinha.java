package com.example.jogodabolinha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Bolinha {
    /*A constante REBOTE define o percentual que será reduzido da velocidade quando ela atingir os limites da tela. Determinamos
    * que a velocidade será reduzida em 60%.*/
    private static final float REBOTE = 0.6f;
    /*Foi definido a velocidade mínima de 50%*/
    private static final float VELOCIDADE_MINIMA = 5f;
    /*Em pixels_por_metro, estipulamos um valor para a proporção de pixels para metros. Isso é necessário, uma vez que tanto os
    * valores do acelerômetro comno as formulas físicas utilizão a unidade de medida m/s²*/
    private static final float PIXELS_POR_METRO = 25;
    /*Atributos da bolinha*/
    private float mPositionX, mPositionY;
    private float mVelocidadeX, mVelocidadeY;
    private float mAceleracaoX, mAceleracaoY;
    private float mLarguraTela, mAlturaTela;
    /*Armazenará o tempo decorrido a ultima utilização. Esse atributo será utilizado para realizar o cálculo da distância percorrida.*/
    private long mTempoUltAtualização = 1;
    /*O atributo mImgBolinha define a imagem da bolinha*/
    private Bitmap mImgBolinha;

    /*inicializa a imagem da bola*/
    public Bolinha(Context context) {
        mImgBolinha = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
    }
    /*Os três métodos get retornam os valores dos atributos da classe bolinha*/
    public float getX() {
        return mPositionX;
    }

    public float getY() {
        return mPositionY;
    }

    public Bitmap getmImgBolinha() {
        return mImgBolinha;
    }

    /*SetAceleração será chamado pela activity da aplicação e receberá a aceleração informada pelo acelerômetro*/
    public void getAceleracao(float x, float y) {
        mAceleracaoX = -x;
        mAceleracaoY = y;
    }
    /*Este método definirá o tamanho da tela para sabermos os limites onde a bolinha poderá andar. Com ele é possivel detectar
    * a colisão da bolinha com as extremidades do visor*/
    public void tamanhoDeTela(int w, int h) {
        mLarguraTela = w;
        mAlturaTela = h;
    }
    /*É o método mais importante do projeto. É nele que serão aplicados conceitos de física para estabelecer a posição e a
    * movimentação da bolinha. Para isso é importante salientar as unidade de medida utilizadas nessa fórmulas:
    * a distância é definida em metros; a velocidade em metros por segundo; e o tempo é definido em segundos. Por isso
    * serão necessários algumas conversões durante o calculo.*/
    public void calcularFisica() {
        if (mLarguraTela <= 0 || mAlturaTela <= 0) return;
        long tempoAtual = System.currentTimeMillis();
        if (mTempoUltAtualização < 0) {
            mTempoUltAtualização = tempoAtual;
            return;
        }
        /*Inicio o cálculo dabendo quanto tempo se passou desde a ultima atualização da posição da bolinha. Armazenei essa informação
        * dentro da variaável tempo decorrido. após isso é feito o calculo da velocidade baseado na fórmula:
        * velocidade = velocidade inicial + aceleração * tempo*/
        long tempoDecorrido = tempoAtual - mTempoUltAtualização;
        mTempoUltAtualização = tempoAtual;

        /*É preciso fazer isso nos eixos X e Y, esse calculo é feito nos atributos velocidadeX e velocidadeY.
        *
        * Nesse calculo é efetuado uma divisão por 1000, uma vez que a unidade de medida deve estar em segundos(1s = 1000ms)
        * Após isso é multiplicado pela constante PIXELS_POR_METRO para obtermos a unidade de medida que devemos usar na tela
        * que é em pixels. Nos atributos positionsX e positionY calculamos a posição de x e y utilizando a fórmula:
        * distância = velocidade * tempo*/
        mVelocidadeX += ((mAceleracaoX * tempoDecorrido) / 1000) * PIXELS_POR_METRO;
        mVelocidadeY += ((mAceleracaoY * tempoDecorrido) / 1000) * PIXELS_POR_METRO;
        mAceleracaoX += ((mVelocidadeX * tempoDecorrido) / 1000) * PIXELS_POR_METRO;
        mAceleracaoY += ((mVelocidadeY * tempoDecorrido) / 1000) * PIXELS_POR_METRO;

        boolean rebateuEmX = false;
        boolean rebateuEmY = false;
        /*Após os calculo, é iniciada a checagem de colisão com as bordas da tela, começando a checagem com a parte superior
        * Caso mPosiçãoY seja menor que zero a vleocidade é invertidad e é descontado do rebote, que estipulamos em 60% da velocidade
        * atual. Tambem setamos o rebateuEmY para true.
        * */

        if (mPositionY < 0) {
            mPositionY = 0;
            mVelocidadeY = -mVelocidadeY * REBOTE;
            rebateuEmY = true;

            /*É conferido também a borda inferior da tela, caso isso aconteça, é feito uma logica similar ao de cima*/
        } else if (mPositionY + mImgBolinha.getHeight() > mAlturaTela) {
            mPositionY = mAlturaTela - mImgBolinha.getHeight();
            mVelocidadeY = -mVelocidadeY * REBOTE;
            rebateuEmY = true;
        }

        if (rebateuEmY && Math.abs(mVelocidadeY) < VELOCIDADE_MINIMA) {
            mVelocidadeY = 0;
        }
        /*Lógica de colisão nas laterais é a mesmo lógica explicada acima*/
        if (mPositionX < 0) {
            mPositionX = 0;
            mVelocidadeX = -mVelocidadeX * REBOTE;
            rebateuEmX = true;
        } else if (mPositionX + mImgBolinha.getHeight() > mAlturaTela) {
            mPositionX = mAlturaTela - mImgBolinha.getHeight();
            mVelocidadeX = -mVelocidadeX * REBOTE;
            rebateuEmX = true;
        }

        if (rebateuEmX && Math.abs(mVelocidadeX) < VELOCIDADE_MINIMA) {
            mVelocidadeX = 0;
        }

    }

}

package com.example.joc1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import java.util.Random;

public class Game {
    private Context context;

    //------------Marges i Llargàries-----------------------
    private int screenWidth;
    private int screenHeight;
    private int margeSuperior;
    private int margeInferior;
    private double margeQuadratBot;
    private double margeQuadratTop;
    private int taulaWidth;


    //---------------Puntuació-------------------------
    private int score;
    private int maxScore;
    private int girsDisponibles;
    private int contadorImatgeNewRecord=0;
    private int contadorImatgeParpadeig=0;

    //Enter que ens defineix si s'executa o no l'easter egg
    private int easterEgg;


    //-------------Objectes de dibuix------------------
    private Canvas canvas;
    private Paint paint;
    private Point halfScreenDimension;
    private Point messagePosition;
    private Rect quadreTauler, quadrePunts, quadreSo, quadreAjuda, quadreNovaPartida, quadreGirs, quadreFons, quadreFigura1, quadreFigura2, quadreFigura3,menuOption,siOption,noOption;
    private Image imatgeSo,imatgeSo2, imatgeAjuda,imatgeAjuda2, imatgeNovaPartida,imatgeNovaPartida2, imatgeGirs, imatgeNoSo,imatgeNoSo2,textControls, imatgeHighScore, imatgeWasted;

    //------------Ints pel centrat dels pentominos--------
    private int longitudFigures; //Guardam el llarg dels cosats dels cuadrats dels pentominos dins els 3 quadrats inferiors
    private int [] puntIniciX = new int [3]; //Guardam els inicis on s-han de comencar a dibuixar els pentominos centrats
    private int [] puntIniciY = new int[3];
    private int [] girsFigures = new int[3];

    //--------Boleans per a decisió de entrades a seccio critiques del codi, semafors, etc-------
    private boolean [] pitjaFigures = new boolean [3];
    private boolean [] pitjaMenu = new boolean[3];
    private boolean [] arrosegaFigures = new boolean [3];
    private boolean [] pintaEnMoviment = new boolean [3];
    //Indica si les figures 1,2 i 3 s'han de pintar (no han estat col·locades) o no (ja s'han col·locat).
    private boolean [] estatFigures = new boolean [3]; 
    //Boleans necesaris 
    private boolean sonido,pitjaGirs=false, seAcabo=false,
                    ajudaSolicitada=false, menuConfirmacio=false,
                    nouRecord=false, jocNou = true,
                    pitjaSi, pitjaNo;
    


    //---------Informació Pentomino de ajuda------------
    private int numeroPentominoSugerit;
    private int girsPentominoSugerit;
    private int girsSugeritsFets;
    private int filaPentominoSugerit;
    private int columnaPentominoSugerit;
    private int contadorAparicioPentomino, contadorParpadeig;


    //---------------MULTIMEDIA-------------------------
    static float volum = (float) 0.05;
    static final int SoButton = R.raw.boton;
    static final int SoPlacePento = R.raw.placepento;
    static final int SoRowDestroy = R.raw.rowdestroy;
    static final int BgMusic = R.raw.bg_music;
    static final int soError = R.raw.error;
    static final int soGameOver = R.raw.gameover;
    private static SoundPool soundPool, sPRowDestroy;
    int idBoton, idPlacePento, idRowDestoy,idItsLit,idSoError, idSoGameOver;
    MediaPlayer mp;

    //--------------------------------------------------
    private int lastMessagePositionX;
    private int lastMessagePositionY;


    //Inicializam els 12 diferents pentominos
    private Pentomino[]  pentominoes = new Pentomino[12];
    
    //Inicialitzam una array 3 per els random;
    private Pentomino[] pentominosRandom = new Pentomino[3];
    
    //Definim la forma de cada un dels pentominos, 0 indica buit 1 indica ocupat
    private int pentF [][] = {{0,0,0,0,0},{0,0,1,1,0},{0,1,1,0,0},{0,0,1,0,0},{0,0,0,0,0}};
    private int pentI [][] = {{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0}};
    private int pentL [][] = {{0,0,1,0,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,1,1,0},{0,0,0,0,0}};
    private int pentN [][] = {{0,0,0,1,0},{0,0,1,1,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,0,0,0}};
    private int pentP [][] = {{0,0,0,0,0},{0,0,0,1,0},{0,0,1,1,0},{0,0,1,1,0},{0,0,0,0,0}};
    private int pentT [][] = {{0,0,0,0,0},{0,1,1,1,0},{0,0,1,0,0},{0,0,1,0,0},{0,0,0,0,0}};
    private int pentU [][] = {{0,0,0,0,0},{0,1,0,1,0},{0,1,1,1,0},{0,0,0,0,0},{0,0,0,0,0}};
    private int pentV [][] = {{0,0,0,0,0},{0,1,0,0,0},{0,1,0,0,0},{0,1,1,1,0},{0,0,0,0,0}};
    private int pentW [][] = {{0,0,0,0,0},{0,1,0,0,0},{0,1,1,0,0},{0,0,1,1,0},{0,0,0,0,0}};
    private int pentX [][] = {{0,0,0,0,0},{0,0,1,0,0},{0,1,1,1,0},{0,0,1,0,0},{0,0,0,0,0}};
    private int pentY [][] = {{0,0,0,1,0},{0,0,1,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,0,0}};
    private int pentZ [][] = {{0,0,0,0,0},{0,1,1,0,0},{0,0,1,0,0},{0,0,1,1,0},{0,0,0,0,0}};
    
    //Defineix si la posicion [x][y] del tauler esta o no ocupada
    private int tablero [][]  = {{0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0}};

    //Defineix el color de cada quadrat del tauler
    private int tableroColor [][]  = {{0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0},
                                {0,0,0,0,0,0,0,0,0,0}};

    public static long fotogrames=0;
    
    //Constructor de la clase Game on instanciam les variables i estructures de dades que farem servir
    //al llarg de la execució del programa
    
    public Game(Context context, MainView m, int screenWidth, int screenHeight) {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        //Donam valors als diferents marges horitzontals que utilitzarem per col·locar els rectangles
        margeSuperior = (screenHeight - screenWidth)/3;
        margeInferior = margeSuperior+screenWidth;
        margeQuadratTop = (margeSuperior-screenWidth*0.125)/2;
        margeQuadratBot = (screenHeight-screenWidth*0.25);

        //Donam valor a altres distancies que reutilitzarem
        taulaWidth = screenWidth/10;
        longitudFigures = screenWidth/25;

        soundPool = new SoundPool.Builder().build();
        sPRowDestroy = new SoundPool.Builder().build();
        idPlacePento = soundPool.load(context,R.raw.placepento,1);
        idBoton = soundPool.load(context,R.raw.boton,1);
        idRowDestoy = sPRowDestroy.load(context,R.raw.rowdestroy,1);
        idSoError = soundPool.load(context,R.raw.error,1);
        idSoGameOver = soundPool.load(context,R.raw.gameover,1);

        mp = MediaPlayer.create(context,BgMusic);


        // Inicialitzam els punts que utilizarem
        halfScreenDimension = new Point(screenWidth/2, screenHeight/2);
        messagePosition = new Point(-1,-1);

        // Valors que utilitzam per a la inicialització dels rectangles
        double altMenu = screenHeight*0.2;

        // Inicialitzam els rectangles
        quadreTauler = new Rect (0,margeSuperior,screenWidth,margeInferior);
        quadrePunts = new Rect ( (int) (screenWidth*0.02),(int) (margeSuperior*0.1),(int)(screenWidth*0.49),(int)(margeSuperior*0.9));
        quadreSo = new Rect ( (int) (screenWidth*0.53125),(int) (margeQuadratTop),(int)(screenWidth*0.65625),(int)(margeSuperior-margeQuadratTop));
        quadreAjuda = new Rect ( (int) (screenWidth*0.6875),(int) (margeQuadratTop),(int)(screenWidth*0.8125),(int)(margeSuperior-margeQuadratTop));
        quadreNovaPartida = new Rect ( (int) (screenWidth*0.84375),(int) (margeQuadratTop),(int)(screenWidth*0.96875),(int)(margeSuperior-margeQuadratTop));
        quadreGirs = new Rect ( (int) (screenWidth*0.42),(int) (margeInferior+screenHeight*0.01),(int)(screenWidth*0.58),(int)(margeInferior+screenHeight*0.01+screenWidth*0.16));
        quadreFons = new Rect (0,0,screenWidth,screenHeight);
        quadreFigura1 = new Rect ( (int) (screenWidth*0.01),(int) (margeQuadratBot),(int)(screenWidth*0.26),(int)(screenHeight));
        quadreFigura2 = new Rect ( (int) (screenWidth*0.375),(int) (margeQuadratBot),(int)(screenWidth*0.625),(int)(screenHeight));
        quadreFigura3 = new Rect ( (int) (screenWidth*0.74),(int) (margeQuadratBot),(int)(screenWidth*0.99),(int)(screenHeight));

        menuOption = new Rect(screenWidth/20,(int)(((screenHeight/2)-(altMenu/2))),screenWidth-screenWidth/20,(int)((screenHeight/2)+(altMenu/2)));
        siOption = new Rect(screenWidth/20,(int)(menuOption.bottom-(menuOption.height()*(0.33))),screenWidth/2,menuOption.bottom);
        noOption = new Rect(screenWidth/2,(int)(menuOption.bottom-(menuOption.height()*(0.33))),screenWidth-screenWidth/20,menuOption.bottom);

        //Inicialitzam les imatges
        imatgeSo = new Image (m,R.drawable.volum);
        imatgeSo2 = new Image (m,R.drawable.volum2);
        imatgeNoSo = new Image (m,R.drawable.novolum);
        imatgeNoSo2 = new Image (m,R.drawable.novolum2);
        imatgeAjuda = new Image (m,R.drawable.ajuda);
        imatgeNovaPartida = new Image (m,R.drawable.play);
        imatgeAjuda2 = new Image (m,R.drawable.ajuda2);
        imatgeNovaPartida2 = new Image (m,R.drawable.play2);
        imatgeGirs = new Image (m,R.drawable.rotar);
        textControls = new Image(m,R.drawable.infotext);
        imatgeHighScore = new Image (m,R.drawable.highscore);
        imatgeWasted = new Image (m,R.drawable.gameover);

        int[] llistaColors =
                {Color.argb(255,255,153,0),
                Color.argb(255,255,51,0),
                Color.argb(255,204,0,102),
                Color.argb(255,204,0,204),
                Color.argb(255,51,200,255),
                Color.argb(255,0,102,204),
                Color.argb(255,0,153,153),
                Color.argb(255,0,204,102),
                Color.argb(255,0,153,51),
                Color.argb(255,102,153,0),
                Color.argb(255,204,204,0),
                Color.argb(255,255,255,102)
        };

        //Inicialitzam els pentominos
        pentominoes[0] = new Pentomino(4,pentF,llistaColors[0],0);
        pentominoes[1] = new Pentomino(4,pentI,llistaColors[1],1);
        pentominoes[2] = new Pentomino(4,pentL,llistaColors[2],2);
        pentominoes[3] = new Pentomino(4,pentN,llistaColors[3],3);
        pentominoes[4] = new Pentomino(4,pentP,llistaColors[4],4);
        pentominoes[5] = new Pentomino(4,pentT,llistaColors[5],5);
        pentominoes[6] = new Pentomino(4,pentU,llistaColors[6],6);
        pentominoes[7] = new Pentomino(4,pentV,llistaColors[7],7);
        pentominoes[8] = new Pentomino(4,pentW,llistaColors[8],8);
        pentominoes[9] = new Pentomino(4,pentX,llistaColors[9],9);
        pentominoes[10] = new Pentomino(4,pentY,llistaColors[10],10);
        pentominoes[11] = new Pentomino(4,pentZ,llistaColors[11],11);

        generarPentominosRandom();

        //Inicialitzam putuació a 0 y els girs disponbiles
        score=0;
        girsDisponibles = 3;


        playBgMusic();
    }
    
    //Metode que dibuixa damunt del canvas tots els elements que veim per pantalla

    public void draw(SurfaceHolder holder) {
        // Only drawing primitives !!!


        if (holder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = holder.lockCanvas();

            //draw a background color for the full screen

            canvas.drawColor(Color.LTGRAY);

            // Unlock and draw the scene
            paint = new Paint();
            paint.setStrokeWidth(1);
            //Dibuixam el fons de la aplicació
            paint.setColor(Color.LTGRAY);
            canvas.drawRect(quadreFons,paint);


            // Dibuixam el quadrat blanc del tauler
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(quadreTauler,paint);

            //Dibuixam el quadrat de la puntuació

            paint.setColor(Color.TRANSPARENT);

            //Dibuixam el quadrat de girs disponibles
            canvas.drawRect(quadreGirs,paint);

            canvas.drawRect(quadreSo,paint);
            canvas.drawRect(quadreSo,paint);
            canvas.drawRect(quadreAjuda,paint);
            canvas.drawRect(quadreNovaPartida,paint);

            for (int i = 0; i < 2; i++) {
                if (i == 0 ){
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                }else{
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                }

                canvas.drawRect(quadrePunts,paint);
                canvas.drawRect(quadreFigura1,paint);
                canvas.drawRect(quadreFigura2,paint);
                canvas.drawRect(quadreFigura3,paint);

            }
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            //Dibuixam els quadrats de les figures



            //Dibuixam les linies del tauler
            paint.setColor(Color.BLACK);

            //Dibuixam els cuadres del tauler ocupats
            for(int i=0;i<tablero.length;i++){
                for(int k=0; k<tablero.length;k++){
                    if (!(tablero[i][k]==0)) {
                        int colorOpac = tableroColor[i][k];
                        int colorNou;
                        if(tablero[i][k]>1){
                            colorNou = Color.argb(256-tablero[i][k]*4,Color.red(colorOpac),Color.green(colorOpac),Color.blue(colorOpac));
                            if(tablero[i][k]>63){
                                tablero[i][k]=0;
                            }else{
                                tablero[i][k]++;
                            }
                        }else{
                            colorNou = colorOpac;
                        }
                        paint.setColor(colorNou);
                        canvas.drawRect(taulaWidth*k,margeSuperior+taulaWidth*i,taulaWidth*(k+1),margeSuperior+taulaWidth*(i+1),paint);
                    }
                }
            }
            paint.setColor(Color.BLACK);
            for(int k=0;k<11;k ++) {
                canvas.drawLine((int) (taulaWidth * k), margeSuperior, (int) (taulaWidth * k), margeInferior, paint);
                canvas.drawLine(0, (int) (margeSuperior + taulaWidth * k), screenWidth, (int) (margeSuperior + taulaWidth * k), paint);
            }

            //Dibuixam la linia del quadrat de puntuació
            canvas.drawRect(quadrePunts.left,(quadrePunts.bottom-quadrePunts.top)/2+quadrePunts.top,quadrePunts.right,(quadrePunts.bottom-quadrePunts.top)/2+quadrePunts.top,paint);

            //Dibuixam el Text del quadrat de puntuació
            paint.setTextSize(75);
            canvas.drawText("Best: "+String.valueOf(maxScore),(float)(quadrePunts.left),((quadrePunts.bottom-quadrePunts.top)/2),paint);
            canvas.drawText("Score: "+String.valueOf(score),quadrePunts.left,(float)(quadrePunts.bottom*0.90),paint);


            //Dibuixam els girs disponibles
            imatgeGirs.draw(canvas,quadreGirs.left,quadreGirs.top,quadreGirs.right-quadreGirs.left,quadreGirs.bottom-quadreGirs.top);
            paint.setTextSize(50);
            canvas.drawText(String.valueOf(girsDisponibles),(int)(quadreGirs.left+(quadreGirs.right-quadreGirs.left)/2.5),(int)(quadreGirs.top+(quadreGirs.bottom-quadreGirs.top)/1.75),paint);


            //Dibuixam els 3 pentominos Random (si fa falta)
            for(int i=0; i<3;i++){
                if(estatFigures[i]){
                    pentominosRandom[i].pintarNormal(canvas,puntIniciX[i],puntIniciY[i],longitudFigures,girsFigures[i]);
                }
            }

            //Dibuixam el pentomino de ajuda
            if(ajudaSolicitada){
                contadorAparicioPentomino++; //La imatge es pintara fins que el contador sigui 150 (5 segons)
                contadorParpadeig++;         //Abans que el contador arribi a 15 la imatge es pinta, després ja no es pinta, a 30 es reinicia
                if (contadorParpadeig<15){
                    pentominosRandom[numeroPentominoSugerit].pintarSugerit(canvas,filaPentominoSugerit,columnaPentominoSugerit,taulaWidth,girsPentominoSugerit,margeSuperior);
                }
                if (contadorParpadeig==30){
                    contadorParpadeig=0;
                }
                if(contadorAparicioPentomino==150){
                    ajudaSolicitada=false;
                }
            }
            //Dibuixam Imatges del Menu
            if(pitjaMenu[0]){
                if (sonido) {
                    imatgeSo2.draw(canvas, quadreSo.left, quadreSo.top, quadreSo.right - quadreSo.left, quadreSo.bottom - quadreSo.top);
                } else {
                    imatgeNoSo2.draw(canvas, quadreSo.left, quadreSo.top, quadreSo.right - quadreSo.left, quadreSo.bottom - quadreSo.top);
                }

            }else {
                if (sonido) {
                    imatgeSo.draw(canvas, quadreSo.left, quadreSo.top, quadreSo.right - quadreSo.left, quadreSo.bottom - quadreSo.top);
                } else {
                    imatgeNoSo.draw(canvas, quadreSo.left, quadreSo.top, quadreSo.right - quadreSo.left, quadreSo.bottom - quadreSo.top);
                }
            }
            if(pitjaMenu[1]){
                imatgeAjuda2.draw(canvas,quadreAjuda.left,quadreAjuda.top,quadreAjuda.right-quadreAjuda.left,quadreAjuda.bottom-quadreAjuda.top);
            }else{
                imatgeAjuda.draw(canvas,quadreAjuda.left,quadreAjuda.top,quadreAjuda.right-quadreAjuda.left,quadreAjuda.bottom-quadreAjuda.top);
            }


            //Dibuixam text de Game Over
            if (seAcabo) {
                paint.setColor(Color.argb(175,237,12,0));
                canvas.drawRect(quadreFons, paint);
                imatgeWasted.draw(canvas,0,quadreTauler.top,screenWidth,quadreTauler.bottom);
            }

            //Dibuixam icono nova partida
            if(pitjaMenu[2]){
                imatgeNovaPartida2.draw(canvas,quadreNovaPartida.left,quadreNovaPartida.top,quadreNovaPartida.right-quadreNovaPartida.left,quadreNovaPartida.bottom-quadreNovaPartida.top);
            }else{
                imatgeNovaPartida.draw(canvas,quadreNovaPartida.left,quadreNovaPartida.top,quadreNovaPartida.right-quadreNovaPartida.left,quadreNovaPartida.bottom-quadreNovaPartida.top);
            }

            //Dibuixam controls.
            if(jocNou){
                textControls.draw(canvas,quadreFons.left,quadreFons.top,quadreFons.right,quadreFons.bottom);
            }

            //DIbuixam menu confirmació
            if(menuConfirmacio){
                paint.setColor(Color.argb(200,204,204,200));
                canvas.drawRect(quadreFons, paint);
                for (int i = 0; i < 2; i++) {
                    if (i == 0 ){
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                    }else{
                        paint.setColor(Color.BLACK);
                        paint.setStyle(Paint.Style.STROKE);
                    }

                    canvas.drawRect(menuOption,paint);
                    canvas.drawRect(siOption,paint);
                    canvas.drawRect(noOption,paint);

                }
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setColor(Color.BLACK);
                paint.setTextSize(75);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Yes", siOption.centerX(), (int)(siOption.centerY()+siOption.height()*0.2), paint);
                canvas.drawText("No", noOption.centerX(), (int)(noOption.centerY()+noOption.height()*0.2), paint);
                paint.setTextSize(50);
                canvas.drawText("Are you sure you want to restart the game?", menuOption.centerX(), (int)(menuOption.centerY()-menuOption.height()*0.1), paint);
            }
            //Dibuixam High Score
            if(nouRecord){
                if(contadorImatgeNewRecord<150){
                    contadorImatgeNewRecord++;
                    contadorImatgeParpadeig++;
                    if(contadorImatgeParpadeig<15){
                        imatgeHighScore.draw(canvas,0,quadreTauler.top,screenWidth,quadreTauler.bottom);
                    }
                    if (contadorImatgeParpadeig>30){
                        contadorImatgeParpadeig=0;
                    }
                }
            }

            //Dibuixam pentominos en moviment
            for(int i=0; i<3; i++){
                if(pintaEnMoviment[i]){
                    pentominosRandom[i].pintarNormal(canvas,lastMessagePositionX,lastMessagePositionY,taulaWidth,girsFigures[i]);
                }
            }

            //Dibuixam fotogrames
            paint.setColor(Color.BLACK);
            paint.setTextSize(75);
            //canvas.drawText("FPS: "+fotogrames,0,margeInferior+80,paint);

            // End painting
            holder.unlockCanvasAndPost(canvas);
        }
    }
    
    /*
    Metode per a gestionar quan pitjam els botons o els recuadres on guardam els pentominos sugerits
     */

    public void handleActionDown(int x, int y) {
        messagePosition.x = x;
        messagePosition.y = y;
        if(quadreFons.contains(x,y)){
            jocNou=false;
        }
        if (quadreFigura1.contains(x,y)&&estatFigures[0]&&(!menuConfirmacio)&&(!seAcabo))
        {
            pitjaFigures[0]=true;
        }
        else if (quadreFigura2.contains(x,y)&&estatFigures[1]&&(!menuConfirmacio)&&(!seAcabo))
        {
            pitjaFigures[1]=true;
        }
        else if (quadreFigura3.contains(x,y)&&estatFigures[2]&&(!menuConfirmacio)&&(!seAcabo))
        {
            pitjaFigures[2]=true;
        }
        else if (quadreSo.contains(x,y)&&(!menuConfirmacio)&&(!seAcabo)){
            pitjaMenu[0]=true;
        }
        else if (quadreAjuda.contains(x,y)&&(!menuConfirmacio)&&(!ajudaSolicitada)&&(!seAcabo)){
            pitjaMenu[1]=true;
        }
        else if (quadreNovaPartida.contains(x,y)&&(!menuConfirmacio)){
            pitjaMenu[2]=true;
        }
        else if (quadreGirs.contains(x,y)&&(!menuConfirmacio)&&(!seAcabo)){
            pitjaGirs=true;
        }
        else if(siOption.contains(x,y)){
            pitjaSi=true;
        }
        else if(noOption.contains(x,y)){
            pitjaNo=true;
        }


    }
    
    /*
    Metode per a gestionar quan despitjam els botons o els recuadres on guardam els pentominos sugerits
     */
    
    public void handleActionUp(int x, int y) {
        messagePosition.x = -1;
        messagePosition.y = -1;

        //Que pasa si despitjam del quadre dels pentominos pero no hem sortit del quadre (es gira)
        if (quadreFigura1.contains(x,y)&&pitjaFigures[0])
        {
                if (girsDisponibles>0) {
                    girsFigures[0] = (girsFigures[0] + 1) % 4;
                    girsDisponibles--;
                    //Calculam els origens pel seu centrat dintre de les caselles inferiors
                    puntIniciX[0] = quadreFigura1.left + ((quadreFigura1.width()-pentominosRandom[0].getLlarg(girsFigures[0])*longitudFigures)/2);
                    puntIniciY[0] = quadreFigura1.top + ((quadreFigura1.height()-pentominosRandom[0].getAlt(girsFigures[0])*longitudFigures)/2);
                    if(comprovarSiEsPotSeguirJugant()){
                        seAcabo = false;
                    } else{
                        playSound(idSoGameOver);
                        mp.pause();
                        seAcabo = true;
                    }

                }
                pitjaFigures[0] = false;
        }
        if (quadreFigura2.contains(x,y)&&pitjaFigures[1]) {
            if (girsDisponibles>0) {
                girsFigures[1] = (girsFigures[1] + 1) % 4;
                girsDisponibles--;
                //Calculam els origens pel seu centrat dintre de les caselles inferiors
                puntIniciX[1] = quadreFigura2.left + ((quadreFigura2.width()-pentominosRandom[1].getLlarg(girsFigures[1])*longitudFigures)/2);
                puntIniciY[1] = quadreFigura2.top + ((quadreFigura2.height()-pentominosRandom[1].getAlt(girsFigures[1])*longitudFigures)/2);
                if(comprovarSiEsPotSeguirJugant()){
                    seAcabo = false;
                } else{
                    playSound(idSoGameOver);
                    mp.pause();
                    seAcabo = true;
                }
            }
            pitjaFigures[1] = false;
        }

        if (quadreFigura3.contains(x,y)&&pitjaFigures[2]) {
            if (girsDisponibles>0) {
                girsFigures[2] = (girsFigures[2] + 1) % 4;
                girsDisponibles--;
                //Calculam els origens pel seu centrat dintre de les caselles inferiors
                puntIniciX[2] = quadreFigura3.left + ((quadreFigura3.width()-pentominosRandom[2].getLlarg(girsFigures[2])*longitudFigures)/2);
                puntIniciY[2] = quadreFigura3.top + ((quadreFigura3.height()-pentominosRandom[2].getAlt(girsFigures[2])*longitudFigures)/2);
                if(comprovarSiEsPotSeguirJugant()){
                    seAcabo = false;
                } else{
                    playSound(idSoGameOver);
                    mp.pause();
                    seAcabo = true;
                }
            }
            pitjaFigures[2] = false;
        }
        //Que pasa si despitjam del quadre pero hem sortit del quadre (es pinta o no es fa res si 
        // la posició es incorreccte)
        
        for (int i = 0; i<3;i++){
            if (arrosegaFigures[i])
            {
                arrosegaFigures[i]=false;
                pintaEnMoviment[i]=false;
                int [] filCol = filaColumna(lastMessagePositionX,lastMessagePositionY);
                if(comprovarSiPentEntraTauler(pentominosRandom[i],girsFigures[i],filCol[0],filCol[1])){
                    ficarATaula(pentominosRandom[i],girsFigures[i],filCol[0],filCol[1]);
                    estatFigures[i]=false;
                    score = score +5;
                    comprovarLinies(true);
                    if (score>=maxScore){
                        maxScore = score;
                        nouRecord = true;
                    }
                    if (!estatFigures[0]&&!estatFigures[1]&&!estatFigures[2]){
                        generarPentominosRandom();
                    }
                    playSound(idPlacePento);
                    if(comprovarSiEsPotSeguirJugant()){
                        seAcabo = false;
                    } else{
                        playSound(idSoGameOver);
                        mp.pause();
                        seAcabo = true;
                    }
                    //eliminam la solicitud de ajuda si s'esta realitzant una ajuda
                    if(ajudaSolicitada){
                        ajudaSolicitada = false;
                        contadorAparicioPentomino = 0;
                        contadorParpadeig=0;
                    }

                }else{
                    System.out.println(lastMessagePositionY);
                    System.out.println(margeInferior);
                    if(lastMessagePositionY<margeInferior){
                        playSound(idSoError);
                    }

                }
            }
        }
        //Que passa si despitjam els quadrats dels menus havent-hi pijat abans

        if (quadreSo.contains(x,y)){
            if(pitjaMenu[0]) {
                playSound(idBoton);
                pitjaMenu[0] = false;
                if (sonido) {
                    sonido = false;
                    mp.pause();
                } else {
                    sonido = true;
                    playBgMusic();
                }
            }
        }
        if (quadreAjuda.contains(x,y)){
            if(pitjaMenu[1]) {
                playSound(idBoton);
                pentominoSugerit();
                pitjaMenu[1] = false;
            }
        }
        if (quadreNovaPartida.contains(x,y)){
            if(pitjaMenu[2]) {
                playSound(idBoton);
                pitjaMenu[2] = false;
                girsDisponibles = 3;
                if(seAcabo){
                    reiniciarGame();
                    menuConfirmacio=false;
                }else{
                    menuConfirmacio=true;
                }
            }
        }
        if (siOption.contains(x,y)&&menuConfirmacio&&pitjaSi){
            playSound(idBoton);
            reiniciarGame();
            menuConfirmacio=false;
        }
        if (noOption.contains(x,y)&&menuConfirmacio&&pitjaNo){
            playSound(idBoton);
            menuConfirmacio=false;
        }
        if(quadreGirs.contains(x,y)){
            if(pitjaGirs){
                easterEgg++;
                if(easterEgg==5){
                    jugarAutomatic();
                    easterEgg=0;
                }
                pitjaGirs = false;
            }
        }else{
            easterEgg=0;
        }
    }
    
    /*
    Metode per a gestionar les accions d'arrosagament de pentominos
     */

    public void handleActionMove(int x, int y) {
        messagePosition.x = x;
        messagePosition.y = y;
        for(int i=0; i<3;i++){
            if(arrosegaFigures[i]){
                lastMessagePositionX=x-(pentominosRandom[i].getLlarg(girsFigures[i])/2)*taulaWidth-screenWidth/20;
                lastMessagePositionY=y-(pentominosRandom[i].getAlt(girsFigures[i]))*taulaWidth-taulaWidth;
                pintaEnMoviment[i]=true;
            }
        }
        //Si sortim del quadre de la figura amb el ratoli pitjat, passam a arrastrar.
        if (!quadreFigura1.contains(x,y)&&pitjaFigures[0]){
            pitjaFigures[0]=false;
            arrosegaFigures[0]=true;
        }
        if (!quadreFigura2.contains(x,y)&&pitjaFigures[1]){
            pitjaFigures[1]=false;
            arrosegaFigures[1]=true;
        }
        if (!quadreFigura3.contains(x,y)&&pitjaFigures[2]){
            pitjaFigures[2]=false;
            arrosegaFigures[2]=true;
        }
        if (!quadreSo.contains(x,y)&&pitjaMenu[0]) {
            pitjaMenu[0] = false;
        }
        if (!quadreAjuda.contains(x,y)&&pitjaMenu[1]) {
            pitjaMenu[1] = false;
        }
        if (!quadreNovaPartida.contains(x,y)&&pitjaMenu[2]) {
            pitjaMenu[2] = false;
        }
        if (!quadreGirs.contains(x,y)&&pitjaGirs) {
            pitjaGirs = false;
        }
    }

    /* Metode que comença a colocar pentominos fins que el propi metode detecta que s'ha acabat
    la partida
    */
    
    private void jugarAutomatic() {
        while(comprovarSiEsPotSeguirJugant()){
            pentominoSugerit();
            ficarATaula(pentominosRandom[numeroPentominoSugerit],girsPentominoSugerit,filaPentominoSugerit,columnaPentominoSugerit);
            girsDisponibles = girsDisponibles - girsSugeritsFets;
            comprovarLinies(true);
            estatFigures[numeroPentominoSugerit]=false;
            score = score +5;
            if (score>maxScore){
                maxScore=score;
                if (!nouRecord) {
                    contadorImatgeNewRecord=90; //El contador ja esta mes avançat perque no parpadeigi tant.
                    nouRecord = true;
                }
            }
            ajudaSolicitada=false;
            try {
                Thread.sleep(100);
            }
            catch(Exception e){}
            if((!estatFigures[0])&&(!estatFigures[1])&&(!estatFigures[2])){
                generarPentominosRandom();
            }
        }
        seAcabo=true;
        mp.pause();
        playSound(idSoGameOver);
    }

    /*
    Metode que donat dues coordenades, indica la fila i la taula a la que es corresponen aquestes
    coordenades.En cas de la coordenada Y sigui molt superior a la fila 0, es retornara fila i
    columna 9 per tal que el pentomino no entri
    */

    public int [] filaColumna (int origenX,int origenY) {
        int longitudQuadre = screenWidth / 10;
        int fila = 0, columna = 0;
        //Si l'origen X es molt menor al marge superior el pentomino no ha de entrar
        if(origenY<(margeSuperior-screenWidth/20)){
            fila = 9;
            columna = 9;
            return new int[]{fila,columna};
        }
        //Afegim una meitat de cuadre  l`origen perque hem de decidir la columna/fila en funcio del centre del cuadrat que colocam (no l-origen)
        int newOrigenX = origenX + screenWidth / 20;
        int newOrigenY = origenY + screenWidth / 20;
        for (int i = 0; i < 10; i++) {
            fila = margeSuperior + (i + 1) * longitudQuadre;
            if (newOrigenY < fila) {
                fila = i;
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            columna = (i + 1) * longitudQuadre;
            if (newOrigenX < columna) {
                columna = i;
                break;
            }
        }


        return new int[]{fila,columna};
    }

    /*
    Metode que retorna true, si el pentomino indicat per parámetres entra a la posició especificada,
    el metode retorna false al contrari
    */

    public boolean comprovarSiPentEntraTauler(Pentomino pentomino, int girs,int filaTaula,int columnaTaula) {

        int [][] formaPent = pentomino.getPentomino(girs);
        int [] coordenadesPent = pentomino.getCoordenades(girs);
        int alt = coordenadesPent[3]-coordenadesPent[1]+1; //Cuants de quadrats d'alt
        int llarg = coordenadesPent[2]-coordenadesPent[0]+1; //Cuants de quadrats de llarg

        boolean esPotFicar = true;
        //Recorrem el pentomino
        for (int i = 0; i<alt; i++){
            for (int k = 0; k<llarg; k++){
                if(filaTaula+i>9||columnaTaula+k>9){
                    esPotFicar = false;
                    break;
                }
                if (tablero[filaTaula+i][columnaTaula+k]==1&&formaPent[coordenadesPent[1]+i][coordenadesPent[0]+k]==1){
                    esPotFicar = false;
                }
            }
            if (!esPotFicar){
                break;
            }
        }
        return esPotFicar;
    }

    /*
    Metode per ficar el pentomino indicat per parametres dins la taula, filaTaula i columnaTaula
    corresponen al cuadrat més a la esquerra i adalt del pentomino.
     */

    public void ficarATaula(Pentomino pentomino, int girs,int filaTaula, int columnaTaula){

        int [][] formaPent = pentomino.getPentomino(girs);

        int [] coordenadesPent = pentomino.getCoordenades(girs);
        int alt = coordenadesPent[3]-coordenadesPent[1]+1; //Cuants de quadrats d'alt
        int llarg = coordenadesPent[2]-coordenadesPent[0]+1; //Cuants de quadrats de llarg

        for (int i = 0; i<alt; i++){
               for (int k = 0; k<llarg; k++){
                   if (formaPent[coordenadesPent[1]+i][coordenadesPent[0]+k]==1){
                       tablero[filaTaula+i][columnaTaula+k]=1;
                       tableroColor[filaTaula+i][columnaTaula+k]=pentomino.getColor();
                   }
               }
        }
    }

    //metode per llevar el pentomino indicat per parametres de la taula

    public void llevarDeTaula(Pentomino pentomino, int girs,int filaTaula, int columnaTaula){

        int [][] formaPent = pentomino.getPentomino(girs);

        int [] coordenadesPent = pentomino.getCoordenades(girs);
        int alt = coordenadesPent[3]-coordenadesPent[1]+1; //Cuants de quadrats d'alt
        int llarg = coordenadesPent[2]-coordenadesPent[0]+1; //Cuants de quadrats de llarg

        for (int i = 0; i<alt; i++){
            for (int k = 0; k<llarg; k++){
                if (formaPent[coordenadesPent[1]+i][coordenadesPent[0]+k]==1){
                    tablero[filaTaula+i][columnaTaula+k]=0;
                }
            }
        }
    }

    /*
    Funcio que en la seva execució substitueix els 3 pentominos aleatoris per 3 pentominos nous,
    a més, reinicia el seu estat a true, (no colocats)
    */

    public void generarPentominosRandom(){
        //Cream 3 randoms
        for (int i=0; i <=2; i++){
            Random random = new Random();
            int decisioPent = random.nextInt(12);
            pentominosRandom[i] = new Pentomino(4,pentominoes[decisioPent].getPentomino(0),pentominoes[decisioPent].getColor(),pentominoes[decisioPent].getIndex());

        }

        //Inicialitzam els 3 ints de girs amb un random.
        for (int i=0; i<=2; i++){
            Random random = new Random();
            girsFigures[i]= random.nextInt(3);
            pitjaFigures[i]=false;
            arrosegaFigures[i]=false;
        }

        //Calculam els origens pel seu centrat dintre de les caselles inferiors
        puntIniciX[0] = quadreFigura1.left + ((quadreFigura1.width()-pentominosRandom[0].getLlarg(girsFigures[0])*longitudFigures)/2);
        puntIniciY[0] = quadreFigura1.top + ((quadreFigura1.height()-pentominosRandom[0].getAlt(girsFigures[0])*longitudFigures)/2);

        puntIniciX[1] = quadreFigura2.left + ((quadreFigura2.width()-pentominosRandom[1].getLlarg(girsFigures[1])*longitudFigures)/2);
        puntIniciY[1] = quadreFigura2.top + ((quadreFigura2.height()-pentominosRandom[1].getAlt(girsFigures[1])*longitudFigures)/2);

        puntIniciX[2] = quadreFigura3.left + ((quadreFigura3.width()-pentominosRandom[2].getLlarg(girsFigures[2])*longitudFigures)/2);
        puntIniciY[2] = quadreFigura3.top + ((quadreFigura3.height()-pentominosRandom[2].getAlt(girsFigures[2])*longitudFigures)/2);

        //Inicialitzam estat dels pentominos a true (tots 3 estan fora col·locar)
        estatFigures[0] = true;
        estatFigures[1] = true;
        estatFigures[2] = true;

    }

    /*
    Metode que comprova les files i les columnes que estan completades en el tauler i retorna
    la puntuació obtinguda, en cas que el boolea sobrescriure sigui true, a mes de realitzar
    l'anterior, també buida les files i columnes i suma la puntuacio
    */

    public int comprovarLinies(boolean sobrescriure){
        boolean liniaTrobada = true;
        boolean [] filesTrobades = {false,false,false,false,false,false,false,false,false,false};
        int numFilesTrobades = 0;
        int numColumnesTrobades = 0;

        //Comprovam files
        for(int i=0;i<tablero.length;i++) {
            for (int k = 0; k < tablero.length; k++) {
                if (tablero[i][k] != 1) {
                    liniaTrobada = false;
                }
            }
            //Si trobam la fila sumam punts i guardam l-index de la fila per després buidar
            if (liniaTrobada){
                if(sobrescriure) {
                    score = score + 10;
                    //soundPool.play(idItsLit,1,1,1,0,1);

                    playSoundRowDestroy();

                    if (score >= maxScore) {
                        maxScore = score;
                    }
                    filesTrobades[i] = true;
                }
                numFilesTrobades++;
            }
            liniaTrobada = true;
        }
        //Comprovam col·lumnes
        for(int k=0;k<tablero.length;k++) {
            for (int i = 0; i < tablero.length; i++) {
                if (tablero[i][k] != 1) {
                    liniaTrobada = false;
                }
            }
            //Si trobam la columna sumam punts (sumam en funcio de les files trobades) i reiniciam la columna tota a 0
            if (liniaTrobada){
                if(sobrescriure) {
                    score = score + 10 - numFilesTrobades;
                    girsDisponibles++;

                    playSoundRowDestroy();

                    if (score >= maxScore) {
                        maxScore = score;
                    }
                    for (int i = 0; i < tablero.length; i++) {
                        tablero[i][k] = 2;
                    }
                }
                numColumnesTrobades++;
            }
            liniaTrobada = true;
        }

        //Reiniciam les files trobades
        if (sobrescriure){
            for (int i = 0; i < tablero.length; i++) {
                if (filesTrobades[i]){
                    girsDisponibles++;
                    for (int k = 0; k < tablero.length; k++) {
                        tablero[i][k] = 2;
                    }
                }
            }
        }

        int puntsGuanyats = numFilesTrobades*10+numColumnesTrobades*10-1;
        return puntsGuanyats;
    }

    /*
    Metode que reinicia tots els parametres necessaris per tornar a iniciar una nova partida,
    també desactiva posibles ajudes solicitades
    */

    public void reiniciarGame(){
        //Reproduim de nou la musica
        playBgMusic();
        //Reiniciam tablero a 0
        for (int i = 0; i < tablero.length; i++) {
                for (int k = 0; k < tablero.length; k++) {
                    tablero[i][k] = 0;
                }
        }
        score = 0;
        seAcabo = false;
        nouRecord = false;
        contadorImatgeNewRecord=0;
        contadorImatgeParpadeig=0;
        //Eliminam la solicitud de ajuda si s'esta realitzant una ajuda
        if(ajudaSolicitada){
            ajudaSolicitada = false;
            contadorAparicioPentomino = 0;
            contadorParpadeig=0;
        }
        generarPentominosRandom();
        jocNou=true;
    }

    /*
    Metode per a guardar totes les variables i estructures de dates necesaries per a seguir amb la
    partida anterior. Aquets metode es crida desde la clase MainView quan s'atura el joc.
    */

    public void save(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("maxScore",maxScore);
        editor.putInt("score",score);

        editor.putInt("indexPent1",pentominosRandom[0].getIndex());
        editor.putInt("indexPent2",pentominosRandom[1].getIndex());
        editor.putInt("indexPent3",pentominosRandom[2].getIndex());

        editor.putInt("girsPent1",girsFigures[0]);
        editor.putInt("girsPent2",girsFigures[1]);
        editor.putInt("girsPent3",girsFigures[2]);

        editor.putInt("puntIniciX0",puntIniciX[0]);
        editor.putInt("puntIniciX1",puntIniciX[1]);
        editor.putInt("puntIniciX2",puntIniciX[2]);

        editor.putInt("puntIniciY0",puntIniciY[0]);
        editor.putInt("puntIniciY1",puntIniciY[1]);
        editor.putInt("puntIniciY2",puntIniciY[2]);

        editor.putBoolean("estatFigures1",estatFigures[0]);
        editor.putBoolean("estatFigures2",estatFigures[1]);
        editor.putBoolean("estatFigures3",estatFigures[2]);


        editor.putString("tablero",escriureTablero()[0]);
        editor.putString("colors",escriureTablero()[1]);

        editor.putInt("girsDisponibles",girsDisponibles);
        editor.putBoolean("sonido",sonido);
        editor.putBoolean("seAcabo",seAcabo);
        editor.putFloat("volum", volum);

        editor.putBoolean("jocNou",jocNou);
        editor.commit();
    }

    /*
    Metode per a retornar totes les variables i estructures de dates guardades al métode save. Aquets
    metode es crida desde la clase MainView quan s'inicia el joc.
    */

    public void retrieve(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        maxScore = sharedPref.getInt("maxScore",0);
        score = sharedPref.getInt("score",0);
        girsDisponibles = sharedPref.getInt("girsDisponibles",3);

        Random random = new Random();
        int random1 = random.nextInt(12);
        int random2 = random.nextInt(12);
        int random3 = random.nextInt(12);

        volum = sharedPref.getFloat("volum",0);

        sonido = sharedPref.getBoolean("sonido",true);
        playBgMusic();

        seAcabo = sharedPref.getBoolean("seAcabo",false);

        pentominosRandom[0] = new Pentomino(4,pentominoes[sharedPref.getInt("indexPent1",random1)].getPentomino(0),pentominoes[sharedPref.getInt("indexPent1",random1)].getColor(),pentominoes[sharedPref.getInt("indexPent1",random1)].getIndex());
        pentominosRandom[1] = new Pentomino(4,pentominoes[sharedPref.getInt("indexPent2",random2)].getPentomino(0),pentominoes[sharedPref.getInt("indexPent2",random2)].getColor(),pentominoes[sharedPref.getInt("indexPent2",random2)].getIndex());
        pentominosRandom[2] = new Pentomino(4,pentominoes[sharedPref.getInt("indexPent3",random3)].getPentomino(0),pentominoes[sharedPref.getInt("indexPent3",random3)].getColor(),pentominoes[sharedPref.getInt("indexPent3",random3)].getIndex());

        girsFigures[0] = sharedPref.getInt("girsPent1",girsFigures[0]);
        girsFigures[1] = sharedPref.getInt("girsPent2",girsFigures[1]);
        girsFigures[2] = sharedPref.getInt("girsPent3",girsFigures[2]);

        puntIniciX[0] = sharedPref.getInt("puntIniciX0",puntIniciX[0]);
        puntIniciX[1] = sharedPref.getInt("puntIniciX1",puntIniciX[1]);
        puntIniciX[2] = sharedPref.getInt("puntIniciX2",puntIniciX[2]);

        puntIniciY[0] = sharedPref.getInt("puntIniciY0",puntIniciY[0]);
        puntIniciY[1] = sharedPref.getInt("puntIniciY1",puntIniciY[1]);
        puntIniciY[2] = sharedPref.getInt("puntIniciY2",puntIniciY[2]);

        estatFigures[0] = sharedPref.getBoolean("estatFigures1",true);
        estatFigures[1] = sharedPref.getBoolean("estatFigures2",true);
        estatFigures[2] = sharedPref.getBoolean("estatFigures3",true);

        jocNou = sharedPref.getBoolean("jocNou",true);

        llegirTablero(sharedPref.getString("tablero","000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000000000"),
                sharedPref.getString("colors",""));
    }

    /*
    Metode per guardar la partida actual en memoria. Recorrem tant tablero com tablero color
    i guardam cada posició a un String. Un per tableroColor i un per tablero.

    Pel cas del color, guardam ints separats per blancs sempre que hagi un posició del tablero ocupada
    i un 0 quan no per tal de tenir Strings de longitud definida.

    Pel cas de saber si hi ha o no una peça, guardam un 1 si hi ha i un 0 si no hi ha una peça.
     */

    public String []escriureTablero(){
        String tableroGuardat = "";
        String colorGuardat = "";

        for (int i = 0; i < tablero.length ; i++) {
            for (int j = 0; j < tablero.length; j++) {
                if(tablero[i][j]==1){
                    colorGuardat = colorGuardat + Integer.toString(tableroColor[i][j]) + " ";
                    tableroGuardat = tableroGuardat + "1";
                }else{
                    colorGuardat = colorGuardat + "0" + " ";
                    tableroGuardat = tableroGuardat + "0";
                }
            }

        }
        return new String[]{tableroGuardat,colorGuardat};
    }

    /*Llegeix el tablero guardat en memoria i el carrega el tablero.
    Cal diferenciar entre si es la primera ejecució del codi o no per tal de executar el valors
    per defecte.

    Agafam la matriu de 1 i 0 que indica si la posició del tablero esta o no ocupada en
    format String. Recorrem cada posició del tablero guardat i comparam l'String. S'Escriu al nou
    array tablero un 1 o un 0 en funció de l'String llegit.

    Pel cas dels colors, agafam l'String que enmagatzema un int per cada color separat per espais " ".
    Separam l'String per cada caracter " " i guardam cada int de color al tableroColor.

     */

    public void llegirTablero(String tableroGuardat, String colorGuardat){
        int contador = 0;
        String [] parts = new String[100];
        boolean primeraExecucio = true;

        if((colorGuardat!="")){
            parts= colorGuardat.split(" ");
            primeraExecucio = false;

        }

        for (int i = 0; i <tablero.length ; i++) {
            for (int j = 0; j < tablero.length; j++) {
                if(primeraExecucio==false){
                    tableroColor[i][j] = Integer.parseInt(parts[contador]);
                }
                if((tableroGuardat.charAt(contador)=='0')){
                    tablero[i][j]=0;
                }else{
                    tablero[i][j]=1;
                }
                contador++;
            }
        }
    }

    /*Metode per a reproduir la musica de fons, al inici del programa i per reproduirla despres de ser
    pausada
    */

    public void playBgMusic(){
        if(sonido){
            mp.setVolume(volum,volum);
            mp.start();
            mp.setLooping(true);
        }
    }

    /*Pausa la musica de fons quan es minitmitza el joc, es crida desde la clase MainView al metode
     Pause
     */

    public void stopBgMusic(){
            mp.pause();

    }

    /*Métode que reprodueix un dels sons d'efectes, aquets són passats al metode per parametre. Aquests
      son:
        -Posar una peça
        -Tocar un botó
        -Posar una peça incorrectament
        -So de gameover
     */

    public void playSound(int id){
        if(sonido){
            soundPool.play(id,1,1,1,0,1);
        }
    }

    /*Per tal d'evitar el solapament de la reproducció de sons, cream un altre soundpool per reproduir
      l'efecte de destruir una fila o una columna ja que l'efecte de posar una peça i de destruir una fila
      sovint es reprodueixen simultaneament.
     */

    public void playSoundRowDestroy(){
        if(sonido){
            sPRowDestroy.play(idRowDestoy,1,1,1,0,1);
        }
    }

    /*Mètode que comprova per a cada pentomino disponible,amb les rotacions disponibles, si es posible
      ficarho a alguna posició del tauler. Aquest metode s'executa cada pic que colocam un pentomino
      disponible al tauler
     */

    public boolean comprovarSiEsPotSeguirJugant(){
        boolean esPotSeguir=false;
        for (int i = 0; i < pentominosRandom.length; i++) { //for de recorrer els 3 pentominos
            if (estatFigures[i]) {
                for (int j = 0; j < tablero.length; j++) {     //for de recorrer files del tauler
                    for (int k = 0; k < tablero.length; k++) {   //for de recorrer columnes del tauler
                            int girsPosibles=girsDisponibles;
                            if(girsDisponibles>4){
                                girsPosibles = 4;
                            }
                            int l=girsFigures[i];
                            while(!esPotSeguir){
                                if(comprovarSiPentEntraTauler(pentominosRandom[i], l, j, k)) {
                                    esPotSeguir = true;
                                    break;
                                }
                                l++;
                                if (l==4){
                                    l=0;
                                }
                                if (l==girsFigures[i]||girsPosibles==0){
                                    break;
                                }
                                girsPosibles--;
                            }
                        if(esPotSeguir){
                            break;
                        }
                    }
                    if (esPotSeguir){
                        break;
                    }
                }
                if(esPotSeguir){
                    break;
                }
            }
            if (esPotSeguir){
                break;
            }
        }


        return esPotSeguir;
    }

    //Aquest mètode ens calcula una posició sugerida al tauler per als pentominos disponibles

    public void pentominoSugerit(){

        double maximPuntuacio = 0;
        double maxPuntuacio;

        for (int i = 0; i < pentominosRandom.length; i++) {      //for de recorrer els 3 pentominos
            if (estatFigures[i]) {
                for (int j = 0; j < tablero.length; j++) {       //for de recorrer files del tauler
                    for (int k = 0; k < tablero.length; k++) {   //for de recorrer columnes del tauler
                        int girsPosibles=girsDisponibles;
                        if(girsDisponibles>4){
                            girsPosibles = 4;
                        }
                        int l=girsFigures[i];
                        int girsFets = 0;
                        while(true){//Comrpovam si el pentomino seleccionat entra al tauler
                            if(comprovarSiPentEntraTauler(pentominosRandom[i], l, j, k)) {
                                maxPuntuacio = comprovarContacte(pentominosRandom[i],l,j,k);
                                ficarATaula(pentominosRandom[i],l,j,k);
                                maxPuntuacio = maxPuntuacio + comprovarLinies(false);
                                llevarDeTaula(pentominosRandom[i],l,j,k);

                                //Amb aquestes condicions, penalitzam les sugerencies que al tenir mes
                                //de 4 girs disponibles no gastin girs. D'aquesta manera si tenim mes
                                //de 4 girs disponibles, el programa gastará mes rotacions, mentre que si
                                //tenim menys de 4 no en gastará tantes. Aixó repercuteix a la ponderació
                                //que es fa de la puntuació obtinguda de la posició sugerida
                                if(girsDisponibles>4){
                                    maxPuntuacio = maxPuntuacio * (1/((girsFets*0.15)+1));
                                }else{
                                    maxPuntuacio = maxPuntuacio * (1/((girsFets*0.3)+1));
                                }

                                if(maxPuntuacio>=maximPuntuacio){

                                        numeroPentominoSugerit=i;
                                        girsPentominoSugerit=l;
                                        girsSugeritsFets=girsFets;
                                        filaPentominoSugerit=j;
                                        columnaPentominoSugerit=k;

                                        maximPuntuacio = maxPuntuacio;

                                }
                            }
                            l++;
                            //Si els girs arriben a 4 es tornen colocar a 0
                            if (l==4){
                                l=0;
                            }
                            //Si s-ha fet una volta completa als girs es surt del while
                            if (l==girsFigures[i]||girsPosibles==0){
                                break;
                            }
                            girsFets++;
                            girsPosibles--;
                        }
                    }
                }
            }
        }

        ajudaSolicitada = true;
        contadorAparicioPentomino = 0;
        contadorParpadeig=0;
        System.out.println(comprovarContacte(pentominosRandom[numeroPentominoSugerit],girsPentominoSugerit,filaPentominoSugerit,columnaPentominoSugerit));
        System.out.println(girsSugeritsFets);
    }

    /*Metode que cerca cuants de costats del pentomino tenen contacte amb un altre pentomino
     o els eixos del tauler (util per a cercar la colocacio mes optima), també mira el nombre
     de espais en blanc que deixam
     */

    public int comprovarContacte(Pentomino pentomino, int girs,int filaTaula,int columnaTaula) {

        int [][] formaPent = pentomino.getPentomino(girs);
        int [] coordenadesPent = pentomino.getCoordenades(girs);
        int alt = coordenadesPent[3]-coordenadesPent[1]+1; //Cuants de quadrats d'alt te el petomino
        int llarg = coordenadesPent[2]-coordenadesPent[0]+1; //Cuants de quadrats de llarg te el petomino

        int puntuacio = 0; //Evaluam quina es la millor posició en funció de la puntuació que té on la poses

        //Recorrem el pentomino que esteim mirant
        for (int i = 0; i<alt; i++){
            for (int k = 0; k<llarg; k++){
                if (formaPent[coordenadesPent[1]+i][coordenadesPent[0]+k]==1){
                    if(filaTaula+i==0||filaTaula+i==9){
                        puntuacio++;
                    }
                    if(columnaTaula+k==0||columnaTaula+k==9){
                        puntuacio++;
                    }
                    //Miram damunt
                    try {
                        //Miram contactes
                        if(tablero[filaTaula+i+1][columnaTaula+k]==1){
                            puntuacio++;
                        }
                    }
                    catch(Exception e){}
                    //Miram davall
                    try {
                        //Miram contactes
                        if(tablero[filaTaula+i-1][columnaTaula+k]==1){
                            puntuacio++;
                        }
                    }
                    catch(Exception e){}
                    //Miram esquerra
                    try {
                        //Miram contactes
                        if(tablero[filaTaula+i][columnaTaula+k-1]==1){
                            puntuacio++;
                        }
                    }
                    catch(Exception e){}
                    //Miram dreta
                    try {
                        //Miram contactes
                        if(tablero[filaTaula+i][columnaTaula+k+1]==1){
                            puntuacio++;
                        }
                    }
                    catch(Exception e){}
                }
            }
        }
        return puntuacio;
    }

    public void update(long fps) {
    }
}



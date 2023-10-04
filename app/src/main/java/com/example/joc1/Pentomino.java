package com.example.joc1;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Pentomino {

    private Paint paint;
    private int [][] coordenades = new int[4][4]; // Conte 4 arrays (1 per cada gir de la figura) amb el seguent contingut: {left (0), top(1), right(2), bot(3)}
    private int[][][] pentForma = new int [4][5][5]; //Tots els girs possibles del pentomino guardats en arrays de 5x5
    private int color; //El color en el que s'imprimirà aquest pentomino
    private int index; //Identifica quin dels 12 pentominos es, aquesta variable es necessaria per guardar el pentomino
    private int [] alt = new int [4]; //Per a cada gir de la figura el nombre de quadrats d-alt que fa la figura
    private int [] llarg = new int [4]; //Per a cada gir de la figura el nombre de quadrats de llarg que fa la figura


    public Pentomino(int numGirs, int[][] p,int color, int index) {

        this.pentForma[0] = p;
        this.color = color;
        this.index = index;
        coordenades[0] = cercaCoordenades(0); //Calculam les coordenades del gir 0
        alt [0] = coordenades[0][3]-coordenades[0][1]+1; //Cuants de quadrats d'alt fa el pentomino per al gir 0
        llarg [0] = coordenades[0][2]-coordenades[0][0]+1; //Cuants de quadras de llarg fa el pentoimino per al gir 0
        for (int i=1; i <numGirs; i++)
        {
            pentForma[i]= girar(pentForma[i-1]); //Giram la array 5x5 cap a la dreta i guardam el valor a pentForma
            coordenades[i] = cercaCoordenades(i); //Calculam les coordenades del gir i
            alt [i] = coordenades[i][3]-coordenades[i][1]+1; //Cuants de quadrats d'alt per al gir i
            llarg [i] = coordenades[i][2]-coordenades[i][0]+1; //Cuants de quadras de llarg per al gir i
        }
    }

    /*  METODE PER GIRAR una array 5 x5
     Aquest metode, donat una array 5x5, en retorna una altra girada 90º cap a la dreta.
     */
    public int[][] girar(int[][] p){
        int [][] pentGirat = new int [5][5];
        for (int i=0;i<=(pentForma.length);i++){
            for(int k=0;k<=(pentForma.length);k++) {
                pentGirat[k][pentForma.length-i] = p[i][k];
            }
        }
        return pentGirat;
    }

    // METODES RETURN, retornen les variables solicitades.
    public int [][] getPentomino(int r){ return pentForma[r];}
    public int getAlt(int r){return alt[r];}
    public int getLlarg(int r){return llarg[r];}
    public int getIndex(){return index;}
    public int [] getCoordenades(int r){return new int []{coordenades[r][0],coordenades[r][1],coordenades[r][2],coordenades[r][3]};}
    public int getColor(){return color;}

    /* METODE PER PINTAR DONADES UNES COORDENADES EN PIXELS
        Aquest metode comença a pintar el pentomino a la posició iniciX i iniciY. Comença a pintar a partir del
        top i left del pentomino. (Això vol dir que si una fila o columna es tot zeros, directament no la pintarà.
     */
    public void pintarNormal(Canvas canvas,int iniciX,int iniciY,int ladoCuadrado, int r){

        paint = new Paint();
        paint.setColor(color);
        for (int  j= 0; j < 2; j++) {
            if (j == 0 ){
                paint.setStyle(Paint.Style.FILL);
            }else{
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(1);
            }

            for (int i= 0;i<alt[r];i++) {
                for (int k = 0; k < llarg[r]; k++) {
                    if (pentForma[r][coordenades[r][1]+i][coordenades[r][0]+k]==1){
                        Rect quadre = new Rect(iniciX+k*ladoCuadrado,iniciY+i*ladoCuadrado,iniciX+(k+1)*ladoCuadrado,iniciY+(i+1)*ladoCuadrado);
                        canvas.drawRect(quadre,paint);
                    }
                }
            }
        }
    }

    /* METODE PER PINTAR DONADES UNA FILA I COLUMNA DEL TAULER.
        Aquest metode comença a pintar el pentomino al quadre del tauler corresponent a la fila i columna introduides al metode.
        Comença a pintar a partir del top i left de pentForma.
     */

    public void pintarSugerit(Canvas canvas,int fila,int columna,int ladoCuadrado, int r, int margeSuperior){

        paint = new Paint();
        paint.setColor(color);
        for (int  j= 0; j < 2; j++) {
            if (j == 0 ){
                paint.setStyle(Paint.Style.FILL);
            }else{
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
            }
            for (int i= 0;i<alt[r];i++) {
                for (int k = 0; k < llarg[r]; k++) {
                    if (pentForma[r][coordenades[r][1]+i][coordenades[r][0]+k]==1){
                        Rect quadre = new Rect(columna*ladoCuadrado+k*ladoCuadrado,margeSuperior+fila*ladoCuadrado+i*ladoCuadrado,columna*ladoCuadrado+(k+1)*ladoCuadrado,margeSuperior+fila*ladoCuadrado+(i+1)*ladoCuadrado);
                        canvas.drawRect(quadre,paint);
                    }
                }
            }
        }
    }

    /*      FUNCIO PER TROBAR EL PUNT D'INICI (left,top) I PUNT DE FINAL (right,bot) del pentomino (en cuadrats)
        Aquesta funció retorna els 4 següents valors respecte a la matriu pentForma (5x5) del gir indicat:
            -left: indica la primera columna on hi ha un 1
            -top: indica la primera fila on hi ha un 1
            -right: indica la darrera columna on hi ha un 1
            -bot: indica la darrera fila on hi ha un 1
     */
    public int[] cercaCoordenades(int r){

        int left = 0,top = 0,right = 0,bot = 0; //inicialitzam el valor de les coordenades

        boolean trobat = false; //Variable per sortir del doble bucle

        //For per calcular la coordenada top, recorrem la matriu de esquerra a dreta (1r for) i de amunt cap avall (2n for)
        for (int i=0;i<=(pentForma.length);i++){
            for(int k=0;k<=(pentForma.length);k++) {
                if (pentForma[r][i][k] == 1) { //Si trobam un 1, guardam el valor de top i sortim dels fors
                    top = i;
                    trobat = true;
                    break;
                }
            }
            if (trobat) {
                break;
            }
        }

        trobat = false;

        //For per calcular la coordenada bot, recorrem la esquerra a dreta (1r for) i de avall cap amunt (2n for)
        for (int i=(pentForma.length);i>=0;i--){
            for(int k=0;k<=(pentForma.length);k++) {
                if (pentForma[r][i][k] == 1) { //Si trobam un 1, guardam el valor de bot i sortim dels fors
                    bot = i;
                    trobat = true;
                    break;
                }
            }
            if (trobat) {
                break;
            }
        }

        trobat = false;

        //For per calcular les coordenades left, recorrem la matriu de amunt per avall (1r for) i de esquerra a dreta (2n for)
        for (int k=0;k<=(pentForma.length);k++){
            for(int i=0;i<=(pentForma.length);i++) {
                if (pentForma[r][i][k] == 1) { //Si trobam un 1, guardam el valor de left i sortim dels fors
                    left = k;
                    trobat = true;
                    break;
                }
            }
            if (trobat) {
                break;
            }
        }

        trobat = false;

        //For per calcular les coordenades right, recorrem la matriu de amunt per avall (1r for) i de dreta a esquerra (2n for)
        for (int k=(pentForma.length);k>=0;k--){
            for(int i=0;i<=(pentForma.length);i++) {
                if (pentForma[r][i][k] == 1) { //Si trobam un 1, guardam el valor de right i sortim dels fors
                    right = k;
                    trobat = true;
                    break;
                }
            }
            if (trobat) {
                break;
            }
        }
        //Els valors retornats són
        return new int[]{left,top,right,bot};
    }
}

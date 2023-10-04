package com.example.joc1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements Runnable {
    private volatile boolean running;
    private Thread gameThread = null;

    // Game
    private Game game;

    // For drawing
    private SurfaceHolder myHolder;

    // Control the fps
    long fps =60;

    MainView(Context context, int screenWidth, int screenHeight) {
        super(context);

        // Initialize our drawing objects
        myHolder = getHolder();
        game = new Game(context, this, screenWidth, screenHeight);
    }

    @Override

    public void run() {

        long ticksPS = 1000/fps;
        long sleepTime;
        long elapsedTime;
        long startFrameTime;
        long endFrameTime;

        while (running) {

            startFrameTime = System.currentTimeMillis();

            game.update(fps);
            game.draw(myHolder);

            endFrameTime = System.currentTimeMillis();

            elapsedTime = endFrameTime - startFrameTime;
            sleepTime = ticksPS - elapsedTime;
            try{
                if (sleepTime > 0){
                    Thread.sleep(sleepTime);
                }
            }
            catch (Exception e){
                System.out.println("Error d-espera");
            }

            // Calculate the fps this frame
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
                Game.fotogrames = fps;
            }
        }

    }

    // Clean up our thread if the game is stopped
    public void pause() {
        game.save();
        game.stopBgMusic();
        running = false;
        try {
            gameThread.join();

        } catch (InterruptedException e) {
            // Error
        }
    }

    // Make a new thread and start it
    // Execution moves to our run method
    public void resume() {

        gameThread = new Thread(this);
        gameThread.start();
        running = true;
        game.retrieve();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int action = motionEvent.getActionMasked();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    game.handleActionDown(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    game.handleActionUp(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    game.handleActionMove(x, y);
                    break;
            }
            return true;
    }


}

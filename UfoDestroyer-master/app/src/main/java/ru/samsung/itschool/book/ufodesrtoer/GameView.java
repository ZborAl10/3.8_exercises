package ru.samsung.itschool.book.ufodesrtoer;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ru.samsung.itschool.book.ufodesrtoer.GameThread;
import ru.samsung.itschool.book.ufodesrtoer.model.Game;
import ru.samsung.itschool.book.ufodesrtoer.model.math.Vector2F;
import ru.samsung.itschool.book.ufodesrtoer.model.util.Tools;

/**
 * View отвечающий за отрисовку игры
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private Game game;

    public GameView(Game game, Context context) {
        super(context);
        Log.d("GameView", "Game view created");
        getHolder().addCallback(this);
        this.game = game;
    }

    private GameThread gameThread = null;
    private float surfaceWidth = 0f, surfaceHeight = 0f;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GameView", "Surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("GameView", "Surface changed");
        surfaceWidth = width;
        surfaceHeight = height;
        if (gameThread == null) {
            gameThread = new GameThread(getContext(), game, holder, width, height);
            gameThread.start();
        } else
            gameThread.updateSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("GameView", "Surface destroyed");
        boolean retry = true;
        // завершаем работу потока
        gameThread.requestStop();
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если ожидание почему-то прервалось - будем ждать снова
            }
        }
        gameThread = null;
        Log.d("GameView", "Game thread joined");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (surfaceWidth > 0 && surfaceHeight > 0) {
            Vector2F point = new Vector2F(event.getX(), event.getY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    Vector2F gameCoordsPoint =
                            Tools.canvasPointToGamePoint(point, surfaceWidth, surfaceHeight);
                    game.getPlayerController().setTarget(gameCoordsPoint.x, gameCoordsPoint.y);
                    return true;
                case MotionEvent.ACTION_UP:
                    game.getPlayerController().stopTargeting();
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }
}

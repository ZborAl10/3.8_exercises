package ru.myitschool.ufodestroyer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import ru.myitschool.ufodestroyer.model.Game;
import ru.myitschool.ufodestroyer.renderer.GameRenderer;

/**
 * Поток, отвечающий за отрисовку игры
 */
public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;
    private long lastTime = System.currentTimeMillis();
    private long lastFpsTime = System.currentTimeMillis();
    private int fpsCounter = 0;
    private int lastFps = 0;

    private Game game;
    private Resources resources;
    private GameRenderer renderer;
    private final Paint fpsPaint = new Paint();

    {
        fpsPaint.setColor(Color.BLACK);
        fpsPaint.setTextAlign(Paint.Align.LEFT);
        fpsPaint.setTextSize(30f);
        fpsPaint.setAntiAlias(true);
    }

    public GameThread(Context context, Game game, SurfaceHolder surfaceHolder, int width, int height) {
        this.surfaceHolder = surfaceHolder;
        this.game = game;

        resources = context.getResources();
        renderer = new GameRenderer(game, resources);
        updateSize(width, height);
    }

    /**
     * Обновляет размеры поверхности
     *
     * @param width  ширина в пикселях
     * @param height высота в пикселях
     */
    public void updateSize(int width, int height) {
        // чтобы ширина и высота менялись атомарно и не в середине расчетов/отрисовки
        synchronized (this) {
            game.setFieldWidthHeightRatio(width, height);
            renderer.setGameFieldSize(width, height);
        }
    }

    /**
     * Устанавливает флаг остановки для этого потока. Как только окончится очередной игровой цикл,
     * поток завершит свою работу
     */
    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            try {
                while (running) {
                    long currentTime = System.currentTimeMillis();
                    float elapsedSeconds = (currentTime - lastTime) / (1000.0f);
                    lastTime = currentTime;

                    // чтобы ширина и высота менялись атомарно и не в середине расчетов/отрисовки
                    synchronized (this) {
                        game.update(elapsedSeconds);

                        if (!running)
                            break;

                        Canvas canvas = surfaceHolder.lockCanvas();
                        if (canvas != null) {
                            try {
                                renderer.render(canvas);

                                canvas.drawText("Fps: " + lastFps, 0f, 30f, fpsPaint);
                            } finally {
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            }
                        }
                    }

                    fpsCounter++;
                    if (currentTime - lastFpsTime > 1000) {
                        lastFpsTime = currentTime;
                        lastFps = fpsCounter;
                        fpsCounter = 0;
                    }
                }
            } finally {
                renderer.close();
            }
        } catch (Exception e) {
            Log.e("GameThread", "Exception occured in GameThread", e);
        } catch (Error e) {
            Log.e("GameThread", "Error occured in GameThread", e);
        }
    }
}

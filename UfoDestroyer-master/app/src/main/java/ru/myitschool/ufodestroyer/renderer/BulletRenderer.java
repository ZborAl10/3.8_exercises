package ru.myitschool.ufodestroyer.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import ru.myitschool.ufodestroyer.model.Game;
import ru.myitschool.ufodestroyer.model.entities.HasCoordsAndSize;
import ru.myitschool.ufodestroyer.model.math.Vector2F;
import ru.myitschool.ufodestroyer.model.util.Tools;

import static ru.myitschool.ufodestroyer.model.util.Tools.gamePointToCanvasPoint;

/**
 * Отвечает за отрисовку пуль
 */
public class BulletRenderer implements Renderer {
    private final Bitmap bitmap;
    private final float pixelWidth;
    private final float pixelHeight;

    public BulletRenderer(Bitmap bitmap, float width, float height) {
        pixelWidth = Math.round(Tools.gameSizeToCanvasSize(Game.BULLET_WIDTH, width, height));
        pixelHeight = Math.round(Tools.gameSizeToCanvasSize(Game.BULLET_HEIGHT, width, height));

        this.bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(pixelWidth), Math.round(pixelHeight), true);
    }

    private final Paint paint = new Paint();
    {
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void render(Canvas canvas, HasCoordsAndSize coordsAndSize) {
        Vector2F point = gamePointToCanvasPoint(coordsAndSize.getCoords(),
                canvas.getWidth(), canvas.getHeight());

        canvas.drawBitmap(bitmap, point.x - pixelWidth / 2f, point.y - pixelHeight / 2f, paint);
    }

}

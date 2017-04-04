package ru.samsung.itschool.book.ufodesrtoer.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import ru.samsung.itschool.book.ufodesrtoer.model.Game;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.HasCoordsAndSize;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Player;
import ru.samsung.itschool.book.ufodesrtoer.model.math.Vector2F;
import ru.samsung.itschool.book.ufodesrtoer.model.util.Tools;

import static ru.samsung.itschool.book.ufodesrtoer.model.util.Tools.gamePointToCanvasPoint;
import static ru.samsung.itschool.book.ufodesrtoer.model.util.Tools.gameSizeToCanvasSize;

/**
 * Класс, отвечающий за отрисовку игрока
 */
public class PlayerRenderer implements Renderer {
    private final Bitmap bitmap;
    private final float pixelWidth;
    private final float pixelHeight;

    public PlayerRenderer(Bitmap bitmap, float width, float height) {
        pixelWidth = Math.round(Tools.gameSizeToCanvasSize(Game.PLAYER_WIDTH, width, height));
        pixelHeight = Math.round(Tools.gameSizeToCanvasSize(Game.PLAYER_HEIGHT, width, height));

        this.bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(pixelWidth), Math.round(pixelHeight), true);
    }

    private final Paint paint = new Paint();
    {
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    private final Paint chargingPaint = new Paint();
    {
        chargingPaint.setColor(Color.RED);
        chargingPaint.setStrokeWidth(5f);
        chargingPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void render(Canvas canvas, HasCoordsAndSize coordsAndSize) {
        if (!(coordsAndSize instanceof Player))
            throw new IllegalArgumentException("Only Player can be rendered by PlayerRenderer");
        Player player = ((Player) coordsAndSize);
        Vector2F point = gamePointToCanvasPoint(coordsAndSize.getCoords(),
                canvas.getWidth(), canvas.getHeight());

        Matrix matrix = new Matrix();
        matrix.postTranslate(-gameSizeToCanvasSize(Game.PLAYER_CENTER_X, canvas.getWidth(), canvas.getHeight()),
                -gameSizeToCanvasSize(Game.PLAYER_CENTER_Y, canvas.getWidth(), canvas.getHeight()));
        // минус, потому что Rotate поворачивает по часовой
        matrix.postRotate(-((Player) coordsAndSize).getAngle());
        matrix.postTranslate(point.x, point.y);

        canvas.drawBitmap(bitmap, matrix, paint);

        // рисуем уровень заряда
        if (player.getChargingLevel() != null) {
            float skip = Tools.gameSizeToCanvasSize(Game.PLAYER_CENTER_X, canvas.getWidth(), canvas.getHeight());

            float[] points = {skip, bitmap.getHeight() / 2f,
                              skip +(bitmap.getWidth() - skip) * player.getChargingLevel(), bitmap.getHeight() / 2f};
            matrix.mapPoints(points);
            canvas.drawLines(points, chargingPaint);
        }
    }
}

package ru.samsung.itschool.book.ufodesrtoer.renderer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import ru.samsung.itschool.R;
import ru.samsung.itschool.book.ufodesrtoer.model.Game;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Bullet;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Enemy;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.GameObject;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Player;

/**
 * Класс, который отвечает за отрисовку игры
 */
public class GameRenderer implements Game.EventListener {
    private Game game;
    private Resources resources;
    private Bitmap background;
    private EnemyRenderer enemyRenderer;
    private PlayerRenderer playerRenderer;
    private BulletRenderer bulletRenderer;

    /**
     * Создает новый экземпляр рендерера
     * @param game игровая модель
     */
    public GameRenderer(Game game, Resources resources) {
        this.game = game;
        this.resources = resources;
        game.addEventListener(this);
    }

    /**
     * Вызывается, чтобы сообщить о размерах игрового поля
     * @param width ширина в пикселях
     * @param height высота в пикселях
     */
    public void setGameFieldSize(int width, int height) {
        // загрузка ресурсов и их масштабирование
        // задний фон
        Bitmap originalBackground = BitmapFactory.decodeResource(resources, R.drawable.background);
        Point destSize = RendererTools.calcFitOutroSize(
                new Point(originalBackground.getWidth(), originalBackground.getHeight()),
                new Point(width, height));
        background = Bitmap.createScaledBitmap(originalBackground, destSize.x, destSize.y, true);

        // противник
        Bitmap ufo = BitmapFactory.decodeResource(resources, R.drawable.ufo);
        enemyRenderer = new EnemyRenderer(ufo, width, height);

        // игрок
        Bitmap player = BitmapFactory.decodeResource(resources, R.drawable.player);
        playerRenderer = new PlayerRenderer(player, width, height);

        // пуля
        Bitmap bullet = BitmapFactory.decodeResource(resources, R.drawable.plasma);
        bulletRenderer = new BulletRenderer(bullet, width, height);

        initRenderers();
    }

    /**
     * Инициализирует и заменяет рендереры, отвечающие за отрисовку отдельных объектов
     */
    private void initRenderers() {
        for (GameObject go: game.getGameObjects()) {
            if (go instanceof Player)
                go.setRenderingHelper(playerRenderer);
            else if (go instanceof Enemy)
                go.setRenderingHelper(enemyRenderer);
            else if (go instanceof Bullet)
                go.setRenderingHelper(bulletRenderer);
            else
                throw new IllegalStateException("Renderer does not know how to render object " + go.getClass().getName());
        }
    }

    public void close() throws Exception {
        game.removeEventListener(this);
    }

    private final Paint paint = new Paint();
    {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
    }

    private final Paint scorePaint = new Paint();
    {
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(30f);
        scorePaint.setTextAlign(Paint.Align.RIGHT);
        scorePaint.setAntiAlias(true);
    }

    /**
     * Рисует текущее состояние игры
     * @param canvas канва для рисования на ней текущего состояния игры
     */
    public void render(Canvas canvas) {
        canvas.drawBitmap(background, (canvas.getWidth() - background.getWidth()) / 2,
                (canvas.getHeight() - background.getHeight()) / 2, paint);

        for (GameObject object: game.getGameObjects()) {
            if (object.getRenderingHelper() != null) {
                Renderer renderer = (Renderer)object.getRenderingHelper();
                renderer.render(canvas, object);
            }
        }

        canvas.drawText("Очки: " + game.getScore(), canvas.getWidth(), 30f, scorePaint);
    }

    @Override
    public void onBulletFired(Bullet bullet) {
        bullet.setRenderingHelper(bulletRenderer);
    }

    @Override
    public void onEnemySpawned(Enemy enemy) {
        enemy.setRenderingHelper(enemyRenderer);
    }
}

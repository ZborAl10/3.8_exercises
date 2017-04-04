package ru.samsung.itschool.book.ufodesrtoer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ru.samsung.itschool.book.ufodesrtoer.model.entities.Bullet;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Enemy;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.GameObject;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.HasCoordsAndSize;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.MovingGameObject;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Player;
import ru.samsung.itschool.book.ufodesrtoer.model.math.Vector2F;
import ru.samsung.itschool.book.ufodesrtoer.model.util.SimpleCoordsAndSize;
import ru.samsung.itschool.book.ufodesrtoer.model.util.Tools;

/**
 * Класс, реализующий логику игры. Для подробного поисания см {@link Game}
 */
public class GameImpl implements Game, PlayerControllerImpl.Owner {
    /**
     * Максимальное время, которое может пройти между обновлениями игровой модели;
     * Необходимо для того, чтобы быть уверенным, что проверка коллизий произойдет
     * верно
     */
    private static final float MAX_SECONDS_BETWEEN_UPDATES = 0.01f;

    /**
     * Время между появлением врагов, в секундах
     */
    private static final float SECONDS_BETWEEN_ENEMY_SPAWNS = 2.0f;

    private Player player = new Player();
    private PlayerControllerImpl playerController = new PlayerControllerImpl(this);

    {
        player.getSize().set(PLAYER_WIDTH, PLAYER_HEIGHT);
        player.getCoords().set(0f, 0f);
        player.setAngle(90f);
    }

    private List<GameObject> gameObjects = new ArrayList<>();
    {
        gameObjects.add(player);
    }

    private List<EventListener> listeners = new ArrayList<>();
    private int score = 0;

    private float gameFieldHeight;
    private final float gameFieldWidth = FIELD_WIDTH;

    @Override
    public void setFieldWidthHeightRatio(float width, float height) {
        gameFieldHeight = FIELD_WIDTH * height / width;
    }

    @Override
    public void addEventListener(EventListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener should never be null");
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(gameObjects);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Вызывается контроллером, когда нужно произвести выстрел
     *
     * @param power сила выстрела, 0 - самый слабый, 1 - самый сильный
     */
    @Override
    public void shoot(float power) {
        double angle = Math.toRadians(player.getAngle());
        float effectivePlayerWidth = PLAYER_WIDTH - PLAYER_CENTER_X;

        Bullet bullet = new Bullet();
        bullet.getCoords().set((float) Math.cos(angle) * effectivePlayerWidth,
                (float) Math.sin(angle) * effectivePlayerWidth);

        Vector2F speed = new Vector2F(bullet.getCoords());
        speed.normalize().multiply(MAX_BULLET_SPEED * player.getChargingLevel());

        bullet.getVelocity().set(speed);

        for (EventListener listener: listeners)
            listener.onBulletFired(bullet);

        gameObjects.add(bullet);
    }

    @Override
    public PlayerController getPlayerController() {
        return playerController;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void update(float elapsedSeconds) {
        playerController.update(elapsedSeconds);
        while (elapsedSeconds > 0) {
            float tick;
            if (elapsedSeconds > MAX_SECONDS_BETWEEN_UPDATES)
                tick = MAX_SECONDS_BETWEEN_UPDATES;
            else
                tick = elapsedSeconds;

            calcPositions(tick);
            deleteOffMapObjects();
            detectCollisions();
            spawnEnemies(tick);
            elapsedSeconds -= tick;
        }
    }

    /**
     * Находит столкнувшиеся с врагами пули и уничтожает их, увеличивая счет
     */
    private void detectCollisions() {
        Set<GameObject> toBeDeleted = new HashSet<>();

        // полный перебор - сравнительно неэффективно
        for (GameObject bullet: gameObjects) {
            if (bullet instanceof Bullet) {
                for (GameObject enemy: gameObjects) {
                    if (enemy instanceof Enemy) {
                        if (Tools.intersects(bullet, enemy)) {
                            score++;
                            toBeDeleted.add(bullet);
                            toBeDeleted.add(enemy);
                        }
                    }
                }
            }
        }

        gameObjects.removeAll(toBeDeleted);
    }

    /**
     * Удаляет объекты, которые находятся за пределами игрового поля
     */
    private void deleteOffMapObjects() {
        HasCoordsAndSize gameField = new SimpleCoordsAndSize(0, gameFieldHeight / 2, gameFieldWidth, gameFieldHeight);

        Iterator<GameObject> objectIterator = gameObjects.iterator();
        while (objectIterator.hasNext()) {
            GameObject object = objectIterator.next();
            if (!Tools.intersects(object, gameField))
                objectIterator.remove();
        }
    }

    /**
     * Обновляет позиции движущихся объектов
     * @param elapsedSeconds количество секунд, которые прошли в игровом мире
     */
    private void calcPositions(float elapsedSeconds) {
        for (GameObject object: gameObjects)
            if (object instanceof MovingGameObject)
                ((MovingGameObject) object).update(elapsedSeconds);
    }

    /**
     * Время с момента появления последнего противника
     */
    private float timeSinceLastSpawn = 0;

    /**
     * Создает новых врагов, если необходимо
     * @param elapsedSeconds количество секунд, которые прошли в игровом мире
     */
    private void spawnEnemies(float elapsedSeconds) {
        timeSinceLastSpawn += elapsedSeconds;
        if (timeSinceLastSpawn > SECONDS_BETWEEN_ENEMY_SPAWNS) {
            timeSinceLastSpawn -= SECONDS_BETWEEN_ENEMY_SPAWNS;
            spawnEnemy();
        }
    }

    private static final Random random = new Random();

    /**
     * Создает нового случайного противника
     */
    private void spawnEnemy() {
        Enemy enemy = new Enemy();
        float direction = 1f;
        if (random.nextBoolean())
            direction = -1f;
        enemy.getCoords().set(direction * gameFieldWidth / 2,
                (gameFieldHeight / 4) + random.nextFloat() * (gameFieldHeight / 2));
        enemy.getSize().set(ENEMY_WIDTH, ENEMY_HEIGHT);

        float dirY = (random.nextFloat() - 0.5f) * 3f;
        enemy.getVelocity().set( - direction * 3f, dirY * 0.5f);

        for (EventListener listener: listeners)
            listener.onEnemySpawned(enemy);

        gameObjects.add(enemy);
    }
}

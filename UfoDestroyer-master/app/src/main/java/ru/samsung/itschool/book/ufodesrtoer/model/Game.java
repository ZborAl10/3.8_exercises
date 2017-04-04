package ru.samsung.itschool.book.ufodesrtoer.model;

import java.util.List;

import ru.samsung.itschool.book.ufodesrtoer.model.entities.Bullet;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Enemy;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.GameObject;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.Player;

/**
 * Интерфейс для игровой модели. Сюда выделено всё, что об игровой модели нужно знать извне.
 *
 * Игровая модель хранит все игровые объекты и отвечает за расчет их новых
 * позиций, столкновений. Предоставляет оповещения о происходящих событиях.
 *
 * Ширина игрового поля равна 20 единицам (константа {@link #FIELD_WIDTH}). Высота игрового поля зависит
 * от соотношения сторон устройства и должна быть установлена путем вызова метода
 * {@link #setFieldWidthHeightRatio} до начала самой игры. Нижняя сторона игрового поля совпадает
 * с осью x (направленной вправо), а ось y направлена вверх из середины игрового поля.
 *
 * <pre>
 *
 *                ↑ (y)
 *                |
 *     -----------+-----------
 *     | Игровое  |   поле   |
 *     |          |          |
 *     |          |          |
 *     |          |          |
 *     -----------+----------|-------→ (x)
 * (-10,0)      (0,0)     (10,0)
 * </pre>
 */
public interface Game {
    public static final float FIELD_WIDTH = 20f;

    // соотношение сторон в модели должно соответствовать размеру изображений
    // иначе, выглядеть будет не очень
    /**
     * Ширина врага
     */
    public static final float ENEMY_WIDTH = 2f;

    /**
     * Высота врага
     */
    public static final float ENEMY_HEIGHT = 0.52532f;

    /**
     * Ширина врага
     */
    public static final float BULLET_WIDTH = 0.6f;

    /**
     * Высота врага
     */
    public static final float BULLET_HEIGHT = 0.6f;

    /**
     * Максимальная скорость пули
     */
    public static final float MAX_BULLET_SPEED = 35f;

    /**
     * Ширина игрока
     */
    public static final float PLAYER_WIDTH = 8f;

    /**
     * Высота игрока
     */
    public static final float PLAYER_HEIGHT = 2.32f;

    /**
     * Координата x центра игрока (точки, вокруг которой игрок поворачивается)
     */
    public static final float PLAYER_CENTER_X = 3.2f;

    /**
     * Координата y центра игрока (точки, вокруг которой игрок поворачивается)
     */
    public static final float PLAYER_CENTER_Y = 1.16f;

    /**
     * События, о которых игровая модель оповещает всех заинтересованные стороны
     */
    interface EventListener {
        /**
         * Оповещает о совершении выстрела
         * @param bullet выстрел
         */
        void onBulletFired(Bullet bullet);

        /**
         * Оповещает о появлении врага
         * @param enemy противник
         */
        void onEnemySpawned(Enemy enemy);
    }

    /**
     * Интерфейс для управления игроком
     */
    interface PlayerController {
        /**
         * Задает цель игрока. Пушка игрока будет вращаться в этом направлении до тех пор, пока не будет
         * вызван метод {@link #stopTargeting()} или пока пушка не будет смотреть в эту сторону. Если
         * пушка и так смотрит в эту сторону, начинается зарядка пушки. Пушка выстрелит, если будет
         * вызван метод {@link #stopTargeting()} в момент, когда пушка заряжена.
         * @param x икс координата цели в игровой системе координат
         * @param y игрек координата цели в игровой системе координат
         */
        void setTarget(float x, float y);

        /**
         * Прекращает прицеливание игроком. Если при вызове этого метода пушка имела заряд, будет
         * произведен выстрел. Если пушка была в процессе поворота, пушка прекратит поворот (выстрела
         * не произойдет). Повторный вызов этого метода без вызова {@link #setTarget(float, float)}
         * ничего не делает.
         */
        void stopTargeting();
    }

    /**
     * Подписывает слушателя на события от игровой модели
     * @param listener слушатель, который будет получать события; не должен быть null
     * @throws NullPointerException если listener равен null
     */
    public void addEventListener(EventListener listener);

    /**
     * Отменяет подписку слушателя на события от игровой модели
     * @param listener слушатель, который более не будет получать события
     */
    public void removeEventListener(EventListener listener);

    /**
     * Задает соотношение сторон игрового поля. Числа width и height должны относится также,
     * как относятся по размеру стороны игрового поля для правильного расчета игры
     * @param width ширина поля, в любых единицах измерения (одинаковых для width и height)
     * @param height высота поля, в любых единицах измерения (одинаковых для width и height)
     */
    void setFieldWidthHeightRatio(float width, float height);

    /**
     * @return Список всех игровых объектов; не подлежит изменению
     */
    List<GameObject> getGameObjects();

    /**
     * @return Объект игрока
     */
    Player getPlayer();

    /**
     * Рассчитывает новое состояние игры по прошествии указанного времени
     * @param elapsedSeconds количество секунд, которые должны пройти в игровом мире
     */
    void update(float elapsedSeconds);

    /**
     * Возвращает объект-контроллер, который позволяет управлять игроком в этой модели.
     * @return объект контроллер для управления действиями игрока
     */
    PlayerController getPlayerController();

    /**
     * @return Текущий счет, набранный игроком
     */
    int getScore();
}

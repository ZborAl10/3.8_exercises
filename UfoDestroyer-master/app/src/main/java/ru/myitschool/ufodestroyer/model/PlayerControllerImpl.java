package ru.myitschool.ufodestroyer.model;
import ru.myitschool.ufodestroyer.model.entities.Player;

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

/**
 * Реализация объекта для управления игроком. Стоит иметь в виду, что методы setTarget и
 * stioTargeting обычно вызываются из основного потока приложения (того, где работает Activity и
 * обработка событий), а метод update - из игрового потока расчета игры и отрисовки, поэтому,
 * следует быть осторожным в предположениях о том, что может или не может выполняться одновременно.
 */
public class PlayerControllerImpl implements Game.PlayerController {
    /**
     * Интерфейс для взаимодействия с игрой. Контроллер должен иметь возможность приказать игровой
     * модели произвести выстрел, чего не должно быть во внешнем интерфейсе Game (иначе, тот, кто
     * будет использовать этот класс сможет стрелять в обход контроллера)
     */
    interface Owner extends Game {

        /**
         * Говорит о том, что необходимо произвести выстрел с силой равной power.
         * @param power сила выстрела, 0 - самый слабый, 1 - самый сильный
         */
        void shoot(float power);
    }

    private final Owner game;

    public PlayerControllerImpl(Owner game) {
        this.game = game;
    }

    /**
     * true, если мы находимся в состоянии прицеливания или заряда (игрок держит палец на экране)
     */
    private boolean targeting = false;

    /**
     * X координата цели игрока
     */
    private float targetX;
    /**
     * Y координата цели игрока
     */
    private float targetY;

    /**
     * Текущий уровень заряда. Равен null, если сейчас не происходит процесс заряда
     */
    private Float currentChargingLevel = null;

    /**
     * Уровень заряда, который должен произойти. При следующем вызове @{link #update}, контроллер
     * сообщит игре о том, что нужно произвести выстрел с такой силой и обнулит эту переменную.
     */
    private Float delayedShotChargingLevel = null;

    /**
     * Скорость поворота пушки игрока, в градусах в секунду
     */
    private static final float PLAYER_ROTATE_SPEED = 80f;

    /**
     * Точность прицеливания игроком, в градусах (минимальный угол, на который допустимо ошибиться
     * при прицеливании)
     */
    private static final float ANGLE_EPSILON = 0.3f;

    /**
     * Скорость заряда пушки, в долях от единицы в секунду (полный заряд - 1.0)
     */
    private static final float CHARGING_SPEED_PER_SECOND = 0.6f;


    /**
     * Обновляет состояние игрока
     */
    synchronized void update(float elapsedSeconds) {
        if (delayedShotChargingLevel != null) {
            game.shoot(delayedShotChargingLevel);
            delayedShotChargingLevel = null;
            currentChargingLevel = null;
        }

        Player player = game.getPlayer();
        if (targeting) {
            double distance = sqrt(targetX * targetX + targetY * targetY);
            // мы не обрабатываем нажатия слишком близко от самого игрока
            if (distance > 0.1) {
                float angle = (float) toDegrees(asin(targetY / distance));
                if (targetX < 0)
                    angle = 180f - angle;
                boolean charging = abs(angle - game.getPlayer().getAngle()) < ANGLE_EPSILON;
                if (charging) { // идет зарядка пушки
                    if (currentChargingLevel == null) {
                        currentChargingLevel = 0f; // начинаем зарядку пушки
                    }
                    currentChargingLevel += elapsedSeconds * CHARGING_SPEED_PER_SECOND;
                    if (currentChargingLevel > 1f)
                        currentChargingLevel = 1f;
                }
                else { // идет поворот пушки
                    currentChargingLevel = null;
                    float newAngle;
                    if (player.getAngle() < angle) {
                        newAngle = player.getAngle() + PLAYER_ROTATE_SPEED * elapsedSeconds;
                        if (newAngle > angle)
                            newAngle = angle;
                    }
                    else {
                        newAngle = player.getAngle() - PLAYER_ROTATE_SPEED * elapsedSeconds;
                        if (newAngle < angle)
                            newAngle = angle;
                    }
                    player.setAngle(newAngle);
                }
            }
        }

        player.setChargingLevel(currentChargingLevel);
    }

    @Override
    public synchronized void setTarget(float x, float y) {
        targetX = x;
        targetY = y;
        targeting = true;
    }

    @Override
    public synchronized void stopTargeting() {
        if (targeting) {
            targeting = false;
            delayedShotChargingLevel = currentChargingLevel;
            currentChargingLevel = null;
        }
    }
}

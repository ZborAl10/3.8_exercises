package ru.myitschool.ufodestroyer.model.entities;

/**
 * Пуля, выпущенная игроком
 */
public class Bullet extends MovingGameObject {
    /**
     * Сила гравитации, в единицах на секунду в квадрате (см. {@link ru.myitschool.ufodestroyer.model.Game}
     */
    private static final float GRAVITY = 9.8f;

    @Override
    protected void adjustVelocity(float elapsedSeconds) {
        getVelocity().add(0, - elapsedSeconds * GRAVITY);
    }
}

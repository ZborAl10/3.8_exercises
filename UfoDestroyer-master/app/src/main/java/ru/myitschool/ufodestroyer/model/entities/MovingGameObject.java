package ru.myitschool.ufodestroyer.model.entities;

import ru.myitschool.ufodestroyer.model.math.Vector2F;

/**
 * Движущийся игровой объект. Хранит также свою скорость, помимо координат и размеров и имеет метод,
 * отвечающий за перерасчет скорости
 */
public abstract class MovingGameObject extends GameObject {
    private Vector2F velocity = new Vector2F();

    /**
     * @return Скорость объекта
     */
    public Vector2F getVelocity() {
        return velocity;
    }

    /**
     * Этот метод должен быть перекрыт в наследниках и может изменять скорость объекта. Цель этого
     * метода - задать значения скорости на основании какого-то закона, по которому перемещается
     * этот объект
     * @param elapsedSeconds сколько секунд прошло с прошлого вызова
     */
    protected abstract void adjustVelocity(float elapsedSeconds);

    /**
     * Расчитывает новую позицию объекта
     * @param elapsedSeconds сколько секунд прошло с прошлого вызова
     */
    public void update(float elapsedSeconds) {
        adjustVelocity(elapsedSeconds);
        Vector2F v = new Vector2F(velocity);
        getCoords().add(v.multiply(elapsedSeconds));
    }
}

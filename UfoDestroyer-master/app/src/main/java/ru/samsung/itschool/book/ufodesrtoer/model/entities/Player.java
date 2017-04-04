package ru.samsung.itschool.book.ufodesrtoer.model.entities;

/**
 * Игровой объект игрока - пушка
 */
public class Player extends GameObject implements HasAngle {
    private float angle;
    private Float chargingLevel = null;

    /**
     * @return Угол поворота пушки против часовой стрелки в градусах. Если пушка смотрит вправо,
     * возвращает 0. Если пушка смотрит влево, возвращает 180.
     */
    @Override
    public float getAngle() {
        return angle;
    }

    /**
     * @return Уровень заряда пушки, null - не заряжается, 0 - минимальный заряд,
     * 0.5 - средний заряд, 1 - максимальный заряд
     */
    public Float getChargingLevel() {
        return chargingLevel;
    }

    /**
     * Устанавливает уровень заряда пушки
     * @param chargingLevel уровень заряда, null - не заряжается, 0 - минимальный заряд,
     * 0.5 - средний заряд, 1 - максимальный заряд
     */
    public void setChargingLevel(Float chargingLevel) {
        this.chargingLevel = chargingLevel;
    }

    /**
     * Задает угол поворота пушки.
     *
     * @param angle Угол поворота пушки против часовой стрелки в градусах. Если нужно, чтобы пушка
     *              смотрела вправо, этот параметр должен быть равен 0. Если нужно, чтобы пушка
     *              смотрела влево, этот параметр должен быть равен 180.
     */
    @Override
    public void setAngle(float angle) {
        while (angle < 0f)
            angle += 180f;
        while (angle > 180f)
            angle -= 180f;
        this.angle = angle;
    }
}

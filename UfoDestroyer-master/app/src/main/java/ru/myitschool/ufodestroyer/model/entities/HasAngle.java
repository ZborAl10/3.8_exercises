package ru.myitschool.ufodestroyer.model.entities;

/**
 * Общий интерфейс для объектов, у которых есть угол поворота
 */
public interface HasAngle {

    /**
     * @return Угол поворота объекта против часовой стрелки в градусах
     */
    public float getAngle();

    /**
     * Задает угол поворота объекта против часовой стралеки в градусах.
     *
     * @param angle Угол поворота против часовой стрелки в градусах
     */
    public void setAngle(float angle);
}

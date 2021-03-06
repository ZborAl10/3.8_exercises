package ru.samsung.itschool.book.ufodesrtoer.model.entities;

import ru.samsung.itschool.book.ufodesrtoer.model.math.Vector2F;

/**
 * Общий интерфейс для объектов, у которых есть положение и размер
 */
public interface HasCoordsAndSize {
    /**
     * @return Координаты центра объекта
     */
    public Vector2F getCoords();

    /**
     * @return Размеры объекта (ширина и высота)
     */
    public Vector2F getSize();
}

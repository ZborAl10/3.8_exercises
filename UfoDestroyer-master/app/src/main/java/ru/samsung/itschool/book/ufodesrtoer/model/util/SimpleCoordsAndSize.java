package ru.samsung.itschool.book.ufodesrtoer.model.util;

import ru.samsung.itschool.book.ufodesrtoer.model.entities.HasCoordsAndSize;
import ru.samsung.itschool.book.ufodesrtoer.model.math.Vector2F;

/**
 * Простой класс, хранящий координаты и ширину с высоток
 */
public class SimpleCoordsAndSize implements HasCoordsAndSize {
    private Vector2F coords = new Vector2F();
    private Vector2F size = new Vector2F();

    @Override
    public Vector2F getCoords() {
        return coords;
    }

    @Override
    public Vector2F getSize() {
        return size;
    }

    /**
     * Создает новый экземпляр
     * @param x Координата центра по оси x
     * @param y Координата центра по оси y
     * @param width Ширина объекта
     * @param height Высота объекта
     */
    public SimpleCoordsAndSize(float x, float y, float width, float height) {
        this.coords.set(x, y);
        this.size.set(width, height);
    }
}

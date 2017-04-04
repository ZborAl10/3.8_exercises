package ru.myitschool.ufodestroyer.renderer;

import android.graphics.Canvas;

import ru.myitschool.ufodestroyer.model.entities.HasCoordsAndSize;

/**
 * Интерфейс для отрисовки определенного вида объектов
 */
public interface Renderer {
    /**
     * Рисует один объект
     * @param canvas канва для рисования
     * @param coordsAndSize координаты и размеры объекта
     */
    public void render(Canvas canvas, HasCoordsAndSize coordsAndSize);
}

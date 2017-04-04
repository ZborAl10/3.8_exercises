package ru.samsung.itschool.book.ufodesrtoer.model.util;

import android.graphics.RectF;

import ru.samsung.itschool.book.ufodesrtoer.model.Game;
import ru.samsung.itschool.book.ufodesrtoer.model.entities.HasCoordsAndSize;
import ru.samsung.itschool.book.ufodesrtoer.model.math.Vector2F;

/**
 * Вспомогательные методы
 */
public class Tools {

    /**
     * Проверяет, пересекаются ли два объекта, заданных прямоугольниками
     * @param o1 Первый объект
     * @param o2 Второй объект
     * @return true, если объекты пересекаются и false в противном случае
     */
    public static boolean intersects(HasCoordsAndSize o1, HasCoordsAndSize o2) {
        RectF rect1 = new RectF(o1.getCoords().x - (o1.getSize().x / 2),
                o1.getCoords().y - (o1.getSize().y / 2),
                o1.getCoords().x + (o1.getSize().x / 2),
                o1.getCoords().y + (o1.getSize().y / 2));

        RectF rect2 = new RectF(o2.getCoords().x - (o2.getSize().x / 2),
                o2.getCoords().y - (o2.getSize().y / 2),
                o2.getCoords().x + (o2.getSize().x / 2),
                o2.getCoords().y + (o2.getSize().y / 2));

        return RectF.intersects(rect1, rect2);
    }

    /**
     * Вычисляет координаты точки для рисования, зная координаты в игровом мире (см. {@link ru.samsung.itschool.book.ufodesrtoer.model.Game}
     * @param point Координаты точки в игровой модели
     * @param width ширина канвы для рисования
     * @param height высота канвы для рисования
     * @return координаты точки на канве, которая соответствует точке point в игровой модели
     */
    public static Vector2F gamePointToCanvasPoint(Vector2F point, float width, float height) {
        float scalingFactor = width / Game.FIELD_WIDTH;
        float x = point.x * scalingFactor + (width / 2f);
        float y = height - scalingFactor * point.y;
        return new Vector2F(x, y);
    }

    /**
     * Вычисляет координаты точки в игровом мире, зная координаты на канве (см. {@link ru.samsung.itschool.book.ufodesrtoer.model.Game}
     * @param point Координаты точки на канве
     * @param width ширина канвы для рисования
     * @param height высота канвы для рисования
     * @return координаты точки в игровой модели, которая соответствует точке point на канве
     */
    public static Vector2F canvasPointToGamePoint(Vector2F point, float width, float height) {
        float scalingFactor = width / Game.FIELD_WIDTH;
        float x = (point.x - (width / 2f)) / scalingFactor;
        float y = (height - point.y) / scalingFactor;
        return new Vector2F(x, y);
    }

    /**
     * Вычисляет расстояние или размер чего-либо на экране в пикселях, зная размеры в игровом мире
     * (см. {@link Game}
     * @param size расстояние или размер чего-либо в игровом мире
     * @param width ширина канвы для рисования
     * @param height высота канвы для рисования
     * @return расстояние в пикселях, соответствующее расстоянию size в игровом мире
     */
    public static float gameSizeToCanvasSize(float size, float width, float height) {
        float scalingFactor = width / Game.FIELD_WIDTH;
        return scalingFactor * size;
    }
}

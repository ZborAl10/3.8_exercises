package ru.samsung.itschool.book.ufodesrtoer.model.entities;

import ru.samsung.itschool.book.ufodesrtoer.model.math.Vector2F;

/**
 * Класс-предок для большинства игровых объектов, таких как пули и противники.
 * Хранит свои координаты на поле и размеры. Может хранить вспомогательный объект
 * для упрощения рендеринга
 */
public class GameObject implements HasCoordsAndSize {
    private Vector2F coords = new Vector2F();
    private Vector2F size = new Vector2F();
    private Object renderingHelper = null;

    /**
     * @return Координаты центра объекта
     */
    public Vector2F getCoords() {
        return coords;
    }

    /**
     * @return Размеры объекта (ширина и высота)
     */
    public Vector2F getSize() {
        return size;
    }

    /**
     * @return Вспомогательный объект для хранения дополнительных данных для отрисовки.
     * Например, в нем можно хранить текущую фазу анимации или другие вспомогательные данные,
     * которые нужно связать с игровым объектом, но которое не нужны для собственно расчета
     */
    public Object getRenderingHelper() {
        return renderingHelper;
    }

    /**
     * Задает вспомогательный объект для хранения дополнительных данных для отрисовки.
     * Например, в нем можно хранить текущую фазу анимации или другие вспомогательные данные,
     * которые нужно связать с игровым объектом, но которое не нужны для собственно расчета
     * @param renderingHelper объект, который можно будет получить с помощью getRenderingHelper()
     */
    public void setRenderingHelper(Object renderingHelper) {
        this.renderingHelper = renderingHelper;
    }
}

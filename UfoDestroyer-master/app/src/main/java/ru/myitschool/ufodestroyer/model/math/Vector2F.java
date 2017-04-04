package ru.myitschool.ufodestroyer.model.math;

import static java.lang.Math.sqrt;
import static java.lang.Math.abs;

/**
 * Двумерный вектор, который может отмечать точку в пространстве или какую-нибудь векторную
 * характеристику (например, скорость)
 */
public class Vector2F {
    public float x;
    public float y;

    /**
     * Создает нулевой вектор
     */
    public Vector2F() {
    }

    /**
     * Создает вектор по составляющим
     * @param x x составляющая вектора
     * @param y y составляющая вектора
     */
    public Vector2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Создает копию вектора
     * @param copy вектор, который нужно скопировать
     */
    public Vector2F(Vector2F copy) {
        this.x = copy.x;
        this.y = copy.y;
    }

    /**
     * Устанавливает составляющие вектора
     * @param x x составляющая вектора
     * @param y y составляющая вектора
     * @return Текущий вектор для удобства цепочки операций
     */
    public Vector2F set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Устанавливает составляющие вектора равными составляющим другого вектора
     * @param source Вектор, составляющие которого нужно взять
     * @return Текущий вектор для удобства цепочки операций
     */
    public Vector2F set(Vector2F source) {
        this.x = source.x;
        this.y = source.y;
        return this;
    }

    /**
     * @return Длину вектора
     */
    public float length() {
        return (float) sqrt(x * x + y * y);
    }

    /**
     * Нормализует вектор (делает его единичной длины). Изменяет текущий вектор и возвращает его же.
     * @throws IllegalStateException если вектор вырожденный (нулевой длины)
     * @return Текущий вектор для удобства цепочки операций
     */
    public Vector2F normalize() {
        float length = length();
        if (abs(length) < 0.00001)
            throw new IllegalStateException("Degenerate vector cannot be normalized");
        x /= length;
        y /= length;
        return this;
    }

    /**
     * Прибавляет к вектору другой вектор. Изменяет текущий вектор и возвращает его
     * @param summand вектор, который нужно прибавить к этому вектору
     * @return Текущий вектор для удобства цепочки операций
     */
    public Vector2F add(Vector2F summand) {
        x += summand.x;
        y += summand.y;
        return this;
    }

    /**
     * Прибавляет к вектору другой вектор, заданный своими составляющими. Изменяет текущий вектор и
     * возвращает его
     * @param x x составляющая прибавляемого вектора
     * @param y y составляющая прибавляемого вектора
     * @return Текущий вектор для удобства цепочки операций
     */
    public Vector2F add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Умножает текущий вектор на factor. Изменяет текущий вектор и возвращает его
     * @param factor множитель
     * @return Текущий вектор для удобства цепочки операций
     */
    public Vector2F multiply(float factor) {
        this.x *= factor;
        this.y *= factor;
        return this;
    }
}

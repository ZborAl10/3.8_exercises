package ru.myitschool.ufodestroyer.renderer;

import android.graphics.Point;

/**
 * Вспомогательные вычисления для рендерера
 */
public class RendererTools {
    /**
     * Расчитывает новые размеры прямоугольника с сохранением соотношения сторон таким образом,
     * чтобы он помещался (был вписан) в другой прямоугольник.
     * @param whatToFit прямоугольник, который нужно вписать. x - ширина, y - высота
     * @param fitArea прямоугольник, в который нужно вписать. x - ширина, y - высота
     * @return прямоугольник, который помещается в fitArea (таким образом, результат совпадает с
     *         fitArea или по ширине, или по высоте, или по тому и другому) и имеет то же
     *         соотношение сторон, что и whatToFit. x - ширина, y - высота
     * @throws IllegalArgumentException если ширина или высота любого из параметров равна нулю
     */
    public static Point calcFitIntoSize(Point whatToFit, Point fitArea) {
        if (whatToFit.x == 0 || whatToFit.y == 0 || fitArea.x == 0 || fitArea.y == 0)
            throw new IllegalArgumentException("Degenerate rectangles not allowed");

        double scaleX = ((double)fitArea.x) / whatToFit.x;
        double scaleY = ((double)fitArea.y) / whatToFit.y;
        if (scaleX < scaleY) {
            return new Point(fitArea.x, (int) Math.round(scaleX * whatToFit.y));
        } else {
            return new Point((int) Math.round(scaleY * whatToFit.x), fitArea.y);
        }
    }

    /**
     * Расчитывает новые размеры прямоугольника с сохранением соотношения сторон таким образом,
     * чтобы он вмещал в себя другой прямоугольник (был описан вокруг него).
     * @param whatToFit прямоугольник, вокруг которого нужно описать другой. x - ширина, y - высота
     * @param fitArea прямоугольник, который должен вмещаться в whatToFit. x - ширина, y - высота
     * @return прямоугольник, в который помещается fitArea (таким образом, результат совпадает с
     *         fitArea или по ширине, или по высоте, или по тому и другому) и имеет то же
     *         соотношение сторон, что и whatToFit. x - ширина, y - высота
     * @throws IllegalArgumentException если ширина или высота любого из параметров равна нулю
     */
    public static Point calcFitOutroSize(Point whatToFit, Point fitArea) {
        if (whatToFit.x == 0 || whatToFit.y == 0 || fitArea.x == 0 || fitArea.y == 0)
            throw new IllegalArgumentException("Degenerate rectangles not allowed");

        double scaleX = ((double)fitArea.x) / whatToFit.x;
        double scaleY = ((double)fitArea.y) / whatToFit.y;
        if (scaleX > scaleY) {
            return new Point(fitArea.x, (int) Math.round(scaleX * whatToFit.y));
        } else {
            return new Point((int) Math.round(scaleY * whatToFit.x), fitArea.y);
        }
    }


}

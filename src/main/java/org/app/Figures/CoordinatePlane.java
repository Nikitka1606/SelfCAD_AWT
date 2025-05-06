package org.app.Figures;

import java.awt.*;
import java.awt.geom.Point2D;

public class CoordinatePlane extends Figure {
    private final Point2D.Double origin; // начало координат на экране
    private double scale; // масштаб

    public CoordinatePlane(int width, int height) {
        this.origin = new Point2D.Double(0, height);
        this.scale = 1 / 5.0;
        preferredColor = Color.WHITE;
    }

    public CoordinatePlane(int width, int height, boolean isTemp) {
        if (isTemp) {
            this.scale = 0.1;
            this.origin = new Point2D.Double(0, 0);
        }
        else {
            this.scale = 1 / 5.0;
            this.origin = new Point2D.Double(0, height);
        }
        preferredColor = Color.WHITE;
    }

    public Point2D.Double toGlobal(Point2D.Double localPoint) {
        // Перевод координат точки на плоскости относительно нуля в экранные
        double globalX = (origin.x + localPoint.x * scale);
        double globalY = (origin.y + localPoint.y * scale);
        return new Point2D.Double(globalX, globalY);
    }

    public Point2D.Double toLocal(Point2D.Double globalPoint) {
        // Перевод глобальных координат в координаты точки на экране
        double localX = ((globalPoint.x - origin.x) / scale);
        double localY = ((globalPoint.y - origin.y) / scale);
        return new Point2D.Double(localX, localY);
    }

    public void zoom(double factor, Point2D.Double zoomCenter) {
        if (scale * factor <= 0.2 && scale * factor >= 0.001) {
            Point2D.Double localZoomCenter = toLocal(zoomCenter);
            scale *= factor;

            origin.x = zoomCenter.x - Math.round(localZoomCenter.x * scale);
            origin.y = zoomCenter.y - Math.round(localZoomCenter.y * scale);
        }
    }

    @Override
    public void moveFigure(double dx, double dy) {
        origin.x += dx;
        origin.y += dy;
    }

    public double getScale() {
        return scale;
    }

    public void drawAxes(Graphics2D g, int panelWidth, int panelHeight) {
        g.setColor(Color.WHITE);

        g.drawLine(0, (int) origin.y, panelWidth, (int) origin.y);

        g.drawLine((int) origin.x, 0, (int) origin.x, panelHeight);

        drawPoint(g, origin);

        //TODO Надо нарисовать стрелки и деления и координатную сетку и чтобы взрывы были
    }
}

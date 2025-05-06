package org.app.Tools.ToolHelp;

import org.app.Figures.DrawnPoint;

import java.awt.geom.Point2D;
import java.util.List;

public class PointRotator {
    private final List<DrawnPoint> points;

    public PointRotator(List<DrawnPoint> points) {
        this.points = points;
    }

    private static Point2D.Double rotatePoint(Point2D.Double point, Point2D.Double center, double angleRadians) {

        double translatedX = point.x - center.x;
        double translatedY = point.y - center.y;

        double rotatedX = translatedX * Math.cos(angleRadians) - translatedY * Math.sin(angleRadians);
        double rotatedY = translatedX * Math.sin(angleRadians) + translatedY * Math.cos(angleRadians);

        return new Point2D.Double(rotatedX + center.x, rotatedY + center.y);
    }

    public void rotatePoints(Point2D.Double point) {
        double angle = Math.atan2(point.y - points.get(0).start.y, point.x - -points.get(0).start.x);
        Point2D.Double center = points.get(0).start;
        for (int i = 1; i < points.size(); i++) {
            points.get(i).start = rotatePoint(points.get(i).start, center, angle);
        }
    }

    public void rotatePoints(double angle) {
        angle = Math.toRadians(angle);
        Point2D.Double center = points.get(0).start;
        for (int i = 1; i < points.size(); i++) {
            points.get(i).start = rotatePoint(points.get(i).start, center, angle);
        }
    }
}

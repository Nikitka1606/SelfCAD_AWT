package org.app.Figures;

import java.awt.geom.Point2D;

public class ThreePointCircle extends Circle {
    public ThreePointCircle() {
        figureType = FigureType.THREE_POINT_CIRCLE;

        maxChildren = 4;
    }

    @Override
    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {

        children.add(new DrawnPoint(point, plane, true, this));

        if (children.size() == 3) {
            Point2D.Double p1 = children.get(0).start;
            Point2D.Double p2 = children.get(1).start;
            Point2D.Double p3 = children.get(2).start;

            Point2D mid1 = new Point2D.Double((p1.x + p2.x) / 2.0, (p1.y + p2.y) / 2.0);
            Point2D mid2 = new Point2D.Double((p2.x + p3.x) / 2.0, (p2.y + p3.y) / 2.0);

            double dx1 = p2.x - p1.x;
            double dy1 = p2.y - p1.y;
            double dx2 = p3.x - p2.x;
            double dy2 = p3.y - p2.y;

            Point2D perp1 = new Point2D.Double(-dy1, dx1);
            Point2D perp2 = new Point2D.Double(-dy2, dx2);

            children.add(0, new DrawnPoint());

            children.get(0).start = intersectLines(mid1, perp1, mid2, perp2);
            if (children.get(0).start != null) {
                this.radius = children.get(0).start.distance(p1);
            }

            this.figureType = FigureType.CIRCLE;
        }

        if (isDrown()) {
            children.get(3).makeInvisible();
            children.get(2).makeInvisible();
            //children.get(1).makeInvisible(); // TODO: понять как удалять лишние точки

            updateParams();
            getInfo();
        }
    }

    private Point2D.Double intersectLines(Point2D p1, Point2D d1, Point2D p2, Point2D d2) {
        double det = d1.getX() * d2.getY() - d1.getY() * d2.getX();
        if (Math.abs(det) < 1e-10) {
            return null; // Линии параллельны, центр не определен
        }

        double t = ((p2.getX() - p1.getX()) * d2.getY() - (p2.getY() - p1.getY()) * d2.getX()) / det;
        return new Point2D.Double(p1.getX() + t * d1.getX(), (p1.getY() + t * d1.getY()));
    }
}

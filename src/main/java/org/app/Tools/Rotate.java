package org.app.Tools;

import org.app.Tools.ToolHelp.PointRotator;
import org.app.Figures.DrawnPoint;
import org.app.Figures.Figure;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Rotate {
    List<Figure> figures;
    List<DrawnPoint> points;
    PointRotator rotator;

    public Rotate(List<Figure> figures) {
        this.figures = figures;
        this.points = new ArrayList<>();

        for (Figure f : figures)
            findAllPoints(f);

        rotator = new PointRotator(points);
        //System.out.println(figures);
        System.out.println(points.size());
        //rotator = new PointRotator();
    }

    private void findAllPoints(Figure figure) {
        if (figure instanceof DrawnPoint p && !points.contains(p)) {
            points.add(p);
            //return;
        }

        for (Figure c : figure.getChildren()) {
            if (!c.getChildren().isEmpty())
                findAllPoints(c);
            else if (c instanceof DrawnPoint p && !points.contains(p)) {
                points.add(p);
            }
        }
    }

    public void rotate(Point2D.Double point) {
        rotator.rotatePoints(point);
    }

    public void rotate(double angle) {
        rotator.rotatePoints(angle);
    }

}

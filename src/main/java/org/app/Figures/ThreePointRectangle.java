package org.app.Figures;

import org.app.Constrains.Constrain;
import org.app.Constrains.Perpendicular;

import java.awt.geom.Point2D;

public class ThreePointRectangle extends Rectangle {
    public ThreePointRectangle() {
        this.figureType = FigureType.THREE_POINT_RECTANGLE;
    }

    @Override
    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {
        children.add(new DrawnPoint(point, plane, this));

        if (children.size() == 3) {
            double dx = children.get(1).start.x - children.get(0).start.x;
            double dy = children.get(1).start.y - children.get(0).start.y;

            height = Math.sqrt(dx * dx + dy * dy);

            double nx = -dy / height;
            double ny = dx / height;

            width = (children.get(2).start.x - children.get(0).start.x) * nx + (children.get(2).start.y - children.get(0).start.y) * ny;

            children.remove(2);

            children.add(new DrawnPoint(
                    plane.toGlobal(new Point2D.Double((children.get(1).start.x + nx * width),
                            (children.get(1).start.y + ny * width))),
                    plane, this));

            children.add(new DrawnPoint(
                    plane.toGlobal(new Point2D.Double((children.get(0).start.x + nx * width),
                            (children.get(0).start.y + ny * width))),
                    plane, this));

            children.add(new Line(children.get(0), children.get(1), this)); // 4
            children.add(new Line(children.get(1), children.get(2), this)); // 5
            children.add(new Line(children.get(2), children.get(3), this)); // 6
            children.add(new Line(children.get(3), children.get(0), this)); // 7

            Constrain perp = new Perpendicular(children.get(4), children.get(5), children.get(1));
            children.get(4).addConstrain(perp);
            children.get(5).addConstrain(perp);

            Constrain perp1 = new Perpendicular(children.get(5), children.get(6), children.get(2));
            children.get(5).addConstrain(perp1);
            children.get(6).addConstrain(perp1);

            Constrain perp2 = new Perpendicular(children.get(6), children.get(7), children.get(3));
            children.get(6).addConstrain(perp2);
            children.get(7).addConstrain(perp2);

            Constrain perp3 = new Perpendicular(children.get(7), children.get(4), children.get(0));
            children.get(7).addConstrain(perp3);
            children.get(4).addConstrain(perp3);

            checkConstrains();
            //updateParams();
            this.figureType = FigureType.RECTANGLE;

            if (width < 0) width *= -1;
            if (height < 0) height *= -1;
        }

        if (children.size() > 1 && children.get(1) != null) {
            if (children.get(1) instanceof DrawnPoint p) {
                return;
            }
        }

        if (children.get(0) instanceof DrawnPoint p) {
        }

    }
}

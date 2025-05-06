package org.app.Figures;

import org.app.Constrains.Constrain;
import org.app.Structures.DXFColorUtils;
import org.app.Structures.Measurement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Figure extends JPanel {
    public List<Boolean> updated;
    public Point2D.Double start;
    protected FigureType figureType;
    protected Color color;
    protected Color preferredColor;
    protected Color selectionColor;
    protected Color highlighColor;
    protected StrokeType strokeType;
    protected Stroke stroke;
    protected boolean highlighted = false;
    protected boolean selected = false;
    protected boolean isDrawable = true;
    protected List<Figure> parentFigure;
    protected List<Figure> children;
    protected int maxChildren = 1;
    protected int curChildrenCount = 0;
    protected ArrayList<Measurement> info;
    protected List<Double> basicParameters;
    protected List<Constrain> constrains;
    protected boolean isFixed;
    private boolean isSupport = false;
    private float hatchScale = 1.0f;

    public Figure() {
        strokeType = StrokeType.FULL_THICK;
        stroke = new BasicStroke(3f);
        children = new ArrayList<>();
        parentFigure = new ArrayList<>();
        basicParameters = new ArrayList<>();
        constrains = new ArrayList<>();
        updated = new ArrayList<>();
        isFixed = false;
        //setDefaultType();
        setPreferredColor();
    }

    public void setSupport() {
        this.isSupport = true;
        /*this.stroke = new BasicStroke(2f);
        this.preferredColor = Color.MAGENTA;
        this.selectionColor = Color.CYAN;
        this.highlighColor = Color.WHITE;
        this.setPreferredColor();*/
    }

    public void setDefaultType() {
        this.isSupport = false;
        /*this.preferredColor = Color.RED;
        this.selectionColor = Color.WHITE;
        this.highlighColor = Color.BLUE;
        this.setPreferredColor();*/
    }

    protected void setNotUpdated() {
        updated.replaceAll(ignored -> false);
    }

    protected void setUpdates() {
        for (Double bp : basicParameters) {
            updated.add(false);
        }
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setStart(Float x, Float y) {
        start.x = x;
        start.y = y;
        updated.set(0, true);
    }

    public void fix() {
        isFixed = true;
    }

    public void unfix() {
        isFixed = false;
    }

    public void makeInvisible() {
        isDrawable = false;
    }

    public void makeVisible() {
        isDrawable = true;
    }

    public double getBasicParameter(int index) {
        if (basicParameters.size() == 0)
            return -1;
        return basicParameters.get(index);
    }

    public void setBasicParameter(int index, Double value) {
        if (index < basicParameters.size())
            basicParameters.set(index, value);
        updateChildren();
        updated.set(index, false);
    }

    public java.util.List<Figure> getParentFigure() {
        return parentFigure;
    }

    public void setParentFigure(Figure parentFigure) {
        this.parentFigure.add(parentFigure);
    }

    public void updateBasicParameter(int index) {

    }

    public void highlight() {
        if (!selected)
            highlighted = true;

        for (Figure f : parentFigure) {
            if (f != null && f.selected) {
                highlighted = true;
                break;
            }
        }

        for (Figure c : children)
            c.highlight();
    }

    public void select() {
        selected = true;
        highlighted = false;

        for (Figure c : children)
            c.select();
    }

    public void deselect() {
        for (Figure f : parentFigure) {
            if (f != null) {
                selected = f.selected;
            } else {
                selected = false;
            }
        }
        if (parentFigure.isEmpty()) {
            selected = false;
            return;
        }
        if (!children.isEmpty()) {
            for (Figure c : children)
                c.deselect();
        }
    }

    public void dehighlight() {
        if (parentFigure.isEmpty()) {
            highlighted = false;
            return;
        }
        for (Figure f : parentFigure) {
            if (f != null && f.highlighted) {
                highlighted = true;
                return;
            }
        }
        highlighted = false;
    }

    public FigureType getFigureType() {
        return figureType;
    }

    public boolean isDrown() {
        return children.size() == maxChildren;
    }

    public void gatherInfo() {
    }

    public ArrayList<Measurement> getInfo() {
        updateParams();
        gatherInfo();
        return info;
    }

    public void updateParams() {
        for (int i = 0; i < basicParameters.size(); i++)
            updateBasicParameter(i);
    }

    public void updateChildren() {
    }

    // обновляет значения переменных фигуры
    public void updateValuesFromInput() {
    }

    // только принимает новые параметры для фигуры и вызывает методы для обновления
    public void setValuesUpdatedFromInfo(/*ArrayList<Measurement> info*/) {
    }

    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {
        children.add(new DrawnPoint(point, plane, this));
        if (isDrown()) {
            updateParams();
            getInfo();
        }
    }

    public Point2D.Double setNextPoint(Figure magnetPoint, Figure parent, CoordinatePlane plane) {
        //setNextPoint(plane.toGlobal(magnetPoint.start), plane);
        this.addChildren(magnetPoint);
        //magnetPoint.parentFigure.add(parent);
        //this.start = magnetPoint.start;

        return null;
    }

    public void setPreferredColor() {
        this.color = preferredColor;
    }

    public void setStroke(StrokeType strokeType, double scale) {
        this.strokeType = strokeType;
        float strokeScale = (float) scale * 30 * hatchScale;

        switch (strokeType) {
            case FULL_THICK:
                stroke = new BasicStroke(3f);
                break;
            case FULL_THIN:
                stroke = new BasicStroke(1f);
                break;

            case DASHED:
                float[] dash1 = {10f * strokeScale, 10f * strokeScale};
                stroke = new BasicStroke(3f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        100f, dash1, 0f);
                break;
            case DASH_DOTTED:
                float[] dash2 = {10 * strokeScale, 10 * strokeScale, strokeScale, 10 * strokeScale};
                stroke = new BasicStroke(3f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND,
                        100f, dash2, 0f);
                break;
            case DOUBLE_DASH_DOTTED:
                float[] dash3 = {10 * strokeScale, 5 * strokeScale, strokeScale, 5 * strokeScale, strokeScale, 5 * strokeScale};
                stroke = new BasicStroke(3f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND,
                        1000f, dash3, 0f);
                setDefaultType();
                break;
            case SUPPORT:
                stroke = new BasicStroke(2f);
                setSupport();
                break;
        }
    }

    public float getHatchScale() {
        return hatchScale;
    }

    public void setHatchScale(float hatchScale) {
        this.hatchScale = hatchScale;
    }

    public void moveFigure(double dx, double dy) {
        for (Figure c : children) {
            c.moveFigure(dx, dy);
        }
    }

    protected void drawMethod(Graphics2D g, CoordinatePlane plane) {
    }

    public void drawFigure(Graphics2D g, CoordinatePlane plane) {
        this.setColor(preferredColor);
        if (highlighted) this.setColor(highlighColor);
        else if (selected) this.setColor(selectionColor);

        setStroke(strokeType, plane.getScale());

        g.setColor(color);
        g.setStroke(stroke);

        drawMethod(g, plane);
    }

    protected void drawPoint(Graphics2D g, Point2D.Double globalpoint, Color color) {
        g.setColor(color);
        g.fillOval((int) globalpoint.x - 5, (int) globalpoint.y - 5, 5 * 2, 5 * 2);
    }

    protected void drawPoint(Graphics2D g, Point2D.Double globalpoint) {
        g.setColor(Color.GREEN);
        g.fillOval((int) globalpoint.x - 5, (int) globalpoint.y - 5, 5 * 2, 5 * 2);
    }

    public int getCurNumOfChildren() {
        return curChildrenCount;
    }

    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        return false;
    }

    protected boolean childrenContain(Point2D.Double p, CoordinatePlane plane) {
        for (Figure f : children) {
            if (f.contains(p, plane))
                return true;
        }
        return false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Figure> getFigureChildren() {
        return children;
    }

    public void setFigureChildren(Figure old, Figure update) {
        if (!children.isEmpty())
            children.set(children.indexOf(old), update);
    }

    public void deleteFigureChildren() {
        children.clear();
    }

    public List<Figure> getChildren() {
        return children;
    }
    public void addChildren(Figure c) {
        c.parentFigure.add(this);
        this.children.add(c);
    }

    public void addConstrain(Constrain c) {
        constrains.add(c);
    }

    public void checkConstrains() {
        for (Constrain c : constrains) {
            c.checkConstrain(this);
        }
        this.updateParams();
    }

    public void getConstrains() {

    }

    public void checkNeighbours() {
        for (Constrain c : constrains)
            c.checkConstrain(this);
    }

    public boolean isChild() {
        return !parentFigure.isEmpty();
    }

    protected void writeToDXF(BufferedWriter writer) throws IOException {
        writer.write("0\nPOLYLINE\n");
        writer.write("8\n0\n");
        writeColor(writer);
        writer.write("66\n1\n");
        writer.write("0\nSEQEND\n");
    }

    protected void writeColor(BufferedWriter writer) throws IOException {
        DXFColorUtils.writeAwtColor(writer, color);
    }

    @Override
    public String toString() {
        return this.figureType.toString();
    }

    public StrokeType getStroke() {
        return this.strokeType;
    }

    protected String toDxfLineType(StrokeType strokeType) {
        return switch (strokeType) {
            case FULL_THICK, FULL_THIN -> "CONTINUOUS";
            case DASHED -> "DASHED";
            case DASH_DOTTED, DOUBLE_DASH_DOTTED -> "DASHDOT";
            case SUPPORT -> "HIDDEN";
        };
    }

    public void setFromDXF(String st) {
        switch (st) {
            case "DASHED" -> strokeType = StrokeType.DASHED;
            case "DASHDOT", "CENTER" -> strokeType = StrokeType.DASH_DOTTED;
            case "DASHDOTDOT" -> strokeType = StrokeType.DOUBLE_DASH_DOTTED;
            case "HIDDEN" -> strokeType = StrokeType.SUPPORT;
            default -> strokeType = StrokeType.FULL_THICK;
        }
    }

    public void removeParent(Figure parent) {
        if (!this.parentFigure.isEmpty())
            this.parentFigure.remove(parent);
    }

    protected int toDxfLineWeight(StrokeType strokeType) {
        return switch (strokeType) {
            case FULL_THICK -> 40;
            case FULL_THIN -> 13;
            case DASHED, DASH_DOTTED, DOUBLE_DASH_DOTTED -> 18;
            case SUPPORT -> 5;
        };
    }

    public String mapFigureTypeToDxfEntity() {
        return switch (this.figureType) {
            case POINT -> "POINT";
            case LINE, CONTROL_TANGENT -> "LINE";
            case RECTANGLE, THREE_POINT_RECTANGLE, POLYGON_DESC, POLYGON_INC -> "POLYLINE";
            case CIRCLE, THREE_POINT_CIRCLE -> "CIRCLE";
            case ARC -> "ARC";
            case B_SPLINE, BEZIER_SPLINE -> "SPLINE";
            default -> "UNKNOWN";
        };
    }

    protected void writeLineTypeAndWeight(BufferedWriter writer, StrokeType strokeType) throws IOException {
        writer.write("6\n" + toDxfLineType(strokeType) + "\n");
        writer.write("370\n" + toDxfLineWeight(strokeType) + "\n");
    }

    public void DXFWrite(BufferedWriter writer) throws IOException {
        writer.write("0\n" + mapFigureTypeToDxfEntity() + "\n");
        writer.write("8\n0\n");

        writeLineTypeAndWeight(writer, this.strokeType);

        writeColor(writer);

        writeToDXF(writer);
    }
}

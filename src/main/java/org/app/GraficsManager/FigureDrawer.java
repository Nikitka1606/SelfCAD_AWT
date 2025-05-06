package org.app.GraficsManager;

import org.app.Interface.FigureInfo;
import org.app.Interface.IconsPanel;
import org.app.Figures.*;
import org.app.Structures.FiguresKeeper;
import org.app.Tools.Rotate;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;

public class FigureDrawer {
    private final FiguresKeeper figuresKeeper;
    private final DrawingPanel drawingPanel;
    private final IconsPanel iconsPanel;
    private final CoordinatePlane coordinatePlane;
    public boolean isPointsRotating = false;
    private boolean isFirstClick = true;
    private FigureType currentTool = null;
    private FigureType prevTool = null;
    private Figure currentFigure;
    private Point2D.Double lastSetPoint;
    private final FigureInfo figureInfo;
    private Rotate rotate;

    public FigureDrawer(FiguresKeeper figuresKeeper, DrawingPanel drawingPanel, IconsPanel iconsPanel, CoordinatePlane coordinatePlane, FigureInfo figureInfo) {

        this.figuresKeeper = figuresKeeper;
        this.drawingPanel = drawingPanel;
        this.iconsPanel = iconsPanel;
        this.coordinatePlane = coordinatePlane;
        this.figureInfo = figureInfo;
        this.lastSetPoint = new Point2D.Double(0, 0);
    }

    public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }

    public void setLastSetPoint(DrawnPoint point) {
        this.lastSetPoint = point.start;
    }

    public void setNextPoint(List<Double> inputs, String coordinateSystem, String CStype) {
        // IDK how to prettify this one
        if (currentFigure instanceof Arc arc && inputs.size() == 1) {
            double radius = inputs.get(0);
            setNextPoint(coordinatePlane.toGlobal(arc.computeCircle(radius * 100)));

        } else if (currentFigure instanceof BezierSpline spline) {
            spline.setMaxChildren();
        } else if (currentFigure instanceof BSpline spline) {
            spline.setMaxChildren();
        } else if (isPointsRotating) {
            rotate.rotate(inputs.get(0));
            isPointsRotating = false;
            drawingPanel.repaint();
        } else if (inputs.size() < 2) {
            throw new IllegalArgumentException("Expected at least 2 inputs for x and y coordinates.");
        } else {
            System.out.println(inputs);

            Point2D.Double point = new Point2D.Double();

            if (Objects.equals(CStype, "polar")) {
                point.x = (inputs.get(0) * Math.cos(Math.toRadians(inputs.get(1)) + Math.PI / 2));
                point.y = -(inputs.get(0) * Math.sin(Math.toRadians(inputs.get(1)) + Math.PI / 2));
            } else
                point = new Point2D.Double(inputs.get(0), -inputs.get(1));

            //Measurement measurement = new Measurement();
            if (Objects.equals(coordinateSystem, "local")) {
                point.x += lastSetPoint.x;
                point.y += lastSetPoint.y;
            }

            point = coordinatePlane.toGlobal(point);

            //System.out.println(lastSetPoint);

            setNextPoint(point);
        }
    }

    public void setNextPoint(Point2D.Double point) {
        lastSetPoint = coordinatePlane.toLocal(point);

        if (isToolChanged() && currentFigure != null && !currentFigure.isDrown()) {
            figuresKeeper.deleteFigure(currentFigure);
            onFirstClick();
        }

        if (isFirstClick) {
            onFirstClick();
        }

        if (currentFigure != null) {
            if (!currentFigure.isDrown()) {
                Figure highlighted = figuresKeeper.getHighlightedFigure();
                if (highlighted != null) {
                    if (highlighted.getFigureType() == FigureType.POINT) {
                        currentFigure.setNextPoint(highlighted, currentFigure, coordinatePlane);
                        //currentFigure.addChildren(highlighted);

                    }
                } else {
                    currentFigure.setNextPoint(point, coordinatePlane);
                }

                //System.out.println(lastSetPoint);

                prevTool = currentTool;
                figureInfo.showInfo();
            } else if (currentFigure.isDrown()) {
                isFirstClick = true;
                prevTool = currentTool;
                currentTool = null;
                currentFigure = null;
                iconsPanel.setCurrentFigureNull();
            }
            assert currentFigure != null;
            figuresKeeper.addFigureChildren(currentFigure);
        }
        drawingPanel.repaint();
    }

    private boolean isToolChanged() {
        currentTool = iconsPanel.getSelectedFigure();
        //System.out.println("Tool updated!");
        //figureInfo.showInfo(lastSetPoint);
        return prevTool != currentTool;
    }

    public void onFirstClick() {
        if (iconsPanel.getSelectedFigure() != null) {
            currentFigure = createFigure(iconsPanel.getSelectedFigure());

            drawingPanel.setCurrentFigure(currentFigure);

            figuresKeeper.addFigure(currentFigure);

            drawingPanel.repaint();

            currentTool = currentFigure.getFigureType();

            isFirstClick = false;
        }
    }

    private Figure createFigure(FigureType figureType) {
        switch (figureType) {
            case LINE -> {
                return new Line();
            }
            case POINT -> {
                return new DrawnPoint();
            }
            case CIRCLE -> {
                return new Circle();
            }
            case THREE_POINT_CIRCLE -> {
                return new ThreePointCircle();
            }
            case RECTANGLE -> {
                return new Rectangle();
            }
            case THREE_POINT_RECTANGLE -> {
                return new ThreePointRectangle();
            }
            case ARC -> {
                return new Arc();
            }
            case POLYGON_DESC -> {
                return new PolygonDescribed();
            }
            case POLYGON_INC -> {
                return new PolygonIncribed();
            }
            case BEZIER_SPLINE -> {
                return new BezierSpline();
            }
            case B_SPLINE -> {
                return new BSpline();
            }
            default -> {
                return null;
            }
        }
    }
}

package org.app.Structures;

import org.app.Constrains.Constrain;
import org.app.Figures.CoordinatePlane;
import org.app.Figures.DrawnPoint;
import org.app.Figures.Figure;
import org.app.Figures.FigureType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class FiguresKeeper extends JPanel {
    private List<Figure> figures;
    private final List<Constrain> constrains;
    private final List<Figure> selectedFigure;
    private final CoordinatePlane coordinatePlane;
    private Figure highlightedFigure;

    public FiguresKeeper(CoordinatePlane coordinatePlane) {
        figures = new ArrayList<>();
        constrains = new ArrayList<>();
        selectedFigure = new ArrayList<>();
        this.coordinatePlane = coordinatePlane;
    }

    public void update() {
        /*for (Figure f : figures) {
            if (f.isDrown()) {
                f.updateParams();
                //f.checkConstrains();
            }
        }*/
        for (Figure f : figures) {
            if (f.isDrown()) {
                //f.updateParams();
                f.updateChildren();
                //f.checkConstrains();
            }
        }
    }

    public void updateParameters() {
        for (Figure f : figures) {
            if (f.isDrown()) {
                f.updateParams();
            }
        }
    }

    public void updateChildren() {
        for (Figure f : figures) {
            if (f.isDrown()) {
                f.updateChildren();
            }
        }
    }

    public void addFigure(Figure figure) {
        if (!(figure instanceof DrawnPoint)) {
            figures.add(0, figure);
        } else {
            figures.add(figure);
        }
    }

    public void addFigure(List<Figure> figure) {
        for (Figure f : figure) {
            if (!(f instanceof DrawnPoint)) {
                figures.add(0, f);
            } else {
                figures.add(f);
            }
        }
    }

    public void addFigureChildren(Figure curFig) {
        if (curFig != null) {
            for (Figure f : curFig.getFigureChildren())
                if (!figures.contains(f)) {
                    if (f.getFigureType() != FigureType.POINT)
                        figures.add(0, f);
                    else
                        figures.add(f);
                    addFigureChildren(f);
                }
        }
    }

    public void deleteFigure(Figure figure) {
        for (Figure c : figure.getFigureChildren()) {
            c.removeParent(figure);
            if (c instanceof DrawnPoint) {
                if (c.getParentFigure().size() <= 1) {
                    figures.remove(c);
                }
            } else {
                if (!c.getFigureChildren().isEmpty()) {
                    deleteFigure(c);
                }
                figures.remove(c);
            }
            //figures.remove(c);
        }


        /*for (Figure c : figure.getFigureChildren()) {
            if (c.getFigureType() == FigureType.POINT && c.getParentFigure().size() > 1) {
                c.removeParent(figure);
                System.out.println("tried to delete children point");
                System.out.println(c.getParentFigure());
                if (c.getParentFigure().get(0) == null)
                    c.getParentFigure().clear();
                continue;
            }
            if (!c.getFigureChildren().isEmpty()) {
                deleteFigure(c);
            }
            figures.remove(c);
        }*/
        figures.remove(figure);
        System.out.println(figures);
    }

    public List<Figure> getFigures() {
        return figures;
    }

    public void setNewFigures(List<Figure> figures) {
        this.figures.clear();

        addFigure(figures);
    }

    public void addNewFigures(List<Figure> figures) {
        addFigure(figures);
    }

    public Figure getCurrentFigure() {
        if (selectedFigure != null && !selectedFigure.isEmpty())
            return selectedFigure.get(selectedFigure.size() - 1);
        else
            return null;
    }

    public List<Figure> getSelectedFigure() {
        return selectedFigure;
    }

    public void addConstrain() {

    }

    public void zoomAll(double scale, Point2D.Double center) {
        coordinatePlane.zoom(scale, center);
    }

    private boolean isAllFiguresDrown() {
        for (Figure f : selectedFigure)
            if (!f.isDrown())
                return false;

        return true;
    }


    public void deleteSelectedFigure() {
        if (selectedFigure != null && !(selectedFigure instanceof DrawnPoint)) {
            if (isAllFiguresDrown()) {
                for (Figure f : selectedFigure)
                    if (f.getParentFigure().isEmpty())
                        deleteFigure(f);

                selectedFigure.clear();
            }
        }
        //System.out.println(figures);
    }

    public void selectFigure(Point2D.Double p) {
        for (Figure figure : figures)
            figure.deselect();

        selectedFigure.clear();
        for (int i = figures.size() - 1; i >= 0; i--) {
            Figure f = figures.get(i);
            if (f.contains(p, coordinatePlane)) {
                if (!selectedFigure.contains(f)) {
                    selectedFigure.add(f);
                    f.select();
                    break;
                }
            }
        }
    }

    public void addFigureToSelection(Point2D.Double p) {
        for (int i = figures.size() - 1; i >= 0; i--) {
            if (figures.get(i).contains(p, coordinatePlane)) {
                if (figures.get(i).isSelected()) {
                    figures.get(i).deselect();
                    selectedFigure.remove(figures.get(i));
                } else {
                    figures.get(i).select();
                    selectedFigure.add(figures.get(i));
                }
                return;
            }
        }
    }

    public void highlightFigure(Point2D.Double p) {
        for (Figure figure : figures) {
            figure.dehighlight();
        }

        highlightedFigure = null;

        for (int i = figures.size() - 1; i >= 0; i--) {
            if (figures.get(i).contains(p, coordinatePlane)) {
                figures.get(i).highlight();
                highlightedFigure = figures.get(i);
                break;
            }
        }
    }

    public void drawAll(Graphics2D g) {
        //update();
        for (Figure figure : figures) {
            if (figure != null) {
                figure.drawFigure(g, coordinatePlane);
            }
        }
    }

    // TODO: методы, которые должны искать среди массива переданных точек, координаты которых совпадают и соединять/разъединять их
    public void joinPoints() {
        for (Figure dp: figures) {
            if (dp instanceof DrawnPoint) {

            }
        }
    }

    public void unjoinPoints() {
        for (Figure dp: figures) {
            if (dp instanceof DrawnPoint) {

            }
        }
    }

    public Figure getHighlightedFigure() {
        return highlightedFigure;
    }
}

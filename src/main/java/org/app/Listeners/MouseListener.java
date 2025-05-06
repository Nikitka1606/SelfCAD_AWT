package org.app.Listeners;

import org.app.Figures.CoordinatePlane;
import org.app.GraficsManager.DrawingPanel;
import org.app.GraficsManager.FigureDrawer;
import org.app.Structures.FiguresKeeper;
import org.app.Interface.FigureInfo;
import org.app.Interface.InfoSpawnInput;
import org.app.Figures.DrawnPoint;
import org.app.Tools.Rotate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

public class MouseListener extends MouseAdapter {
    private final FiguresKeeper figuresKeeper;
    private final DrawingPanel drawingPanel;
    private final CoordinatePlane coordinatePlane;
    private final FigureInfo figureInfo;
    public boolean isPointsRotating = false;
    private final FigureDrawer figureDrawer;
    private boolean wheelDragging = false;
    private boolean lmbDragging = false;
    private Rotate rotate;
    private Point prevCursorPos;
    private Point curCursorPos;
    private final InfoSpawnInput spawnInput;

    public MouseListener(DrawingPanel drawingPanel,
                         FigureInfo figureInfo,
                         FiguresKeeper figuresKeeper,
                         CoordinatePlane coordinatePlane,
                         InfoSpawnInput spawnInput,
                         FigureDrawer figureDrawer) {
        this.figureInfo = figureInfo;
        this.spawnInput = spawnInput;
        this.drawingPanel = drawingPanel;
        this.figureDrawer = figureDrawer;
        this.figuresKeeper = figuresKeeper;
        this.coordinatePlane = coordinatePlane;
    }

    public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        spawnInput.hideInputs();

        if (SwingUtilities.isLeftMouseButton(e)) { // ЛКМ
            prevCursorPos = e.getPoint();
            lmbDragging = true;

            figureDrawer.setNextPoint(new Point2D.Double(e.getPoint().x, e.getPoint().y));
        }

        if (SwingUtilities.isRightMouseButton(e)) { // ПКМ
            // figure selection
            if (e.isShiftDown()) {
                figuresKeeper.addFigureToSelection(new Point2D.Double(e.getPoint().x, e.getPoint().y));
            } else {
                figuresKeeper.selectFigure(new Point2D.Double(e.getPoint().x, e.getPoint().y));
                if (figuresKeeper.getSelectedFigure().size() == 1 && figuresKeeper.getSelectedFigure().get(0) instanceof DrawnPoint p)
                    figureDrawer.setLastSetPoint(p);
            }
            // right click menu
            if (figuresKeeper.getCurrentFigure() == null)
                spawnInput.showInputs(e.getPoint());

            figureInfo.showInfo();

            drawingPanel.repaint();
        }

        if (SwingUtilities.isMiddleMouseButton(e)) { // колёсико
            wheelDragging = true;
            prevCursorPos = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            wheelDragging = false;
            drawingPanel.repaint();
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            lmbDragging = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (wheelDragging) {
            spawnInput.hideInputs();
            coordinatePlane.moveFigure(e.getPoint().x - (prevCursorPos).x,
                    e.getPoint().y - (prevCursorPos).y);
            prevCursorPos = e.getPoint();
            drawingPanel.repaint();
        }

        if (lmbDragging) {
            if (figuresKeeper.getHighlightedFigure() != null) {
                figuresKeeper.getHighlightedFigure().moveFigure((e.getPoint().x - (prevCursorPos).x) / coordinatePlane.getScale(),
                        (e.getPoint().y - (prevCursorPos).y) / coordinatePlane.getScale());
                prevCursorPos = e.getPoint();

                if (e.isControlDown())
                    figuresKeeper.updateChildren();
                else
                    figuresKeeper.updateParameters();

                drawingPanel.repaint();
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        spawnInput.hideInputs();
        if (e.getPreciseWheelRotation() < 0) {
            drawingPanel.zoomIn(new Point2D.Double(e.getPoint().x, e.getPoint().y));
        } else {
            drawingPanel.zoomOut(new Point2D.Double(e.getPoint().x, e.getPoint().y));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        drawingPanel.setCursorPosition(coordinatePlane.toLocal(new Point2D.Double(e.getPoint().x, e.getPoint().y)));
        figuresKeeper.highlightFigure(new Point2D.Double(e.getPoint().x, e.getPoint().y));

        if (isPointsRotating) {
            if (rotate != null) {
                //rotate.rotate(coordinatePlane.toLocal(new Point2D.Double(e.getX(), e.getY())));
                //figuresKeeper.updateChildren();
            }
        }

        drawingPanel.repaint();
    }
}

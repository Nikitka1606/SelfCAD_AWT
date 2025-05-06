package org.app.Structures;

import org.app.Figures.*;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.app.Structures.Pair.getFirstValue;

public class DxfParser {

    public static List<Figure> parseDxf(InputStream inputStream) throws IOException {
        List<Figure> figures = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        String currentEntity = null;
        List<Pair<Integer, String>> entityData = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            int code = Integer.parseInt(line);
            String value = reader.readLine().trim();

            if (code == 0) {
                // создать фигуру
                if (currentEntity != null) {
                    List<Figure> figures1 = createFigureFromEntity(currentEntity, entityData);
                    if (figures1 != null)
                        for (Figure f : figures1) {
                            figures.add(f);
                        }
                }
                currentEntity = value;
                entityData.clear();
            } else {
                entityData.add(new Pair(code, value));
            }
        }

        // Добавить последнюю фигуру
        if (currentEntity != null) {
            List<Figure> figures1 = createFigureFromEntity(currentEntity, entityData);
            if (figures1 != null)
                for (Figure f : figures1) {
                    figures.add(f);
                }
        }

        return figures;
    }

    private static List<Figure> createFigureFromEntity(String entityType, List<Pair<Integer, String>> entityData) {
        CoordinatePlane cp = new CoordinatePlane(1920, 1080, true);
        switch (entityType) {
            case "POINT":
                return parsePoint(entityData, cp);
            case "LINE":
                return parseLine(entityData, cp);
            case "CIRCLE":
                return parseCircle(entityData, cp);
            case "ARC":
                return parseArc(entityData, cp);
            case "LWPOLYLINE", "POLYLINE":
                return parsePolyline(entityData, cp);
            default:
                return null;
        }
    }

    private static List<Figure> parseLine(List<Pair<Integer, String>> entityData, CoordinatePlane cp) {
        try {
            double x1 = Double.parseDouble(getFirstValue(entityData, 10));
            double y1 = -Double.parseDouble(getFirstValue(entityData, 20));
            double x2 = Double.parseDouble(getFirstValue(entityData, 11));
            double y2 = -Double.parseDouble(getFirstValue(entityData, 21));

            String lineType = getFirstValue(entityData, 6);

            Line line = new Line();
            line.setFromDXF(lineType);

            DrawnPoint startPoint = new DrawnPoint(new Point2D.Double(x1, y1), cp, line);
            DrawnPoint endPoint = new DrawnPoint(new Point2D.Double(x2, y2), cp, line);

            line.addChildren(startPoint);
            line.addChildren(endPoint);

            line.updateParams();

            ArrayList<Figure> figures = new ArrayList<>();
            figures.add(line);
            figures.add(startPoint);
            figures.add(endPoint);

            return figures;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Figure> parseCircle(List<Pair<Integer, String>> entityData, CoordinatePlane cp) {
        try {
            double centerX = Double.parseDouble(getFirstValue(entityData, 10));
            double centerY = -Double.parseDouble(getFirstValue(entityData, 20));
            double radius = Double.parseDouble(getFirstValue(entityData, 40));

            String lineType = getFirstValue(entityData, 6);

            Circle circle = new Circle();
            circle.setFromDXF(lineType);

            DrawnPoint centerPoint = new DrawnPoint(new Point2D.Double(centerX, centerY), cp, circle);
            DrawnPoint radPoint = new DrawnPoint(new Point2D.Double(centerX + radius, centerY), cp, circle);

            circle.addChildren(centerPoint);
            circle.addChildren(radPoint);

            circle.updateParams();

            ArrayList<Figure> figures = new ArrayList<>();
            figures.add(circle);
            figures.add(centerPoint);
            figures.add(radPoint);

            return figures;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Figure> parseArc(List<Pair<Integer, String>> entityData, CoordinatePlane cp) {
        try {
            double centerX = Double.parseDouble(getFirstValue(entityData, 10));
            double centerY = -Double.parseDouble(getFirstValue(entityData, 20));
            double radius = Double.parseDouble(getFirstValue(entityData, 40));

            double dxfStartAngle = 360 - Double.parseDouble(getFirstValue(entityData, 51)) % 360;
            double dxfEndAngle = 360 - Double.parseDouble(getFirstValue(entityData, 50)) % 360;

            int side = 1;
            if (dxfStartAngle > dxfEndAngle)
                side = -1;

            String lineType = getFirstValue(entityData, 6);

            double startRad = Math.toRadians(dxfStartAngle);
            double endRad = Math.toRadians(dxfEndAngle);
            double midAngle = dxfStartAngle + (dxfEndAngle - dxfStartAngle) / 2.0;
            double midRad = Math.toRadians(midAngle);

            Arc arc = new Arc();
            arc.setFromDXF(lineType);

            // Стартовая точка дуги
            double startX = centerX + radius * Math.cos(startRad);
            double startY = centerY + radius * Math.sin(startRad);
            DrawnPoint startPoint = new DrawnPoint(new Point2D.Double(startX, startY), cp, arc);

            // Промежуточная точка дуги
            double midX = centerX + radius * Math.cos(midRad) * side;
            double midY = centerY + radius * Math.sin(midRad) * side;
            DrawnPoint midPoint = new DrawnPoint(new Point2D.Double(midX, midY), cp, arc);

            // Конечная точка дуги
            double endX = centerX + radius * Math.cos(endRad);
            double endY = centerY + radius * Math.sin(endRad);
            DrawnPoint endPoint = new DrawnPoint(new Point2D.Double(endX, endY), cp, arc);

            arc.addChildren(startPoint);
            arc.addChildren(endPoint);
            arc.addChildren(midPoint);

            arc.updateParams();

            ArrayList<Figure> figures = new ArrayList<>();
            figures.add(arc);
            figures.add(startPoint);
            figures.add(midPoint);
            figures.add(endPoint);

            return figures;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Figure> parsePolyline(List<Pair<Integer, String>> entityData, CoordinatePlane cp) {
        try {
            ArrayList<Point2D.Double> points = new ArrayList<>();
            int verts = Integer.parseInt(getFirstValue(entityData, 90));

            for (int i = 0; i < verts; i++) {
                double x = Double.parseDouble(getFirstValue(entityData, 10, 2 * i + 10));
                double y = -Double.parseDouble(getFirstValue(entityData, 20, 2 * i + 11));
                points.add(new Point2D.Double(x, y));
            }

            String lineType = getFirstValue(entityData, 6);


            boolean isClosed = "1".equals(getFirstValue(entityData, 70));

            ArrayList<Figure> figures = new ArrayList<>();
            DrawnPoint prevPoint = null;

            for (Point2D.Double currentCoord : points) {
                DrawnPoint currentPoint;

                if (prevPoint == null) {
                    // Для самой первой точки
                    currentPoint = new DrawnPoint(currentCoord, cp, null);
                    figures.add(currentPoint);
                } else {
                    // Для последующих линий
                    currentPoint = new DrawnPoint(currentCoord, cp, null);
                    figures.add(currentPoint);

                    Line line = new Line();
                    line.setFromDXF(lineType);

                    line.addChildren(prevPoint);
                    line.addChildren(currentPoint);

                    line.updateParams();

                    figures.add(line);
                }

                prevPoint = currentPoint;
            }

            if (isClosed && points.size() > 2) {
                DrawnPoint firstPoint = (DrawnPoint) figures.get(0);
                Line closingLine = new Line();
                closingLine.setFromDXF(lineType);

                closingLine.addChildren(prevPoint);
                closingLine.addChildren(firstPoint);

                closingLine.updateParams();

                figures.add(closingLine);
            }

            return figures;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Figure> parsePoint(List<Pair<Integer, String>> entityData, CoordinatePlane cp) {
        try {
            double x = Double.parseDouble(getFirstValue(entityData, 10));
            double y = -Double.parseDouble(getFirstValue(entityData, 10));

            DrawnPoint point = new DrawnPoint(new Point2D.Double(x, y), cp);
            ArrayList<Figure> figures = new ArrayList<>();
            figures.add(point);

            return figures;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
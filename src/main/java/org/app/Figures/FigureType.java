package org.app.Figures;

public enum FigureType {
    POINT("Point"),                                    // ok
    LINE("Line"),                                      // ok
    RECTANGLE("Rectangle"),                            // ok
    THREE_POINT_RECTANGLE("Triple-point rectangle"),   // ok
    CIRCLE("Circle"),                                  // ok
    THREE_POINT_CIRCLE("Triple-point circle"),         // ok
    ARC("Arc"),                                        // ok
    BEZIER_SPLINE("Bezier spline"),                    // ok
    B_SPLINE("B-spline"),                              // ok
    POLYGON_INC("Inscribed polygon"),                  // ok
    POLYGON_DESC("Described polygon"),                  // ok
    CONTROL_TANGENT("Control tangent");

    private final String name;

    FigureType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

package org.app.Structures;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;

public class DXFColorUtils {

    // Конвертация Color в DXF-индекс (стандартные цвета AutoCAD)
    public static int awtColorToDXFIndex(Color color) {
        if (color == null) return 256; // ByLayer

        // Базовые цвета AutoCAD
        if (color.equals(Color.RED))     return 1;
        if (color.equals(Color.YELLOW))  return 2;
        if (color.equals(Color.GREEN))   return 3;
        if (color.equals(Color.CYAN))    return 4;
        if (color.equals(Color.BLUE))    return 5;
        if (color.equals(Color.MAGENTA)) return 6;
        if (color.getRGB() == Color.WHITE.getRGB()) return 7;

        return 256; // По умолчанию - ByLayer
    }

    // Конвертация Color в DXF RGB (группа 420)
    public static int awtColorToDXFRGB(Color color) {
        if (color == null) return 0;
        return color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
    }

    // Полная запись цвета в DXF
    public static void writeAwtColor(BufferedWriter writer, Color color) throws IOException {
        int index = awtColorToDXFIndex(color);
        int rgb = awtColorToDXFRGB(color);

        writer.write("62\n" + index + "\n");
        if (index == 256 || index == 0) return; // Для ByLayer/ByBlock не пишем RGB

        // Записываем TrueColor только если цвет нестандартный
        if (index > 7 || index < 1) {
            writer.write("420\n" + rgb + "\n");
        }
    }
}
package org.app.Structures;

import java.io.File;
import java.io.IOException;

import org.app.Figures.Figure;
import org.app.Figures.FigureType;

public class FileManager {
    FiguresKeeper keeper;
    static String filePath;

    public FileManager(FiguresKeeper keeper) {
        filePath = "C:/Users/halni/IdeaProjects/SelfCAD_FXver/saves/save.dxf";
        this.keeper = keeper;
    }

    public void saveFile() throws IOException {
        try (DXFWriter dxfWriter = new DXFWriter(filePath)) {
            for (Figure f : keeper.getFigures()) {
                if (f.isChild()) {
                    if (f.getFigureType() == FigureType.POINT)
                        continue;
                }
                dxfWriter.addFigure(f);
            }
        }
    }

    public static File loadFile() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Файл не найден: " + filePath);
        }
        if (!file.isFile()) {
            throw new IOException("Указанный путь не является файлом: " + filePath);
        }
        return file;
    }

    public String getActualDir() {
        return filePath;
    }
}

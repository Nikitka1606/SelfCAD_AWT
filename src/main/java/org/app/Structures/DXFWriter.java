package org.app.Structures;

import org.app.Figures.Figure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс для записи DXF-файла
 */
public class DXFWriter implements AutoCloseable {
    private final BufferedWriter writer;

    public DXFWriter(String filePath) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(filePath));
        writeHeader();
        writeTables();
        startEntities();
    }

    // Запись заголовка DXF
    private void writeHeader() throws IOException {
        writer.write("0\nSECTION\n");
        writer.write("2\nHEADER\n");
        writer.write("9\n$ACADVER\n1\nAC1018\n"); // Версия AutoCAD 2004
        writer.write("0\nENDSEC\n");
    }

    // Запись таблиц (слой 0)
    private void writeTables() throws IOException {
        writer.write("0\nSECTION\n");
        writer.write("2\nTABLES\n");
        writer.write("0\nTABLE\n");
        writer.write("2\nLAYER\n");
        writer.write("70\n1\n"); // Количество слоёв

        // Слой 0 (по умолчанию)
        writer.write("0\nLAYER\n");
        writer.write("2\n0\n");
        writer.write("70\n0\n");
        writer.write("62\n7\n"); // Цвет (7 = белый/чёрный)
        writer.write("6\nCONTINUOUS\n"); // Тип линии

        writer.write("0\nENDTAB\n");
        writer.write("0\nENDSEC\n");
    }

    // Начало секции ENTITIES
    private void startEntities() throws IOException {
        writer.write("0\nSECTION\n");
        writer.write("2\nENTITIES\n");
    }

    // Добавление фигуры в файл
    public void addFigure(Figure figure) throws IOException {
        figure.DXFWrite(writer);
    }

    // Завершение записи и закрытие файла
    @Override
    public void close() throws IOException {
        writer.write("0\nENDSEC\n"); // Конец секции ENTITIES
        writer.write("0\nEOF\n");    // Конец файла
        writer.close();
    }
}
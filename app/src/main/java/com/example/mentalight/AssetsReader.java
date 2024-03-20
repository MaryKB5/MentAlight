package com.example.mentalight;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
// Eine Hilfsklasse zum Lesen von Dateien aus dem Assets-Verzeichnis
public class AssetsReader {
    // Methode zum Laden von JSON aus einer Assets-Datei
    public static String loadJsonFromAssets(Context context, String fileName) {
        String json = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            try {
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                json = new String(buffer, StandardCharsets.UTF_8);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Fehlermeldung ausgeben, falls ein Fehler beim Lesen der Datei auftritt
        }
        return json;
    }

}

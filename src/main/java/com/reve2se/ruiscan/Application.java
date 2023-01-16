package com.reve2se.ruiscan;

import com.formdev.flatlaf.FlatLightLaf;
import com.reve2se.ruiscan.form.MainForm;
import com.reve2se.ruiscan.model.DB;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    public static String globalSkin;

    private static void setFlatLaf() {
        try {
            FlatLightLaf.setup();
            globalSkin = "FlatLaf";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        FlatLightLaf.setup();
        try {
//            Path dbPath = Paths.get("ruiscan.db");
//            if (Files.exists(dbPath)) {
//                DB db = DB.parseDB(Files.readAllBytes(dbPath));
//
//            }
            UIManager.setLookAndFeel(globalSkin);
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        MainForm.startMainForm();
    }
}

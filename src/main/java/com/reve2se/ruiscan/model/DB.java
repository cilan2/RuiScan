package com.reve2se.ruiscan.model;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DB {
    private String lastEnscanPath;
    private String lastOneforallPath;
    private String lastJsfinderPath;
    private String lastCiprPath;
    private String lastXrayPath;
    private String lastFofaMapPath;


    public String getLastEnscanPath() {
        return lastEnscanPath;
    }
    public String getLastOneforallPath() {
        return lastOneforallPath;
    }
    public String getLastJsfinderPath() {
        return lastJsfinderPath;
    }
    public String getLastCiprPath() {
        return lastCiprPath;
    }
    public String getLastXrayPath() {
        return lastXrayPath;
    }
    public String getLastFofaMapPath() {
        return lastFofaMapPath;
    }

    public void setLastEnscanPath(String lastEnscanPath) {
        this.lastEnscanPath = lastEnscanPath;
    }
    public void setLastOneforallPath(String lastOneforallPath) {
        this.lastOneforallPath = lastOneforallPath;
    }
    public void setLastJsfinderPath(String lastJsfinderPath) {
        this.lastJsfinderPath = lastJsfinderPath;
    }
    public void setLastCiprPath(String lastCiprPath) {
        this.lastCiprPath = lastCiprPath;
    }
    public void setLastXrayPath(String lastXrayPath) {
        this.lastXrayPath = lastXrayPath;
    }
    public void setLastFofaMapPath(String lastFofaMapPath) {
        this.lastFofaMapPath = lastFofaMapPath;
    }

//    初始化数据库格式
    public String getDB() {
        return String.format("%s=%s;%s=%s;%s=%s;%s=%s;%s=%s;%s=%s", "lastEnscanPath", getLastEnscanPath(), "lastOneforallPath", getLastOneforallPath(), "lastJsfinderPath", getLastJsfinderPath(), "lastCiprPath", getLastCiprPath(), "lastXrayPath", getLastXrayPath(), "lastFofaMapPath", getLastFofaMapPath());
    }

//    将工具路径写入数据库
    public static DB parseDB(byte[] data) {
        DB db = new DB();
        String[] temp = new String(data).split(";");
        db.setLastEnscanPath(temp[0].split("=")[1]);
        db.setLastOneforallPath(temp[1].split("=")[1]);
        db.setLastJsfinderPath(temp[2].split("=")[1]);
        db.setLastCiprPath(temp[3].split("=")[1]);
        db.setLastXrayPath(temp[4].split("=")[1]);
        db.setLastFofaMapPath(temp[5].split("=")[1]);
        return db;
    }
    public void saveDB() {
        try {
            Files.write(Paths.get("ruiscan.db"), getDB().getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

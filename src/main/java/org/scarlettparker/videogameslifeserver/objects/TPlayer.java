package org.scarlettparker.videogameslifeserver.objects;

import org.bukkit.Bukkit;

import java.util.Arrays;

import static org.scarlettparker.videogameslifeserver.manager.ConfigManager.*;
import static org.scarlettparker.videogameslifeserver.utils.WorldUtils.handleFinalDeath;
import static org.scarlettparker.videogameslifeserver.utils.WorldUtils.setPlayerName;

public class TPlayer {
    private String name;

    public TPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLives() {
        Object lives = getJsonObjectAttribute(playerFile, name, "lives");
        return lives instanceof Integer ? (int) lives : 0; // default value is 0
    }

    public void setLives(int lives) {
        if (lives == 0) {
            handleFinalDeath(name);
        }
        setJsonObjectAttribute(playerFile, name, "lives", lives);
        setPlayerName(Bukkit.getPlayer(name), lives);
    }

    public Death[] getDeaths() {
        Object deaths = getJsonObjectAttribute(playerFile, name, "deaths");
        return deaths instanceof Death[] ? (Death[]) deaths : new Death[0]; // default value is empty array
    }

    public void setDeaths(Death[] deaths) {
        setJsonObjectAttribute(playerFile, name, "deaths", deaths);
    }

    public String[] getTasks() {
        Object tasks = getJsonObjectAttribute(playerFile, name, "tasks");
        return tasks instanceof String[] ? (String[]) tasks : new String[0]; // default value is empty array
    }

    public String getCurrentTask() {
        Object currentTask = getJsonObjectAttribute(playerFile, name, "currentTask");
        return currentTask instanceof String ? (String) currentTask : "-1"; // default value is -1
    }

    public void setCurrentTask(String taskID) {
        setJsonObjectAttribute(playerFile, name, "currentTask", taskID);
    }

    public void setTasks(String[] tasks) {
        setJsonObjectAttribute(playerFile, name, "tasks", tasks);
    }

    public int getSessionTasks() {
        Object sessionTasks = getJsonObjectAttribute(playerFile, name, "sessionTasks");
        return sessionTasks instanceof Integer ? (int) sessionTasks : 0; // default value is 0
    }

    public void setSessionTasks(int sessionTasks) {
        setJsonObjectAttribute(playerFile, name, "sessionTasks", sessionTasks);
    }

    public int getTokens() {
        Object tokens = getJsonObjectAttribute(playerFile, name, "tokens");
        return tokens instanceof Integer ? (int) tokens : 0; // default value is 0
    }

    public void setTokens(int tokens) {
        setJsonObjectAttribute(playerFile, name, "tokens", tokens);
    }

    public boolean isZombie() {
        Object zombie = getJsonObjectAttribute(playerFile, name, "zombie");
        return zombie instanceof Boolean && (boolean) zombie; // default value is false
    }

    public void setZombie(boolean zombie) {
        setJsonObjectAttribute(playerFile, name, "zombie", zombie);
    }

    public boolean getShopping() {
        Object shop = getJsonObjectAttribute(playerFile, name, "shop");
        return shop instanceof Boolean && (boolean) shop; // default value is false
    }

    public void setShopping(boolean shop) {
        setJsonObjectAttribute(playerFile, name, "shop", shop);
    }

    public String[] getPunishments() {
        Object punishments = getJsonObjectAttribute(playerFile, name, "punishments");
        return punishments instanceof String[] ? (String[]) punishments : new String[0]; // default value is empty array
    }

    public void setPunishments(String[] punishments) {
        setJsonObjectAttribute(playerFile, name, "punishments", punishments);
    }

    public void addPunishment(String punishmentID) {
        String[] currentPunishments = getPunishments();
        String[] tempPunishments = new String[currentPunishments.length + 1];

        // copy old punishments into new array
        System.arraycopy(currentPunishments, 0, tempPunishments, 0, currentPunishments.length);

        // add new punishment to last position and update
        tempPunishments[currentPunishments.length] = punishmentID;
        setPunishments(tempPunishments);
    }

    public boolean hasPunishment(String punishment) {
        return Arrays.asList(getPunishments()).contains(punishment);
    }
}
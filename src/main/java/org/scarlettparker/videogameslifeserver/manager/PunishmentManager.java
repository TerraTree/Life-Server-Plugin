package org.scarlettparker.videogameslifeserver.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.scarlettparker.videogameslifeserver.objects.Punishment;
import org.scarlettparker.videogameslifeserver.objects.TPlayer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import static org.scarlettparker.videogameslifeserver.manager.ConfigManager.addJsonObject;
import static org.scarlettparker.videogameslifeserver.manager.ConfigManager.punishFile;
import static org.scarlettparker.videogameslifeserver.manager.ConfigManager.returnAllObjects;

public class PunishmentManager {

    public static void generatePunishments() {
        String path = "tasks/punish.txt";
        InputStream is = TaskManager.class.getClassLoader().getResourceAsStream(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] attributes = line.split(":");
                Punishment tempPunish = new Punishment(attributes[0]);
                String json = convertPunishmentToJson(tempPunish);

                // add json task to file
                addJsonObject(punishFile, json);

                // set task attributes
                tempPunish.setDescription(attributes[1]);
                tempPunish.setDifficulty(Integer.parseInt(attributes[2]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // convert a task object to json string
    private static String convertPunishmentToJson(Punishment punishment) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(punishment);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean assignRandomPunishment(TPlayer player, int difficulty) {
        // filter punishments by difficulty
        List<Punishment> punishments = getPunishmentsByDifficulty(difficulty);
        if (punishments.isEmpty()) {
            return false;
        }

        // get the player's current punishments
        String[] currentPunishments = player.getPunishments();
        Set<String> currentPunishmentsSet = new HashSet<>(Arrays.asList(currentPunishments));
        punishments.removeIf(punishment -> currentPunishmentsSet.contains(punishment.getName()));

        if (punishments.isEmpty() && difficulty != 2) {
            // kill player if they're not a red life
            Bukkit.getPlayer(player.getName()).sendMessage(ChatColor.RED + "You already have every punishment "
                + "possible. Because of this, you shall lose a life.");
            Bukkit.getPlayer(player.getName()).setHealth(0.0);
            return false;
        }

        // randomly select a punishment from the remaining list
        Random random = new Random();
        Punishment selectedPunishment = punishments.get(random.nextInt(punishments.size()));

        player.addPunishment(selectedPunishment.getName());
        return true;
    }

    private static List<Punishment> getPunishmentsByDifficulty(int difficulty) {
        List<Punishment> matchingPunishments = new ArrayList<>();
        JsonObject punishmentsObject = returnAllObjects(punishFile);

        for (String key : punishmentsObject.keySet()) {
            JsonElement jsonElement = punishmentsObject.get(key);
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonElement.toString());
                Punishment punishment = new Punishment(jsonNode.get("name").asText());
                if (punishment.getDifficulty() == difficulty) {
                    matchingPunishments.add(punishment);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return matchingPunishments;
    }
}
package com.tubes.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tubes.model.CharacterFactory;
import com.tubes.model.HumanCharacter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameController {
    @FXML private ImageView characterImage;
    @FXML private Label characterName;
    @FXML private Label characterDescription;
    @FXML private Label scoreLabel;

    private final List<com.tubes.model.Character> characters = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    public void initialize() {
        characters.add(CharacterFactory.createCharacter("zombie", "Saskia", "Rambut panjang, Lipstick merah, Lanyard di leher", "Tangerang, 30 Sep 2005"));
        characters.add(CharacterFactory.createCharacter("human", "Saskia", "Rambut panjang, Lipstick merah, Lanyard di leher", "Tangerang, 30 Sep 2005"));
        characters.add(CharacterFactory.createCharacter("zombie", "Wiwok", "Memakai peci, Bibir tebal, Jas rapi", "Solo, 1 Okt 1924"));
        characters.add(CharacterFactory.createCharacter("human", "Wiwok", "Memakai peci, Bibir tebal, Jas rapi", "Solo, 1 Okt 1924"));
        characters.add(CharacterFactory.createCharacter("zombie", "Timmy", "Kepala botak, Otak keluar, Kaos hitam", "Semarang, 23 Aug 1945"));
        characters.add(CharacterFactory.createCharacter("human", "Timmy", "Kepala botak, Otak keluar, Kaos hitam", "Semarang, 23 Aug 1945"));

        Collections.shuffle(characters);
        showCharacter();
    }

    private void showCharacter() {
        if (currentIndex >= characters.size()) {
            goToEnding();
            return;
        }

        com.tubes.model.Character current = characters.get(currentIndex);
        characterName.setText(current.getName());
        characterDescription.setText(current.getDescription());

        String imageName = getImageFileName(current);
        characterImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/" + imageName)));
    }

    private String getImageFileName(com.tubes.model.Character character) {
        String baseName = character.getName().toLowerCase();
        if (character instanceof HumanCharacter) {
            return "doppel" + baseName + ".png";
        } else {
            return baseName + ".png";
        }
    }

    @FXML private void handleHumanButton() {
        if (characters.get(currentIndex) instanceof HumanCharacter) {
            score++;
        }
        currentIndex++;
        updateScore();
        showCharacter();
    }

    @FXML private void handleDoppelButton() {
        if (!(characters.get(currentIndex) instanceof HumanCharacter)) {
            score++;
        }
        currentIndex++;
        updateScore();
        showCharacter();
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    private void goToEnding() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Selesai");
        alert.setContentText("Skor kamu: " + score + "/" + characters.size());
        alert.showAndWait();
    }
}

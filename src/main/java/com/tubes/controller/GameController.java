package com.tubes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tubes.model.Character;
import com.tubes.model.CharacterFactory;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GameController {

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;
    @FXML private ImageView characterImage;
    @FXML private ImageView counterImage;

    @FXML private HBox dialogBox;
    @FXML private Button nextButton;

    private List<String[]> characterData = new ArrayList<>();
    private int currentIndex = 0;
    private Character currentCharacter;
    private Random random = new Random();

    public void initialize() {
        // Load gambar background dan counter
        backgroundImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/background.png")));
        counterImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/counter.png")));

        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        counterImage.fitWidthProperty().bind(rootPane.widthProperty());
        counterImage.fitHeightProperty().bind(rootPane.heightProperty());

        // Isi data karakter: {name, description, ttm}
        characterData.add(new String[]{"Saskia", "Saskia adalah karakter cerdas dan ceria", "Bandung, 1 Januari 2020"});
        characterData.add(new String[]{"Abby", "Abby suka berpetualang dan pemberani", "Surabaya, 5 Mei 2019"});
        characterData.add(new String[]{"Wiwok", "Wiwok penuh semangat dan ceria", "Jakarta, 17 Agustus 2045"});
        characterData.add(new String[]{"Jhon", "Jhon adalah pemikir yang tenang", "Medan, 12 Desember 2018"});
        characterData.add(new String[]{"Timmy", "Timmy lucu dan suka bercanda", "Yogyakarta, 3 Maret 2021"});

        loadCurrentCharacter();

        dialogBox.setVisible(false);
        dialogBox.setManaged(false);

        rootPane.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                    if (newWin != null) {
                        startCharacterEntranceAnimation();
                    }
                });
            }
        });
    }

    private void loadCurrentCharacter() {
        String[] data = characterData.get(currentIndex);
        String name = data[0];
        String description = data[1];
        String ttm = data[2];

        // Random pilih "zombie" atau "human"
        String type = random.nextBoolean() ? "zombie" : "human";

        currentCharacter = CharacterFactory.createCharacter(type, name, description, ttm);

        // Tentukan path gambar
        String imagePath;
        if ("human".equals(type)) {
            imagePath = "/com/tubes/assets/dopple" + name + ".png";  // contoh: doppleSaskia.png
        } else {
            imagePath = "/com/tubes/assets/" + name + ".png";        // contoh: Saskia.png
        }

        // Set gambar besar
        characterImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        characterImage.setFitWidth(400);
        characterImage.setFitHeight(400);
        characterImage.setPreserveRatio(true);
    }

    private void startCharacterEntranceAnimation() {
        characterImage.setTranslateX(-rootPane.getWidth());

        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.5), characterImage);
        tt.setFromX(-rootPane.getWidth());
        tt.setToX(0);

        tt.setOnFinished(e -> {
            dialogBox.setVisible(true);
            dialogBox.setManaged(true);
        });

        tt.play();
    }

    private void playExitAnimation(Runnable afterExit) {
        TranslateTransition exit = new TranslateTransition(Duration.seconds(1), characterImage);
        exit.setFromX(0);
        exit.setToX(rootPane.getWidth()); // Keluar ke kanan

        exit.setOnFinished(e -> afterExit.run());

        exit.play();
    }

    @FXML
    private void nextCharacter() {
        dialogBox.setVisible(false);
        dialogBox.setManaged(false);

        playExitAnimation(() -> {
            currentIndex++;
            if (currentIndex >= characterData.size()) {
                currentIndex = 0;
            }

            loadCurrentCharacter();
            startCharacterEntranceAnimation();
        });
    }
}

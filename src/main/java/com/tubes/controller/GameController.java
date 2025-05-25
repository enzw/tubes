package com.tubes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tubes.model.Character;
import com.tubes.model.CharacterFactory;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        backgroundImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/background.png")));
        counterImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/counter.png")));

        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        counterImage.fitWidthProperty().bind(rootPane.widthProperty());
        counterImage.fitHeightProperty().bind(rootPane.heightProperty());

        characterData.add(new String[]{"Saskia", "Nama : Saskia Paramether\nNo. Apart : 105-A", "1"});
        characterData.add(new String[]{"Abby", "Nama : Bob Abby\nNo. Apart : 666-F", "3"});
        characterData.add(new String[]{"Wiwok", "Nama : Wiwok Detok\nNo. Apart : 501-O", "2"});
        characterData.add(new String[]{"Jhon", "Nama : Jhon Walker\nNo. Apart : 122-I", "1"});
        characterData.add(new String[]{"Timmy", "Nama : Timmy Tummy\nNo. Apart : 431-A", "2"});

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
        String baseDescription = data[1];
        String lantai = data[2];

        // Random pilih "zombie" atau "human"
        String type = random.nextBoolean() ? "zombie" : "human";

        // Deskripsi khusus sesuai tipe
        String finalDescription;
        if ("human".equals(type)) {
            finalDescription = baseDescription;
        } else {
            finalDescription = baseDescription.replace("Nama", "Subjek")
                                              .replace("No. Apart", "Lokasi Terakhir");
        }

        currentCharacter = CharacterFactory.createCharacter(type, name, finalDescription, lantai);

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
        exit.setToX(rootPane.getWidth());

        exit.setOnFinished(e -> afterExit.run());

        exit.play();
    }

    @FXML private Label characterDescription;

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
            characterDescription.setText(currentCharacter.getDescription());
            startCharacterEntranceAnimation();
        });
    }
}

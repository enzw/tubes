package com.tubes.controller;

import java.util.ArrayList;
import java.util.List;

import com.tubes.model.Character;
import com.tubes.model.HumanCharacter;

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
    @FXML private Label dialogLabel;
    @FXML private ImageView dialogCharacterImage;

    @FXML private Button nextButton;  // Tombol next karakter

    private List<Character> characters = new ArrayList<>();
    private int currentIndex = 0;
    private Character currentCharacter;

    public void initialize() {
        // Load gambar background dan counter
        backgroundImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/background.png")));
        counterImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/counter.png")));

        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        counterImage.fitWidthProperty().bind(rootPane.widthProperty());
        counterImage.fitHeightProperty().bind(rootPane.heightProperty());

        // Buat 5 karakter (HumanCharacter contoh)
        characters.add(new HumanCharacter("Saskia", "Saskia adalah karakter cerdas dan ceria", "Bandung, 1 Januari 2020"));
        characters.add(new HumanCharacter("Abby", "Abby suka berpetualang dan pemberani", "Surabaya, 5 Mei 2019"));
        characters.add(new HumanCharacter("Wiwok", "Wiwok penuh semangat dan ceria", "Jakarta, 17 Agustus 2045"));
        characters.add(new HumanCharacter("Jhon", "Jhon adalah pemikir yang tenang", "Medan, 12 Desember 2018"));
        characters.add(new HumanCharacter("Timmy", "Timmy lucu dan suka bercanda", "Yogyakarta, 3 Maret 2021"));

        loadCurrentCharacter();

        // Hide dialog awalnya
        dialogBox.setVisible(false);
        dialogBox.setManaged(false);

        // Setup listener agar animasi mulai saat scene siap
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
        currentCharacter = characters.get(currentIndex);

        String imagePath = "/com/tubes/assets/" + currentCharacter.getName() + ".png";

        // Update gambar karakter besar
        characterImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        characterImage.setFitWidth(400);
        characterImage.setFitHeight(400);
        characterImage.setPreserveRatio(true);

        // Update gambar kecil di dialog
        dialogCharacterImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        dialogCharacterImage.setFitWidth(50);
        dialogCharacterImage.setFitHeight(50);
        dialogCharacterImage.setPreserveRatio(true);
    }

    private void startCharacterEntranceAnimation() {
        // Start posisi di luar layar kanan
        characterImage.setTranslateX(rootPane.getWidth());

        TranslateTransition tt = new TranslateTransition(Duration.seconds(3), characterImage);
        tt.setFromX(rootPane.getWidth());
        tt.setToX(0);

        tt.setOnFinished(e -> {
            showDialogTtm();
        });

        tt.play();
    }

    private void showDialogTtm() {
        dialogLabel.setText(
            currentCharacter.getName() + "\n" +
            currentCharacter.getDescription() + "\n" +
            "TTM: " + currentCharacter.getTtm()
        );
        dialogBox.setVisible(true);
        dialogBox.setManaged(true);
    }

    @FXML
    private void nextCharacter() {
        dialogBox.setVisible(false);
        dialogBox.setManaged(false);

        currentIndex++;
        if (currentIndex >= characters.size()) {
            currentIndex = 0;  // Loop ke karakter pertama
        }

        loadCurrentCharacter();
        startCharacterEntranceAnimation();
    }

    @FXML
    private void closeDialog() {
        dialogBox.setVisible(false);
        dialogBox.setManaged(false);
    }
}

package com.tubes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @FXML private Label characterDescription;

    @FXML private StackPane idCardPane;
    @FXML private ImageView idCardImage;
    @FXML private ImageView idCardCharacterImage;

    private List<String[]> characterData = new ArrayList<>();
    private int currentIndex = 0;
    private Character currentCharacter;
    private Random random = new Random();

    // Alias Map
    private static final Map<String, List<String>> nameAliases = Map.of(
        "Saskia", List.of("Sarah", "Silvy", "Sandra"),
        "Abby", List.of("Adit", "Agung"),
        "Wiwok", List.of("Wendy", "Wawan"),
        "Jhon", List.of("Jimmy", "Joko"),
        "Timmy", List.of("Toni", "Tatang")
    );

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

        currentIndex = random.nextInt(characterData.size());
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
        String originalLantai = data[2];

        String type = random.nextBoolean() ? "zombie" : "human";

        // Tentukan lantai
        String lantai;
        if ("human".equals(type)) {
            List<String> allLantai = new ArrayList<>();
            for (String[] d : characterData) {
                if (!allLantai.contains(d[2])) {
                    allLantai.add(d[2]);
                }
            }
            allLantai.remove(originalLantai);
            lantai = allLantai.isEmpty() ? originalLantai : allLantai.get(random.nextInt(allLantai.size()));
        } else {
            lantai = originalLantai;
        }

        // Tentukan alias
        String alias = name;
        if ("human".equals(type) && random.nextBoolean()) {
            List<String> aliases = nameAliases.getOrDefault(name, List.of(name));
            alias = aliases.get(random.nextInt(aliases.size()));
        }

        String finalName = "human".equals(type) ? alias + " (Human)" : name;

        // Bangun ulang deskripsi
        String noApartemen = baseDescription.split("No\\. Apart ?: ")[1];
        String finalDescription = "Identitas saya :\n" +
                "Nama : " + alias + "\n" +
                "No. Apart : " + noApartemen + "\n" +
                "Lantai : " + lantai;

        currentCharacter = CharacterFactory.createCharacter(type, finalName, finalDescription, lantai);

        // Peluang 30% human pakai gambar zombie asli
        String imagePath;
        if ("human".equals(type)) {
            boolean useZombieImage = random.nextInt(100) < 70;  // 30% chance
            if (useZombieImage) {
                imagePath = "/com/tubes/assets/" + name + ".png";  // gambar asli zombie
            } else {
                imagePath = "/com/tubes/assets/dopple" + name + ".png";  // gambar dopple alias
            }
        } else {
            imagePath = "/com/tubes/assets/" + name + ".png";  // zombie selalu gambar asli
        }

        characterImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        characterImage.setFitWidth(400);
        characterImage.setFitHeight(400);
        characterImage.setPreserveRatio(true);

        characterDescription.setText(finalDescription);
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

    @FXML
    private void showIdCard() {
        idCardImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/idCard.png")));

        // Ambil nama asli dari data
        String originalName = characterData.get(currentIndex)[0];
        String path = "/com/tubes/assets/" + originalName + ".png";

        idCardCharacterImage.setImage(new Image(getClass().getResourceAsStream(path)));
        idCardPane.setVisible(true);
        idCardPane.setManaged(true);
    }

    @FXML
    private void closeIdCard() {
        idCardPane.setVisible(false);
        idCardPane.setManaged(false);
    }
}

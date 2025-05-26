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
    @FXML private Label idCardCharacterDesc; // <- Tambahan deskripsi di ID Card

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

    // Variabel untuk menyimpan lantai asli dari karakter
    private String trueLantai = "";

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

        // Simpan lantai asli
        trueLantai = originalLantai;

        // Tentukan lantai palsu untuk deskripsi (beda dengan lantai asli jika human)
        String fakeLantai = originalLantai;
        if ("human".equals(type)) {
            List<String> allLantai = new ArrayList<>();
            for (String[] d : characterData) {
                if (!allLantai.contains(d[2])) {
                    allLantai.add(d[2]);
                }
            }
            allLantai.remove(originalLantai);
            if (!allLantai.isEmpty()) {
                fakeLantai = allLantai.get(random.nextInt(allLantai.size()));
            }
        }

        // Tentukan alias nama
        String alias = name;
        if ("human".equals(type) && random.nextBoolean()) {
            List<String> aliases = nameAliases.getOrDefault(name, List.of(name));
            alias = aliases.get(random.nextInt(aliases.size()));
        }

        String finalName = "human".equals(type) ? alias + " (Human)" : name;

        // Ambil nomor apartemen dari baseDescription
        String noApartemen = baseDescription.split("No\\. Apart ?: ")[1];

        String finalDescription = "Zombie :\n" + '"' +
                " Halo, Nama saya " + alias + "\n" +
                "Nomer apart saya " + noApartemen + "\n" +
                "Saya tinggal di lantai " + fakeLantai + " " + '"';

        currentCharacter = CharacterFactory.createCharacter(type, finalName, finalDescription, fakeLantai);

        // Set gambar karakter sesuai ketentuan
        String imagePath;
        if ("human".equals(type)) {
            boolean useZombieImage = random.nextInt(100) < 70;
            if (useZombieImage) {
                imagePath = "/com/tubes/assets/" + name + ".png";
            } else {
                imagePath = "/com/tubes/assets/dopple" + name + ".png";
            }
        } else {
            imagePath = "/com/tubes/assets/" + name + ".png";
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

        String originalName = characterData.get(currentIndex)[0];
        String path = "/com/tubes/assets/" + originalName + ".png";
        idCardCharacterImage.setImage(new Image(getClass().getResourceAsStream(path)));

        String desc = currentCharacter.getDescription();

        String nama = "";
        if (desc.contains("Nama saya ")) {
            nama = desc.split("Nama saya ")[1].split("\n")[0].trim();
        }

        String noApart = "";
        if (desc.contains("apart saya ")) {
            noApart = desc.split("apart saya ")[1].split("\n")[0].trim();
        }

        // Gunakan lantai asli yang disimpan di variabel trueLantai
        String finalDesc = "Nama   : " + nama + "\n" +
                           "No. Apart : " + noApart + "\n" +
                           "Lantai : " + trueLantai;

        idCardCharacterDesc.setText(finalDesc);

        idCardPane.setVisible(true);
        idCardPane.setManaged(true);
    }

    @FXML
    private void closeIdCard() {
        idCardPane.setVisible(false);
        idCardPane.setManaged(false);
    }
}

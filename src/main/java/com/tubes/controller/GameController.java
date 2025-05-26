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
    @FXML private Label idCardCharacterDesc;

    private List<String[]> characterData = new ArrayList<>();
    private int currentIndex = 0;
    private Character currentCharacter;
    private Random random = new Random();

    private static final Map<String, List<String>> nameAliases = Map.of(
        "Saskia", List.of("Sarah", "Silvy", "Sandra"),
        "Abby", List.of("Adit", "Agung"),
        "Wiwok", List.of("Wendy", "Wawan"),
        "Jhon", List.of("Jimmy", "Joko"),
        "Timmy", List.of("Toni", "Tatang")
    );

    private String currentCharacterType = "";

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

        // Random type assignment
        String type = random.nextBoolean() ? "zombie" : "human";
        trueLantai = originalLantai;
        currentCharacterType = type;

        String fakeLantai = originalLantai;
        if ("human".equals(type)) {
            List<String> allLantai = new ArrayList<>();
            for (String[] d : characterData) {
                if (!allLantai.contains(d[2])) allLantai.add(d[2]);
            }
            allLantai.remove(originalLantai);
            if (!allLantai.isEmpty()) {
                fakeLantai = allLantai.get(random.nextInt(allLantai.size()));
            }
        }

        // Ambiguous alias
        String alias = name;
        if (random.nextBoolean()) {
            List<String> aliases = nameAliases.getOrDefault(name, List.of(name));
            alias = aliases.get(random.nextInt(aliases.size()));
        }

        // Ambiguous speech style
        String[] greetings = {
            "Halo, saya " + alias,
            "Hmm... saya rasa nama saya " + alias,
            "Mereka memanggilku " + alias,
            "Aku... " + alias + ", ya?",
            "Namaku... mungkin " + alias
        };

        String[] hints = {
            "Aku tinggal di lantai " + fakeLantai,
            "Sepertinya aku berasal dari lantai " + fakeLantai,
            "Tempat tinggalku? Lantai " + fakeLantai + ", kalau tidak salah.",
            "Lantai " + fakeLantai + "... ya, lantai itu.",
            "Yang jelas aku bukan dari lantai bawah."
        };

        String greeting = greetings[random.nextInt(greetings.length)];
        String hint = hints[random.nextInt(hints.length)];

        String noApartemen = baseDescription.split("No\\. Apart ?: ")[1];

        String finalDescription = '"' + greeting + "\n" +
                                  "Nomor apartemenku " + noApartemen + "\n" +
                                  hint + '"';

        currentCharacter = CharacterFactory.createCharacter(type, alias, finalDescription, fakeLantai);

        // Ambiguous image logic
        String imagePath;
        int r = random.nextInt(100);
        if (r < 40) {
            imagePath = "/com/tubes/assets/" + name + ".png"; // bisa benar
        } else if (r < 80) {
            imagePath = "/com/tubes/assets/dopple" + name + ".png"; // bisa menyamar
        } else {
            imagePath = "/com/tubes/assets/" + name + ".png"; // kembali ke asli
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
            currentIndex = (currentIndex + 1) % characterData.size();
            loadCurrentCharacter();
            startCharacterEntranceAnimation();
        });
    }

    @FXML
    private void showIdCard() {
        idCardImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/idCard.png")));

        String[] data = characterData.get(currentIndex);
        String originalName = data[0];
        String baseDescription = data[1];
        String originalLantai = data[2];

        // Ambil data langsung dari string deskripsi asli
        String nama = baseDescription.split("Nama ?: ")[1].split("\n")[0].trim();
        String noApart = baseDescription.split("No\\. Apart ?: ")[1].trim();

        idCardCharacterImage.setImage(new Image(getClass().getResourceAsStream("/com/tubes/assets/" + originalName + ".png")));

        String finalDesc = "Nama   : " + nama + "\n" +
                        "No. Apart : " + noApart + "\n" +
                        "Lantai : " + originalLantai;

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

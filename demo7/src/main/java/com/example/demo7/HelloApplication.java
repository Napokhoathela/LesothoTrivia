package com.example.demo7;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {

    private int questionIndex = 0;
    private int score = 0;
    private String[] questions = {
            "What is the capital city of Lesotho?",
            "What is the highest point in Lesotho?",
            "Which river forms part of the border between Lesotho and South Africa?",
            "What is the traditional Basotho blanket called?",
            "What is the name of the national park in Lesotho known for its dinosaur footprints?"
    };
    private String[][] options = {
            {"Maseru", "Leribe", "Mokhotlong", "Butha-Buthe"},
            {"Thabana Ntlenyana", "Mount Qiloane", "Njesuthi", "Tsoelike"},
            {"Orange River", "Vaal River", "Caledon River", "Lepelle River"},
            {"Sesotho", "Mokorotlo", "Bogolan", "Kente"},
            {"Sehlabathebe National Park", "Ts'ehlanyane National Park", "Bokong Nature Reserve", "Sequoia National Park"}
    };
    private String[] correctAnswers = {"Maseru", "Thabana Ntlenyana", "Caledon River", "Mokorotlo", "Ts'ehlanyane National Park"};

    private Label questionLabel;
    private Button[] optionButtons = new Button[4]; // Initialize optionButtons array
    private Timeline timeline;
    private Label timerLabel;

    @Override
    public void start(Stage primaryStage) {
        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-size: 18px;");

        ImageView imageView = new ImageView(new Image("lesotho.jpg"));
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);

        VBox questionAndImageBox = new VBox(10, imageView, questionLabel);
        questionAndImageBox.setAlignment(Pos.CENTER);
        questionAndImageBox.setPadding(new Insets(20));
        questionAndImageBox.setStyle("-fx-background-color: #f2f2f2;");

        GridPane answerGrid = new GridPane();
        answerGrid.setAlignment(Pos.CENTER);
        answerGrid.setHgap(10);
        answerGrid.setVgap(10);
        answerGrid.setPadding(new Insets(20));
        answerGrid.setStyle("-fx-background-color: #f2f2f2;");

        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            optionButtons[i] = new Button(options[questionIndex][i]);
            optionButtons[i].setOnAction(event -> checkAnswer(optionIndex));
            optionButtons[i].setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
            answerGrid.add(optionButtons[i], i % 2, i / 2);
        }

        timerLabel = new Label();
        timerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox root = new VBox(10, questionAndImageBox, answerGrid, timerLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f2f2f2;");

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        startTimer();
        displayQuestion();
    }

    private void displayQuestion() {
        questionLabel.setText(questions[questionIndex]);
        // Load corresponding image for the current question
        ImageView imageView = new ImageView(new Image("question" + questionIndex + ".jpg"));
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        ((VBox) questionLabel.getParent()).getChildren().set(0, imageView);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[questionIndex][i]);
        }
    }

    private void checkAnswer(int optionIndex) {
        timeline.stop(); // Stop the timer when an answer is selected
        if (options[questionIndex][optionIndex].equals(correctAnswers[questionIndex])) {
            score++; // Increment score if answer is correct
            showFeedback("Correct!", "green");
        } else {
            showFeedback("Incorrect. Try again.", "red");
        }
        // Move to the next question
        questionIndex++;
        // Check if all questions have been answered
        if (questionIndex < questions.length) {
            startTimer(); // Start the timer for the next question
            displayQuestion();
        } else {
            showFinalScore();
        }
    }

    private void showFeedback(String message, String color) {
        Label feedbackLabel = new Label(message);
        feedbackLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + color + ";");
        ((VBox) questionLabel.getParent()).getChildren().add(feedbackLabel);
    }

    private void showFinalScore() {
        Label finalScoreLabel = new Label("Final Score: " + score + "/" + questions.length);
        finalScoreLabel.setStyle("-fx-font-weight: bold;");
        ((VBox) questionLabel.getParent()).getChildren().add(finalScoreLabel);
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timerLabel.setText("Time left: " + (20 - timeline.getCurrentTime().toSeconds()) + " seconds");
            if (timeline.getCurrentTime().toSeconds() >= 20) {
                timeline.stop();
                showFeedback("Time's up!", "red");
                questionIndex++;
                if (questionIndex < questions.length) {
                    displayQuestion();
                    startTimer();
                } else {
                    showFinalScore();
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

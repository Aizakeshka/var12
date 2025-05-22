package com.example.var12;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    private TextField amountField;
    private ComboBox<String> fromCurrencyComboBox;
    private ComboBox<String> toCurrencyComboBox;
    private Label resultLabel;

    private final Map<String, Double> exchangeRates = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        initializeExchangeRates();

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        amountField = new TextField();
        amountField.setPromptText("Введите сумму");
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                amountField.setText(oldValue);
            }
        });

        // Создаем ComboBox для выбора валют
        fromCurrencyComboBox = new ComboBox<>();
        toCurrencyComboBox = new ComboBox<>();
        fromCurrencyComboBox.getItems().addAll("USD", "EUR", "KZT", "JPY");
        toCurrencyComboBox.getItems().addAll("USD", "EUR", "KZT", "JPY");
        fromCurrencyComboBox.setValue("USD");
        toCurrencyComboBox.setValue("KZT");

        Button swapButton = new Button("⇄");
        swapButton.setOnAction(e -> swapCurrencies());

        HBox currencyBox = new HBox(10);
        currencyBox.getChildren().addAll(
                fromCurrencyComboBox,
                swapButton,
                toCurrencyComboBox
        );

        Button convertButton = new Button("Конвертировать");
        convertButton.setOnAction(e -> convertCurrency());

        resultLabel = new Label("Результат конвертации");

        amountField.textProperty().addListener((obs, oldVal, newVal) -> convertCurrency());
        fromCurrencyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> convertCurrency());
        toCurrencyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> convertCurrency());

        root.getChildren().addAll(
                new Label("Сумма:"),
                amountField,
                new Label("Из валюты:"),
                currencyBox,
                convertButton,
                resultLabel
        );

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Конвертер валют");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeExchangeRates() {
        exchangeRates.put("USD_KZT", 460.00);
        exchangeRates.put("USD_EUR", 0.92);
        exchangeRates.put("USD_JPY", 142.86); // 1/0.007

        exchangeRates.put("EUR_KZT", 490.00);
        exchangeRates.put("EUR_USD", 1.09);

        exchangeRates.put("JPY_USD", 0.007);

        exchangeRates.put("KZT_USD", 1.0/460.00);
        exchangeRates.put("KZT_EUR", 1.0/490.00);

        exchangeRates.put("USD_USD", 1.0);
        exchangeRates.put("EUR_EUR", 1.0);
        exchangeRates.put("KZT_KZT", 1.0);
        exchangeRates.put("JPY_JPY", 1.0);

        exchangeRates.put("JPY_EUR", exchangeRates.get("JPY_USD") * exchangeRates.get("USD_EUR"));
        exchangeRates.put("JPY_KZT", exchangeRates.get("JPY_USD") * exchangeRates.get("USD_KZT"));
        exchangeRates.put("EUR_JPY", 1.0 / exchangeRates.get("JPY_EUR"));
    }

    private void convertCurrency() {
        try {
            String fromCurrency = fromCurrencyComboBox.getValue();
            String toCurrency = toCurrencyComboBox.getValue();

            if (fromCurrency.equals(toCurrency)) {
                resultLabel.setText("Выберите разные валюты");
                return;
            }

            if (amountField.getText().isEmpty()) {
                resultLabel.setText("Введите сумму");
                return;
            }

            double amount = Double.parseDouble(amountField.getText());
            String rateKey = fromCurrency + "_" + toCurrency;

            if (!exchangeRates.containsKey(rateKey)) {
                resultLabel.setText("Курс не найден");
                return;
            }

            double rate = exchangeRates.get(rateKey);
            double result = amount * rate;

            DecimalFormat df = new DecimalFormat("#.##");
            String formattedResult = df.format(result);

            resultLabel.setText(String.format("%s %s = %s %s",
                    amountField.getText(), fromCurrency,
                    formattedResult, toCurrency));

        } catch (NumberFormatException e) {
            resultLabel.setText("Ошибка ввода суммы");
        }
    }

    private void swapCurrencies() {
        String temp = fromCurrencyComboBox.getValue();
        fromCurrencyComboBox.setValue(toCurrencyComboBox.getValue());
        toCurrencyComboBox.setValue(temp);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
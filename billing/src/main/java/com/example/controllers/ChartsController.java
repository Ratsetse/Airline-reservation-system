package com.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;

public class ChartsController {

    @FXML
    private ChoiceBox<String> chartTypeChoiceBox;

    @FXML
    private BorderPane chartContainer;

    @FXML
    public void initialize() {
        chartTypeChoiceBox.getItems().addAll("Bar Chart", "Pie Chart", "Line Chart");
    }

    @FXML
    private void handleLoadChart() {
        String type = chartTypeChoiceBox.getValue();
        if (type == null) return;

        switch (type) {
            case "Bar Chart":
                showBarChart();
                break;
            case "Pie Chart":
                showPieChart();
                break;
            case "Line Chart":
                showLineChart();
                break;
        }
    }

    private void showBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Month");
        yAxis.setLabel("Payments");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Payments Data");
        series.getData().add(new XYChart.Data<>("Jan", 200));
        series.getData().add(new XYChart.Data<>("Feb", 450));
        series.getData().add(new XYChart.Data<>("Mar", 300));
        barChart.getData().add(series);
        chartContainer.setCenter(barChart);
    }

    private void showPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(
                new PieChart.Data("Paid", 70),
                new PieChart.Data("Unpaid", 30)
        );
        chartContainer.setCenter(pieChart);
    }

    private void showLineChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Payments Trend");
        series.getData().add(new XYChart.Data<>(1, 100));
        series.getData().add(new XYChart.Data<>(2, 200));
        series.getData().add(new XYChart.Data<>(3, 300));
        lineChart.getData().add(series);
        chartContainer.setCenter(lineChart);
    }
}

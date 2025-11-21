package com.example.controllers;

import com.example.models.Customer;
import com.example.models.CustomerManager;
import com.example.models.BillCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.List;

public class MainController {

    @FXML private TextField txtCustomerId;
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtMeterNumber;
    @FXML private TextField txtUsage;
    @FXML private TextField txtSearch;

    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, String> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colAddress;
    @FXML private TableColumn<Customer, String> colMeter;
    @FXML private TableColumn<Customer, Double> colUsage;
    @FXML private TableColumn<Customer, Double> colBill;

    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private final CustomerManager manager = CustomerManager.getInstance();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("address"));
        colMeter.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("meterNumber"));
        colUsage.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("usage"));
        colBill.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("lastBill"));

        customerList.addAll(manager.getCustomers());
        tableCustomers.setItems(customerList);

        // 👇 When a row is clicked, load data into text fields
        tableCustomers.setOnMouseClicked(e -> {
            Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtCustomerId.setText(selected.getId());
                txtName.setText(selected.getName());
                txtAddress.setText(selected.getAddress());
                txtMeterNumber.setText(selected.getMeterNumber());
                txtUsage.setText(String.valueOf(selected.getUsage()));
            }
        });
    }
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/billing/login.fxml"));
            Stage stage = (Stage) txtCustomerId.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/com/example/billing/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("LEC Billing - Login");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Logout Failed");
            alert.setContentText("Could not return to login screen.");
            alert.showAndWait();
        }
    }

    // ---------------- Add ----------------
    @FXML
    private void handleAddCustomer() {
        String id = txtCustomerId.getText().trim();
        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String meter = txtMeterNumber.getText().trim();
        String usageText = txtUsage.getText().trim();

        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || meter.isEmpty() || usageText.isEmpty()) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        double usage;
        try {
            usage = Double.parseDouble(usageText);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Usage must be a valid number.");
            return;
        }

        double bill = BillCalculator.calculate(usage);
        Customer customer = new Customer(id, name, address, meter, usage, bill);

        if (manager.addCustomer(customer)) {
            customerList.add(customer);
            clearForm();
        } else {
            showAlert("Duplicate Error", "Customer ID already exists!");
        }
    }

    // ---------------- Update ----------------
    @FXML
    private void handleUpdateCustomer() {
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a customer to update.");
            return;
        }

        try {
            double usage = Double.parseDouble(txtUsage.getText().trim());
            double bill = BillCalculator.calculate(usage);

            // Create an updated object
            Customer updated = new Customer(
                    txtCustomerId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtMeterNumber.getText(),
                    usage,
                    bill
            );

            // ✅ Update both in the manager and list
            boolean success = manager.updateCustomer(selected.getId(), updated);

            if (success) {
                int index = customerList.indexOf(selected);
                customerList.set(index, updated);
                tableCustomers.refresh();
                clearForm();
                showAlert("Success", "Customer updated successfully!");
            } else {
                showAlert("Update Failed", "Could not update customer. Try again.");
            }

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Usage must be a valid number.");
        }
    }

    // ---------------- Delete ----------------
    @FXML
    private void handleDeleteCustomer() {
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a customer to delete.");
            return;
        }

        if (manager.deleteCustomer(selected.getId())) {
            customerList.remove(selected);
            showAlert("Success", "Customer deleted.");
        }
    }

    // ---------------- Print Receipt ----------------
    @FXML
    private void handlePrintReceipt() {
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Select a customer to print receipt.");
            return;
        }

        String receipt = """
                ----- LESOTHO ELECTRICITY RECEIPT -----
                Customer ID: %s
                Name: %s
                Address: %s
                Meter Number: %s
                Usage: %.2f kWh
                Bill: M %.2f
                -------------------------------------
                """.formatted(
                selected.getId(),
                selected.getName(),
                selected.getAddress(),
                selected.getMeterNumber(),
                selected.getUsage(),
                selected.getLastBill()
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Receipt");
        alert.setHeaderText("Customer Receipt");
        alert.setContentText(receipt);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    // ---------------- Search ----------------
    @FXML
    private void handleSearchCustomer() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            tableCustomers.setItems(customerList);
            return;
        }

        List<Customer> filtered = manager.search(keyword);
        tableCustomers.setItems(FXCollections.observableArrayList(filtered));
    }

    // ---------------- Open Charts ----------------
    @FXML
    private void handleOpenCharts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/billing/ChartsView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Customer Bills Chart");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open charts window.");
        }
    }

    // ---------------- Utilities ----------------
    private void clearForm() {
        txtCustomerId.clear();
        txtName.clear();
        txtAddress.clear();
        txtMeterNumber.clear();
        txtUsage.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}


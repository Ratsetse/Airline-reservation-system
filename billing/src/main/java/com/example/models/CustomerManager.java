package com.example.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerManager {

    // ✅ Save file path – guaranteed to work on all computers
    private final String DATA_FILE = System.getProperty("user.home") + File.separator + "customers.dat";

    // Singleton pattern: one shared instance across app
    private static CustomerManager instance = new CustomerManager();

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    private CustomerManager() {
        loadFromFile(); // Automatically load on instantiation
    }

    public static CustomerManager getInstance() {
        return instance;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    // ---------------- Add Customer ----------------
    public boolean addCustomer(Customer c) {
        if (findById(c.getId()).isPresent()) return false;
        customers.add(c);
        saveToFile(); // auto-save after adding
        return true;
    }

    // ---------------- Update Customer ----------------
    public boolean updateCustomer(String id, Customer updated) {
        Optional<Customer> opt = findById(id);
        if (opt.isPresent()) {
            Customer existing = opt.get();
            existing.setName(updated.getName());
            existing.setAddress(updated.getAddress());
            existing.setMeterNumber(updated.getMeterNumber());
            existing.setUsage(updated.getUsage());
            existing.setLastBill(updated.getLastBill());
            saveToFile(); // auto-save after update
            return true;
        }
        return false;
    }

    // ---------------- Delete Customer ----------------
    public boolean deleteCustomer(String id) {
        Optional<Customer> opt = findById(id);
        if (opt.isPresent()) {
            customers.remove(opt.get());
            saveToFile(); // auto-save after delete
            return true;
        }
        return false;
    }

    // ---------------- Find by ID ----------------
    public Optional<Customer> findById(String id) {
        return customers.stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    // ---------------- Search by ID or Name ----------------
    public List<Customer> search(String query) {
        String lower = query.toLowerCase();
        List<Customer> result = new ArrayList<>();
        for (Customer c : customers) {
            if (c.getId().toLowerCase().contains(lower) ||
                    c.getName().toLowerCase().contains(lower)) {
                result.add(c);
            }
        }
        return result;
    }

    // ---------------- Load from File ----------------
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            System.out.println("No previous data found. File will be created at: " + f.getAbsolutePath());
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                ArrayList<Customer> list = (ArrayList<Customer>) obj;
                customers.clear();
                customers.addAll(list);
                System.out.println("✅ Loaded " + customers.size() + " customers from file: " + f.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to load customers: " + e.getMessage());
        }
    }

    // ---------------- Save to File ----------------
    public void saveToFile() {
        ArrayList<Customer> list = new ArrayList<>(customers);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(list);
            System.out.println("💾 Customers saved successfully to: " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("⚠️ Failed to save customers: " + e.getMessage());
        }
    }

    // ---------------- Clear All Customers (Optional) ----------------
    public void clearAll() {
        customers.clear();
        saveToFile();
    }
}

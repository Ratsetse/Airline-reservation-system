package com.example.models;

public class BillCalculator {

    public static double calculate(double usage) {
        if (usage <= 100) {
            return usage * 1.20;
        } else if (usage <= 300) {
            return (100 * 1.20) + ((usage - 100) * 1.50);
        } else {
            return (100 * 1.20) + (200 * 1.50) + ((usage - 300) * 2.00);
        }
    }
}

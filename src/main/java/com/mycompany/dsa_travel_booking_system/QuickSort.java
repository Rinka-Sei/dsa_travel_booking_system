package com.mycompany.dsa_travel_booking_system;

import java.util.List;

public class QuickSort {

    // Sort by Price (Low to High)
    public static void sortByPrice(List<PackageDeal> packages, int low, int high) {
        if (low < high) {
            int pi = partitionPrice(packages, low, high);
            sortByPrice(packages, low, pi - 1);
            sortByPrice(packages, pi + 1, high);
        }
    }

    private static int partitionPrice(List<PackageDeal> packages, int low, int high) {
        double pivot = packages.get(high).getTotalPrice();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (packages.get(j).getTotalPrice() <= pivot) {
                i++;
                PackageDeal temp = packages.get(i);
                packages.set(i, packages.get(j));
                packages.set(j, temp);
            }
        }
        PackageDeal temp = packages.get(i + 1);
        packages.set(i + 1, packages.get(high));
        packages.set(high, temp);
        return i + 1;
    }

    // Sort by Feature: Hotel Rating (High to Low)
    public static void sortByRating(List<PackageDeal> packages, int low, int high) {
        if (low < high) {
            int pi = partitionRating(packages, low, high);
            sortByRating(packages, low, pi - 1);
            sortByRating(packages, pi + 1, high);
        }
    }

    private static int partitionRating(List<PackageDeal> packages, int low, int high) {
        double pivot = packages.get(high).getHotelRating();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            // Sort descending for rating (higher rating first)
            if (packages.get(j).getHotelRating() >= pivot) {
                i++;
                PackageDeal temp = packages.get(i);
                packages.set(i, packages.get(j));
                packages.set(j, temp);
            }
        }
        PackageDeal temp = packages.get(i + 1);
        packages.set(i + 1, packages.get(high));
        packages.set(high, temp);
        return i + 1;
    }

    static void sortHotelsByPrice(List<Hotel> list, int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
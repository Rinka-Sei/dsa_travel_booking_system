package com.mycompany.dsa_travel_booking_system.ds;

import com.mycompany.dsa_travel_booking_system.model.PackageDeal;
import com.mycompany.dsa_travel_booking_system.model.Hotel;
import java.util.List;

/**
 * QuickSort implementation for sorting lists of travel items such as PackageDeal objects.
 *
 * Time Complexities:
 * <ul>
 *   <li>Best / Average case: O(n log n)</li>
 *   <li>Worst case: O(n^2) when partitions are highly unbalanced</li>
 * </ul>
 * Space Complexity: O(log n) call stack space on average.
 */
public class QuickSort {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private QuickSort() {
    }

    /**
     * Sorts a list of package deals in ascending order by total price using QuickSort.
     * <p>
     * Time Complexity: O(n log n) average case, O(n^2) worst case.
     * </p>
     *
     * @param packages the list of PackageDeal objects to sort
     * @param low      the starting index of the sub-array to sort
     * @param high     the ending index of the sub-array to sort
     */
    public static void sortByPrice(List<PackageDeal> packages, int low, int high) {
        if (packages == null || packages.isEmpty()) {
            return;
        }
        if (low < high && low >= 0 && high < packages.size()) {
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

    /**
     * Sorts a list of package deals in descending order by hotel rating using QuickSort.
     * <p>
     * Time Complexity: O(n log n) average case, O(n^2) worst case.
     * </p>
     *
     * @param packages the list of PackageDeal objects to sort
     * @param low      the starting index of the sub-array to sort
     * @param high     the ending index of the sub-array to sort
     */
    public static void sortByRating(List<PackageDeal> packages, int low, int high) {
        if (packages == null || packages.isEmpty()) {
            return;
        }
        if (low < high && low >= 0 && high < packages.size()) {
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

    /**
     * Stub method for sorting hotels by price (unsupported).
     *
     * @param list the list of hotels
     * @param i    start index
     * @param i0   end index
     * @throws UnsupportedOperationException always thrown as method is not supported yet
     */
    static void sortHotelsByPrice(List<Hotel> list, int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

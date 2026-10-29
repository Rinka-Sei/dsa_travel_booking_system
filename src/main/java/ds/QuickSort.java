package ds;

import model.PackageDeal;
import model.Hotel;
import java.util.List;

/**
 * QuickSort implementation for sorting lists of travel items such as PackageDeal objects.
 * Supports toggleable Ascending and Descending sorting modes for both total price and hotel ratings.
 *
 * <p>Time Complexities:</p>
 * <ul>
 *   <li>Best / Average case: O(n log n)</li>
 *   <li>Worst case: O(n^2) when partitions are highly unbalanced</li>
 * </ul>
 * <p>Space Complexity: O(log n) call stack space on average.</p>
 */
public class QuickSort {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private QuickSort() {
    }

    /**
     * Sorts a list of package deals by total price in ascending order by default.
     *
     * @param packages the list of PackageDeal objects to sort
     * @param low      the starting index
     * @param high     the ending index
     */
    public static void sortByPrice(List<PackageDeal> packages, int low, int high) {
        sortByPrice(packages, low, high, true);
    }

    /**
     * Sorts a list of package deals by total price in the specified direction (ascending or descending).
     * <p>
     * Time Complexity: O(n log n) average case, O(n^2) worst case.
     * </p>
     *
     * @param packages  the list of PackageDeal objects to sort
     * @param low       the starting index of the sub-array to sort
     * @param high      the ending index of the sub-array to sort
     * @param ascending {@code true} for ascending order (low to high), {@code false} for descending order
     */
    public static void sortByPrice(List<PackageDeal> packages, int low, int high, boolean ascending) {
        if (packages == null || packages.isEmpty()) {
            return;
        }
        if (low < high && low >= 0 && high < packages.size()) {
            int pi = partitionPrice(packages, low, high, ascending);
            sortByPrice(packages, low, pi - 1, ascending);
            sortByPrice(packages, pi + 1, high, ascending);
        }
    }

    private static int partitionPrice(List<PackageDeal> packages, int low, int high, boolean ascending) {
        double pivot = packages.get(high).getTotalPrice();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = ascending
                    ? packages.get(j).getTotalPrice() <= pivot
                    : packages.get(j).getTotalPrice() >= pivot;
            if (condition) {
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
     * Sorts a list of package deals by hotel rating in descending order by default.
     *
     * @param packages the list of PackageDeal objects to sort
     * @param low      the starting index
     * @param high     the ending index
     */
    public static void sortByRating(List<PackageDeal> packages, int low, int high) {
        sortByRating(packages, low, high, false);
    }

    /**
     * Sorts a list of package deals by hotel rating in the specified direction.
     * <p>
     * Time Complexity: O(n log n) average case, O(n^2) worst case.
     * </p>
     *
     * @param packages  the list of PackageDeal objects to sort
     * @param low       the starting index of the sub-array to sort
     * @param high      the ending index of the sub-array to sort
     * @param ascending {@code true} for ascending order (low to high rating), {@code false} for descending order (high rating first)
     */
    public static void sortByRating(List<PackageDeal> packages, int low, int high, boolean ascending) {
        if (packages == null || packages.isEmpty()) {
            return;
        }
        if (low < high && low >= 0 && high < packages.size()) {
            int pi = partitionRating(packages, low, high, ascending);
            sortByRating(packages, low, pi - 1, ascending);
            sortByRating(packages, pi + 1, high, ascending);
        }
    }

    private static int partitionRating(List<PackageDeal> packages, int low, int high, boolean ascending) {
        double pivot = packages.get(high).getHotelRating();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = ascending
                    ? packages.get(j).getHotelRating() <= pivot
                    : packages.get(j).getHotelRating() >= pivot;
            if (condition) {
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

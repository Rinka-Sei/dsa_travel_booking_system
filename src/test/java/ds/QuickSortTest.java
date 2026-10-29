package ds;

import model.Flight;
import model.Hotel;
import model.PackageDeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link QuickSort}.
 */
public class QuickSortTest {

    private Flight defaultFlight;

    @BeforeEach
    public void setUp() {
        defaultFlight = new Flight("FL001", "NYC", "LAX", "10:00", 100.0);
    }

    @Test
    public void testSortByPriceEmptyList() {
        List<PackageDeal> emptyList = new ArrayList<>();
        QuickSort.sortByPrice(emptyList, 0, emptyList.size() - 1, true);
        assertTrue(emptyList.isEmpty(), "Empty list should remain empty.");

        QuickSort.sortByPrice(emptyList, 0, emptyList.size() - 1, false);
        assertTrue(emptyList.isEmpty(), "Empty list should remain empty when sorting descending.");
    }

    @Test
    public void testSortByPriceNullList() {
        assertDoesNotThrow(() -> QuickSort.sortByPrice(null, 0, 0, true),
                "Sorting a null list should be handled gracefully without throwing NPE.");
    }

    @Test
    public void testSortByPriceSingleElementList() {
        List<PackageDeal> singleList = new ArrayList<>();
        Hotel hotel = new Hotel("H01", "Grand Hotel", "NYC", 4.5, 200.0);
        singleList.add(new PackageDeal(defaultFlight, hotel));

        QuickSort.sortByPrice(singleList, 0, singleList.size() - 1, true);
        assertEquals(1, singleList.size());
        assertEquals(300.0, singleList.get(0).getTotalPrice(), 0.001);
    }

    @Test
    public void testSortByPriceAscendingAndDescending() {
        List<PackageDeal> packages = new ArrayList<>();
        Hotel h1 = new Hotel("H01", "Hotel A", "NYC", 4.0, 300.0); // total 400.0
        Hotel h2 = new Hotel("H02", "Hotel B", "NYC", 3.5, 100.0); // total 200.0
        Hotel h3 = new Hotel("H03", "Hotel C", "NYC", 5.0, 500.0); // total 600.0
        Hotel h4 = new Hotel("H04", "Hotel D", "NYC", 4.8, 250.0); // total 350.0

        packages.add(new PackageDeal(defaultFlight, h1));
        packages.add(new PackageDeal(defaultFlight, h2));
        packages.add(new PackageDeal(defaultFlight, h3));
        packages.add(new PackageDeal(defaultFlight, h4));

        // Test Ascending
        QuickSort.sortByPrice(packages, 0, packages.size() - 1, true);

        assertEquals(200.0, packages.get(0).getTotalPrice(), 0.001);
        assertEquals(350.0, packages.get(1).getTotalPrice(), 0.001);
        assertEquals(400.0, packages.get(2).getTotalPrice(), 0.001);
        assertEquals(600.0, packages.get(3).getTotalPrice(), 0.001);

        // Test Descending
        QuickSort.sortByPrice(packages, 0, packages.size() - 1, false);

        assertEquals(600.0, packages.get(0).getTotalPrice(), 0.001);
        assertEquals(400.0, packages.get(1).getTotalPrice(), 0.001);
        assertEquals(350.0, packages.get(2).getTotalPrice(), 0.001);
        assertEquals(200.0, packages.get(3).getTotalPrice(), 0.001);
    }

    @Test
    public void testSortByRatingAscendingAndDescending() {
        List<PackageDeal> packages = new ArrayList<>();
        Hotel h1 = new Hotel("H01", "Hotel A", "NYC", 3.0, 100.0);
        Hotel h2 = new Hotel("H02", "Hotel B", "NYC", 5.0, 200.0);
        Hotel h3 = new Hotel("H03", "Hotel C", "NYC", 4.2, 150.0);
        Hotel h4 = new Hotel("H04", "Hotel D", "NYC", 4.8, 180.0);

        packages.add(new PackageDeal(defaultFlight, h1));
        packages.add(new PackageDeal(defaultFlight, h2));
        packages.add(new PackageDeal(defaultFlight, h3));
        packages.add(new PackageDeal(defaultFlight, h4));

        // Test Descending (high rating first)
        QuickSort.sortByRating(packages, 0, packages.size() - 1, false);

        assertEquals(5.0, packages.get(0).getHotelRating(), 0.001);
        assertEquals(4.8, packages.get(1).getHotelRating(), 0.001);
        assertEquals(4.2, packages.get(2).getHotelRating(), 0.001);
        assertEquals(3.0, packages.get(3).getHotelRating(), 0.001);

        // Test Ascending (low rating first)
        QuickSort.sortByRating(packages, 0, packages.size() - 1, true);

        assertEquals(3.0, packages.get(0).getHotelRating(), 0.001);
        assertEquals(4.2, packages.get(1).getHotelRating(), 0.001);
        assertEquals(4.8, packages.get(2).getHotelRating(), 0.001);
        assertEquals(5.0, packages.get(3).getHotelRating(), 0.001);
    }

    @Test
    public void testSortByPriceAndRatingWithDuplicates() {
        List<PackageDeal> packages = new ArrayList<>();
        Hotel h1 = new Hotel("H01", "Hotel A", "NYC", 4.0, 200.0); // price = 300, rating = 4.0
        Hotel h2 = new Hotel("H02", "Hotel B", "NYC", 4.0, 200.0); // price = 300, rating = 4.0

        packages.add(new PackageDeal(defaultFlight, h1));
        packages.add(new PackageDeal(defaultFlight, h2));

        QuickSort.sortByPrice(packages, 0, packages.size() - 1, true);
        assertEquals(2, packages.size());
        assertEquals(300.0, packages.get(0).getTotalPrice(), 0.001);

        QuickSort.sortByRating(packages, 0, packages.size() - 1, false);
        assertEquals(2, packages.size());
        assertEquals(4.0, packages.get(0).getHotelRating(), 0.001);
    }
}

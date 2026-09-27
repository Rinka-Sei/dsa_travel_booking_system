package com.mycompany.dsa_travel_booking_system.ds;

import com.mycompany.dsa_travel_booking_system.model.Flight;
import com.mycompany.dsa_travel_booking_system.model.Hotel;
import com.mycompany.dsa_travel_booking_system.model.PackageDeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link QuickSort}.
 */
class QuickSortTest {

    private Flight flight1;
    private Flight flight2;
    private Flight flight3;

    private Hotel hotel1;
    private Hotel hotel2;
    private Hotel hotel3;

    @BeforeEach
    void setUp() {
        flight1 = new Flight("F1", "NYC", "LON", 1000, 500.0);
        flight2 = new Flight("F2", "NYC", "PAR", 1200, 300.0);
        flight3 = new Flight("F3", "NYC", "TYO", 1500, 700.0);

        hotel1 = new Hotel("H1", "Grand Hotel", "LON", 4.5, 200.0);
        hotel2 = new Hotel("H2", "Budget Inn", "PAR", 3.0, 50.0);
        hotel3 = new Hotel("H3", "Luxury Resort", "TYO", 5.0, 500.0);
    }

    // --- Tests for sortByPrice (Low to High) ---

    @Test
    void testSortByPriceEmptyList() {
        List<PackageDeal> packages = new ArrayList<>();
        QuickSort.sortByPrice(packages, 0, packages.size() - 1);
        assertTrue(packages.isEmpty());
    }

    @Test
    void testSortByPriceSingleItem() {
        List<PackageDeal> packages = new ArrayList<>();
        packages.add(new PackageDeal(flight1, hotel1)); // Price: 500 + 200 = 700.0

        QuickSort.sortByPrice(packages, 0, packages.size() - 1);
        assertEquals(1, packages.size());
        assertEquals(700.0, packages.get(0).getTotalPrice());
    }

    @Test
    void testSortByPriceUnsortedList() {
        List<PackageDeal> packages = new ArrayList<>();
        PackageDeal pkg1 = new PackageDeal(flight1, hotel1); // 700.0
        PackageDeal pkg2 = new PackageDeal(flight2, hotel2); // 350.0
        PackageDeal pkg3 = new PackageDeal(flight3, hotel3); // 1200.0

        packages.add(pkg1);
        packages.add(pkg2);
        packages.add(pkg3);

        QuickSort.sortByPrice(packages, 0, packages.size() - 1);

        assertEquals(3, packages.size());
        assertEquals(350.0, packages.get(0).getTotalPrice());
        assertEquals(700.0, packages.get(1).getTotalPrice());
        assertEquals(1200.0, packages.get(2).getTotalPrice());
    }

    // --- Tests for sortByRating (High to Low) ---

    @Test
    void testSortByRatingEmptyList() {
        List<PackageDeal> packages = new ArrayList<>();
        QuickSort.sortByRating(packages, 0, packages.size() - 1);
        assertTrue(packages.isEmpty());
    }

    @Test
    void testSortByRatingSingleItem() {
        List<PackageDeal> packages = new ArrayList<>();
        packages.add(new PackageDeal(flight1, hotel1)); // Rating: 4.5

        QuickSort.sortByRating(packages, 0, packages.size() - 1);
        assertEquals(1, packages.size());
        assertEquals(4.5, packages.get(0).getHotelRating());
    }

    @Test
    void testSortByRatingUnsortedList() {
        List<PackageDeal> packages = new ArrayList<>();
        PackageDeal pkg1 = new PackageDeal(flight1, hotel1); // Rating: 4.5
        PackageDeal pkg2 = new PackageDeal(flight2, hotel2); // Rating: 3.0
        PackageDeal pkg3 = new PackageDeal(flight3, hotel3); // Rating: 5.0

        packages.add(pkg1);
        packages.add(pkg2);
        packages.add(pkg3);

        QuickSort.sortByRating(packages, 0, packages.size() - 1);

        assertEquals(3, packages.size());
        assertEquals(5.0, packages.get(0).getHotelRating());
        assertEquals(4.5, packages.get(1).getHotelRating());
        assertEquals(3.0, packages.get(2).getHotelRating());
    }
}

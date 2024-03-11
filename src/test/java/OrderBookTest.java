import org.example.Order;
import org.example.OrderBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {
    private OrderBook orderBook;
    private TreeMap<Double, TreeMap<Long, Order>> bids;
    private TreeMap<Double, TreeMap<Long, Order>> offers;
    private Map<Long, Order> orderMap;

    @BeforeEach
    void setUp() {
        bids = new TreeMap<>(Collections.reverseOrder());
        offers = new TreeMap<>();
        orderMap = new HashMap<>();

        orderBook = new OrderBook(bids, offers, orderMap);
    }

    @Test
    void testAddOrder() {
        var order = new Order(1, 91.5, 'B', 10, System.currentTimeMillis());
        orderBook.addOrder(order);
        assertEquals(1, bids.get(order.getPrice()).size());
        assertEquals(orderMap.get(order.getId()).getTimestamp(), order.getTimestamp());
    }

    @Test
    void testRemoveOrder() {
        var order = new Order(1, 91.5, 'O', 10, System.currentTimeMillis());
        orderBook.addOrder(order);
        orderBook.removeOrder(1);
        assertTrue(orderMap.isEmpty());
        assertEquals(0, offers.get(order.getPrice()).size());
    }

    @Test
    void testModifyOrderSize() {
        var order = new Order(1, 91.5, 'B', 10, System.currentTimeMillis());
        orderBook.addOrder(order);
        orderBook.modifyOrderSize(1, 20);
        assertEquals(20, orderMap.get(order.getId()).getSize());
        assertEquals(20, bids.get(order.getPrice()).values().stream().toList().getFirst().getSize());
    }

    @Test
    void testGetPriceForLevelBids() {
        var order1 = new Order(1, 91.5, 'B', 10, System.currentTimeMillis());
        var order2 = new Order(2, 200.0, 'B', 20, System.currentTimeMillis() + 1);
        var order3 = new Order(2, 52.0, 'B', 20, System.currentTimeMillis() - 1);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        assertEquals(200.0, orderBook.getPriceForLevel('B', 1));
        assertEquals(91.5, orderBook.getPriceForLevel('B', 2));
        assertEquals(52.0, orderBook.getPriceForLevel('B', 3));

    }

    @Test
    void testGetPriceForLevelOffers() {
        var order1 = new Order(1, 91.5, 'O', 10, System.currentTimeMillis());
        var order2 = new Order(2, 200.0, 'O', 20, System.currentTimeMillis() + 1);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        assertEquals(91.5, orderBook.getPriceForLevel('O', 1));
        assertEquals(200.0, orderBook.getPriceForLevel('O', 2));
    }

    @Test
    void testGetTotalSizeForLevel() {
        var order1 = new Order(1, 91.5, 'B', 10, System.currentTimeMillis());
        var order2 = new Order(2, 91.5, 'B', 20, System.currentTimeMillis() + 1);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        assertEquals(30, orderBook.getTotalSizeForLevel('B', 1));
    }

    @Test
    void testGetAllOrdersForSideLevelTimeOrder() {
        var order1 = new Order(1, 91.5, 'B', 10, System.currentTimeMillis());
        var order2 = new Order(2, 91.5, 'B', 10, System.currentTimeMillis() + 1);
        var order3 = new Order(3, 200.0, 'B', 20, System.currentTimeMillis() + 2);
        var order4 = new Order(3, 900.0, 'O', 20, System.currentTimeMillis() + 3);

        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);

        var orders = orderBook.getAllOrdersForSideLevelTimeOrder('B');
        assertEquals(3, orders.size());
        assertEquals(3, orders.get(0).getId());
        assertEquals(1, orders.get(1).getId());
        assertEquals(2, orders.get(2).getId());

    }
}

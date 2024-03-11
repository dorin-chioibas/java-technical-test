package org.example;

import java.util.*;

public class OrderBook {
    private TreeMap<Double, TreeMap<Long, Order>> bids;
    private TreeMap<Double, TreeMap<Long, Order>> offers;
    private Map<Long, Order> orderMap;

    public OrderBook() {
        bids = new TreeMap<>(Collections.reverseOrder());
        offers = new TreeMap<>();
        orderMap = new HashMap<>();
    }

    public OrderBook(TreeMap<Double, TreeMap<Long, Order>> bids, TreeMap<Double, TreeMap<Long, Order>> offers, Map<Long, Order> orderMap) {
        this.bids = bids;
        this.offers = offers;
        this.orderMap = orderMap;
    }

    public void addOrder(Order order) {
        var orderBook = getBidsOrOffers(order);
        orderBook.computeIfAbsent(order.getPrice(), k -> new TreeMap<>())
                .put(order.getTimestamp(), order);
        orderMap.put(order.getId(), order);
    }

    public void removeOrder(long orderId) {
        var removedOrder = orderMap.remove(orderId);
        Optional.ofNullable(removedOrder)
                .ifPresent(order -> {
                    var orderBook = getBidsOrOffers(order);
                    orderBook.get(order.getPrice()).remove(order.getTimestamp());
                });
    }

    public void modifyOrderSize(long orderId, long newSize) {
        Optional.ofNullable(orderMap.get(orderId))
                .ifPresent(order -> order.setSize(newSize));
    }

    public Double getPriceForLevel(char side, int level) {
        var orderBook = getBidsOrOffers(side);
        return orderBook.keySet().stream()
                .skip(level - 1)
                .findFirst()
                .orElseThrow();
    }

    public Long getTotalSizeForLevel(char side, int level) {
        var orderBook = getBidsOrOffers(side);
        var priceForLevel = getPriceForLevel(side, level);
        return orderBook.get(priceForLevel).values().stream()
                .mapToLong(Order::getSize)
                .sum();
    }

    public List<Order> getAllOrdersForSideLevelTimeOrder(char side) {
        var orderBook = getBidsOrOffers(side);
        return orderBook.values().stream()
                .flatMap(treeMap -> treeMap.values().stream())
                .toList();
    }

    private TreeMap<Double, TreeMap<Long, Order>> getBidsOrOffers(Order order) {
        return getBidsOrOffers(order.getSide());
    }

    private TreeMap<Double, TreeMap<Long, Order>> getBidsOrOffers(char side) {
        return side == 'B' ? bids : offers;
    }
}

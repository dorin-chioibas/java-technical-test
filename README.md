# Java Technical Test

## Part A

Regarding the Part A of the test, given the fact that the problem is referring to a component used in a trading system, I
thought about performance first. The problem states the need for high frequency of additions and removals, thus it is required
to use a data structure which has good performance on both operations. Storing orders on a price time priority basis 
means there is a need for the data to be sorted first based on price and then based on time. A TreeMap is a sorted map which
offers O(log(n)) time complexity on additions, removals and retrievals, so I thought it is suitable for this use case. I
also considered using a separate hash map so mutating the orders would happen in O(1), even though it's going to take
additional space. The rest of the methods have been built around this idea. I also wrote some tests which can help you 
play with the functions, but they could also represent a starting point in testing various other use cases that you might
want to try.

## Part B

Regarding the Part B of the test, I would suggest ensuring thread safety for the `OrderBook` class, in case it is used 
concurrently. Switching to thread safe collections like `ConcurrentHashMap` would be a smart choice.
For performance reasons some caching technique could be used here, to ensure faster retrieval of some data. 
Otherwise, from a technical point of view, the classes might need proper error handling, validation, data persistence etc. 
From a feature request point of view, maybe a next step would be to implement a functionality in `OrderBook` class to match bids and offers 
and thus facilitate trade execution.
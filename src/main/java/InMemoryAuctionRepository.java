import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InMemoryAuctionRepository implements AuctionRepository {
    private HashSet<Item> items;
    private HashSet<Bidder> bidders;
    private HashSet<Bid> bids;

    public InMemoryAuctionRepository(HashSet<Item> items, HashSet<Bidder> bidders, HashSet<Bid> bids) {
        this.items = items;
        this.bidders = bidders;
        this.bids = bids;
    }

    @Override
    public Bid createBid(Bid bid) {
        if (!items.stream().anyMatch(i -> i.getId() == bid.getItem().getId()))
            throw new IllegalArgumentException("Invalid item");
        if (!bidders.stream().anyMatch(i -> i.getId() == bid.getBidder().getId()))
            throw new IllegalArgumentException("Invalid bidder");
        bids.add(bid);
        return bid;
    }

    @Override
    public SortedSet<Bid> getBids(Item item) {
        if (!items.stream().anyMatch(i -> i.getId() == item.getId()))
            throw new IllegalArgumentException("Invalid item");
        Comparator<Bid> byAmount =
                Comparator.comparingDouble(Bid::getAmount);
        Supplier<TreeSet<Bid>> supplier =
                () -> new TreeSet<>(byAmount);
        return bids
                .stream()
                .filter(b -> b.getItem().getId() == item.getId())
                .collect(Collectors.toCollection(supplier));
    }

    @Override
    public Set<Item> getItems(Bidder bidder) {
        if (!bidders.stream().anyMatch(i -> i.getId() == bidder.getId()))
            throw new IllegalArgumentException("Invalid bidder");
        return bids
                .stream()
                .filter(b -> b.getBidder().getId() == bidder.getId())
                .map(b -> b.getItem())
                .collect(Collectors.toSet());
    }
}

import java.util.Set;
import java.util.SortedSet;

public interface AuctionTracker {
    Bid bid(Bidder bidder, double amount, Item item);

    Bid getWinningBid(Item item);

    SortedSet<Bid> getBids(Item item);

    Set<Item> getItems(Bidder bidder);
}

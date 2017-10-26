import java.util.Set;
import java.util.SortedSet;

public interface AuctionRepository {
    Bid createBid(Bid bid);

    SortedSet<Bid> getBids(Item item);

    Set<Item> getItems(Bidder bidder);
}

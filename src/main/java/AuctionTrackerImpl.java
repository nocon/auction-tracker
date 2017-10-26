import java.util.Set;
import java.util.SortedSet;

public class AuctionTrackerImpl implements AuctionTracker {
    private AuctionRepository auctionRepository;

    public AuctionTrackerImpl(AuctionRepository repository) {
        auctionRepository = repository;
    }

    @Override
    public Bid bid(Bidder bidder, double amount, Item item) {
        Bid winningBit = getWinningBid(item);

        if (amount <= 0)
            throw new IllegalArgumentException("Bid amount must be above 0");

        if (winningBit != null && winningBit.getAmount() > amount)
            throw new IllegalArgumentException("Bid amount too small");

        Bid newBid = new Bid(bidder, amount, item);

        return auctionRepository.createBid(newBid);
    }

    @Override
    public Bid getWinningBid(Item item) {
        return auctionRepository.getBids(item).size() > 0 ? auctionRepository.getBids(item).last() : null;
    }

    @Override
    public SortedSet<Bid> getBids(Item item) {
        return auctionRepository.getBids(item);
    }

    @Override
    public Set<Item> getItems(Bidder bidder) {
        return auctionRepository.getItems(bidder);
    }
}

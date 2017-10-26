import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;

public class AuctionTrackerTests {
    private final Bidder nonExistentBidder = new Bidder(99, "John Doe");
    private final Item nonExistentItem = new Item(99, "Holy Grail");
    
    HashSet<Bidder> bidders;
    HashSet<Item> items;
    HashSet<Bid> bids;

    AuctionTracker unit;

    @Before
    public void SetUp() {
        //TODO: Randomise the test data

        bidders = new HashSet<Bidder>();
        bidders.add(new Bidder(1, "John"));
        bidders.add(new Bidder(2, "Mark"));
        bidders.add(new Bidder(3, "Greg"));

        items = new HashSet<>();
        items.add(new Item(1, "Apple"));
        items.add(new Item(2, "Orange"));
        items.add(new Item(3, "Duck"));

        bids = new HashSet<>();

        unit = new AuctionTrackerImpl(new InMemoryAuctionRepository(items, bidders, bids));
    }

    @Test
    public void bid_GeneratesANewBid() {
        //TODO: Use proper mock
        final Bidder bidder = bidders.iterator().next();
        final Item item = items.iterator().next();
        final double amount = 10;
        unit.bid(bidder, amount, item);
        assertEquals(1, bids.size());
        Bid bid = bids.iterator().next();
        assertEquals(bidder, bid.getBidder());
        assertEquals(amount, bid.getAmount());
        assertEquals(item, bid.getItem());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bid_BelowZero_ThrowsArgumentException() {
        unit.bid(bidders.iterator().next(), -100, items.iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bid_EqualToZero_ThrowsArgumentException() {
        unit.bid(bidders.iterator().next(), 0, items.iterator().next());
    }

    //TODO: Should this actually be considered exceptional?
    @Test(expected = IllegalArgumentException.class)
    public void bid_BelowWinningBit_ThrowsArgumentException() {
        final Bidder bidder = bidders.iterator().next();
        final Item item = items.iterator().next();
        bids.add(new Bid(bidder, 10, item));
        unit.bid(bidder, 9, item);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bid_WithNonExistentBidder_ThrowsArgumentException() {
        unit.bid(bidders.iterator().next(), 20, nonExistentItem );
    }

    @Test(expected = IllegalArgumentException.class)
    public void bid_WithNonExistentItem_ThrowsArgumentException() {
        unit.bid(nonExistentBidder, 20, items.iterator().next() );
    }

    //TODO: What happens when bidder outbids himself?

    @Test(expected = IllegalArgumentException.class)
    public void getWinningBid_WithNonExistentItem_ThrowsArgumentException() {
        unit.getWinningBid(nonExistentItem);
    }

    @Test
    public void getWinningBid_ReturnsHighestBid() {
        final Bidder bidder = bidders.iterator().next();
        final Item item = items.iterator().next();
        final double topBidAmount = 222;
        bids.add(new Bid(bidder, 10, item));
        bids.add(new Bid(bidder, 12, item));
        bids.add(new Bid(bidder, topBidAmount, item));
        Bid bid = unit.getWinningBid(item);
        assertEquals(topBidAmount, bid.getAmount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllBids_WithNonExistentItem_ThrowsArgumentException() {
        unit.getBids(nonExistentItem);
    }

    @Test
    public void getBids_ReturnsAllBids() {
        final Bidder bidder = bidders.iterator().next();
        final Item item = items.iterator().next();
        final int count = 200;
        for (int i = 0; i < count; i++) {
            bids.add(new Bid(bidder, count, item));
        }
        SortedSet<Bid> newBids = unit.getBids(item);

        //TODO: This one fails. Didn't have enough time to figure out why.
        assertEquals(count, newBids.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItems_WithNonExistentBidder_ThrowsArgumentException() {
        unit.getItems(nonExistentBidder);
    }

    @Test
    public void getItems_ReturnsAllItems() {
        final Bidder bidder = bidders.iterator().next();
        final Item item = items.iterator().next();
        bids.add(new Bid(bidder, 10, item));
        Set<Item> foundItems = unit.getItems(bidder);
        assertEquals(1, foundItems.size());
    }

    //TODO: Test with more items, there may be some problems with duplicates
}

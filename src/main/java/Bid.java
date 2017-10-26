public class Bid {
    private final double amount;
    private final Bidder bidder;
    private final Item item;

    public Bid(Bidder bidder, double amount, Item item) {
        this.amount = amount;
        this.bidder = bidder;
        this.item = item;
    }

    public double getAmount() {
        return amount;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public Item getItem() {
        return item;
    }
}

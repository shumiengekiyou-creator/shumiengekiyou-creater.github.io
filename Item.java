public class Item {
    private final String id;
    private final String name;
    private int stock;

    public Item(String id, String name, int stock) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("商品IDは必須です");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("商品名は必須です");
        if (stock < 0) throw new IllegalArgumentException("在庫は0以上です");
        this.id = id.trim();
        this.name = name.trim();
        this.stock = stock;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }

    public void addStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("数量は1以上で入力してください");
        stock += qty;
    }

    public void removeStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("数量は1以上で入力してください");
        if (stock - qty < 0) throw new IllegalArgumentException("在庫不足です（現在在庫: " + stock + "）");
        stock -= qty;
    }
}

import java.util.*;

public class Main {
    private static final String FILE_NAME = "inventory.csv";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        InventoryService service = new InventoryService();
        CsvStorage storage = new CsvStorage(FILE_NAME);

        // 起動時に読込
        service.loadAll(storage.load());

        System.out.println("=== 在庫・入出庫ミニシステム ===");
        System.out.println("保存ファイル: " + FILE_NAME);

        while (true) {
            printMenu();
            String cmd = readLine(sc, "> ");

            try {
                switch (cmd) {
                    case "1" -> handleAddNewItem(sc, service);
                    case "2" -> handleInStock(sc, service);
                    case "3" -> handleOutStock(sc, service);
                    case "4" -> handleListAll(service);
                    case "5" -> handleSearch(sc, service);
                    case "9" -> {
                        // 手動保存
                        storage.save(service.listAll());
                        System.out.println("保存しました。");
                    }
                    case "0" -> {
                        // 終了時に自動保存
                        storage.save(service.listAll());
                        System.out.println("保存して終了します。おつかれさま！");
                        sc.close();
                        return;
                    }
                    default -> System.out.println("メニュー番号を選んでください。");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("エラー: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("予期せぬエラー: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- メニュー ---");
        System.out.println("1) 商品登録");
        System.out.println("2) 入庫（+）");
        System.out.println("3) 出庫（-）");
        System.out.println("4) 一覧表示");
        System.out.println("5) 検索（ID/名前）");
        System.out.println("9) 保存");
        System.out.println("0) 終了");
    }

    private static void handleAddNewItem(Scanner sc, InventoryService service) {
        String id = readLine(sc, "商品ID: ");
        String name = readLine(sc, "商品名: ");
        service.addNewItem(id, name);
        System.out.println("登録しました。");
    }

    private static void handleInStock(Scanner sc, InventoryService service) {
        String id = readLine(sc, "商品ID: ");
        int qty = readInt(sc, "数量(1以上): ");
        service.inStock(id, qty);
        Item it = service.getById(id);
        System.out.println("入庫しました。現在在庫: " + it.getStock());
    }

    private static void handleOutStock(Scanner sc, InventoryService service) {
        String id = readLine(sc, "商品ID: ");
        int qty = readInt(sc, "数量(1以上): ");
        service.outStock(id, qty);
        Item it = service.getById(id);
        System.out.println("出庫しました。現在在庫: " + it.getStock());
    }

    private static void handleListAll(InventoryService service) {
        List<Item> list = service.listAll();
        if (list.isEmpty()) {
            System.out.println("まだ商品がありません。");
            return;
        }
        System.out.println("\nID\t名前\t在庫");
        System.out.println("--------------------------------");
        for (Item it : list) {
            System.out.println(it.getId() + "\t" + it.getName() + "\t" + it.getStock());
        }
    }

    private static void handleSearch(Scanner sc, InventoryService service) {
        String kw = readLine(sc, "検索キーワード: ");
        List<Item> res = service.searchByKeyword(kw);
        if (res.isEmpty()) {
            System.out.println("該当なし。");
            return;
        }
        System.out.println("\nID\t名前\t在庫");
        System.out.println("--------------------------------");
        for (Item it : res) {
            System.out.println(it.getId() + "\t" + it.getName() + "\t" + it.getStock());
        }
    }

    private static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine();
        return (s == null) ? "" : s.trim();
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                int v = Integer.parseInt(s.trim());
                if (v <= 0) {
                    System.out.println("1以上で入力してください。");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("数字で入力してください。");
            }
        }
    }
}

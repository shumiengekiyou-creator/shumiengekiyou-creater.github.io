import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CsvStorage {
    private final Path path;

    public CsvStorage(String filename) {
        this.path = Paths.get(filename);
    }

    // CSV形式: id,name,stock
    public List<Item> load() {
        List<Item> items = new ArrayList<>();
        if (!Files.exists(path)) return items;

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isBlank()) continue;

                // 先頭行がヘッダならスキップ
                if (lineNo == 1 && line.equalsIgnoreCase("id,name,stock")) continue;

                String[] parts = line.split(",", -1);
                if (parts.length != 3) {
                    // 壊れてる行は飛ばす（初心者向けに落ちない設計）
                    continue;
                }
                String id = parts[0].trim();
                String name = parts[1].trim();
                int stock;
                try {
                    stock = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    continue;
                }
                if (id.isBlank() || name.isBlank() || stock < 0) continue;

                items.add(new Item(id, name, stock));
            }
        } catch (IOException e) {
            System.out.println("CSV読込に失敗しました: " + e.getMessage());
        }

        return items;
    }

    public void save(List<Item> items) {
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            bw.write("id,name,stock");
            bw.newLine();
            for (Item it : items) {
                // カンマ禁止設計にしてるのでシンプルに保存
                bw.write(it.getId() + "," + it.getName() + "," + it.getStock());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("CSV保存に失敗しました: " + e.getMessage());
        }
    }
}

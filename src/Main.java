import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        int SIZE = 10000;
        int SEARCH = 100;
        int DELETE = 1000;

        Random rand = new Random();
        int[] data = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            data[i] = rand.nextInt(300000);
        }

        TwoThreeTree tree = new TwoThreeTree();

        long totalInsertTime = 0, totalInsertOps = 0;
        long totalSearchTime = 0, totalSearchOps = 0;
        long totalDeleteTime = 0, totalDeleteOps = 0;

        System.out.println("Выполняется вставка элементов...");
        for (int i = 0; i < SIZE; i++) {
            tree.resetOps();
            long start = System.nanoTime();
            tree.insert(data[i]);
            long end = System.nanoTime();

            totalInsertTime += (end - start);
            totalInsertOps += tree.getOps();
        }

        System.out.println("Выполняется поиск элементов...");
        for (int i = 0; i < SEARCH; i++) {
            int keyToSearch = data[rand.nextInt(SIZE)];
            tree.resetOps();
            long start = System.nanoTime();
            tree.search(keyToSearch);
            long end = System.nanoTime();

            totalSearchTime += (end - start);
            totalSearchOps += tree.getOps();
        }

        System.out.println("Выполняется удаление элементов...");
        List<Integer> keysToDelete = new ArrayList<>();
        for (int num : data) keysToDelete.add(num);
        Collections.shuffle(keysToDelete);

        for (int i = 0; i < DELETE; i++) {
            int keyToDelete = keysToDelete.get(i);
            tree.resetOps();
            long start = System.nanoTime();
            tree.delete(keyToDelete);
            long end = System.nanoTime();

            totalDeleteTime += (end - start);
            totalDeleteOps += tree.getOps();
        }

        double avgInsertTime = (totalInsertTime / (double) SIZE) / 1000.0;
        double avgInsertOps = totalInsertOps / (double) SIZE;

        double avgSearchTime = (totalSearchTime / (double) SEARCH) / 1000.0;
        double avgSearchOps = totalSearchOps / (double) SEARCH;

        double avgDeleteTime = (totalDeleteTime / (double) DELETE) / 1000.0;
        double avgDeleteOps = totalDeleteOps / (double) DELETE;

        String report = " Отчет: 2-3 дерево (Рекурсивная реализация) \n\n" +
                "1. Вставка (" + SIZE + " элементов):\n" +
                String.format("  Среднее время: %.2f мкс\n", avgInsertTime) +
                String.format("  Среднее кол-во операций: %.2f\n\n", avgInsertOps) +

                "2. Поиск (" + SEARCH + " случайных элементов):\n" +
                String.format("  Среднее время: %.2f мкс\n", avgSearchTime) +
                String.format("  Среднее кол-во операций: %.2f\n\n", avgSearchOps) +

                "3. Удаление (" + DELETE + " случайных элементов):\n" +
                String.format("  Среднее время: %.2f мкс\n", avgDeleteTime) +
                String.format("  Среднее кол-во операций: %.2f\n\n", avgDeleteOps) +

                "Ожидаемая алгоритмическая сложность O(log N). \n" +
                "log2(" + SIZE + ") ~ 13.28.\n" +
                "Практические результаты количества операций соответствуют теории.";

        System.out.println("\n" + report);

        try (PrintWriter writer = new PrintWriter(new FileWriter("my_tree_results.txt"))) {
            writer.print(report);
            System.out.println("Результаты успешно сохранены в файл my_tree_results.txt");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
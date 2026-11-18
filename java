import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        // Тест Завдання 1
        List<String> names = List.of("Ivan", "Maria", "Peter", "Olena", "John", "Anna");
        System.out.println("Task 1: " + getOddIndexedNames(names));

        // Тест Завдання 2
        System.out.println("Task 2: " + sortUpperCaseDesc(names));

        // Тест Завдання 3
        String[] numbersArray = {"1, 2, 0", "4, 5"};
        System.out.println("Task 3: " + extractAndSortNumbers(numbersArray));

        // Тест Завдання 4
        long a = 25214903917L;
        long c = 11L;
        long m = 1L << 48;
        System.out.print("Task 4: ");
        randomStream(1L, a, c, m)
                .limit(5)
                .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Тест Завдання 5
        Stream<String> first = Stream.of("A", "B", "C", "D");
        Stream<String> second = Stream.of("1", "2", "3");
        System.out.println("Task 5: " + zip(first, second).collect(Collectors.toList()));
    }

    // Завдання 1
    public static String getOddIndexedNames(List<String> names) {
        return IntStream.range(0, names.size())
                .filter(i -> i % 2 != 0)
                .mapToObj(i -> i + ". " + names.get(i))
                .collect(Collectors.joining(", "));
    }

    // Завдання 2
    public static List<String> sortUpperCaseDesc(List<String> strings) {
        return strings.stream()
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    // Завдання 3
    public static String extractAndSortNumbers(String[] array) {
        return Arrays.stream(array)
                .flatMap(s -> Arrays.stream(s.split(",\\s*")))
                .map(Integer::parseInt)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    // Завдання 4
    public static Stream<Long> randomStream(long seed, long a, long c, long m) {
        return Stream.iterate(seed, x -> (a * x + c) % m);
    }

    // Завдання 5
    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Iterator<T> firstIterator = first.iterator();
        Iterator<T> secondIterator = second.iterator();
        Stream.Builder<T> builder = Stream.builder();

        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            builder.add(firstIterator.next());
            builder.add(secondIterator.next());
        }

        return builder.build();
    }
}

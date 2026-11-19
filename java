import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {

    public static void main(String[] args) {
        // Тест Завдання 1
        List<String> names = List.of("Ivan", "Maria", "Peter", "Olena", "John", "Anna");
        System.out.println("Task 1: " + getOddIndexedNames(names));

        // Тест Завдання 2
        System.out.println("Task 2: " + sortUpperCaseDesc(names));

        // Тест Завдання 3 (перевіряємо також пробіли перед комою)
        String[] numbersArray = {"1, 2, 0", "4 , 5"};
        System.out.println("Task 3: " + extractAndSortNumbers(numbersArray));

        // Тест Завдання 4 (перевіряємо на 10 значеннях — жодних від'ємних чисел)
        long a = 25214903917L;
        long c = 11L;
        long m = 1L << 48;
        System.out.print("Task 4: ");
        randomStream(1L, a, c, m)
                .limit(10)
                .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Тест Завдання 5 (перевіряємо на нескінченних стрімах!)
        Stream<Long> infiniteFirst = randomStream(1L, a, c, m);
        Stream<Long> infiniteSecond = randomStream(2L, a, c, m);
        System.out.println("Task 5: " + zip(infiniteFirst, infiniteSecond)
                .limit(10)
                .collect(Collectors.toList()));
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
                .flatMap(s -> Arrays.stream(s.split("\\s*,\\s*")))
                .map(Integer::parseInt)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    // Завдання 4 (без переповнення long завдяки BigInteger)
    public static Stream<Long> randomStream(long seed, long a, long c, long m) {
        BigInteger bigA = BigInteger.valueOf(a);
        BigInteger bigC = BigInteger.valueOf(c);
        BigInteger bigM = BigInteger.valueOf(m);

        return Stream.iterate(seed, x -> BigInteger.valueOf(x)
                .multiply(bigA)
                .add(bigC)
                .mod(bigM)
                .longValue());
    }

    // Завдання 5 (лінивий zip через власний Iterator та Spliterator)
    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Iterator<T> firstIterator = first.iterator();
        Iterator<T> secondIterator = second.iterator();

        Iterator<T> zipIterator = new Iterator<>() {
            private boolean pickFirst = true;

            @Override
            public boolean hasNext() {
                return firstIterator.hasNext() && secondIterator.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T nextElement = pickFirst ? firstIterator.next() : secondIterator.next();
                pickFirst = !pickFirst;
                return nextElement;
            }
        };

        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(
                zipIterator,
                Spliterator.ORDERED | Spliterator.NONNULL
        );

        return StreamSupport.stream(spliterator, false);
    }
}

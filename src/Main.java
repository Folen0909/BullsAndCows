import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {



    public static void main(String[] args) throws IOException {
        //Scanner scanner = new Scanner(System.in);
        FileWriter testNumbers = new FileWriter("testNumbers.txt");
        int bulls, cows, numOfTries, totalGuesses = 0, sizeOfTestPool;
        //List<String> startingNumbers = getRandomValues(generateList(), 100);
        //List<String> startingNumbers = getSameValues(10, "6789");
        List<String> startingNumbers = new ArrayList<>(List.of(new String[]{"0123", "3210", "1023", "2013", "3102"}));
        testNumbers.write("Tested numbers: \n");
        for(String num : startingNumbers) {
            testNumbers.write(num + "\n");
        }
        sizeOfTestPool = startingNumbers.size();
        Set<Character> guess;
        System.out.println("When guessed correctly just enter any letter (without numbers)\n");

        for (String potGuess : startingNumbers) {
            List<String> numbers = generateList();
            guess = createSet("3478");
            numOfTries = 0;
            while(true) {
                numOfTries++;

                if (numOfTries == 2) {
                    guess = createSet(numbers.get(numbers.size() / 2));
                }
                Set<Character> finalGuess = guess;
                System.out.println(convertToString(finalGuess));
                if (numOfTries % 2 == 1) {
                    System.out.println(numOfTries + ". try. Enter bulls and cows: ");
                    bulls = numberOfBulls(potGuess, finalGuess);
                    cows = numberOfCows(potGuess, finalGuess);
                    if (bulls == 4) {
                        totalGuesses += numOfTries;
                        break;
                    }
                    /*
                    if (!scanner.hasNextInt()) {
                        break;
                    }
                    bulls = scanner.nextInt();
                    cows = scanner.nextInt();
                     */

                    System.out.println(bulls + " " + cows);

                    if (bulls == 0 && cows == 0) {
                        numbers.remove(convertToString(guess));
                        numbers.removeIf(num -> (numberOfContainedDigits(num, finalGuess) != 0));
                    }
                    if ((bulls == 0 && cows == 1) || (bulls == 1 && cows == 0)) {
                        numbers.remove(convertToString(guess));
                        numbers.removeIf(num -> (numberOfContainedDigits(num, finalGuess) != 1));
                        removeNumberOfBulls(numbers, bulls, finalGuess);
                    }
                    if ((bulls == 0 && cows == 2) || (bulls == 1 && cows == 1) || (bulls == 2 && cows == 0)) {
                        numbers.remove(convertToString(guess));
                        numbers.removeIf(num -> (numberOfContainedDigits(num, finalGuess) != 2));
                        removeNumberOfBulls(numbers, bulls, finalGuess);
                    }
                    if ((bulls == 0 && cows == 3) || (bulls == 1 && cows == 2) || (bulls == 2 && cows == 1) || (bulls == 3 && cows == 0)) {
                        numbers.remove(convertToString(guess));
                        numbers.removeIf(num -> (numberOfContainedDigits(num, finalGuess) != 3));
                        removeNumberOfBulls(numbers, bulls, finalGuess);
                    }
                    if ((bulls == 0 && cows == 4) || (bulls == 1 && cows == 3) || (bulls == 2 && cows == 2)) {
                        numbers.remove(convertToString(guess));
                        numbers.removeIf(num -> (numberOfContainedDigits(num, finalGuess) != 4));
                        removeNumberOfBulls(numbers, bulls, finalGuess);
                    }
                    if (numbers.size() == 1) {
                        guess = createSet(numbers.get(0));
                    }
                    else {
                        guess = createSet(numbers.get(numbers.size() / 2));
                    }
                } else {
                    System.out.println(numOfTries + ". try. Enter (-1) if guessed is lower, or (1) if it is higher: ");
                    bulls = numberOfBulls(potGuess, finalGuess);
                    if (bulls == 4) {
                        totalGuesses += numOfTries;
                        break;
                    }
                    /*
                    if (!scanner.hasNextInt()) {
                        break;
                    }

                    int clue = scanner.nextInt();
                    */
                    int clue = (Integer.parseInt(potGuess) <= Integer.parseInt(convertToString(finalGuess))) ? 1 : -1;
                    System.out.println(clue);
                    if (clue == 1) {
                        numbers.removeIf(num -> (Integer.parseInt(num) >= Integer.parseInt(convertToString(finalGuess))));
                    }
                    if (clue == -1) {
                        numbers.removeIf(num -> (Integer.parseInt(num) <= Integer.parseInt(convertToString(finalGuess))));
                    }
                    if (numbers.size() == 1) {
                        guess = createSet(numbers.get(0));
                    }
                    else {
                        guess = createSet(numbers.get((numbers.size() - 1) / 2));
                    }

                }
            }
            System.out.println("Guessed correct in " + numOfTries + " tries.\n");
        }


        System.out.println("Total number of guesses: " + totalGuesses);
        System.out.println("Test pool size: " + sizeOfTestPool);
        System.out.println("Average number of guesses: " + (double)totalGuesses/sizeOfTestPool);
        testNumbers.write("Total number of guesses: " + totalGuesses + "\n");
        testNumbers.write("Test pool size: " + sizeOfTestPool + "\n");
        testNumbers.write("Average number of guesses: " + (double)totalGuesses/sizeOfTestPool);
        testNumbers.close();
    }

    private static List<String> getRandomValues(List<String> startingNumbers, int size) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            temp.add(startingNumbers.get(new Random().nextInt(startingNumbers.size() - 1)));
        }
        return temp;
    }

    private static List<String> getSameValues(int size, String value) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            temp.add(value);
        }
        return temp;
    }

    private static boolean hasUniqueDigits(String number) {
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < number.length(); i++) {
            char digit = number.charAt(i);
            if (set.contains(digit)) {
                return false;
            }
            set.add(digit);
        }
        return true;
    }

    private static List<String> generateList() {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String number = String.format("%04d", i);
            if (hasUniqueDigits(number)) {
                temp.add(number);
            }
        }
        return temp;
    }

    private static Set<Character> createSet(String input) {
        Set<Character> set = new LinkedHashSet<>();
        for (char c : input.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    private static int numberOfContainedDigits(String number, Set<Character> digits) {
        int count = 0;
        for (Character digit : digits) {
            if (number.contains(String.valueOf(digit))) {
                count++;
            }
        }
        return count;
    }

    private static int numberOfCows(String number, Set<Character> digits) {
        String guess = convertToString(digits);
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (number.contains(String.valueOf(guess.charAt(i))) && !(number.charAt(i) == guess.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    private static int numberOfBulls(String number, Set<Character> digits) {
        String guess = convertToString(digits);
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (number.charAt(i) == guess.charAt(i)) {
                count++;
            }
        }
        return count;
    }
    private static String convertToString(Set<Character> set) {
        StringBuilder number = new StringBuilder();
        for (Character c : set) {
            number.append(c);
        }
        return number.toString();
    }

    private static void removeNumberOfBulls(List<String> numbers, int bulls, Set<Character> finalGuess) {
        if (bulls == 0) {
            numbers.removeIf(num -> (numberOfBulls(num, finalGuess) != 0));
        }
        if (bulls == 1) {
            numbers.removeIf(num -> (numberOfBulls(num, finalGuess) != 1));
        }
        if (bulls == 2) {
            numbers.removeIf(num -> (numberOfBulls(num, finalGuess) != 2));
        }
        if (bulls == 3) {
            numbers.removeIf(num -> (numberOfBulls(num, finalGuess) != 3));
        }
    }


}
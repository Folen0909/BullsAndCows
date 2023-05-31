import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Expand {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int lengthOfWord, bulls, cows, numberOfTries, totalGuesses = 0, sizeOfTestPool;
        System.out.println("Enter length of guessing word (limit 10): ");
        lengthOfWord = scanner.nextInt();
        long start = System.currentTimeMillis();
        List<String> testingNumbers = generateList(lengthOfWord);
        FileWriter testFile = new FileWriter("wordLength" + lengthOfWord + ".txt");
        testFile.write("Testing numbers: \n");

        sizeOfTestPool = testingNumbers.size();
        Set<Character> guess;
        System.out.println("Wait for program to complete (length word > 4 may take some time)\n");

        for (String potGuess : testingNumbers) {
            List<String> numbers = new ArrayList<>(testingNumbers);
            guess = createCharacterSetFromString(startingValue(lengthOfWord));
            numberOfTries = 0;
            while(true) {
                numberOfTries++;
                String finalGuess = convertCharacterSetToString(guess);
                if (numberOfTries % 2 == 1) {
                    bulls = numberOfBulls(potGuess, finalGuess);
                    cows = numberOfCows(potGuess, finalGuess);
                    if (bulls == lengthOfWord) {
                        totalGuesses += numberOfTries;
                        if (lengthOfWord < 5) {
                            testFile.write(potGuess + " => tries " + numberOfTries + "\n");
                        }
                        break;
                    }
                    removeNonPotentials(numbers, bulls, cows, finalGuess, lengthOfWord);
                    guess = (numbers.size() == 1) ? createCharacterSetFromString(numbers.get(0)) : createCharacterSetFromString(numbers.get(numbers.size() / 2));
                } else {
                    bulls = numberOfBulls(potGuess, finalGuess);
                    if (bulls == lengthOfWord) {
                        totalGuesses += numberOfTries;
                        if (lengthOfWord < 5) {
                            testFile.write(potGuess + " => tries " + numberOfTries + "\n");
                        }
                        break;
                    }
                    int clue = (Integer.parseInt(potGuess) <= Integer.parseInt(finalGuess)) ? 1 : -1;
                    if (clue == 1) {
                        numbers.removeIf(num -> (Integer.parseInt(num) >= Integer.parseInt(finalGuess)));
                    } else {
                        numbers.removeIf(num -> (Integer.parseInt(num) <= Integer.parseInt(finalGuess)));
                    }
                    guess = (numbers.size() == 1) ? createCharacterSetFromString(numbers.get(0)) : createCharacterSetFromString(numbers.get(numbers.size() / 2));
                }
            }
        }

        long end = System.currentTimeMillis();
        double time = end - start;
        String timeValue = "ms";

        if (time > 10000) {
            time = time / 1000;
            timeValue = "s";
        }

        System.out.println("Total number of guesses: " + totalGuesses);
        System.out.println("Test pool size: " + sizeOfTestPool);
        System.out.println("Average number of guesses: " + (double)totalGuesses/sizeOfTestPool);
        System.out.println("Time of execution: " + time + timeValue);
        testFile.write("Total number of guesses: " + totalGuesses + "\n");
        testFile.write("Test pool size: " + sizeOfTestPool + "\n");
        testFile.write("Average number of guesses: " + (double)totalGuesses/sizeOfTestPool + "\n");
        testFile.write("Time of execution: " + time + timeValue);
        testFile.close();
    }

    private static void removeNonPotentials(List<String> numbers, int bulls, int cows, String finalGuess, int lengthOfWord) {
        numbers.remove(finalGuess);
        numbers.removeIf(num -> (numberOfBulls(num, finalGuess) != bulls));
        int sumOfBullsAnsCows = bulls + cows;
        for (int numBulls = 0; numBulls <= lengthOfWord; numBulls++) {
            if (numBulls >= sumOfBullsAnsCows) {
                for (int numCows = 0; numCows <= lengthOfWord - numBulls; numCows++) {
                    int total = numBulls + numCows;
                    if (total == sumOfBullsAnsCows) {
                        numbers.removeIf(num -> (numberOfContainedDigits(num, finalGuess) != total));
                        break;
                    }
                }
            }
        }
    }

    private static String startingValue(int lengthOfWord) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < lengthOfWord; i++) {
            temp.append(i);
        }
        return temp.toString();
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

    private static List<String> generateList(int length) {
        List<String> temp = new ArrayList<>();
        int max = (int) Math.pow(10, length);
        for (int i = 0; i < max; i++) {
            String number = String.format("%0" + length + "d", i);
            if (hasUniqueDigits(number)) {
                temp.add(number);
            }
        }
        return temp;
    }

    private static Set<Character> createCharacterSetFromString(String input) {
        Set<Character> set = new LinkedHashSet<>();
        for (char c : input.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    private static int numberOfContainedDigits(String number, String guess) {
        int count = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (number.contains(String.valueOf(guess.charAt(i)))) {
                count++;
            }
        }
        return count;
    }

    private static int numberOfCows(String number, String guess) {
        int count = 0;
        for (int i = 0; i < number.length(); i++) {
            if (number.contains(String.valueOf(guess.charAt(i))) && !(number.charAt(i) == guess.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    private static int numberOfBulls(String number, String guess) {
        int count = 0;
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == guess.charAt(i)) {
                count++;
            }
        }
        return count;
    }
    private static String convertCharacterSetToString(Set<Character> set) {
        StringBuilder number = new StringBuilder();
        for (Character c : set) {
            number.append(c);
        }
        return number.toString();
    }
}
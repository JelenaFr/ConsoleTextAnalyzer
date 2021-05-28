import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConsoleTextAnalyzer {
    static int stopWordsPosition;
    static int capitalLettersArgPosition;
    static int charactersCounterArgPosition;


    public static void main(String[] args) throws IOException {
        for (String str : args) {
            stopWordsPosition = findIndex(args, "-S") + 1;
            capitalLettersArgPosition = findIndex(args, "-L");
            charactersCounterArgPosition = findIndex(args, "-C");


        }
        List<String> stopWords = Arrays.asList(args[findIndex(args, "-S") + 1].split(","));
        String[] filePathPosition = args[findIndex(args, "-F") + 1].split(",");


        for (int i = 0; i < filePathPosition.length; i++) {
            BufferedReader br = new BufferedReader(new FileReader(filePathPosition[i]));
            String readFileContext;
            String context;
            while ((readFileContext = br.readLine()) != null) {
                context = readFileContext.replaceAll("\\p{Punct}", "");
                System.out.println(context);
                List<String> arrFromTextFile = Arrays.asList(context.split(" "));
                getAnalysisResult(args, arrFromTextFile, stopWords, findCapitalizeWords(context, args));
            }
            br.close();
        }
    }

    public static int findIndex(String[] arr, String element) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(element))
                index = i;
        }
        return index;
    }


    public static List<String> findCapitalizeWords(String str, String[] args) {
        List<String> capitalizeWords = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-L")) {
                Pattern pattern = Pattern.compile("[A-Z][a-z]+");
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    capitalizeWords.add(matcher.group());
                }
            }
        }
        return capitalizeWords;
    }

    public static int countCharacters(List<String> arr) {
        String result = arr.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        return result.length();
    }

    public static void getAnalysisResult(String[] args, List<String> arr, List<String> stopWords, List<String> capitalizeWords) {
        if (args.length < 3) {
            System.out.println("No Flags => " + arr.size() + " Words");
        } else {
            List<String> arrFilter = arr.stream()
                    .filter(k -> !stopWords.contains(k.toLowerCase(Locale.ROOT)))
                    .filter(k -> !capitalizeWords.contains(k))
                    .collect(Collectors.toList());
            System.out.println(arrFilter.size() + " words");

            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-C")) {
                    System.out.println(countCharacters(arrFilter) + " symbols");
                }
            }
        }
    }
}

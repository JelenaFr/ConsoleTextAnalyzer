import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConsoleTextAnalyzer {public static void main(String[] args) throws IOException {
    ConsoleTextAnalyzer consoleTextAnalyzer = new ConsoleTextAnalyzer();
    HashMap<String, String> params = consoleTextAnalyzer.convertToKeyValuePair(args);



    if (params.containsKey("-F")) {
        String[] allArgsFiles  = params.get("-F").split(",");
        String[] allStopWords  = params.get("-S").split(",");


        for (int i = 0; i < allArgsFiles.length; i++) {
            BufferedReader br = new BufferedReader(new FileReader(allArgsFiles[i]));
            String readFileContext;
            String context;
            while ((readFileContext = br.readLine()) != null) {
                context = readFileContext.replaceAll("\\p{Punct}", " ").trim();
                System.out.println(context);
                List<String> arrFromTextFile = Arrays.asList(context.split(" "));

                consoleTextAnalyzer.getAnalysisResult(params, arrFromTextFile, allStopWords, consoleTextAnalyzer.capitalizeWordFilter(params, context));
            }
            br.close();
        }

    }
    else {System.out.println("Not correct file arguments");}


}

    private HashMap<String, String> convertToKeyValuePair(String[] args) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String[] splitFromEqual = arg.split("=");
                String key = splitFromEqual[0].substring(0);
                String value = splitFromEqual[1];
                params.put(key, value);
            } else
                params.put(arg, "");
        }
        return params;
    }

    private List<String> capitalizeWordFilter(HashMap<String, String> params, String str) {
        List<String> capitalizeWords = new ArrayList<>();

        if (params.containsKey("-L")) {
            Pattern pattern = Pattern.compile("[A-Z][a-z]+");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                capitalizeWords.add(matcher.group());
            }
        }

        return capitalizeWords;
    }




    public void getAnalysisResult(HashMap<String, String> params, List<String> arr,String[] allStopWords, List<String> capitalizeWords) {


        if (params.size() == 1) {
            System.out.println("No Flags => " + arr.size() + " Words");
        }
        if  (params.size() >= 2) {
            ConsoleTextAnalyzer consoleTextAnalyzer = new ConsoleTextAnalyzer();

            List<String> stopWords = Arrays.asList(allStopWords);
            List<String> filteredWords = arr.stream()

                    .filter(k -> !stopWords.contains(k.toLowerCase(Locale.ROOT)))
                    .filter(k -> capitalizeWords.contains(k))
                    .collect(Collectors.toList());

            filteredWords.forEach(n-> System.out.print(n+" "));
            System.out.println();
            System.out.println(filteredWords.size() + " words");

            if (checkFlagContain(params, "-C")) {
                System.out.println( consoleTextAnalyzer.countCharacters(filteredWords)+" symbols");

            }
        }







    }

    private boolean checkFlagContain(HashMap<String, String> params, String flag) {

        return params.containsKey(flag);
    }


    private int countCharacters(List<String> arr) {
        String result = arr.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        return result.length();
    }

}

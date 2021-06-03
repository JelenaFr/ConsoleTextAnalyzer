import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleTextAnalyzer {
    public static void main(String[] args) throws IOException {
        ConsoleTextAnalyzer consoleTextAnalyzer = new ConsoleTextAnalyzer();
        Map<String, String> params = consoleTextAnalyzer.convertToKeyValuePair(args);
        if (params.containsKey("-F")) {
            String[] allArgsFiles = params.get("-F").split(",");

            for (int i = 0; i < allArgsFiles.length; i++) {
                BufferedReader br = new BufferedReader(new FileReader(allArgsFiles[i]));
                String readFileContext;
                String line;
                while ((readFileContext = br.readLine()) != null) {
                    line = readFileContext.replaceAll("\\p{Punct}", " ").trim();
                    System.out.println(line);
                    List<String> arrFromTextFile = Arrays.asList(line.split(" "));

                    consoleTextAnalyzer.getAnalysisResult(params, arrFromTextFile, consoleTextAnalyzer.getStopwords(params, line));
                }
                br.close();
            }

        } else {
            System.out.println("Not correct file arguments");
        }
    }
    private Map<String, String> convertToKeyValuePair(String[] args) {
        Map<String, String> params = new HashMap<>();
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
    private void getAnalysisResult(Map<String, String> params, List<String> arr, List<String> allStopWords) {
        List<String> filteredWords = new ArrayList<>();
        if (params.size() == 1) {
            System.out.print("No Flags => ");
        }

        if (params.size() >= 2) {
            if (params.containsKey("-L")) {
                for (int i = 0; i < arr.size(); i++) {
                    if (Character.isUpperCase(arr.get(i).charAt(0))) {
                        filteredWords.add(arr.get(i));
                    }
                }
                arr = filteredWords;
            }

            if (params.containsKey("-S")) {
                filteredWords = arr.stream()
                        .map(String::toLowerCase)
                        .filter(k -> !allStopWords.contains(k))
                        .collect(Collectors.toList());
                arr = filteredWords;
            }

        }


        System.out.println(arr.size() + " words");
        //System.out.println(arr);
        if (params.containsKey("-C")) {
            System.out.println(countCharacters(arr) + " symbols");
        }

    }

    private int countCharacters(List<String> arr) {
        String result = arr.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        return result.length();
    }

    private List<String> getStopwords(Map<String, String> params, String line) {
        List<String> allStopWords = new ArrayList<>();
        if (params.containsKey("-S")) {
            allStopWords = Arrays.asList(params.get("-S").split(","));
        }
        return allStopWords;
    }
}

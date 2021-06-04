import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleTextAnalyzer {
    public static void main(String[] args) {
        ConsoleTextAnalyzer consoleTextAnalyzer = new ConsoleTextAnalyzer();
        Map<String, String> params = consoleTextAnalyzer.convertToKeyValuePair(args);
        List<String> stopWords = consoleTextAnalyzer.findStopwords(params);
        String[] argsF = params.get("-F").split(",");
        if (params.containsKey("-F")) {
            for (int i = 0; i < argsF.length; i++) {
                String fileText = consoleTextAnalyzer.readfile(argsF[i]);
                List<String> text = Arrays.asList(fileText.split(" "));
                try {
                    consoleTextAnalyzer.getAnalysisResult(params, text, stopWords);
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Empty file");
                }

            }

        } else {
            System.out.println("Correct file arguments not found");
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
            } else {
                params.put(arg, "");
            }
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
        System.out.println(arr);
        System.out.println(arr.size() + " words");

        if (params.containsKey("-C")) {
            System.out.println(countCharacters(arr) + " symbols");
        }
    }

    private int countCharacters(List<String> arr) {
        String str = String.join("", arr);
        return str.length();
    }

    private List<String> findStopwords(Map<String, String> params) {
        List<String> allStopWords = new ArrayList<>();
        if (params.containsKey("-S")) {
            allStopWords = Arrays.asList(params.get("-S").split(","));
        }
        return allStopWords;
    }


    private String readfile(String fileName) {
        String line = null;
        try (Reader reader = new InputStreamReader(new FileInputStream(fileName))) {
            int a = reader.read();
            StringBuilder result = new StringBuilder();
            while (a > 0) {
                result.append((char) a);
                a = reader.read();


            }
            String str = result.toString();
            line = str.replaceAll("[^\\p{L}]+", " ");
            System.out.println(line);
        } catch (FileNotFoundException e) {

            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("Wrong arguments");
        }

        return line;
    }


}

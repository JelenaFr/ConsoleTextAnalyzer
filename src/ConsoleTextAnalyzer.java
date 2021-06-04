import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleTextAnalyzer {
    String argF= "-F";
    String argC= "-C";
    String argL= "-L";
    String argS= "-S";
    public static void main(String[] args) {

        ConsoleTextAnalyzer consoleTextAnalyzer = new ConsoleTextAnalyzer();
        Map<String, String> params = consoleTextAnalyzer.convertToKeyValuePair(args);
        List<String> stopWords = consoleTextAnalyzer.findStopwords(params);
        consoleTextAnalyzer.startAnalyze(params, stopWords);

    }

    private Map<String, String> convertToKeyValuePair(String[] args) {
        Map<String, String> params = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String[] splitFromEqual = arg.split("=");
                String key = splitFromEqual[0];
                String value = splitFromEqual[1];
                params.put(key, value);
            } else {
                params.put(arg, "");
            }
        }
        return params;
    }

    private void getAnalysisResult(Map<String, String> params, List<String> text, List<String> stopWords) {
        List<String> filteredWords = new ArrayList<>();
        if (params.size() == 1) {
            System.out.print("No Flags => ");
        }

        if (params.size() >= 2) {
            if (params.containsKey(argL)) {
                for (String s : text) {
                    if (Character.isUpperCase(s.charAt(0))) {
                        filteredWords.add(s);
                    }
                }
                text = filteredWords;
            }
            if (params.containsKey(argS)) {
                filteredWords = text.stream()
                        .map(String::toLowerCase)
                        .filter(k -> !stopWords.contains(k))
                        .collect(Collectors.toList());
                text = filteredWords;
            }
        }
        //System.out.println(arr);
        System.out.println(text.size() + " words");

        if (params.containsKey(argC)) {
            System.out.println(countCharacters(text) + " symbols");
        }
    }

    private int countCharacters(List<String> arr) {
        String str = String.join("", arr);
        return str.length();
    }

    private List<String> findStopwords(Map<String, String> params) {
        List<String> allStopWords = new ArrayList<>();
        if (params.containsKey(argS)) {
            allStopWords = Arrays.asList(params.get("-S").split(","));
        }
        return allStopWords;
    }

    private String readFile(String file) {
        String line = null;
        try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
            int a = reader.read();
            StringBuilder result = new StringBuilder();
            while (a > 0) {
                result.append((char) a);
                a = reader.read();
            }
            String str = result.toString();
            line = str.replaceAll("[^\\p{L}]+", " ");
            //System.out.println(line);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            System.out.println("Wrong arguments");
        }
        return line;
    }
    public void startAnalyze (Map<String, String> params, List<String> stopWords ) {
        if (params.containsKey(argF)) {
            String[] argsF = params.get(argF).split(",");
            for (String file : argsF) {
                String fileText = readFile(file);
                List<String> text = Arrays.asList(fileText.split(" "));
                try {
                    getAnalysisResult(params, text, stopWords);
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Empty file");
                }
            }
        } else {
            System.out.println("Correct file arguments not found");
        }
    }
}

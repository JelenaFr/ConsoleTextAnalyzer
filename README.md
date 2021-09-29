# ConsoleTextAnalyzer

This is text analyzer tool which produces a set of statistics data about the file contents.
The tool can calculate and print for each file:
1.	number of words in the file, restricted by:
a.	stopwords - if passed to the application, those words will not be counted. For example we need to exclude English articles (a, the) and preposition words (at, on etc.) or any other words
b.	capital letter - if flag -L is passed to the application only words starting with capital letter should be counted
2.	number of characters in the words, which are being counted
Input parameters:
-F=<filepath>,<filepath> - collection of files to be analyzed (example: -F=C:\test\text1.txt,C:\test\text2.txt)
-S=<word>,<word> - collection of stopwords to be ignored (example: -S=a,at,the,on)
-L if present, only words starting from capital letters should be counted
-C if present, should also count number of characters in the words
 
 
Sample application call:
java -jar scraper.jar -F=C:\test\text1.txt,C:\test\text2.txt -S=at,on,the -C
java -jar scraper.jar -F=C:\test\text1.txt -S=on,in,a -C -L
 
Input: Quick brown Fox jumped over a fat lazy Dog
No Flags => 9 Words
-C => 9 Words, 34 symbols
-S=brown,over,dog -C => Quick brown Fox jumped over a fat lazy Dog => 6 Words, 22 symbols
-S=brown,over,dog -C -L => Quick brown Fox jumped over a fat lazy Dog => 2 Words, 8 symbols


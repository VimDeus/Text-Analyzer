import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class TextAnalyzer {
    public static void main(String[] args) {

    	
    	try {

    		URL url = new URL("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            

            String line;
            String poem = " ";
            
            boolean poemStarted = false;
            
            while ((line = in.readLine()) != null) {

                if (line.contains("<h1>The Raven</h1>")) {
                    poemStarted = true;
                }
                if (poemStarted) {
                    poem += line;
                }

                if (line.contains("END OF THE")) {
                    break;
                }

            }
            in.close();
           

            poem = poem.replaceAll("<[^>]*>", "");

            
            String[] words = poem.split(" ");

            
            Map<String, Integer> wordFrequencies = new HashMap<>();

            
            for (String word : words) {
                word = word.toLowerCase().replaceAll("[^a-zA-Z ]", ""); 
                if (wordFrequencies.containsKey(word)) {
                    wordFrequencies.put(word, wordFrequencies.get(word) + 1);
                } else {
                    wordFrequencies.put(word, 1);
                }
            }

            
            List<Entry<String, Integer>> list = new ArrayList<>(wordFrequencies.entrySet());
            
            Collections.sort(list, new Comparator<Entry<String, Integer>>() {
                @Override
                public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
            

            int count = 0;
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the number of words you want to see: ");
            int numWords = sc.nextInt();
            for (Entry<String, Integer> entry : list) {
            	if (count >= numWords) {
            	break;
            	
            }
            System.out.println(entry.getKey() + ": " + entry.getValue());
            count++;
            }
            
            sc.close();
            	
            } catch (Exception e) {
            	System.out.println("An error occurred: " + e.getMessage());
            	
            	
            }
           }
          }


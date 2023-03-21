import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TextAnalyzer {
	private Integer inputText;
	private JFrame frame;
	private JTextField numWordsField;
	private JTextArea resultArea;



	public TextAnalyzer() {
		frame = new JFrame("Text Analyzer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setLocationRelativeTo(null);
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());

		numWordsField = new JTextField();
		numWordsField.setToolTipText("Input Word Amount Here");
		numWordsField.setPreferredSize(new Dimension(150, 30));
		numWordsField.setForeground(Color.GRAY);
		inputPanel.add(numWordsField, BorderLayout.CENTER);

		numWordsField.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (numWordsField.getText().equals("Input Word Amount Here")) {
		            numWordsField.setText("");
		            numWordsField.setForeground(Color.BLACK);
		        }
		    }

		    @Override
		    public void focusLost(FocusEvent e) {
		        if (numWordsField.getText().isEmpty()) {
		            numWordsField.setText("Input Word Amount Here");
		            numWordsField.setForeground(Color.GRAY);
		        }
		    }
		});

		JButton analyzeButton = new JButton("Analyze");
		analyzeButton.setBackground(new Color(51, 153, 255));
		analyzeButton.setForeground(Color.WHITE);
		analyzeButton.setPreferredSize(new Dimension(100, 30));

		analyzeButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String fieldVal = numWordsField.getText();
		        inputText = Integer.parseInt(fieldVal);
		        String output = analyzeText(inputText);
		        resultArea.setText(output);
		    }
		});

		inputPanel.add(analyzeButton, BorderLayout.EAST);

		frame.add(inputPanel, BorderLayout.NORTH);

		resultArea = new JTextArea();
		resultArea.setEditable(false);
		resultArea.setBackground(Color.DARK_GRAY);
		resultArea.setForeground(Color.WHITE);
		resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
		resultArea.setText("This is where the results of the word counts will be placed after entering the number of words to analyze for frequency.");
		resultArea.setWrapStyleWord(true);
		resultArea.setLineWrap(true);
		resultArea.setFont(new Font("Arial", Font.ITALIC, 14));

		JScrollPane scrollPane = new JScrollPane(resultArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.add(scrollPane, BorderLayout.CENTER);

		frame.setVisible(true);
	}
	
	
	public static String analyzeText(int numWords) {
	        StringBuilder sb = new StringBuilder();
	        
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
	            
	            for (Entry<String, Integer> entry : list) {
	                if (count >= numWords) {
	                    break;
	                }
	                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
	                count++;
	            }
	            
	            
	            
	        } catch (Exception e) {
	            sb.append("An error occurred: ").append(e.getMessage());
	        }
	        return sb.toString();
	        
	    }
	
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TextAnalyzer();
            }
        });
	}
	
	
}


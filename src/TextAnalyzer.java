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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
* The TextAnalyzer class provides a simple graphical user interface (GUI) for analyzing
* the frequency of words in the poem. The poem is retrieved from a URL and the user can input
* the number of words to analyze for word occurrence frequency.
* 
* @version 0.8.1
*/
public class TextAnalyzer {
	
	private Integer inputText;
	private JFrame frame;
	private JTextField numWordsField;
	private JTextArea resultArea;

	
	/**
    * Constructs a new TextAnalyzer object and creates the GUI.
    */
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
		
			/**
			* Sets the text field to an empty string and changes the text color to black
			* when the field gains focus.
			*/
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (numWordsField.getText().equals("Input Word Amount Here")) {
		            numWordsField.setText("");
		            numWordsField.setForeground(Color.BLACK);
		        }
		    }

		    
		    /**
		     * Sets the text field to the default value and changes the text color to gray
		     * when the field loses focus and has no text entered.
		     */
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
		
			/**
			* Gets the input text from the text field, analyzes the text, and updates the
			* result area with the word frequencies.
			*/ 
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
	
	
	
	
	/**
    Analyzes the frequency of words in a poem retrieved from a URL.
    @param numWords the number of most frequent words to analyze
    @return a String containing the most frequent words and their frequency
    */
	public static String analyzeText(int numWords) {
	    StringBuilder sb = new StringBuilder();

	    try {
	        
	         // Initialize database connection
	         // Username and Password have been changed for privacy. When accessing local SQL Server, input user specific values
	         
	    	String dburl = "jdbc:mysql://localhost:3306/word_occurrences";
	    	String user = "username";
	    	String password = "password";
	    	Connection conn = DriverManager.getConnection(dburl, user, password);


	       
	        // Get poem text
	         
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

	        
	         //  Count word frequencies
	         
	        Map<String, Integer> wordFrequencies = new HashMap<>();
	        for (String word : words) {
	            word = word.toLowerCase().replaceAll("[^a-zA-Z ]", "");
	            if (wordFrequencies.containsKey(word)) {
	                wordFrequencies.put(word, wordFrequencies.get(word) + 1);
	            } else {
	                wordFrequencies.put(word, 1);
	            }
	        }

	        
	         //  Store word frequencies in database (with duplicate check)
	         
	        String insertStatement = "INSERT INTO word (word, frequency) SELECT ?, ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM word WHERE word = ?)";
	        PreparedStatement pstmt = conn.prepareStatement(insertStatement);
	        for (Entry<String, Integer> entry : wordFrequencies.entrySet()) {
	            pstmt.setString(1, entry.getKey());
	            pstmt.setInt(2, entry.getValue());
	            pstmt.setString(3, entry.getKey());
	            pstmt.executeUpdate();
	        }
	        pstmt.close();


	        
	         // Retrieve top numWords word frequencies from database
	        
	        String selectStatement = "SELECT * FROM word ORDER BY frequency DESC LIMIT ?";
	        pstmt = conn.prepareStatement(selectStatement);
	        pstmt.setInt(1, numWords);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            sb.append(rs.getString("word")).append(": ").append(rs.getInt("frequency")).append("\n");
	        }
	        rs.close();
	        pstmt.close();
	        conn.close();

	    } catch (Exception e) {
	        sb.append("An error occurred: ").append(e.getMessage());
	    }
	    return sb.toString();
	}

	
	
		/**
	    Starts the application by creating a new instance of TextAnalyzer in a Swing thread.
	    @param args the command-line arguments.
	    */
	public static void main(String[] args) {
    		SwingUtilities.invokeLater(new Runnable() {
        	/**
        	* Creates a new instance of TextAnalyzer.
        	*/
        	@Override
            public void run() {
                new TextAnalyzer();
            }
        });
	}
	
	
}


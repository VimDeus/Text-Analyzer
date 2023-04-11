import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
* the number of words to analyze for word occurrence frequency. This is the client side of the application.
* 
* @version 1.0.1
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
     * Gets the input text from the text field, analyzes the text, and updates the
     * result area with the word frequencies.
     *
     * @param numWords the number of most frequent words to analyze
     * @return a String containing the most frequent words and their frequency
     */
	public String analyzeText(int numWords) {
	    StringBuilder sb = new StringBuilder();

	    //Server Connection 
	    
	    try {
	        String hostName = "localhost";
	        int port = 12345;

	        try (Socket clientSocket = new Socket(hostName, port);
	             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) {

	            out.println(numWords);
	            String response;
	            
	            while ((response = in.readLine()) != null) {
	                sb.append(response).append("\n");
	            }
	        }
	    } catch (IOException e) {
	        sb.append("An error occurred: ").append(e.getMessage());
	    }

	    return sb.toString();
	}

	
	 /**
     * Starts the application by creating a new instance of TextAnalyzer in a Swing thread.
     *
     * @param args the command-line arguments
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


import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * The TextAnalysisServer class provides server functionality for a client/server application
 * that analyzes the frequency of words in a poem retrieved from a URL.
 * The server receives requests from the client to compute the word frequency.
 * 
 * @version 1.0.1
 */
public class TextAnalysisServer {
	
	
	/**
     * The main method starts the server and listens for client connections.
     * When a client connects, it reads the number of words to analyze from the client,
     * computes the word frequencies, and sends the result back to the client.
     *
     * @param args the command-line arguments
     * @throws IOException if an I/O error occurs while creating the server socket
     */
    public static void main(String[] args) throws IOException {
        
    	int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) {

                    System.out.println("Client connected");

                    String inputLine = in.readLine();
                    int numWords = Integer.parseInt(inputLine);
                    String output = analyzeText(numWords);
                    out.println(output);
                }
            }
        }
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
    }

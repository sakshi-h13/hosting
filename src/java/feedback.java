import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/feedback")
public class feedback extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/authdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "sakshi@123";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String rating = request.getParameter("rating");
        String feedback = request.getParameter("feedback");

        // Validate and handle null values
        if (name == null || name.trim().isEmpty()) {
            name = "Anonymous";
        }
        if (email == null || email.trim().isEmpty()) {
            email = "No Email Provided";
        }
        if (rating == null || rating.trim().isEmpty()) {
    rating = "0";  // Default value instead of "No Rating" if column expects an integer
}

        if (feedback == null || feedback.trim().isEmpty()){
            feedback = "No Feedback";
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Insert feedback data into the database
            String sql = "INSERT INTO feedback (name, email, rating, feedback) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, rating);
            preparedStatement.setString(4, feedback);
            
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                out.println("<h1>Feedback Submitted Successfully!</h1>");
            } else {
                out.println("<h1>Error submitting feedback. Please try again.</h1>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(feedback.class.getName()).log(Level.SEVERE, null, e);
            out.println("<h1>Error processing your request:</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                out.println("<h1>Error closing the connection:</h1>");
                out.println("<p>" + e.getMessage() + "</p>");
            }
        }
    }
}

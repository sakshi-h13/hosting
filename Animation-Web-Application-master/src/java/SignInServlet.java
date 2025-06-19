import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignInServlet")
public class SignInServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/authdb";
    private static final String DB_USER = "root";  // Change as needed
    private static final String DB_PASSWORD = "sakshi@123";  // Change as needed

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Retrieve form data
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob");

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to insert data
            String sql = "INSERT INTO users (email, username, password, dob) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, dob);

            // Execute query
            int rowsInserted = pstmt.executeUpdate();

            // Redirect based on the result
            if (rowsInserted > 0) {
                response.sendRedirect("loginpage.html");  // Redirect to login page
            } else {
                response.sendRedirect("index.html");  // Redirect back to sign-in page with error message
            }

            // Close resources
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            response.sendRedirect("index.html"); // Redirect to sign-in page with error message
        }
    }
}

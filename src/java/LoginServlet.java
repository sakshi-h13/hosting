import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("loginpage.html"); // Redirect GET requests to login page
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            out.println("<script>alert('Username and password are required!'); window.location='loginpage.html';</script>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/authdb", "root", "sakshi@123");

            // Check if username exists
            String checkUserSql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(checkUserSql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Username exists, now check password
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    // Correct password, redirect to dashboard
                    response.sendRedirect("dashboard.html");
                } else {
                    // Incorrect password
                    out.println("<script>alert('Incorrect password!'); window.location='loginpage.html';</script>");
                }
            } else {
                // Username not found
                out.println("<script>alert('User unavailable. Please sign up.'); window.location='loginpage.html';</script>");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('Internal Server Error'); window.location='error.html';</script>");
        }
    }
}

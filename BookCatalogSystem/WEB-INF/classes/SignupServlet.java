import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class SignupServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    
        String username = request.getParameter("username"); 
        String password = request.getParameter("password"); 
        String role = request.getParameter("role"); 
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_catalog", "root", "root")) {
                String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);

                ps.executeUpdate();
                
                response.getWriter().println("Signup successful");
				response.sendRedirect("login.html");
            }
        } catch (ClassNotFoundException e) {
            response.getWriter().println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e){
            response.getWriter().println("SQL Error: " + e.getMessage());
        }
    }
}
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LoginServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        
        String username =request.getParameter("username");
        String password =request.getParameter("password");

        if(username== null||username.isEmpty()||password==null||password.isEmpty()){
            response.getWriter().println("Username or Password cannot be empty. <a href='login.html'>Try again</a>");
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_catalog", "root", "root")) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()){
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("role", rs.getString("role"));

                    String role = rs.getString("role");
                    if ("admin".equals(role)){
                        response.sendRedirect("admindashboard.html");
                    } else{
                        response.sendRedirect("userdashboard.html");
                    }
                } else{
                    response.getWriter().println("Invalid credentials. <a href='login.html'>Try again</a>");
                }
            }
        } catch (ClassNotFoundException e){
            response.getWriter().println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e){
            response.getWriter().println("SQL Error: " + e.getMessage());
        }
    }
}
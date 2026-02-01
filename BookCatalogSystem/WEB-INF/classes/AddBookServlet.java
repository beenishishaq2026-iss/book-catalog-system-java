import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
public class AddBookServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String title= request.getParameter("title");
        String author= request.getParameter("author");
        double price= Double.parseDouble(request.getParameter("price"));
        int quantity= Integer.parseInt(request.getParameter("quantity"));

        String url= "jdbc:mysql://localhost:3306/book_catalog";
        String dbUser= "root";
        String dbPassword= "root";

        Connection con= null;
        PreparedStatement ps= null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(url, dbUser, dbPassword);

            String query = "INSERT INTO books(title, author, price, quantity) VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            int rowsAffected=ps.executeUpdate();

            if (rowsAffected > 0) {
                out.println("<h3>Book added successfully!</h3>");
            } else {
                out.println("<h3>Failed to add the book.</h3>");
            }
        } catch (ClassNotFoundException e) {
            out.println("<h3>Driver not found: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<h3>Database error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) 
					ps.close();
                if (con != null) 
					con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

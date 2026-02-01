import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.io.*;

public class DisplayUserCatalog extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        displayBooks(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        displayBooks(request, response);
    }

    private void displayBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String url = "jdbc:mysql://localhost:3306/book_catalog"; 
        String user = "root";  
        String password = "root";  

        String query = "SELECT id, title, author, price, quantity FROM books";  // SQL query to fetch books
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(url, user, password);

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            out.println("<html><body>");
            out.println("<h1>Available Books</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th><th>Quantity</th><th>Actions</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");  
                out.println("<td>" + title + "</td>");
                out.println("<td>" + author + "</td>");
                out.println("<td>" + price + "</td>");
                out.println("<td>" + quantity + "</td>");
                out.println("<td>");
                
                out.println("<form action='AddToCartServlet' method='POST' style='display:inline;'>");
                out.println("<input type='hidden' name='id' value='" + id + "' />");
                out.println("<input type='number' name='quantity' value='1' min='1' max='" + quantity + "' required />");
                out.println("<input type='submit' value='Add to Cart' />");
                out.println("</form>");
            
                out.println("<form action='RemoveFromCartServlet' method='POST' style='display:inline; margin-left: 10px;'>");
                out.println("<input type='hidden' name='id' value='" + id + "' />");
                out.println("<input type='submit' value='Remove from Cart' />");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<br><a href='index.html'>Back to Home</a>");
            out.println("</body></html>");

        } catch (SQLException e) {
            out.println("<html><body><h3>Error fetching books: " + e.getMessage() + "</h3></body></html>");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            out.println("<html><body><h3>JDBC Driver not found: " + e.getMessage() + "</h3></body></html>");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
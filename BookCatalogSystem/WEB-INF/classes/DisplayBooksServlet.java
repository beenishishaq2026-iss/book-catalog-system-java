import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.io.*;

public class DisplayBooksServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String url = "jdbc:mysql://localhost:3306/book_catalog";
        String User = "root"; 
        String Password = "root";  

        String query = "SELECT id, title, author, price, quantity FROM books";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(url,User,Password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            out.println("<html><body>");
            out.println("<h1>Available Books</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>Title</th><th>Author</th><th>Price</th><th>Quantity</th></tr>");

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                out.println("<tr>");
                out.println("<td>" + title + "</td>");
                out.println("<td>" + author + "</td>");
                out.println("<td>" + price + "</td>");
                out.println("<td>" + quantity + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<br><a href='index.html'>Back to Home</a>");
			out.println("<br><a href='logout.html'>logout</a><br>");
            out.println("</body></html>");

        } catch (SQLException e) {
            out.println("<html><body><h3>Error fetching books: " + e.getMessage() + "</h3></body></html>");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            out.println("<html><body><h3>JDBC Driver not found: " + e.getMessage() + "</h3></body></html>");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) 
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null) 
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

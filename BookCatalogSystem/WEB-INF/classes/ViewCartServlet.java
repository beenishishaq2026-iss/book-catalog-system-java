import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class ViewCartServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null){
            response.sendRedirect("login.html");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/book_catalog";
        String dbUser = "root"; 
        String dbPassword = "root";  
        String query = "SELECT b.title, b.author, b.price, c.quantity FROM cart c " +
                       "JOIN books b ON c.book_id = b.id WHERE c.user_id = ?";
          try{
			  Class.forName("com.mysql.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            out.println("<html><body>");
            out.println("<h1>Your Cart</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>Book Title</th><th>Author</th><th>Price</th><th>Quantity</th></tr>");

            double totalPrice = 0.0;
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

                totalPrice += price * quantity;
            }

            out.println("</table>");
            out.println("<h3>Total Price: $" + totalPrice + "</h3>");
            out.println("<br><a href='index.html'>Back to Home</a>");
            out.println("</body></html>");
			 }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error fetching cart items: " + e.getMessage() + "</p>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Unexpected error: " + e.getMessage() + "</p>");
        }
    }
}

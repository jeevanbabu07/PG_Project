package main;

import java.sql.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Admin_login")
public class Admin_login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       response.setContentType("text/html");
       PrintWriter pw=response.getWriter();
       String username=request.getParameter("username");
       String password=request.getParameter("password");
       
       try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project","root","");
                        PreparedStatement pst=(PreparedStatement) con.prepareStatement("SELECT * FROM admin_database WHERE BINARY username=? AND BINARY password=?");
                        pst.setString(1, username);
			pst.setString(2, password);
                        ResultSet rs=pst.executeQuery();
                        if(rs.next()){
                            response.sendRedirect("http://localhost:8080/Exam_seating_arrangement/index.html");
                        }
                        else{
                            pw.println("<script type=\"text/javascript\">");
                            pw.println("alert('Invalid Username or Password!');");
                            pw.println("location='http://localhost:8080/Exam_seating_arrangement/admin_login.html';");
                                                        pw.println("</script>");

                        }
		}
		catch(ClassNotFoundException | SQLException e) {
			pw.println(e);
		}
    }
}
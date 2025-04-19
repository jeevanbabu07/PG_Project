package main;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

@WebServlet("/delete_stu")
public class delete_stu extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        PrintWriter pw = response.getWriter();

        String className = request.getParameter("delete_cls");
        String regno = request.getParameter("delete_reg");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "");

            
            if ((regno != null && !regno.isEmpty()) && (className != null && !className.isEmpty())) {
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Enter className or RegNo');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                pw.println("</script>");
            }
            else if ((regno == null || regno.isEmpty()) && (className == null || className.isEmpty())) {
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Enter className or RegNo');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                pw.println("</script>");
            }
            else if ((regno == null || regno.isEmpty()) && (className != null && !className.isEmpty())) {

                    PreparedStatement pst2 = con.prepareStatement("DELETE FROM class WHERE class_name = ?");
                    pst2.setString(1, className);
                    int rowsAffectedClass = pst2.executeUpdate();

                    if (rowsAffectedClass > 0) {
                        pw.println("<script type=\"text/javascript\">");
                        pw.println("alert('" + className + " Deleted successfully');");
                        pw.println("window.location.href='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                        pw.println("</script>");

                    } else {
                        pw.println("<script type=\"text/javascript\">");
                        pw.println("alert('No matching records found');");
                        pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                        pw.println("</script>");
                    }
                }
            
            else if ((className == null || className.isEmpty()) && (regno != null && !regno.isEmpty())) {
                PreparedStatement pst4 = con.prepareStatement("DELETE FROM student WHERE reg_no = ?");
                pst4.setString(1, regno);
                int rowsAffectedStudent4 = pst4.executeUpdate();

                if (rowsAffectedStudent4 > 0) {
                    pw.println("<script type=\"text/javascript\">");
                    pw.println("alert('Record(s) deleted successfully');");
                    pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                    pw.println("</script>");
                } else {
                    pw.println("<script type=\"text/javascript\">");
                    pw.println("alert('No matching records found');");
                    pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                    pw.println("</script>");
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            pw.println(e);
        }
    }
}

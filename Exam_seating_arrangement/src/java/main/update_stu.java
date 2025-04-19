package main;

import java.sql.ResultSet;
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

@WebServlet("/update_stu")
public class update_stu extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        PrintWriter pw = response.getWriter();

        String className = request.getParameter("update_class");
        String regno = request.getParameter("u_regno");
        String name = request.getParameter("u_name");
        String dob = request.getParameter("u_dob");
        String class_check = null;
        String class_check1 = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "");
            PreparedStatement pst1 = con.prepareStatement("SELECT class_name FROM class WHERE class_name = ?");
            pst1.setString(1, className);
            ResultSet rs = pst1.executeQuery();
            if (rs.next()) {
                class_check = rs.getString("class_name");
            }
            if (class_check != null && !class_check.isEmpty()) {
                PreparedStatement pst3 = con.prepareStatement("SELECT reg_no FROM student WHERE reg_no = ?");
                pst3.setString(1, regno);
                ResultSet rs1 = pst3.executeQuery();
                if (rs1.next()) {
                    class_check1 = rs1.getString("reg_no");
                }
               
                if (class_check1 == null) {
                   if (regno.length() <= 10) {
                    PreparedStatement pst2 = con.prepareStatement("INSERT INTO student(department, reg_no, name, dob) VALUES (?, ?, ?, ?)");
                    pst2.setString(1, className);
                    pst2.setString(2, regno);
                    pst2.setString(3, name);
                    pst2.setString(4, dob);
                    pst2.executeUpdate();
                    pw.println("<script type=\"text/javascript\">");
                    pw.println("alert('Record Inserted successfully');");
                    pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                    pw.println("</script>");
                   }
                   else{
                        pw.println("<script type=\"text/javascript\">");
        pw.println("alert('Regno length must be 10 characters or fewer');");
        pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
        pw.println("</script>");
                   }
                } else {
                    PreparedStatement pst4 = con.prepareStatement("UPDATE student SET department=?, name=?, dob=? WHERE reg_no=?");
                    pst4.setString(1, className);
                    pst4.setString(2, name);
                    pst4.setString(3, dob);
                    pst4.setString(4, regno);
                    pst4.executeUpdate();
                    pw.println("<script type=\"text/javascript\">");
                    pw.println("alert('Record Updated Successfully');");
                    pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                    pw.println("</script>");
                }
            } else {
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Invalid class name');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/stu_up_del.html';");
                pw.println("</script>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            pw.println(e);
        }
    }
}

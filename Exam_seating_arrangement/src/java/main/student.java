package main;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/student")
public class student extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String regNo = request.getParameter("reg_no");
        String dob = request.getParameter("dob");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println(" <link\n" +
"      rel=\"stylesheet\"\n" +
"      href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css\"\n" +
"      integrity=\"sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==\"\n" +
"      crossorigin=\"anonymous\"\n" +
"      referrerpolicy=\"no-referrer\"\n" +
"    />");
        out.println("<title>Login Form</title>");
        out.println("<link rel='stylesheet' href='student.css' />");
        out.println("</head>");
        out.println("<body>");
        out.println("<div id='al'>");       
        out.println("<h3><i class=\"fa-solid fa-graduation-cap\"></i>STUDENT</h3>");
        out.println("<button id=\"logout\">LOGOUT</button>");
        out.println("</div>");
        out.println("<form action=\"student\" method=\"get\">");
        out.println("<h2>Seat Locator</h2>");
        out.println("<hr>");
        out.println("<label for=\"reg_no\"><b>Registration Number:</b></label>");
        out.println("<input type=\"text\" name=\"reg_no\" placeholder=\"Enter Register Number\" required><br><br>");
        out.println("<label for=\"dob\">Date of Birth:</label>");
        out.println("<input type=\"date\" name=\"dob\" required><br><br>");
        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");
        out.println("<script>");
        out.println("document.getElementById(\"logout\").addEventListener(\"click\", function() {");
            out.println("window.location.href = \"start.html\";");
        out.println("});");
        out.println("</script>");
        out.println("<div id='results'>");

        if (regNo != null && dob != null) {
            try {
                // Establish database connection
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/main_project", "root", "");

                LocalDate currentDate = LocalDate.now();
                String sql = "SELECT * FROM arrangement WHERE reg_no = ? AND dob = ? AND date >= ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, regNo);
                ps.setString(2, dob);
                ps.setDate(3, Date.valueOf(currentDate));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<table border='1'>");
                    out.println("<tr><th>Department</th><th>Reg No</th><th>Name</th><th>Subject</th><th>Course Code</th><th>Hall Name</th><th>Date</th><th>Session</th><th>Row</th><th>Col</th></tr>");
                    do {
                        out.println("<tr>");
                        out.println("<td>" + rs.getString("department") + "</td>");
                        out.println("<td>" + rs.getString("reg_no") + "</td>");
                        out.println("<td>" + rs.getString("name") + "</td>");
                        out.println("<td>" + rs.getString("subject") + "</td>");
                        out.println("<td>" + rs.getString("course_code") + "</td>");
                        out.println("<td>" + rs.getString("hall_name") + "</td>");
                        out.println("<td>" + rs.getDate("date") + "</td>");
                        out.println("<td>" + rs.getString("session") + "</td>");
                        out.println("<td>" + rs.getInt("row") + "</td>");
                        out.println("<td>" + rs.getInt("col") + "</td>");
                        out.println("</tr>");
                    } while (rs.next());
                    out.println("</table>");
                } else {
                    out.println("<h3 id='msg'>No exams scheduled for today!</h3>");
                }
                con.close();
            } catch (Exception e) {
                out.println("<h3>Error: " + e.getMessage() + "</h3>");
            }
        }

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
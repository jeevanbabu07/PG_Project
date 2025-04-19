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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/subject_up")
public class subject_up extends HttpServlet {

    private Connection con;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    try (Statement stmt = con.createStatement()) {

        ResultSet rs1 = stmt.executeQuery("SELECT class_name,subject_1,subject_2,subject_3,subject_4,subject_5,subject_6,subject_7 FROM class");

        out.println("<html>");
        out.println("<head><title>Class</title>");
        out.println("<link rel='stylesheet' type='text/css' href='Subject_Up.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<table height=20% width=70%>");

        int count = 0;
        while (rs1.next()) {
            String className = rs1.getString("class_name");
            String subject1 = rs1.getString("subject_1");
            String subject2 = rs1.getString("subject_2");
            String subject3 = rs1.getString("subject_3");
            String subject4 = rs1.getString("subject_4");
            String subject5 = rs1.getString("subject_5");
            String subject6 = rs1.getString("subject_6");
            String subject7 = rs1.getString("subject_7");

            if (count % 3 == 0) {
                out.println("<tr>");
            }

            out.println("<td>");
            out.println("<table id='t1' border='1'>");
            out.println("<tr id='t2'><th colspan='2' id='t3'><center>" + className + "</center></th></tr>");
            out.println("<tr id='t2'><th id='t3'>No.</th><th id='t3'>Subject</th></tr>");

            if (subject1 != null && !"null".equals(subject1)) {
                out.println("<tr><td id='t4'><center>1</center></td><td id='t5'><center>" + subject1 + "</center></td></tr>");
            }
            if (subject2 != null && !"null".equals(subject2)) {
                out.println("<tr><td id='t4'><center>2</center></td><td id='t5'><center>" + subject2 + "</center></td></tr>");
            }
            if (subject3 != null && !"null".equals(subject3)) {
                out.println("<tr><td id='t4'><center>3</center></td><td id='t5'><center>" + subject3 + "</center></td></tr>");
            }
            if (subject4 != null && !"null".equals(subject4)) {
                out.println("<tr><td id='t4'><center>4</center></td><td id='t5'><center>" + subject4 + "</center></td></tr>");
            }
            if (subject5 != null && !"null".equals(subject5)) {
                out.println("<tr><td id='t4'><center>5</center></td><td id='t5'><center>" + subject5 + "</center></td></tr>");
            }
            if (subject6 != null && !"null".equals(subject6)) {
                out.println("<tr><td id='t4'><center>6</center></td><td id='t5'><center>" + subject6 + "</center></td></tr>");
            }
            if (subject7 != null && !"null".equals(subject7)) {
                out.println("<tr><td id='t4'><center>7</center></td><td id='t5'><center>" + subject7 + "</center></td></tr>");
            }

            out.println("</table>");
            out.println("</td>");

            if (count % 3 == 2) {
                out.println("</tr>");
            }

            count++;
        }

        if (count % 3 != 0) {
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("</body>");
        out.println("</html>");

    } catch (SQLException e) {
        throw new ServletException("Failed to fetch data from the database", e);
    } finally {
        out.close();
    }
}

    @Override
    public void destroy() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = response.getWriter();

        
        String className = request.getParameter("class");
        int numSubjects = Integer.parseInt(request.getParameter("subject"));

        
        String[] subjects = new String[7];
        for (int i = 0; i < numSubjects; i++) {
            subjects[i] = request.getParameter("subject_" + (i + 1)); 
        }

        
        for (int i = numSubjects; i < 7; i++) {
            subjects[i] = null;
        }

        
        String class_check = null;

        try {
            
            PreparedStatement pst2 = con.prepareStatement("SELECT class_name FROM class WHERE class_name = ?");
            pst2.setString(1, className);
            ResultSet rs = pst2.executeQuery();

            if (rs.next()) {
                class_check = rs.getString("class_name");
            }

           
            if (class_check != null && !class_check.isEmpty()) {
               
                PreparedStatement pst1 = con.prepareStatement(
                        "UPDATE class SET subject_1 = ?, subject_2 = ?, subject_3 = ?, subject_4 = ?, subject_5 = ?, subject_6 = ?, subject_7 = ? WHERE class_name = ?");

                
                for (int i = 0; i < 7; i++) {
                    if (subjects[i] != null && !subjects[i].isEmpty()) {
                        pst1.setString(i + 1, subjects[i]);
                    } else {
                        pst1.setNull(i + 1, java.sql.Types.VARCHAR); 
                    }
                }
                pst1.setString(8, className);

               
                int i = pst1.executeUpdate();

                
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Subjects Updated Successfully');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/subj_upd_frame.html';");
                pw.println("</script>");
            } else {
                
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Class name not found');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/subj_upd_frame.html';");
                pw.println("</script>");
            }
        } catch (SQLException e) {
            
            e.printStackTrace();
            pw.println("<script type=\"text/javascript\">");
            pw.println("alert('Error occurred: " + e.getMessage() + "');");
            pw.println("location='http://localhost:8080/Exam_seating_arrangement/subj_upd_frame.html';");
            pw.println("</script>");
        } finally {
            pw.close();
        }
    }
}

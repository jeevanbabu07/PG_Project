package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/student_insert")
public class student_insert extends HttpServlet {

    // Database connection details
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/main_project";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Declare connection object at the class level
    private Connection con;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Initialize database connection when the servlet is loaded
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Statement stmt = con.createStatement()) {
            ResultSet rs1 = stmt.executeQuery("SELECT class_name FROM class");

            out.println("<html>");
            out.println("<head><title>Class</title>");
            out.println("<link rel='stylesheet' type='text/css' href='Insert_stude.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<table border='1'><tr><th>No.</th><th>Class</th></tr>");
            int count = 0;

            while (rs1.next()) {
                count++;
                String className = rs1.getString("class_name");

                out.println("<tr><td>" + count + "</td><td>" + className + "</td></tr>");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        String formType = request.getParameter("formType");

        if ("classForm".equals(formType)) {
            handleClassForm(request, response, pw);
        } else if ("studentForm".equals(formType)) {
            handleStudentForm(request, response, pw);
        }
    }

    private void handleClassForm(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) {
        String className = request.getParameter("class");
        int numSubjects = Integer.parseInt(request.getParameter("subject"));
        String[] subjects = new String[7];
        for (int i = 0; i < numSubjects; i++) {
            subjects[i] = request.getParameter("subject_" + (i + 1));
        }
        for (int i = numSubjects; i < 7; i++) {
            subjects[i] = null;
        }

        try {
            // Check if class name exists
            PreparedStatement pst2 = con.prepareStatement("SELECT class_name FROM class WHERE class_name = ?");
            pst2.setString(1, className);
            ResultSet rs = pst2.executeQuery();

            if (rs.next()) {
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Class name already exists');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/insert_stude_class.html';");
                pw.println("</script>");
            } else {
               
                // Insert the class into the database
                PreparedStatement pst1 = con.prepareStatement(
                        "INSERT INTO class(class_name, subject_1, subject_2, subject_3, subject_4, subject_5, subject_6, subject_7) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                pst1.setString(1, className);
                for (int i = 0; i < 7; i++) {
                    if (subjects[i] != null) {
                        pst1.setString(i + 2, subjects[i]);
                    } else {
                        pst1.setNull(i + 2, java.sql.Types.VARCHAR);
                    }
                }
                pst1.executeUpdate();
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Class name Created');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/insert_stude_class.html';");
                pw.println("</script>");

                // Store className in session
                HttpSession cls = request.getSession();
                cls.setAttribute("className", className);
            }
        } catch (SQLException e) {
            pw.println("<script type=\"text/javascript\">");
            pw.println("alert('Error: " + e.getMessage() + "');");
            pw.println("location='http://localhost:8080/Exam_seating_arrangement/insert_stude_class.html';");
            pw.println("</script>");
        }
    }

    private void handleStudentForm(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) {
        HttpSession cls = request.getSession();
        String className = (String) cls.getAttribute("className");
        String regno = request.getParameter("regno");
        String name = request.getParameter("name");
        String dob = request.getParameter("dob");

        try {
            // Check if student already exists
            PreparedStatement pst2 = con.prepareStatement("SELECT reg_no FROM student WHERE reg_no = ?");
            pst2.setString(1, regno);
            ResultSet rs = pst2.executeQuery();

            if (rs.next()) {
                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Register Number already exists');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/insert_stu_student.html';");
                pw.println("</script>");
            } else {
                // Insert student into database
                 if (className != null && !className.isEmpty()) {
                PreparedStatement pst = con.prepareStatement("INSERT INTO student(department, reg_no, name, dob) VALUES (?, ?, ?, ?)");
                pst.setString(1, className);
                pst.setString(2, regno);
                pst.setString(3, name);
                pst.setString(4, dob);
                pst.executeUpdate();

                pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Student record inserted');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/insert_stu_student.html';");
                pw.println("</script>");
                 }
                 else{
                     pw.println("<script type=\"text/javascript\">");
                pw.println("alert('Start by defining the class first.');");
                pw.println("location='http://localhost:8080/Exam_seating_arrangement/insert_stu_student.html';");
                pw.println("</script>");

                 }
            }
        } catch (SQLException e) {
            pw.println("<script type=\"text/javascript\">");
            pw.println("alert('Define a new class.');");
            pw.println("location='http://localhost:8080/Exam_seating_arrangement/insert_stu_student.html';");
            pw.println("</script>");
        }
    }

    @Override
    public void destroy() {
        try {
            // Close the database connection when the servlet is destroyed
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.destroy();
    }
}

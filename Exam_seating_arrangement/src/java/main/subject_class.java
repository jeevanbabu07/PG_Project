package main;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/subject_class")
public class subject_class extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String[] selectedClasses = (String[]) session.getAttribute("selectedClasses");
        int generatedNumber = (Integer) request.getAttribute("generatedNumber");

        try {
            PreparedStatement checkClassExistence = con.prepareStatement("SELECT COUNT(department) FROM student WHERE department = ?");
            PreparedStatement insertSubject = con.prepareStatement("INSERT INTO subject (id, class) VALUES (?, ?)");

            for (String selectedClass : selectedClasses) {
              
                checkClassExistence.setString(1, selectedClass);
                ResultSet rs = checkClassExistence.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    
                    insertSubject.setInt(1, generatedNumber);
                    insertSubject.setString(2, selectedClass);
                    insertSubject.addBatch();
                } else {
                    
                    out.println("<p>Class " + selectedClass + " does not exist in the student table, skipping.</p>");
                }
            }

            
            insertSubject.executeBatch();

            response.sendRedirect("http://localhost:8080/Exam_seating_arrangement/display_subject?id=" + generatedNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Failed to store the data in the database.</p>");
        }

        out.close();
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
}
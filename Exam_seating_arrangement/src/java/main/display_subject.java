package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/display_subject")
public class display_subject extends HttpServlet {
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
    try (PrintWriter out = response.getWriter()) {
        int id = Integer.parseInt(request.getParameter("id"));
        String className = null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT class FROM subject WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DisplaySubject</title>");
out.println("<link " +
    "rel=\"stylesheet\" " +
    "href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css\" " +
    "integrity=\"sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==\" " +
    "crossorigin=\"anonymous\" " +
    "referrerpolicy=\"no-referrer\" " +
"/>"
);

            out.println("<link rel='stylesheet' href='Display_Subject.css' />");
            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 id='heading'><b>Exam Seating Arrangements</b></h1>");
            out.println("<div id='navigation'>");
            out.println("<ul>");
            out.println("<div id='al'>");
            out.println("<h3><i class='fa-solid fa-user-tie'></i>ADMIN</h3>");
            out.println("<form method='post' action='display_subject?action=logout'>");
             out.println("<input type='hidden' name='id' value='" + id + "'>");
            out.println("<button id='logout'>LOGOUT</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<br />");
            out.println("<li id=\"student_list\">");
            out.println("<a data-social=\"student\">");
            out.println("<i class=\"fa-solid fa-graduation-cap\"></i><i>Student List</i></a>");
            out.println("</li>");
            out.println("<li id=\"subject_list\">");
            out.println("<a id='a'>");
            out.println("<i class=\"fa-solid fa-book-open\"></i><i>Subject</i></a>");
            out.println("</li>");
            out.println("<li id=\"manage_list\">");
            out.println("<a data-social=\"Hall Allocation\" ><i class=\"fa-solid fa-person-shelter\"></i><i>Manage Rooms</i></a>");
            out.println("</li>");
            out.println("<li id=\"final\"><a data-social=\"output\"><i class=\"fa-solid fa-note-sticky\"></i><i>Arrangements</i></a>");
            out.println("</li>");
            out.println("</ul>");
            out.println("</div>");
            out.println("<form action='display_subject' method='post'>");
            out.println("<div id='class_up'>");

            int counter = 0; // Counter to track the number of displayed classes in a row
            while (rs.next()) {
                className = rs.getString("class");

                // Start a new row every 3 classes
                if (counter % 3 == 0) {
                    if (counter > 0) {
                        out.println("</div>"); // Close previous row
                    }
                    out.println("<div class='row'>"); // Start new row
                }

                // Display the class
                out.println("<div class='class-item'>");
                out.println("<h2>" + className + "</h2>");
                out.println("<br>");

                PreparedStatement ps1 = con.prepareStatement("SELECT subject_1, subject_2, subject_3, subject_4, subject_5, subject_6, subject_7 FROM class WHERE class_name = ?");
                ps1.setString(1, className);
                ResultSet rs1 = ps1.executeQuery();

                if (rs1.next()) {
                    out.println("<div id='cs'>");
                    out.println("<label for=\"coursecode_" + className + "\">Course Code :</label>");
                    out.println("<input type=\"text\" id=\"coursecode_" + className + "\" name=\"coursecode_" + className + "\" placeholder='Enter the course code' required><br><br>");
                    out.println("<label for='subject_" + className + "'>Subject:</label>");
                    out.println("<input list='subjectList_" + className + "' id='subject_" + className + "' name='subject_" + className + "' placeholder='Select or type a subject' required>");

                    out.println("<datalist id='subjectList_" + className + "'>");
                    for (int i = 1; i <= 7; i++) {
                        String subject = rs1.getString("subject_" + i);
                        if (subject != null) {
                            out.println("<option value='" + subject + "'>");
                        }
                    }
                    out.println("</div>");
                    out.println("</datalist>");
                    out.println("<br>");
                }

                out.println("</div>"); // End of class item
                counter++; // Increment the counter

                // If 3 classes have been added, start a new row
                if (counter % 3 == 0) {
                    out.println("</div>"); // Close the current row
                    out.println("<hr>");
                }
            }

            if (counter % 3 != 0) {
                out.println("</div>");
                out.println("<hr>");
            }
            out.println("<div id='ds'>");
            out.println("<br>");
            out.println("<label for=\"date\">Date:</label><input type=\"date\" id=\"date\" name=\"date\" required><br><br>");
            out.println("<label>Session:</label>");
            out.println("<label><input type=\"radio\" id=\"an\" name=\"session\" value=\"AN\" required> AN</label>");
            out.println("<label><input type=\"radio\" id=\"fn\" name=\"session\" value=\"FN\" required> FN</label>");
            out.println("<br>"); 
            out.println("<input type='hidden' name='id' value='" + id + "'>");
            out.println("<button type=\"submit\" id=\"update_submit\">Update</button>");
            out.println("</div>");
            out.println("</form>");

            out.println("<form method='post' action='display_subject?action=process'>");
            out.println("<input type='hidden' name='id' value='" + id + "'>");
            out.println("<button type=\"submit\" id=\"previous\"><b><<<b> Previous</button>");
            out.println("</form>");
            

            out.println("</body>");
            out.println("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Failed to retrieve the data from the database.</p>");
        }
    }
}
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");

    // Handle different actions based on the action parameter
    if ("process".equals(action)) {
        doProcessData(request, response);
    } else if ("logout".equals(action)) {
        doProcessData1(request, response);
    } else {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String date = request.getParameter("date");
            String session = request.getParameter("session");
            int id = Integer.parseInt(request.getParameter("id"));

            // Check if date and session are provided
            if (date == null || date.isEmpty() || session == null || session.isEmpty()) {
                out.println("<script>alert('Missing date or session information. Please fill all the fields.');</script>");
                return;
            }

            try {
                // Query to check if there is any entry in the subject table for the given id and class
                PreparedStatement checkClassStmt = con.prepareStatement(
                        "SELECT * FROM subject WHERE id = ? AND class IS NOT NULL AND class != ''");
                checkClassStmt.setInt(1, id);
                ResultSet checkClassResult = checkClassStmt.executeQuery();

                // If no class is found for the provided id, redirect to student.html
               if (!checkClassResult.next()) {
                    out.println("<script>");
                    out.println("alert('No student record classes found for the selected class.');");
                    out.println("window.location.href = 'http://localhost:8080/Exam_seating_arrangement/index.html';");
                    out.println("</script>");
                    return;
                }


                // Proceed with normal processing (update) if class exists
                PreparedStatement ps = con.prepareStatement("SELECT class FROM subject WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                Set<String> courseCodes = new HashSet<>();
                List<String> classes = new ArrayList<>();
                Map<String, String> courseCodeMap = new HashMap<>();
                Map<String, String> subjectMap = new HashMap<>();

                // Process each class associated with the id
                while (rs.next()) {
                    String className = rs.getString("class");
                    classes.add(className);

                    // Get the course code and subject from the form input
                    String code = request.getParameter("coursecode_" + className);
                    String subject = request.getParameter("subject_" + className);

                    if (code == null || subject == null || code.trim().isEmpty() || subject.trim().isEmpty()) {
                        out.println("<script>alert('Please enter course code and subject for all classes');window.history.back();</script>");
                        return;
                    }

                    // Trim and check for duplicates
                    code = code.trim();
                    subject = subject.trim();

                    if (!courseCodes.add(code)) {
                        out.println("<script>alert('Duplicate course code found: " + code + "');window.history.back();</script>");
                        return;
                    }

                    // Map the class name to the entered course code and subject
                    courseCodeMap.put(className, code);
                    subjectMap.put(className, subject);
                }

                // Start the transaction and update the database
                con.setAutoCommit(false);
                for (String cls : classes) {
                    PreparedStatement update = con.prepareStatement(
                            "UPDATE subject SET course_code = ?, subject = ?, date = ?, session = ? WHERE id = ? AND class = ?");
                    update.setString(1, courseCodeMap.get(cls));
                    update.setString(2, subjectMap.get(cls));
                    update.setString(3, date);
                    update.setString(4, session);
                    update.setInt(5, id);
                    update.setString(6, cls);
                    update.executeUpdate();
                }

                // Commit the transaction
                con.commit();
                response.sendRedirect("hall?id=" + id);

            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<script>alert('Error while updating data: " + e.getMessage() + "');</script>");
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


 protected void doProcessData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    String idParam = request.getParameter("id");

    
    if (idParam == null || idParam.trim().isEmpty()) {
        try (PrintWriter out = response.getWriter()) {
            out.println("<p>Error: The ID parameter is missing or invalid.");
            out.println(" Please make sure to provide a valid ID. Provided ID: " + (idParam != null ? idParam : "None") + "</p>");
        }
        return;
    }

    try {
       
        int id = Integer.parseInt(idParam); 

        try (PrintWriter out = response.getWriter()) {
           
            PreparedStatement psDelete = con.prepareStatement("DELETE FROM subject WHERE id = ?");
            psDelete.setInt(1, id);
            int rowsAffected = psDelete.executeUpdate();

            if (rowsAffected > 0) {
               response.sendRedirect("http://localhost:8080/Exam_seating_arrangement/index.html");
            } else {
                response.sendRedirect("http://localhost:8080/Exam_seating_arrangement/index.html");
            }
        }

    } catch (NumberFormatException e) {
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<p>Error: Invalid ID format. Please provide a valid numeric ID.");
            out.println(" Provided ID: " + idParam + "</p>");
        }
    } catch (SQLException e) {
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<p>Error deleting record: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}

  protected void doProcessData1 (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    String idParam = request.getParameter("id");

    
    if (idParam == null || idParam.trim().isEmpty()) {
        try (PrintWriter out = response.getWriter()) {
            out.println("<p>Error: The ID parameter is missing or invalid.");
            out.println(" Please make sure to provide a valid ID. Provided ID: " + (idParam != null ? idParam : "None") + "</p>");
        }
        return;
    }

    try {
       
        int id = Integer.parseInt(idParam); 

        try (PrintWriter out = response.getWriter()) {
           
            PreparedStatement psDelete = con.prepareStatement("DELETE FROM subject WHERE id = ?");
            psDelete.setInt(1, id);
            int rowsAffected = psDelete.executeUpdate();

            if (rowsAffected > 0) {
               response.sendRedirect("http://localhost:8080/Exam_seating_arrangement/start.html");
            } else {
                response.sendRedirect("http://localhost:8080/Exam_seating_arrangement/start.html");
            }
        }

    } catch (NumberFormatException e) {
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<p>Error: Invalid ID format. Please provide a valid numeric ID.");
            out.println(" Provided ID: " + idParam + "</p>");
        }
    } catch (SQLException e) {
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<p>Error deleting record: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}

    @Override
    public String getServletInfo() {
        return "Servlet for displaying and updating subjects based on class.";
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

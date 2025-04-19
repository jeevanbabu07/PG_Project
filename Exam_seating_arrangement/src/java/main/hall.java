package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "hall", urlPatterns = {"/hall"})
public class hall extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String id = request.getParameter("id");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DisplaySubject</title>");
            out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css\" />");
            out.println("<link rel='stylesheet' href='hall.css' />");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 id='heading'><b>Exam Seating Arrangements</b></h1>");
            out.println("<div id='navigation'>");
            out.println("<ul>");
            out.println("<div id='al'>");
            out.println("<h3><i class='fa-solid fa-user-tie'></i>ADMIN</h3>");
            out.println("<form method='post' action='hall?action=logout'>");
             out.println("<input type='hidden' name='id' value='" + id + "'>");
            out.println("<button id='logout'>LOGOUT</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<br />");
            out.println("<li id=\"student_list\"><a><i class=\"fa-solid fa-graduation-cap\"></i><i>Student List</i></a></li>");
            out.println("<li id=\"subject_list\"><a><i class=\"fa-solid fa-book-open\"></i><i>Subject</i></a></li>");
            out.println("<li id=\"manage_list\"><a id='a'><i class=\"fa-solid fa-person-shelter\"></i><i>Manage Rooms</i></a></li>");
            out.println("<li id=\"final\"><a data-social=\"output\"><i class=\"fa-solid fa-note-sticky\"></i><i>Arrangements</i></a></li>");
            out.println("</ul>");
            out.println("</div>");

            if (id == null || id.isEmpty()) {
                out.println("<p>Error: ID not provided. Please provide a valid ID in the URL.</p>");
                return;
            }

           
            try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "")) {
                String departmentCountQuery = "SELECT department, COUNT(reg_no) AS student_count "
                        + "FROM student "
                        + "WHERE department IN (SELECT class FROM subject WHERE id = ?) "
                        + "GROUP BY department";
                try (PreparedStatement ps = con.prepareStatement(departmentCountQuery)) {
                    ps.setInt(1, Integer.parseInt(id)); 
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        out.println("<table border='1' id='t1'>");
                        out.println("<tr><th id='t3'>Department</th><th id='t3'>Student Count</th></tr>");
                        while (rs.next()) {
                            String departmentName = rs.getString("department");
                            int studentCount = rs.getInt("student_count");
                            out.println("<tr>");
                            out.println("<td id='t4'>" + (departmentName != null ? departmentName : "N/A") + "</td>");
                            out.println("<td  id='t4'>" + studentCount + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<p>Error: Unable to fetch the student count by department. Please check the logs.</p>");
            }

            
            try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "")) {
                String totalStudentQuery = "SELECT COUNT(*) AS total_students "
                        + "FROM student "
                        + "WHERE department IN ( "
                        + "    SELECT class "
                        + "    FROM subject "
                        + "    WHERE id = ? "
                        + ")";
                try (PreparedStatement ps = con.prepareStatement(totalStudentQuery)) {
                    ps.setInt(1, Integer.parseInt(id));
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int totalStudents = rs.getInt("total_students");
                            out.println("<h2 id='he2'>Total Students: "  + totalStudents + "</h2>");
                            out.println("<hr>");
                            int totalSeats = 0;
                            String totalSeatsQuery = "SELECT SUM(row * col) AS total_seats FROM hall WHERE id = ?";
                            try (PreparedStatement seatPs = con.prepareStatement(totalSeatsQuery)) {
                                seatPs.setInt(1, Integer.parseInt(id));
                                try (java.sql.ResultSet seatRs = seatPs.executeQuery()) {
                                    if (seatRs.next()) {
                                        totalSeats = seatRs.getInt("total_seats");
                                    }
                                }
                            }
                            int balanceStudents = totalStudents - totalSeats;
                            out.println("<div id='bs'>");
                            out.println("<h2>Balance Students: "+ balanceStudents +"</h2>");
                            out.println("</div>");
                            
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<p>Error: Unable to fetch the total student count. Please check the logs.</p>");
            }

           
            try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "")) {
                String query = "SELECT room, row, col FROM hall WHERE id = ?";
                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setInt(1, Integer.parseInt(id));
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        out.println("<form method='post' action='hall'>"); // Form for deletion
                        out.println("<table border='1' id='rt'>");
                        out.println("<tr><th id='rt3'>Hall Name</th><th id='rt3'>Row</th><th id='rt3'>Column</th><th id='rt3'>Delete</th></tr>");
                        while (rs.next()) {
                            String hallName = rs.getString("room");
                            int row = rs.getInt("row");
                            int col = rs.getInt("col");
                            out.println("<tr>");
                            out.println("<td id='rt4'><center>" + hallName + "</center></td>");
                            out.println("<td id='rt4'><center>" + row + "</center></td>");
                            out.println("<td id='rt4'><center>" + col + "</center></td>");
                            out.println("<td id='rt4'><center><input type='checkbox' name='hallToDelete' value='" + hallName + "'></center></td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                        out.println("<input type='hidden' name='id' value='" + id + "' />");
                        out.println("<button type='submit' id='delete' name='action' value='classdelete'>DELETE</button>");
                        out.println("</form>");
                        
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<p>Error: Unable to fetch data from the database. Please check the logs.</p>");
            }
            out.println("<div id=\"hall\">");
            out.println("<form action=\"hall\" method=\"post\" id=\"hall_det\">");
            out.println("<label>Hall Name:</label><input type=\"text\" id=\"hn\" name=\"hall_name\" placeholder=\"Enter the Class Name\" required/><br><br>");
            out.println("<label>Row:</label><input type=\"number\" step=\"1\" name=\"row\" placeholder=\"Enter the Row\" required><br><br>");
            out.println("<label>Column:</label><input type=\"number\" step=\"1\" name=\"column\" placeholder=\"Enter the Column\" required><br><br>");
            out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\" />");
            out.println("<button type=\"submit\" id='insert' name=\"action\" value=\"insert\">INSERT</button>");
            out.println("</form>");
            out.println("</div>");
             out.println("<form method='post'  action='hall'>");
            out.println("<input type='hidden' name='id' value='" + id + "' />");
            out.println("<input type='hidden' name='action' value='delete' />");
            out.println("<button type=\"submit\" id=\"previous\"><b><<<</b> Previous</button>");
            out.println("</form>");
            out.println("<form method='post' action='hall'>");
            out.println("<input type='hidden' name='id' value='" + id + "' />"); 
            out.println("<input type='hidden' name='action' value='submit' />");
            out.println("<button type='submit' id='submit'>Submit</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            response.getWriter().println("<p>Error: ID not provided. Please provide a valid ID.</p>");
            return;
        }

        if ("insert".equalsIgnoreCase(action)) {
            handleInsert(request, response, id);
        } else if ("submit".equalsIgnoreCase(action)) {
            handleSubmit(request, response, id);
        } else if ("classdelete".equalsIgnoreCase(action)) {
            classDelete(request, response, id); 
        }else if ("delete".equalsIgnoreCase(action)) {
            handleDelete(request, response, id);
        }else if ("logout".equals(action)) {
        doProcessData1(request, response); }
    }

    private void classDelete(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
        String[] hallsToDelete = request.getParameterValues("hallToDelete");
         if (hallsToDelete == null || hallsToDelete.length == 0) {
        response.setContentType("text/html"); // Set the content type for HTML
        response.getWriter().println("<script type='text/javascript'>alert('Select the class'); window.history.back();</script>");
        return;
    }
        else{

        try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "")) {
            String deleteQuery = "DELETE FROM hall WHERE room = ? AND id = ?"; 
            try (PreparedStatement ps = con.prepareStatement(deleteQuery)) {
                for (String hallName : hallsToDelete) {
                    ps.setString(1, hallName);  
                    ps.setString(2, id); 
                    ps.executeUpdate();
                }
            }
            response.sendRedirect("hall?id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: Unable to delete the selected halls. Please check the logs.");
        }
        }
    }

    private void handleInsert(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
        String hallName = request.getParameter("hall_name");
        String row = request.getParameter("row");
        String column = request.getParameter("column");

        if (hallName == null || hallName.isEmpty() || row == null || row.isEmpty() || column == null || column.isEmpty()) {
            response.getWriter().println("<p>Error: Missing required fields. Please provide all data.</p>");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "")) {
            int totalStudents = 0;
            String studentCountQuery = "SELECT COUNT(*) AS total_students "
                    + "FROM student "
                    + "WHERE department IN ( "
                    + "    SELECT class "
                    + "    FROM subject "
                    + "    WHERE id = ? "
                    + ")";
            try (PreparedStatement ps = con.prepareStatement(studentCountQuery)) {
                ps.setInt(1, Integer.parseInt(id));
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalStudents = rs.getInt("total_students");
                    }
                }
            }
            int totalSeats = 0;
            String totalSeatsQuery = "SELECT SUM(row * col) AS total_seats FROM hall WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(totalSeatsQuery)) {
                ps.setInt(1, Integer.parseInt(id));
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalSeats = rs.getInt("total_seats");
                    }
                }
            }
            int currentSeats = Integer.parseInt(row) * Integer.parseInt(column);

            if ((totalSeats + currentSeats) > totalStudents) {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<script>alert('Error: Adding current seats (" + currentSeats + ") will exceed total students (" + totalStudents + ").');</script>");
                    processRequest(request, response);
                } catch (ServletException ex) {
                    Logger.getLogger(hall.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            String checkQuery = "SELECT COUNT(*) FROM hall WHERE id = ? AND room = ?";
            try (PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
                checkPs.setInt(1, Integer.parseInt(id));
                checkPs.setString(2, hallName);

                try (java.sql.ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        response.setContentType("text/html;charset=UTF-8");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("<script>alert('Error: Hall name already exists!');</script>");
                            processRequest(request, response);
                        } catch (ServletException ex) {
                            Logger.getLogger(hall.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                }
            }
            String insertQuery = "INSERT INTO hall (id, room, row, col) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                ps.setInt(1, Integer.parseInt(id));
                ps.setString(2, hallName);
                ps.setInt(3, Integer.parseInt(row));
                ps.setInt(4, Integer.parseInt(column));
                int rowsInserted = ps.executeUpdate();

                if (rowsInserted > 0) {
                    response.setContentType("text/html;charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        processRequest(request, response);
                    } catch (ServletException ex) {
                        Logger.getLogger(hall.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    response.getWriter().println("<p>Error: Data insertion failed.</p>");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: Database connection failed. Please check the logs.</p>");
        }
    }
private void handleDelete(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
    Connection con = null;
    PreparedStatement psDeleteHall = null;
    PreparedStatement psUpdateSubject = null;

    try {
        // Establish the connection
        con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "");
        
        // Start a transaction
        con.setAutoCommit(false);
        
        // Prepare the UPDATE query for the "subject" table
        String deleteQuery1 = "UPDATE subject SET subject = NULL, course_code = NULL, date = NULL, session = NULL WHERE id = ?";
        psUpdateSubject = con.prepareStatement(deleteQuery1);
        psUpdateSubject.setInt(1, Integer.parseInt(id));

        // Prepare the DELETE query for the "hall" table
        String deleteQuery = "DELETE FROM hall WHERE id = ?";
        psDeleteHall = con.prepareStatement(deleteQuery);
        psDeleteHall.setInt(1, Integer.parseInt(id));

        // First, attempt to update the "subject" table (this should always run)
        int rowsUpdatedSubject = psUpdateSubject.executeUpdate();

        // Then, attempt to delete from the "hall" table only if rows exist
        int rowsDeletedHall = psDeleteHall.executeUpdate();

        // If the delete query affected rows, commit the transaction
        if (rowsDeletedHall > 0 || rowsUpdatedSubject > 0) {
            con.commit();
            response.sendRedirect("display_subject?id=" + id); // Redirect on success
        } else {
            con.rollback(); // Rollback if no rows were affected
            response.sendRedirect("display_subject?id=" + id); // Handle failure gracefully
        }
    } catch (SQLException e) {
        try {
            if (con != null) {
                con.rollback(); // Rollback in case of any error
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle rollback exception
        }
        e.printStackTrace();
        response.getWriter().println("<p>Error: Database operation failed. Please check the logs.</p>");
    } finally {
        try {
            // Clean up resources
            if (psDeleteHall != null) psDeleteHall.close();
            if (psUpdateSubject != null) psUpdateSubject.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle cleanup exceptions
        }
    }
}
 protected void doProcessData1 (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "")) {
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
            PreparedStatement psDelete1 = con.prepareStatement("DELETE FROM hall WHERE id = ?");
            psDelete1.setInt(1, id);
            int rowsAffected1 = psDelete1.executeUpdate();

            if (rowsAffected > 0 && rowsAffected1>0) {
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
       catch (SQLException e) {
        e.printStackTrace();
        response.getWriter().println("<p>Error: Database connection failed. Please check the logs.</p>");
    }
}



    private void handleSubmit(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
          int totalStudents = 0;
        int totalSeats = 0;
        int balanceStudents = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "")) {
            String totalStudentQuery = "SELECT COUNT(*) AS total_students "
                    + "FROM student "
                    + "WHERE department IN ( "
                    + "    SELECT class "
                    + "    FROM subject "
                    + "    WHERE id = ? "
                    + ")";
            try (PreparedStatement ps = con.prepareStatement(totalStudentQuery)) {
                ps.setInt(1, Integer.parseInt(id));
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalStudents = rs.getInt("total_students");
                    }
                }
            }
            String totalSeatsQuery = "SELECT SUM(row * col) AS total_seats FROM hall WHERE id = ?";
            try (PreparedStatement seatPs = con.prepareStatement(totalSeatsQuery)) {
                seatPs.setInt(1, Integer.parseInt(id));
                try (java.sql.ResultSet seatRs = seatPs.executeQuery()) {
                    if (seatRs.next()) {
                        totalSeats = seatRs.getInt("total_seats");
                    }
                }
            }
            balanceStudents = totalStudents - totalSeats;
            if (balanceStudents == 0) {
                response.sendRedirect("arrangements?id=" + id);
            } else {
                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<script>alert('Some students still need to be assigned to their respective halls.');</script>");
                    processRequest(request, response);
                } catch (ServletException ex) {
                    Logger.getLogger(hall.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: Unable to fetch data from the database. Please check the logs.</p>");
        }
    }
}

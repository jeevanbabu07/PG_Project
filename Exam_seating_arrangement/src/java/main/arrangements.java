package main;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet(name = "arrangements", urlPatterns = {"/arrangements"})
public class arrangements extends HttpServlet {  // Class name corrected

    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/main_project", "root", "");
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

 protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
        String id1 = request.getParameter("id");
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

            out.println("<link rel='stylesheet' href='Arrangement.css' />");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 id='heading'><b>Exam Seating Arrangements</b></h1>");
            out.println("<div id='navigation'>");
            out.println("<ul>");
            out.println("<div id='al'>");
            out.println("<h3><i class='fa-solid fa-user-tie'></i>ADMIN</h3>");
            out.println("<form method='post' action='arrangements?action=logout'>");
             out.println("<input type='hidden' name='id' value='" + id1 + "'>");
            out.println("<button id='logout'>LOGOUT</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<br />");
            out.println("<li id=\"student_list\">");
            out.println("<a>");
            out.println("<i class=\"fa-solid fa-graduation-cap\"></i><i>Student List</i></a>");
            out.println("</li>");
            out.println("<li id=\"subject_list\">");
            out.println("<a>");
            out.println("<i class=\"fa-solid fa-book-open\"></i><i>Subject</i></a>");
            out.println("</li>");
            out.println("<li id=\"manage_list\">");
            out.println("<a><i class=\"fa-solid fa-person-shelter\"></i><i>Manage Rooms</i></a>");
            out.println("</li>");
            out.println("<li id=\"final\"><a id='a'><i class=\"fa-solid fa-note-sticky\"></i><i>Arrangements</i></a>");
            out.println("</li>");
            out.println("</ul>");
            out.println("</div>");
            
        String[] roomIds = request.getParameterValues("id");

        // Ensure room IDs are provided
        if (roomIds == null || roomIds.length == 0) {
            out.println("<p>Error: No room IDs provided in the request.</p>");
            return;
        }

        try (Connection conn = getConnection()) {
            // Fetch room details
            String hallQuery = "SELECT room, row, col FROM hall WHERE id IN ("; 
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < roomIds.length; i++) {
                placeholders.append("?");
                if (i < roomIds.length - 1) {
                    placeholders.append(", ");
                }
            }
            hallQuery += placeholders.toString() + ")"; 
            PreparedStatement hallStmt = conn.prepareStatement(hallQuery);
            for (int i = 0; i < roomIds.length; i++) {
                hallStmt.setInt(i + 1, Integer.parseInt(roomIds[i]));
            }

            ResultSet hallResult = hallStmt.executeQuery();
            Map<String, int[]> roomData = new LinkedHashMap<>(); // Room details (rows, cols)

            while (hallResult.next()) {
                String room = hallResult.getString("room");
                int rows = hallResult.getInt("row");
                int cols = hallResult.getInt("col");
                roomData.put(room, new int[]{rows, cols});
            }

            if (roomData.isEmpty()) {
                out.println("<p>Error: No halls found with the provided IDs.</p>");
                return;
            }

            // Fetch student details
            String studentQuery = "SELECT reg_no, name, dob, department FROM student WHERE department IN (" 
                    + "SELECT class FROM subject WHERE id IN (";
            placeholders.setLength(0);
            for (int i = 0; i < roomIds.length; i++) {
                placeholders.append("?");
                if (i < roomIds.length - 1) {
                    placeholders.append(", ");
                }
            }
            studentQuery += placeholders.toString() + "))";

            PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
            for (int i = 0; i < roomIds.length; i++) {
                studentStmt.setInt(i + 1, Integer.parseInt(roomIds[i]));
            }

            ResultSet studentResult = studentStmt.executeQuery();
            Map<String, Queue<Student>> departmentQueues = new LinkedHashMap<>();

            while (studentResult.next()) {
                String regNo = studentResult.getString("reg_no");
                String name = studentResult.getString("name");
                String dob = studentResult.getString("dob");
                String department = studentResult.getString("department");

                departmentQueues.putIfAbsent(department, new LinkedList<>());
                departmentQueues.get(department).add(new Student(regNo, name, dob, department));
            }

            // Shuffle students so that no two adjacent seats have the same department
            Queue<Student> shuffledQueue = new LinkedList<>();
            while (!departmentQueues.isEmpty()) {
                Iterator<Map.Entry<String, Queue<Student>>> it = departmentQueues.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Queue<Student>> entry = it.next();
                    Queue<Student> queue = entry.getValue();

                    if (!queue.isEmpty()) {
                        shuffledQueue.add(queue.poll());
                    }
                    if (queue.isEmpty()) {
                        it.remove();
                    }
                }
            }

            // Assign students to rooms and display them row-wise
            out.println("<form action=\"arrangements\" method=\"POST\">");
            for (Map.Entry<String, int[]> entry : roomData.entrySet()) {
                String room = entry.getKey();
                int rows = entry.getValue()[0];
                int cols = entry.getValue()[1];
                int totalSeats = rows * cols;

                out.println("<h2 id='room'><center>" + room + "</center></h2>");
                out.println("<p id='ts'>Total Seats: " + totalSeats + "</p><br>");
                out.println("<table border='3' cellpadding='20' cellspacing='20'>");

                int seatCount = 0;
                out.println("<tr>");
                out.println("<th id='c1'></th>");
                for (int d = 1; d <= cols; d++) {
                    out.println("<th id='c'> C" + d + "</th>");
                }
                out.println("</tr>");

                for (int r = 1; r <= rows; r++) {
                    out.println("<tr>");
                    out.println("<th id='r'> R" + r + "</th>");
                    for (int c = 1; c <= cols; c++) {
                        if (!shuffledQueue.isEmpty() && seatCount < totalSeats) {
                            Student student = shuffledQueue.poll();
                            out.println("<td id='data'>");
                            out.println("<center><b>Reg No:</b> " + student.getRegNo() + "<br><b>Dept:</b> " + student.getDepartment() + "</center>");
                            out.println("<input type=\"hidden\" name=\"row_" + room + "_" + r + "_" + c + "\" value=\"" + r + "\">");
                            out.println("<input type=\"hidden\" name=\"col_" + room + "_" + r + "_" + c + "\" value=\"" + c + "\">");
                            out.println("<input type=\"hidden\" name=\"regNo_" + room + "_" + r + "_" + c + "\" value=\"" + student.getRegNo() + "\">");
                            out.println("</td>");
                            seatCount++;
                        } else {
                            out.println("<td>Empty</td>");
                        }
                    }
                    out.println("</tr>");
                }

                out.println("</table>");
                out.println("<br>");
                out.println("<hr>");
                out.println("<br>");
            }
            // Within processRequest
for (String id : roomIds) {
    out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\">");
}
out.println("<button type=\"submit\" id=\"sub\">Submit</button>");
out.println("</form>");
out.println("<button type=\"submit\" id=\"pre\" onclick=\"goToHallPage()\">Previous</button>");
out.println("<script>");
out.println("function goToHallPage() {");
out.println("  var id = '" + String.join(",", roomIds) + "';");  // Get the room IDs as a string
out.println("  window.location.href = 'hall?id=' + id;");  // Redirect to hall page with id as parameter
out.println("}");
out.println("</script>");


        } catch (SQLException e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}

    // Helper class for student
    static class Student {
        private final String regNo;
        private final String name;
        private final String dob;
        private final String department;

        public Student(String regNo, String name, String dob, String department) {
            this.regNo = regNo;
            this.name = name;
            this.dob = dob;
            this.department = department;
        }

        public String getRegNo() {
            return regNo;
        }

        public String getName() {
            return name;
        }

        public String getDob() {
            return dob;
        }

        public String getDepartment() {
            return department;
        }
    }

    @Override
    public String getServletInfo() {
        return "ArrangementsServlet handles seating arrangements for multiple rooms and classes.";
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    String action = request.getParameter("action");
     if ("logout".equals(action)) {
        doProcessData1(request, response); 
     }else{

    try (PrintWriter out = response.getWriter(); Connection conn = getConnection()) {
        String[] ids = request.getParameterValues("id");

        if (ids == null || ids.length == 0) {
            out.println("<p>Error: No room IDs received.</p>");
            return;
        }

        boolean hasError = false;

        for (String id : ids) {
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();

                if (paramName.startsWith("regNo_")) {
                    String regNo = request.getParameter(paramName);
                    String[] parts = paramName.split("_");

                    if (parts.length == 4) {
                        String room = parts[1];
                        String row = request.getParameter("row_" + room + "_" + parts[2] + "_" + parts[3]);
                        String col = request.getParameter("col_" + room + "_" + parts[2] + "_" + parts[3]);

                        if (row == null || col == null) {
                            out.println("<p>Error: Missing row or column for regNo " + regNo + " in room '" + room + "'.</p>");
                            hasError = true;
                            continue;
                        }

                        String studentQuery = "SELECT department, name, dob FROM student WHERE reg_no = ?";
                        try (PreparedStatement studentStmt = conn.prepareStatement(studentQuery)) {
                            studentStmt.setString(1, regNo);
                            ResultSet studentResult = studentStmt.executeQuery();

                            if (!studentResult.next()) {
                                out.println("<p>Error: No student data found for regNo " + regNo + ".</p>");
                                hasError = true;
                                continue;
                            }

                            String department = studentResult.getString("department");
                            String name = studentResult.getString("name");
                            java.sql.Date dob = studentResult.getDate("dob");

                            String subjectQuery = "SELECT subject, course_code, date, session FROM subject WHERE id = ? AND class = ?";
                            try (PreparedStatement subjectStmt = conn.prepareStatement(subjectQuery)) {
                                subjectStmt.setInt(1, Integer.parseInt(id));
                                subjectStmt.setString(2, department);
                                ResultSet subjectResult = subjectStmt.executeQuery();

                                if (!subjectResult.next()) {
                                    out.println("<p>Error: No subject data found for ID " + id + " matching department '" + department + "'.</p>");
                                    hasError = true;
                                    continue;
                                }

                                String subject = subjectResult.getString("subject");
                                String courseCode = subjectResult.getString("course_code");
                                java.sql.Date subjectDate = subjectResult.getDate("date");
                                String session = subjectResult.getString("session");

                                String insertQuery = "INSERT INTO arrangement (id, department, reg_no, name, dob, subject, course_code, hall_name, date, session, row, col) "
                                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                    insertStmt.setInt(1, Integer.parseInt(id));
                                    insertStmt.setString(2, department);
                                    insertStmt.setString(3, regNo);
                                    insertStmt.setString(4, name);
                                    insertStmt.setDate(5, dob);
                                    insertStmt.setString(6, subject);
                                    insertStmt.setString(7, courseCode);
                                    insertStmt.setString(8, room);
                                    insertStmt.setDate(9, subjectDate);
                                    insertStmt.setString(10, session);
                                    insertStmt.setInt(11, Integer.parseInt(row));
                                    insertStmt.setInt(12, Integer.parseInt(col));

                                    insertStmt.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        }

       if (!hasError) {
            out.println("<div background:#F0F8FF;>");
            // Include the link to the external CSS file in the HTML header
            out.println("<head>");
             out.println(" <link\n" +
"      rel=\"stylesheet\"\n" +
"      href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css\"\n" +
"      integrity=\"sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==\"\n" +
"      crossorigin=\"anonymous\"\n" +
"      referrerpolicy=\"no-referrer\"\n" +
"    />");
            out.println("<link rel='stylesheet' type='text/css' href='pdf.css'>");
            out.println("</head>");
            
            out.println("<div style='text-align: center;'><p style='color: green; font-weight: bold;margin-top:300px;font-size:18px;'>Arrangement innovated !</p></div>");
            out.println("<form action='DownloadPDF' method='GET'>");
            out.println("<input type='hidden' name='id' value='" + ids[0] + "' />");
            out.println("<div style='text-align: center; margin-top: 20px;'>"
                    + "<button type='submit' id='ds' class='button'>Export as PDF</button></div>");
            out.println("</form>");
            out.println("</div>");
             out.println("<div id='al'>");       
        out.println("<h3 id='ad'><i class=\"fa-solid fa-user-tie\"></i>ADMIN</h3>");
        out.println("<button id=\"logout\">LOGOUT</button>");
        out.println("</div>");
            out.println("<script>");
            out.println("document.getElementById(\"logout\").addEventListener(\"click\", function() {");
            out.println("window.location.href = \"start.html\";");
            out.println("});");
            out.println("</script>");
    out.println("<script>");
        out.println("document.getElementById(\"logout\").addEventListener(\"click\", function() {");
            out.println("window.location.href = \"start.html\";");
        out.println("});");
        out.println("</script>");
}
    } catch (SQLException ex) {
        response.getWriter().println("<p>Error: Database connection error - " + ex.getMessage() + "</p>");
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
}
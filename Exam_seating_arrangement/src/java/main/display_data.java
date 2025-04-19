package main;

import java.sql.PreparedStatement;
import jakarta.servlet.RequestDispatcher;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/display_data")
public class display_data extends HttpServlet {

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
            ResultSet rs1 = stmt.executeQuery("SELECT class_name FROM class");

            out.println("<html>");
            out.println("<head><title>Class Data</title>");
            out.println("<link rel='stylesheet' type='text/css' href='Display_Data.css'>");

            // Add JavaScript for disabling back button
            out.println("<script language='javascript' type='text/javascript'>");
            out.println("function DisableBackButton() { window.history.forward(); }");
            out.println("window.onload = DisableBackButton;");
            out.println("window.onpageshow = function(evt) { if (evt.persisted) { DisableBackButton(); } };");
            out.println("window.onunload = function() { void(0); };");
            out.println("</script>");

            out.println("</head>");
            out.println("<body>");
            out.println("<h1 id='sc'>Select Class:</h1>");
            out.println("<hr>");
            out.println("<form onsubmit=\"submitForm(event)\" method=\"post\">");

            int count = 0;
            out.println("<table>");
            while (rs1.next()) {
                if (count % 3 == 0) {
                    if (count > 0) {
                        out.println("</tr>"); 
                    }
                    out.println("<tr class='row'>"); 
                }
                String className = rs1.getString("class_name");
                out.println("<td>");
                out.println("<label id='" + className + "'>");
                out.println("<input type='checkbox' name='selectedClasses' value='" + className + "'><span></span>" + className + "</label>");
                out.println("</td>");
                count++;
            }
            out.println("</table>");
            out.println("<br>");
            out.println("<button type='submit' id='sub'>Submit</button>");
            out.println("</form>");

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
        PrintWriter out = response.getWriter();

        String[] selectedClasses = request.getParameterValues("selectedClasses");

        HttpSession session = request.getSession();
        session.setAttribute("selectedClasses", selectedClasses);

        out.println("<html><head>");
        out.println("<link rel='stylesheet' type='text/css' href='Display_Data.css'>");

        out.println("</head><body>");
        out.println("<h1>Selected Classes:</h1><br>");

        if (selectedClasses != null && selectedClasses.length > 0) {
            for (String className : selectedClasses) {
                try (Statement stmt2 = con.createStatement();
                     ResultSet rs2 = stmt2.executeQuery("SELECT reg_no, name, dob FROM student WHERE department = '" + className + "' ORDER BY reg_no ASC")) {
                    if (rs2.next()) {
                        out.println("<table border='1' id='table'>");
                        out.println("<tr><th colspan='3'><center>" + className + "</center></th></tr>");
                        out.println("<tr><th>REG NO</th><th>NAME</th><th>DATE OF BIRTH</th></tr>");
                        do {
                            String regno = rs2.getString("reg_no");
                            String name = rs2.getString("name");
                            String dob = rs2.getString("dob");
                            out.println("<tr><td id='td'>" + regno + "</td><td id='td'>" + name + "</td><td id='td'>" + dob + "</td></tr>");
                        } while (rs2.next());
                        out.println("</table><br>");
                    } else {
                        out.println("<h3 id='sc1'>No Records Found for " + className + " !</h3>");
                    }
                } catch (SQLException e) {
                    throw new ServletException("Failed to fetch data from the database", e);
                }
            }

            out.println("<form method='post' action='display_data?action=process'>");
            out.println("<input type='hidden' name='selectedClasses' value='" + String.join(",", selectedClasses) + "'>");
            out.println("<button type='submit' id='process'>Process Data</button>");
            out.println("</form>");
            out.println("<iframe src=\"stu_up_del.html\" class=\"frame\"></iframe>");
        } else {
            out.println("<iframe src=\"stu_up_del.html\" class=\"frame\"></iframe>");
            out.println("<p>No classes selected.</p>");
        }

        out.println("</body></html>");
        out.close();
    }

    private static int currentNumber = 1;

    protected void doProcessData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String selectedClassesStr = request.getParameter("selectedClasses");
        String[] selectedClasses = selectedClassesStr.split(",");
        int generatedNumber;

        HttpSession session = request.getSession();
        session.setAttribute("selectedClasses", selectedClasses);

        do {
            generatedNumber = generateNextNumber();
        } while (checkNumberExistsInDB(generatedNumber));

        request.setAttribute("generatedNumber", generatedNumber);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/subject_class");
        dispatcher.forward(request, response);
    }

    private boolean checkNumberExistsInDB(int number) {
        boolean exists = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/main_project", "root", "");
             PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM subject WHERE id = ?")) {
            stmt.setInt(1, number);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    exists = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    private int generateNextNumber() {
        return currentNumber++;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("process".equals(action)) {
            doProcessData(req, res);
        } else {
            super.service(req, res);
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
}

package main;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "DownloadPDF", urlPatterns = {"/DownloadPDF"})
public class DownloadPDF extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the "id" parameter
        String id = request.getParameter("id");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=SeatingArrangement_" + id + ".pdf");

        if (id != null) {
            try (OutputStream out = response.getOutputStream()) {
                PdfWriter writer = new PdfWriter(out);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                try {
                    // Add title
                   document.add(new Paragraph("Seating Arrangement")
    .setBold()
    .setFontSize(16)
    .setTextAlignment(TextAlignment.CENTER)
    .setUnderline());

                    document.add(new Paragraph(" ")); // Blank line

                    try (Connection conn = getConnection()) {
                        // Query to fetch distinct rooms for the given ID
                        String roomQuery = "SELECT DISTINCT hall_name FROM arrangement WHERE id = ?";
                        try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
                            roomStmt.setInt(1, Integer.parseInt(id));

                            try (ResultSet roomRs = roomStmt.executeQuery()) {
                                while (roomRs.next()) {
                                    String roomName = roomRs.getString("hall_name");

                                    // Add room name as a center-aligned heading
                                    document.add(new Paragraph("Room: " + roomName).setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));

                                    // Query to calculate total seats
                                    String totalSeatsQuery = "SELECT COUNT(*) AS total_seats FROM arrangement WHERE id = ? AND hall_name = ?";
                                    try (PreparedStatement totalSeatsStmt = conn.prepareStatement(totalSeatsQuery)) {
                                        totalSeatsStmt.setInt(1, Integer.parseInt(id));
                                        totalSeatsStmt.setString(2, roomName);

                                        try (ResultSet totalSeatsRs = totalSeatsStmt.executeQuery()) {
                                            if (totalSeatsRs.next()) {
                                                int totalSeats = totalSeatsRs.getInt("total_seats");
                                                document.add(new Paragraph("Total Seats: " + totalSeats)
                                                        .setFontSize(12));
                                            }
                                        }
                                    }

                                    document.add(new Paragraph(" ")); // Blank line

                                    // Table structure
                                    float[] columnWidths = {100, 150, 150, 50, 50};
                                    Table table = new Table(columnWidths);
                                    table.setWidth(UnitValue.createPercentValue(100));

                                    // Table Headers
                                    table.addHeaderCell(new Cell().add(new Paragraph("Reg No").setBold()));
                                    table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold()));
                                    table.addHeaderCell(new Cell().add(new Paragraph("Department").setBold()));
                                    table.addHeaderCell(new Cell().add(new Paragraph("Row").setBold()));
                                    table.addHeaderCell(new Cell().add(new Paragraph("Col").setBold()));

                                    // Query to fetch data for the current room
                                    String dataQuery = "SELECT reg_no, name, department, row, col FROM arrangement WHERE id = ? AND hall_name = ?";
                                    try (PreparedStatement dataStmt = conn.prepareStatement(dataQuery)) {
                                        dataStmt.setInt(1, Integer.parseInt(id));
                                        dataStmt.setString(2, roomName);

                                        try (ResultSet dataRs = dataStmt.executeQuery()) {
                                            boolean dataFound = false;

                                            while (dataRs.next()) {
                                                dataFound = true;

                                                // Add row data
                                                table.addCell(new Cell().add(new Paragraph(dataRs.getString("reg_no"))));
                                                table.addCell(new Cell().add(new Paragraph(dataRs.getString("name"))));
                                                table.addCell(new Cell().add(new Paragraph(dataRs.getString("department"))));
                                                table.addCell(new Cell().add(new Paragraph(String.valueOf(dataRs.getInt("row")))));
                                                table.addCell(new Cell().add(new Paragraph(String.valueOf(dataRs.getInt("col")))));
                                            }

                                            if (dataFound) {
                                                document.add(table); // Add table only if data is found
                                            } else {
                                                document.add(new Paragraph("No data found for this room.").setFontSize(12));
                                            }
                                        }
                                    }
                                    document.add(new Paragraph(" ")); // Add a blank line after each room
                                }
                            }
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        document.add(new Paragraph("Database error: " + e.getMessage()).setFontSize(12));
                        e.printStackTrace();
                    }
                } finally {
                    document.close();
                }

                response.flushBuffer();
            } catch (Exception e) {
                response.reset();
                response.setContentType("text/plain");
                response.getWriter().println("Error generating PDF: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<p style='color: red;'>Error: No ID received.</p>");
            out.println("</body></html>");
        }
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/main_project", "root", "");
    }
}
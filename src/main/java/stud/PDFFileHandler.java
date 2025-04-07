package stud;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PDFFileHandler extends AbstractFileHandler {
    private static class PageState {
        PDPageContentStream contentStream;
        float y;
    
        PageState(PDPageContentStream contentStream, float y) {
            this.contentStream = contentStream;
            this.y = y;
        }
    }
    
    private static final float MARGIN = 50;
    private static final float TABLE_CELL_HEIGHT = 20;
    private static final float TABLE_START_Y = 700;
    private static final float COLUMN_WIDTH = 150;
    private static final float PAGE_BOTTOM_MARGIN = 50;

    public PDFFileHandler(Set<String> existingGroups, List<Student> students, List<AttendanceRecord> attendanceRecords, List<AttendanceRecord> filteredAttendanceRecords) {
        super(existingGroups, students, attendanceRecords, filteredAttendanceRecords);
    }

    @Override
    public boolean exportData(String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDType0Font font = PDType0Font.load(document, new File("fonts/DejaVuSans.ttf"));

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setFont(font, 12);
            float tableY = TABLE_START_Y;

            // Draw Group Names
            PageState state = checkPageOverflow(document, contentStream, font, tableY);
            contentStream = state.contentStream;
            tableY = state.y;

            drawSectionHeader(contentStream, font, "Group Names", tableY);
            tableY -= TABLE_CELL_HEIGHT * 1.5;

            for (String group : this.existingGroups) {
                state = checkPageOverflow(document, contentStream, font, tableY);
                contentStream = state.contentStream;
                tableY = state.y;
                drawTextRow(contentStream, font, group, tableY);
                tableY -= TABLE_CELL_HEIGHT;
            }

            // Draw Students Table
            tableY -= TABLE_CELL_HEIGHT;
            tableY = drawTableHeader(document, contentStream, font, "Students", new String[]{"ID", "First Name", "Last Name", "Group"}, tableY);

            for (Student student : this.students) {
                state = checkPageOverflow(document, contentStream, font, tableY);
                contentStream = state.contentStream;
                tableY = state.y;
                tableY = drawTableRow(document, contentStream, font, new String[]{
                        String.valueOf(student.getId()),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getGroup()
                }, tableY);
            }

            // Draw Attendance Table
            tableY -= TABLE_CELL_HEIGHT;
            tableY = drawTableHeader(document, contentStream, font, "Attendance Records", new String[]{"Date", "ID", "Status"}, tableY);

            for (AttendanceRecord record : this.filteredAttendanceRecords) {
                state = checkPageOverflow(document, contentStream, font, tableY);
                contentStream = state.contentStream;
                tableY = state.y;
                tableY = drawTableRow(document, contentStream, font, new String[]{
                        record.getDate(),
                        String.valueOf(record.getStudentId()),
                        record.getStatus()
                }, tableY);
            }

            contentStream.close();
            document.save(new File(filePath));
            document.close();
            return true;

        } catch (IOException e) {
            System.err.println("Error exporting PDF: " + e.getMessage());
            return false;
        }
    }

    private PageState checkPageOverflow(PDDocument document, PDPageContentStream contentStream, PDType0Font font, float y) throws IOException {
        if (y < PAGE_BOTTOM_MARGIN) {
            contentStream.close();
    
            PDPage newPage = new PDPage();
            document.addPage(newPage);
            contentStream = new PDPageContentStream(document, newPage, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setFont(font, 12);
            y = TABLE_START_Y;
        }
        return new PageState(contentStream, y);
    }
    

    private void drawSectionHeader(PDPageContentStream contentStream, PDType0Font font, String title, float y) throws IOException {
        contentStream.setFont(font, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, y);
        contentStream.showText(title);
        contentStream.endText();
    }

    private void drawTextRow(PDPageContentStream contentStream, PDType0Font font, String text, float y) throws IOException {
        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private float drawTableHeader(PDDocument document, PDPageContentStream contentStream, PDType0Font font, String title, String[] columns, float y) throws IOException {
        PageState state = checkPageOverflow(document, contentStream, font, y);
        contentStream = state.contentStream;
        y = state.y;

        drawSectionHeader(contentStream, font, title, y);
        y -= TABLE_CELL_HEIGHT * 1.5;

        contentStream.setFont(font, 12);
        for (int i = 0; i < columns.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN + (i * COLUMN_WIDTH), y);
            contentStream.showText(columns[i]);
            contentStream.endText();
        }

        y -= TABLE_CELL_HEIGHT * 1.5;
        return y;
    }

    private float drawTableRow(PDDocument document, PDPageContentStream contentStream, PDType0Font font, String[] values, float y) throws IOException {
        PageState state = checkPageOverflow(document, contentStream, font, y);
        contentStream = state.contentStream;
        y = state.y;


        contentStream.setFont(font, 12);
        for (int i = 0; i < values.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN + (i * COLUMN_WIDTH), y);
            contentStream.showText(values[i]);
            contentStream.endText();
        }

        y -= TABLE_CELL_HEIGHT;
        return y;
    }

    @Override
    public void importData(String filePath) {
        System.out.println("PDF import is not supported.");
    }
}
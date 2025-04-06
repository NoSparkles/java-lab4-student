package stud;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileHandler extends AbstractFileHandler {
    public ExcelFileHandler(Set<String> existingGroups, List<Student> students, List<AttendanceRecord> attendanceRecords, List<AttendanceRecord> filteredAttendanceRecords) {
        super(existingGroups, students, attendanceRecords, filteredAttendanceRecords);
    }

    @Override
    public void importData(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            importGroups(workbook.getSheet("Groups"));
            importStudents(workbook.getSheet("Students"));
            importAttendanceRecords(workbook.getSheet("Attendance"));

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
        }
    }

    private void importGroups(Sheet sheet) {
        if (sheet == null) return;

        for (Row row : sheet) {
            Cell cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && !"Group Name".equalsIgnoreCase(cell.getStringCellValue().trim())) {
                existingGroups.add(cell.getStringCellValue().trim());
            }
        }
    }

    private void importStudents(Sheet sheet) {
        if (sheet == null) return;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            students.add(new Student(
                (int) row.getCell(0).getNumericCellValue(),
                getCellValue(row.getCell(1)),
                getCellValue(row.getCell(2)),
                getCellValue(row.getCell(3))
            ));
            existingGroups.add(getCellValue(row.getCell(3))); // Ensure the group is stored
        }
    }

    private void importAttendanceRecords(Sheet sheet) {
        if (sheet == null) return;
    
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row
    
            String status = getCellValue(row.getCell(2));
    
            if (!"null".equalsIgnoreCase(status)) { // Skip "null" status records
                attendanceRecords.add(new AttendanceRecord(
                    (int) row.getCell(1).getNumericCellValue(),
                    getCellValue(row.getCell(0)),
                    status
                ));
            }
        }
    }
    

    @Override
    public boolean exportData(String filePath) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {

            exportGroups(workbook.createSheet("Groups"));
            exportStudents(workbook.createSheet("Students"));
            exportAttendanceRecords(workbook.createSheet("Attendance"));

            workbook.write(fos);
            return true;

        } catch (IOException e) {
            System.err.println("Error exporting Excel file: " + e.getMessage());
            return false;
        }
    }

    private void exportGroups(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Group Name");

        int rowIndex = 1;
        for (String group : this.existingGroups) {
            sheet.createRow(rowIndex++).createCell(0).setCellValue(group);
        }
    }

    private void exportStudents(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("First Name");
        header.createCell(2).setCellValue("Last Name");
        header.createCell(3).setCellValue("Group");

        int rowIndex = 1;
        for (Student student : this.students) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getFirstName());
            row.createCell(2).setCellValue(student.getLastName());
            row.createCell(3).setCellValue(student.getGroup());
        }
    }

    private void exportAttendanceRecords(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Date");
        header.createCell(1).setCellValue("ID");
        header.createCell(2).setCellValue("Attendance Status");

        int rowIndex = 1;
        for (AttendanceRecord record : this.filteredAttendanceRecords) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(record.getDate());
            row.createCell(1).setCellValue(record.getStudentId());
            row.createCell(2).setCellValue(record.getStatus());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue()); // Convert numeric to string
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
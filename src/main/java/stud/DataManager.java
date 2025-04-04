package stud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class DataManager {
    private List<Student> students;
    private List<AttendanceRecord> attendanceRecords;
    private List<AttendanceRecord> filteredAttendaceRecords;

    public List<Student> getStudents() {
        return this.students;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return this.attendanceRecords;
    }

    public List<AttendanceRecord> getFilteredAttendanceRecords() {
        return this.filteredAttendaceRecords;
    }

    public void showDataInTableView(TableView<AttendanceRecord> tableView) {
        if (this.filteredAttendaceRecords == null || this.filteredAttendaceRecords.isEmpty()) {
            System.out.println("No attendance records available.");
            return;
        }

        ObservableList<AttendanceRecord> observableRecords = FXCollections.observableArrayList(this.filteredAttendaceRecords);

        tableView.setItems(observableRecords);
    }


    public void importFromCSV(String filePath) {
        List<Student> students = new ArrayList<>();
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        boolean isReadingStudents = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Empty line separates students from attendance records
                if (line.isEmpty()) {
                    isReadingStudents = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (isReadingStudents) {
                    // Ignore header
                    if (fields[0].equals("ID")) continue;

                    students.add(new Student(
                        Integer.parseInt(fields[0]), 
                        fields[1], 
                        fields[2], 
                        fields[3]
                    ));
                } else {
                    // Ignore header
                    if (fields[0].equals("Date")) continue;

                    attendanceRecords.add(new AttendanceRecord(
                        Integer.parseInt(fields[1]),
                        fields[2], 
                        fields[3], 
                        fields[0], 
                        fields[4], 
                        fields[5]
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        this.students = students;
        this.attendanceRecords = attendanceRecords;
        this.filteredAttendaceRecords = attendanceRecords;
    }

    public void importFromExcel(String filePath) {
        // Implement Excel import logic here
    }

    public boolean exportStudentsToCSV(List<Student> students, String filePath) {
        // Implement CSV export logic here
        return false;
    }

    public boolean exportStudentsToExcel(List<Student> students, String filePath) {
        // Implement Excel export logic here
        return false;
    }

    public boolean exportStudentsToPDF(List<Student> students, String filePath) {
        // Implement PDF export logic here
        return false;
    }

    public boolean exportAttendanceToCSV(List<AttendanceRecord> attendanceRecords, String filePath) {
        // Implement CSV export logic here
        return false;
    }

    public boolean exportAttendanceToExcel(List<AttendanceRecord> attendanceRecords, String filePath) {
        // Implement Excel export logic here
        return false;
    }

    public boolean exportAttendanceToPDF(List<AttendanceRecord> attendanceRecords, String filePath) {
        // Implement PDF export logic here
        return false;
    }
}
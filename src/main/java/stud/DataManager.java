package stud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class DataManager {
    TableView<AttendanceRecord> tableView;

    private List<Student> students;
    private List<AttendanceRecord> attendanceRecords;
    private List<AttendanceRecord> filteredAttendaceRecords;

    public DataManager(TableView<AttendanceRecord> tableView) {
        this.tableView = tableView;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return this.attendanceRecords;
    }

    public List<AttendanceRecord> getFilteredAttendanceRecords() {
        return this.filteredAttendaceRecords;
    }

    public void filterAttendance(LocalDate fromDate, LocalDate toDate, String groupOrStudent, String filterType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
        // Generate a list of all possible dates in the range
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
    
        // Get all students for filtering
        List<Student> studentsToFilter;
        if (filterType.equals("Student")) {
            studentsToFilter = students.stream()
                .filter(s -> s.getFirstName().toLowerCase().contains(groupOrStudent.toLowerCase()) 
                          || s.getLastName().toLowerCase().contains(groupOrStudent.toLowerCase()))
                .collect(Collectors.toList());
        } else if (filterType.equals("Group")) {
            studentsToFilter = students.stream()
                .filter(s -> s.getGroup().equalsIgnoreCase(groupOrStudent))
                .collect(Collectors.toList());
        } else {
            studentsToFilter = new ArrayList<>(students); // No student/group filter
        }
    
        List<AttendanceRecord> filteredRecords = new ArrayList<>();
    
        // Cycle through each student and each date in the range
        for (Student student : studentsToFilter) {
            for (LocalDate date : allDates) {
                String dateStr = date.format(formatter);
    
                // Find existing records for the student on this date
                AttendanceRecord existingRecord = attendanceRecords.stream()
                    .filter(r -> r.getStudentId() == student.getId() && r.getDate().equals(dateStr))
                    .findFirst()
                    .orElse(null);
    
                if (existingRecord != null) {
                    filteredRecords.add(existingRecord);
                } else {
                    // Create a new attendance record with "null" status
                    filteredRecords.add(new AttendanceRecord(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        dateStr,
                        student.getGroup(),
                        "null"
                    ));
                }
            }
        }
    
        this.filteredAttendaceRecords = filteredRecords;
        this.showDataInTableView();
    }
    
    public void showDataInTableView() {
        ObservableList<AttendanceRecord> observableRecords = FXCollections.observableArrayList(this.filteredAttendaceRecords);

        this.tableView.getItems().clear();
        this.tableView.setItems(observableRecords);
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
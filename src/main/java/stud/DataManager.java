package stud;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class DataManager {
    private TableView<AttendanceRecord> tableView;
    private Set<String> existingGroups = new HashSet<>();
    private List<Student> students = new ArrayList<>();
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    private List<AttendanceRecord> filteredAttendanceRecords;
    private DataProcessor dataProcessor;

    public DataManager(TableView<AttendanceRecord> tableView) {
        this.tableView = tableView;
    }

    public Set<String> getExistingGroups() {
        return this.existingGroups;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return this.attendanceRecords;
    }

    public void addAttendanceRecord(AttendanceRecord newRecord) {
        // Remove any existing record for the same student and date
        attendanceRecords.removeIf(record -> 
            record.getStudentId() == newRecord.getStudentId() && record.getDate().equals(newRecord.getDate())
        );
    
        // Add the new entry
        attendanceRecords.add(newRecord);
    }

    public List<AttendanceRecord> getFilteredAttendanceRecords() {
        return this.filteredAttendanceRecords;
    }

    public void filterAttendance(LocalDate fromDate, LocalDate toDate, String groupOrStudent, String filterType, boolean showNull) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
        // Generate a list of all possible dates in the range
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
    
        // Get students based on filterType (Group OR Student)
        List<Student> studentsToFilter;
        if (filterType.equals("Student")) {
            studentsToFilter = students.stream()
                .filter(s -> s.getFirstName().toLowerCase().contains(groupOrStudent.toLowerCase()) 
                          || s.getLastName().toLowerCase().contains(groupOrStudent.toLowerCase()))
                .collect(Collectors.toList());
        } else if (filterType.equals("Group")) {
            studentsToFilter = students.stream()
                .filter(s -> s.getGroup().toLowerCase().contains(groupOrStudent.toLowerCase()))
                .collect(Collectors.toList());
        } else {
            studentsToFilter = new ArrayList<>(students); // No student/group filter
        }
    
        List<AttendanceRecord> filteredRecords = new ArrayList<>();
    
        // Cycle through each student and each date in the range
        for (Student student : studentsToFilter) {
            for (LocalDate date : allDates) {
                String dateStr = date.format(formatter);
    
                // Find existing attendance record
                AttendanceRecord existingRecord = attendanceRecords.stream()
                    .filter(r -> r.getStudentId() == student.getId() && r.getDate().equals(dateStr))
                    .findFirst()
                    .orElse(null);
    
                if (existingRecord != null) {
                    filteredRecords.add(existingRecord);
                } else if (showNull) {
                    // Only add "null" entries if showNull is true
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
    
        this.filteredAttendanceRecords = filteredRecords;
        this.showDataInTableView();
    }
    
    public void showDataInTableView() {
        ObservableList<AttendanceRecord> observableRecords = FXCollections.observableArrayList(this.filteredAttendanceRecords);

        this.tableView.getItems().clear();
        this.tableView.setItems(observableRecords);
    }

    public void importData(String filePath, String fileType) {
        this.dataProcessor = DataProcessorFactory.getDataProcessor(fileType, this);
        this.dataProcessor.importData(filePath);

        this.filteredAttendanceRecords = this.attendanceRecords;
    }
}
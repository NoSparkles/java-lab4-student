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
    private Set<Student> studentLookup = new HashSet<>();
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

    public Student getStudentById(int studentId) {
        return studentLookup.stream()
            .filter(student -> student.getId() == studentId)
            .findFirst()
            .orElse(new Student(studentId, "Unknown", "Unknown", "No Group"));
    }

    public void createStudent(String firstName, String lastName, String group) {
        if (firstName.isEmpty() || lastName.isEmpty() || group.isEmpty()) {
            System.out.println("Error: All fields must be filled!");
            return;
        }
    
        int newStudentId = generateNewStudentId();
        Student newStudent = new Student(newStudentId, firstName, lastName, group);
    
        studentLookup.add(newStudent);
        students.add(newStudent);
        System.out.println("New Student Created: " + newStudent);
    }
    
    private int generateNewStudentId() {
        return students.stream()
                       .mapToInt(Student::getId)
                       .max()
                       .orElse(100) + 1; // Default starts at 101 if no students exist
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
    
        List<AttendanceRecord> filteredRecords = new ArrayList<>();
        List<Student> studentsToFilter;
    
        // Determine which students to filter
        if ("Student".equals(filterType)) {
            studentsToFilter = students.stream()
                .filter(s -> s.getFirstName().toLowerCase().contains(groupOrStudent.toLowerCase()) 
                          || s.getLastName().toLowerCase().contains(groupOrStudent.toLowerCase()))
                .collect(Collectors.toList());
        } else if ("Group".equals(filterType)) {
            studentsToFilter = students.stream()
                .filter(s -> s.getGroup().toLowerCase().contains(groupOrStudent.toLowerCase()))
                .collect(Collectors.toList());
        } else {
            studentsToFilter = new ArrayList<>(students); // No student/group filter
        }
        // If either fromDate or toDate is null, disable showNull and return existing records only
        if (fromDate == null || toDate == null) {
            showNull = false; // Ensure no empty entries are added
    
            filteredRecords = attendanceRecords.stream()
                .filter(r -> studentsToFilter.stream().anyMatch(s -> s.getId() == r.getStudentId()))
                .collect(Collectors.toList());
        } else {
            List<LocalDate> allDates = new ArrayList<>();
            LocalDate currentDate = fromDate;
            while (!currentDate.isAfter(toDate)) {
                allDates.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }
    
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
                        filteredRecords.add(new AttendanceRecord(student.getId(), dateStr, "null"));
                    }
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
        if (this.existingGroups != null && this.students != null && this.attendanceRecords != null && this.studentLookup != null && this.filteredAttendanceRecords != null) {
            this.existingGroups.clear();
            this.students.clear();
            this.attendanceRecords.clear();
            this.studentLookup.clear();
            this.filteredAttendanceRecords.clear();
        }
        this.dataProcessor = DataProcessorFactory.getDataProcessor(fileType, this);
        this.dataProcessor.importData(filePath);

        this.studentLookup = new HashSet<>(this.students);
        this.filteredAttendanceRecords = this.attendanceRecords;
    }

    public void exportData(String filePath, String fileType) {
        this.dataProcessor = DataProcessorFactory.getDataProcessor(fileType, this);
        this.dataProcessor.exportData(filePath);
    }
}
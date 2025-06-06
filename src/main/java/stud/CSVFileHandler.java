package stud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CSVFileHandler extends AbstractFileHandler {
    public CSVFileHandler(Set<String> existingGroups, List<Student> students, List<AttendanceRecord> attendanceRecords, List<AttendanceRecord> filteredAttendanceRecords) {
        super(existingGroups, students, attendanceRecords, filteredAttendanceRecords);
    }

    @Override
    public void importData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean readingGroups = true;
            boolean readingStudents = false;
            boolean readingAttendance = false;
    
            while ((line = reader.readLine()) != null) {
                line = line.trim();
    
                // Detect section changes
                if (line.isEmpty()) {
                    if (readingGroups) {
                        readingGroups = false;
                        readingStudents = true;
                    } else if (readingStudents) {
                        readingStudents = false;
                        readingAttendance = true;
                    }
                    continue;
                }
    
                String[] fields = line.split(",");
    
                if (readingGroups) {
                    if (!fields[0].equals("Group Name")) { 
                        existingGroups.add(fields[0]); // Store unique group names
                    }
                } else if (readingStudents) { 
                    if (!fields[0].equals("ID") && fields.length == 4) {
                        students.add(new Student(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3]));
                        existingGroups.add(fields[3]); // Ensure the group is stored
                    }
                } else if (readingAttendance) { 
                    if (!fields[0].equals("Date") && fields.length == 3 && !"null".equalsIgnoreCase(fields[2])) { 
                        // Skip attendance records where status is "null"
                        attendanceRecords.add(new AttendanceRecord(Integer.parseInt(fields[1]), fields[0], fields[2]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    @Override
    public boolean exportData(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Export Group Names
            writer.write("Group Name\n");
            for (String group : this.existingGroups) {
                writer.write(group + "\n");
            }

            writer.write("\n"); // Add a blank line before student data

            // Export Students
            writer.write("ID,First Name,Last Name,Group\n");
            for (Student student : this.students) {
                writer.write(student.getId() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getGroup() + "\n");
            }

            writer.write("\n"); // Add a blank line before attendance data

            // Export Attendance Records
            writer.write("Date,ID,Attendance Status\n");
            for (AttendanceRecord record : this.filteredAttendanceRecords) {
                writer.write(record.getDate() + "," + record.getStudentId() + "," + record.getStatus() + "\n");
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error exporting CSV: " + e.getMessage());
            return false;
        }
    }
}

package stud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CSVFileHandler extends AbstractFileHandler {
    public CSVFileHandler(Set<String> existingGroups, List<Student> students, List<AttendanceRecord> attendanceRecords) {
        super(existingGroups, students, attendanceRecords);
    }

    @Override
    public void importData(String filePath) {
        boolean isReadingGroups = true;
        boolean isReadingStudents = false;
        

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Detect section changes
                if (line.isEmpty()) {
                    if (isReadingGroups) {
                        isReadingGroups = false;
                        isReadingStudents = true;
                    } else if (isReadingStudents) {
                        isReadingStudents = false;
                    }
                    continue;
                }

                String[] fields = line.split(",");
                if (isReadingGroups) {
                    // Store unique group names
                    if (!fields[0].equals("Group Name")) {
                        existingGroups.add(fields[0]);
                    }
                } else if (isReadingStudents) {
                    // Ignore headers
                    if (fields[0].equals("ID")) continue;

                    students.add(new Student(
                        Integer.parseInt(fields[0]),
                        fields[1],
                        fields[2],
                        fields[3]
                    ));
                } else {
                    // Ignore headers
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
    }

    @Override
    public boolean exportData(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,First Name,Last Name,Group\n");
            for (Student student : this.students) {
                writer.write(student.getId() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getGroup() + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting CSV: " + e.getMessage());
            return false;
        }
    }
}

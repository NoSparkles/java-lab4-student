package stud;

import java.util.List;
import java.util.Set;

public abstract class AbstractFileHandler implements DataProcessor {
    protected Set<String> existingGroups;
    protected List<Student> students;
    protected List<AttendanceRecord> attendanceRecords;
    protected List<AttendanceRecord> filteredAttendanceRecords;

    public AbstractFileHandler(Set<String> existingGroups, List<Student> students, List<AttendanceRecord> attendanceRecords, List<AttendanceRecord> filteredAttendanceRecords) {
        this.existingGroups = existingGroups;
        this.students = students;
        this.attendanceRecords = attendanceRecords;
        this.filteredAttendanceRecords = filteredAttendanceRecords;
    }

    @Override
    public abstract void importData(String filePath);

    @Override
    public abstract boolean exportData(String filePath);
}

package stud;
import java.util.List;

public class DataContainer {
    private final List<Student> students;
    private final List<AttendanceRecord> attendanceRecords;

    public DataContainer(List<Student> students, List<AttendanceRecord> attendanceRecords) {
        this.students = students;
        this.attendanceRecords = attendanceRecords;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return this.attendanceRecords;
    }
}

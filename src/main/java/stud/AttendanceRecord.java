package stud;

public class AttendanceRecord {
    private int studentId;
    private String date;
    private String status;

    public AttendanceRecord(int studentId, String date, String status) {
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public String getDate() {
        return this.date;
    }

    public String getStatus() {
        return this.status;
    }
}
package stud;

class AttendanceRecord {
    int studentId;
    String firstName;
    String lastName;
    String date;
    String group;
    String status;

    public AttendanceRecord(int studentId, String firstName, String lastName, String date, String group, String status) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.group = group;
        this.status = status;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getDate() {
        return this.date;
    }

    public String getGroup() {
        return this.group;
    }

    public String getStatus() {
        return this.status;
    }
}


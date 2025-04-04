package stud;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String group;

    public Student(int id, String firstName, String lastName, String group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getGroup() {
        return this.group;
    }
}

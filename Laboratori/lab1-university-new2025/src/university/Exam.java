package university;

public class Exam {
    private int courseCode;
    private int studentID;
    private int grade;

    public Exam(int courseCode, int studentID, int grade) {
        this.courseCode = courseCode;
        this.studentID = studentID;
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }
}

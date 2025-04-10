package university;

public class Course {
    
    private final int MAX_STUDENTS = 100;
    private int courseCode;
    private String title;
    private String teacher;
    private int[] attendees;
    private int numberAttendees;
    private Exam[] studentsExams;
    private int numberExams;

    public Course(int courseCode, String title, String teacher) {
        this.courseCode = courseCode;
        this.title = title;
        this.teacher = teacher;
        this.attendees = new int[MAX_STUDENTS];
        this.numberAttendees = 0;
        this.studentsExams = new Exam[MAX_STUDENTS];
        this.numberExams = 0;
    }

    public int getCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return courseCode + "," + title + "," + teacher;
    }

    public void addAttendee(int studentID) {
        attendees[numberAttendees++] = studentID;
    }

    public int getAttendee(int index) {
        return attendees[index];
    }

    public int getNumberAttendees() {
        return numberAttendees;
    }

    public void addExam(int studentID, int grade) {
        studentsExams[numberExams++] = new Exam(courseCode, studentID, grade);
    }

    public float getAverageGrade() {
        int i;
        float avg = 0;

        if (numberExams == 0)
            return 0;

        for (i = 0; i < numberExams; i++)
            avg = avg + studentsExams[i].getGrade();

        return (avg / numberExams);
    }
}

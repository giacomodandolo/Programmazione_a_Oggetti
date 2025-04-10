package university;

public class Student {
    
    private final int MAX_COURSES = 25;
    private int ID;
    private String first;
    private String last;
    private int[] courses;
    private int numberCourses;
    private Exam[] takenExams;
    private int numberExams;

    public Student(int ID, String first, String last) {
        this.ID = ID;
        this.first = first;
        this.last = last;
        this.courses = new int[MAX_COURSES];
        this.numberCourses = 0;
        this.takenExams = new Exam[MAX_COURSES];
        this.numberExams = 0;
    }

    public int getID() {
        return ID;
    }

    public String getInfo() {
        return ID + " " + first + " " + last;
    }

    public void addCourse(int courseCode) {
        courses[numberCourses++] = courseCode;
    }

    public int getCourse(int index) {
        return courses[index];
    }

    public int getNumberCourses() {
        return numberCourses;
    }

    public boolean checkCourse(int courseCode) {
        for (int c : courses)
            if (c == courseCode)
                return true;
        
        return false;
    }

    public void addExam(int courseCode, int grade) {
        takenExams[numberExams++] = new Exam(courseCode, ID, grade);
    }

    public float getAverageGrade() {
        int i;
        float avg = 0;

        if (numberExams == 0)
            return 0;

        for (i = 0; i < numberExams; i++)
            avg = avg + takenExams[i].getGrade();

        return (avg / numberExams);
    }

    
    public float getScore() {
        float score;

        score = this.getAverageGrade();
        score = score + (numberExams/numberCourses)*10;

        return score;
    }

    public String getInfoScore() {
        return first + " " + last + " : " + getScore();
    }
}

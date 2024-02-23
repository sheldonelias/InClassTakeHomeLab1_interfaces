package Model;

import Controller.Enrollment;


public class Class
{
    //INSTANCE VARIABLES
    String className;
    String classNumber;
    Student[] students;

    //CONSTRUCTOR
    public Class(String className, String classNumber, boolean doesExist)
    {
        if(!doesExist)
        {
            this.className = className;
            this.classNumber = classNumber;
            students = Enrollment.enterStudentData(this.className, this.classNumber);
        }else
        {
            this.className = className;
            this.classNumber = classNumber;
        }
    }

    //USER DEFINED METHODS
    //GETTERS AND SETTERS
    public Student[] getStudents()
    {
        return students;
    }

    public void setStudents(Student[] students)
    {
        this.students = students;

        //DEBUG: Loop shows all students in Student[] students array assigned to class
        /*
        for(Student student: this.students)
        {
            if(student != null) {
                System.out.println(" setStudents() | " + student.getLastName());
            }
        }
        */
    }

    public String getClassName()
    {
        return className;
    }
    public String getClassNumber()
    {
        return classNumber;
    }
}

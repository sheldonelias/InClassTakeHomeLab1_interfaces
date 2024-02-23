package Controller;

import Model.Student;

//Example of helper class
public class ResponseTally
{
    Student student;
    int answers = 0, passes = 0;

    public Student getStudent()
    {
        return student;
    }

    public int getAnswers()
    {
        return answers;
    }
    public int getPasses()
    {
        return passes;
    }
}

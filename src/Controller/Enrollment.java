package Controller;

import java.util.InputMismatchException;
import java.util.Scanner;
import Model.Student;

public class Enrollment
{
    //Creates an array of students for a class
    public static Student[] enterStudentData(String className, String classNumber)
    {
        //Creating user input captures
        Scanner scnr = new Scanner(System.in);

        Student[] students = new Student[0];
        int studentCount = -1;

        while(true)
        {
            try
            {
                //Getting the count of students in a class
                System.out.print("Enter total enrolled students: ");
                studentCount = scnr.nextInt();
                break;

            } catch (InputMismatchException excpt)
            {
                //Error message
                System.out.println("You entered an invalid value: " + excpt.getMessage());
                System.out.println("Try again.");

                //Resets scnr
                scnr.reset();
                scnr.next();
            }
        }


        //Instantiating array to hold all student names
        students = new Student[studentCount];
        //Building the array of full student places
        for(int i = 0; i < students.length; ++i)
        {
            //Instantiating new students data keyed in and not loading from a file
            students[i] = new Student(false, String.valueOf (i+1));
            System.out.println("- - - - -");
        }

        printStudentData(students, className, classNumber);

        return students;
    }

    static void  printStudentData(Student[] students, String className, String classNumber)
    {

        System.out.println("You enrolled the following students in: \n" +
                classNumber + ": " + className);
        //Prints the data of all students in one given array
        for(Student student: students)
        {
            System.out.println(
                    student.getStudentID() + " " +
                    student.getFirstName() + " " +
                    student.getLastName()
                    );
        }
        System.out.println("- - - - -");
    }
}

package Model;

//IO Classes
import java.util.InputMismatchException;
import java.util.Scanner;

public class Student
{
    private String firstName;
    private String lastName;
    private String studentID;

    public Student(boolean isLoading, String studentID)
    {
        if(!isLoading)
        {
            setStudentID(studentID);
            System.out.println("- - - - -");
            System.out.println("Enter data for student " + studentID);
            setFirstNameByKeyIn();
            setLastNameByKeyIn();
        }

    }

    public Student(String firstName, String lastName, String studentID)
    {
        setFirstName(firstName);
        setLastName(lastName);
        setStudentID(studentID);

    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setStudentID(String studentID)
    {
        this.studentID = studentID;
    }

    public void setFirstNameByKeyIn()
    {
        System.out.print("Enter first name: ");
        Scanner scnr = new Scanner(System.in);
        firstName = scnr.nextLine();
    }

    public void setLastNameByKeyIn()
    {
        System.out.print("Enter last name: ");
        Scanner scnr = new Scanner(System.in);
        lastName = scnr.nextLine();
    }

    public void setStudentIDByKeyIn()
    {
        System.out.print("Enter student id: ");
        Scanner scnr = new Scanner(System.in);

        while(true)
        {
            try
            {
                studentID = scnr.nextLine();
                break;

            }catch (InputMismatchException excpt)
            {
                System.out.println("Invalid input. Try again.");

                scnr.reset();
                scnr.next();
            }
        }



    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getStudentID()
    {
        return studentID;
    }
}

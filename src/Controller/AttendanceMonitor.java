package Controller;

//IO classes
import java.util.InputMismatchException;
import java.util.Scanner;

//Generics classes
import java.util.ArrayList;

//Package classes
import Model.Class;
import Model.Student;
import View.AttendanceReporter;
import Program.Program;

//Date and Time classes
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AttendanceMonitor
{
    //Model members
    static Class monitoredClass;
    static ArrayList<Student> attendingStudents = new ArrayList<Student>();

    //Date and Time members
    static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd 'at' HH:mm:ss");
    static String dateTimeString;

    public static void selectClass()
    {
        System.out.println("Choose a class to take attendance.");

        for(int i = 0; i < Program.getClasses().length; i++)
        {
            System.out.println("Enter: " + (i+1) + " for " + Program.getClasses()[i].getClassName());
        }

        try
        {
            Scanner scnr = new Scanner(System.in);

            int userInput;

            while(true)
            {
                try
                {
                    userInput = scnr.nextInt();
                    break;
                }catch(InputMismatchException excpt)
                {
                    //Error message
                    System.out.println("You entered an invalid value: " + excpt.getMessage());
                    System.out.println("Try again.");

                    //Resets scnr
                    scnr.reset();
                    scnr.next();
                }
            }
            //Must subtract 1 from userInput because arrays are zero-indexed (start at zero)
            monitoredClass = Program.getClasses()[userInput-1];
            takeAttendance(monitoredClass);

        }catch(InputMismatchException excpt)
        {
            System.out.println("Invalid entry. Try again. " + excpt.getMessage());
            System.out.println("Try again.");
        }
    }

    public static void takeAttendance(Class monitoredClass)
    {
        //Clearing attendance from previous session.
        attendingStudents.clear();

        dateTimeString = dtf1.format(LocalDateTime.now());

        Scanner scnr = new Scanner(System.in);

        for(Student student: monitoredClass.getStudents())
        {
            if(student != null) {
                System.out.println(String.format("Is %s %s present? (Y)es, (N)o, E(x)it",
                        student.getFirstName(), student.getLastName()));
                String userInput = scnr.next();
                switch (userInput) {
                    case "y":
                    case "Y":
                        attendingStudents.add(student);
                        break;
                    case "n":
                    case "N":
                        System.out.println(String.format("%s %s present is not present.",
                                student.getFirstName(), student.getLastName()));
                        break;
                    case "x":
                    case "X": System.out.println("You selected Exit.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid entry. Try again.");
                        takeAttendance(monitoredClass);
                        break;
                }
            }
        }

        AttendanceReporter.printAttendance(attendingStudents,
                monitoredClass.getClassNumber(), monitoredClass.getClassName());
    }
}

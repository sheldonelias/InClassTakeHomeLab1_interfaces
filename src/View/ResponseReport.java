package View;

import Controller.ResponseTally;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ResponseReport
{
    //Date and Time
    static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd 'at' HH:mm:ss");
    static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    static String dateTimeString;
    static String dateStringFileNaming;

    public static void exportResponseTally(String classNum, String classNam, ResponseTally[] responseTallies) throws IOException
    {
        //Set date at instantiation. We will learn to get a real date
        dateTimeString = dtf1.format(LocalDateTime.now());
        dateStringFileNaming = dtf2.format(LocalDate.now());

        try
        {
            //Use file output class and instantiate with file name and path as arg
            String outputFile = String.format("src/Response_%s_%s_%s.txt",
                    classNum, classNam, dateStringFileNaming);
            FileOutputStream fis = new FileOutputStream(outputFile);
            //Instantiate a printwriter
            PrintWriter pw = new PrintWriter(fis);

            //DEBUG: Check print to console that className is working
            //System.out.println(classNumber + ": " + className + " " + dateTimeString);
            //Write to file first line of className and date
            pw.println("RANDOM STUDENT PICKER RESPONSE REPORT");
            pw.println(classNum + ": " + classNam + " " + dateTimeString);

            //Loop through student list to print every student in array
            for(int i = 0; i < responseTallies.length; ++i)
            {
                if(responseTallies[i] != null)
                {
                    String line =
                            "Answers: " + responseTallies[i].getAnswers()  + " " +
                                    "Passes: " +              responseTallies[i].getPasses() +
                                    " " + responseTallies[i].getStudent().getFirstName() +
                                    " " + responseTallies[i].getStudent().getLastName();

                    //DEBUG: Check print to console that student info is working
                    //System.out.println( line);

                    pw.println(line);
                }
            }

            //Must flush and close, kind of like airplane toilets all PrintWriter objects
            pw.flush();
            pw.close();


        }catch(IOException excpt)
        {
            System.out.println("Output file inaccessible. " + excpt.getMessage());
        }
    }
}

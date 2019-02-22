package first.auto;


import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ReadTxtFile  {


String filename = "C:\\aData\\abc.txt";

    @Test
    public void ReadTxtFile() throws FileNotFoundException {

        Scanner in = new Scanner(new File(filename));
        while (in.hasNextLine())
        {
            String temp = in.nextLine();
            int f = temp.indexOf(':');
            int x = temp.length();

            String temp2 = temp.substring(f+1,x);
            String temp3 = temp2.replace('"', ' ');
            temp3 = temp3.replace(',',' ');
            temp3=temp3.trim();
            System.out.println(temp3);

        }


    }
}



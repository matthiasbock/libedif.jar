package controller;

import java.io.*;

/**
 * Open EDIF netlist file from Lattice Semiconductor's iCEcube2,
 * parse it and export as JSON
 */
public class Main
{
	public static void main(String[] args)
	{
	    String content = readFile("example.edf", false);
	    System.out.println(content);
	}
	
	/**
	 * Opens the file with the given name, reads in all lines from it
	 * and returns the whole content as a String (with line separators).
	 *  
	 * @param filename: Name of file to read
	 * @return: File content
	 */
	public static String readFile(String filename, boolean includeEmptyLines)
	{
        // open EDIF file
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // read EDIF to String
        String content = "";

        String line;
        try
        {
            // read line after line from file
            line = br.readLine();
            while (line != null)
            {
                // decide whether to include the line or not
                if (line.trim().length() > 0 || includeEmptyLines)
                {
                    content += line;
                    content += System.lineSeparator();
                }
                line = br.readLine();
            }
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // close EDIF file
        try
        {
            br.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // return the read lines
        return content;
	}
}

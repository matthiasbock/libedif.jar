package net.matthiasbock.libedif.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.matthiasbock.libedif.model.parser.EdifElement;

/**
 * Open EDIF netlist file from Lattice Semiconductor's iCEcube2,
 * parse it and export as JSON
 */
public class Main
{
	public static void main(String[] args)
	{
	    String content = readFile("example.edf", false);
	    if (content == null)
	        return;
	    
	    EdifElement edif = new EdifElement(content);
	    String json = edif.toJson();
	    System.out.println(json);
	    writeFile("example.json", json);
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
        }
        catch (FileNotFoundException e)
        {
            System.out.printf("File not found: %s\n", filename);
            return null;
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
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // return the read lines
        return content;
	}
	
	/**
	 * Write string to file
	 */
	public static void writeFile(String filename, String content)
	{
	    // open file for writing
	    BufferedWriter writer = null;
	    try
	    {
	        writer = new BufferedWriter(new FileWriter(filename));
	        writer.write(content);
	    }
	    catch ( IOException e)
	    {
            e.printStackTrace();
	    }

	    // close file
	    try
        {
            writer.close();
        }
        catch ( IOException e)
        {
            e.printStackTrace();
        }
	}
}

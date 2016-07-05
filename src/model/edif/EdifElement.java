package model.edif;

import java.util.*;

/**
 * Elements, of which an EDIF file consists
 */
public class EdifElement
{
    private String name = null;
    private List<String> attributes = new ArrayList<>();
    private List<EdifElement> subElements = new ArrayList<>();
    
    /**
     * Initialize an EDIF element
     * Parse element properties from String
     * 
     * @param s: String to parse EDIF from 
     */
    public EdifElement(String s)
    {
        // find first opening bracket
        int indexOpen = s.indexOf('(');
        
        // find corresponding closing bracket
        int indexClose = -1;
        int numOpenBrackets = 1;
        byte[] sourceBytes = s.getBytes();
        for (int i=indexOpen+1; i<s.length(); i++)
        {
            if (sourceBytes[i] == '(')
            {
                // a sub-element bracket was openend
                numOpenBrackets++;
            }
            else if (sourceBytes[i] == ')')
            {
                // a bracket was closed
                numOpenBrackets--;
                if (numOpenBrackets == 0)
                {
                    // the initial opening bracket is closed now
                    indexClose = i;
                    break;
                }
            }
        }

        // closing bracket was not found
        if (indexClose < 0)
        {
            System.out.println("EDIF Element parsing failed: Closing bracket not found");
            return;
        }
        
        // parse element without brackets
        s = s.substring(indexOpen+1, indexClose-1);
        sourceBytes = s.getBytes();
        int indexPreviousSeparator = -1;
        boolean previousByteWasSeparator = false;
        for (int currentIndex=0; currentIndex<s.length(); currentIndex++)
        {
            if (isSeparator(sourceBytes[currentIndex]))
            {
                if (previousByteWasSeparator)
                {
                    // disregard multiple separators in a row
                }
                else
                {
                    if (getName() == null)
                    {
                        // the element name wasn't set yet
                        System.out.printf("currentIndex: %d\n", currentIndex);
                        setName(s.substring(indexPreviousSeparator+1, currentIndex));
                    }
                }
                
                indexPreviousSeparator = currentIndex;
                previousByteWasSeparator = true;
            }
            else
            {
                previousByteWasSeparator = false;
            }
        }
    }
    
    /**
     * Check, if a given char is a separator
     */
    public static boolean isSeparator(byte b)
    {
        return b == ' ' || b == '\t' || b == '\n' || b == '\r';
    }
    
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        System.out.println("Element name: \""+name+"\"");
    }

    public List<String> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(List<String> attributes)
    {
        this.attributes = attributes;
    }

    public List<EdifElement> getSubElements()
    {
        return subElements;
    }

    public void setSubElements(List<EdifElement> subElements)
    {
        this.subElements = subElements;
    }
}

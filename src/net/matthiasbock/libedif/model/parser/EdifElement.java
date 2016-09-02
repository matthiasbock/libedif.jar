package net.matthiasbock.libedif.model.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Elements, of which an EDIF file consists
 */
public class EdifElement
{
    private String name = null;
    private int hierarchyLevel;
    private List<String> attributes = new ArrayList<>();
    private List<EdifElement> subElements = new ArrayList<>();
    
    private String identation = null;
    
    /**
     * Initialize an EDIF element
     * Parse element properties from String
     * 
     * @param s: String to parse EDIF from 
     */
    public EdifElement(String s, int hierachyLevel)
    {
        setHierarchyLevel(hierarchyLevel);
        
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
        
        boolean insideSubElement = false;
        // index of the sub-element's opening bracket
        int indexSubElementBegin = 0;

        for (int currentIndex=0; currentIndex<s.length(); currentIndex++)
        {
            if (insideSubElement)
            {
                if (isClosingBracket(sourceBytes[currentIndex]))
                {
                    numOpenBrackets--;
                    
                    // was it the last open bracket?
                    if (numOpenBrackets == 0)
                    {
                        // the sub-element ends here
                        // including the byte at currentIndex => currentIndex+1
                        String subElement = s.substring(indexSubElementBegin, currentIndex+1);

                        EdifElement e = new EdifElement(subElement, hierachyLevel+1);
                        addSubElement(e);
                        //System.out.printf("\tNew sub-element: %s\n", e.getName());
                        
                        // treat the closing bracket as a separator
                        insideSubElement = false;
                        previousByteWasSeparator = true;
                        indexPreviousSeparator = currentIndex;
                    }
                }
                else if (isOpeningBracket(sourceBytes[currentIndex]))
                {
                    // the sub-element has sub-elements of it's own
                    // disregard them, they will be parsed later
                    numOpenBrackets++;
                }
            }
            else
            {
                if (isSeparator(sourceBytes[currentIndex]))
                {
                    if (previousByteWasSeparator)
                    {
                        // disregard multiple separators in a row
                    }
                    else
                    {
                        // interpret the string between the last separator and this separator
                        String attribute = s.substring(indexPreviousSeparator+1, currentIndex).trim();

                        // disregard empty spaces after sub-elements' closing brackets
                        if (attribute.length() > 0)
                        {
                            if (getName() == null)
                            {
                                // the element name wasn't set yet
                                setName(attribute);
                            }
                            else
                            {
                                // if it's not the name, it's an attribute
                                addAttribute(attribute);
                            }
                        }
                    }
                    
                    indexPreviousSeparator = currentIndex;
                    previousByteWasSeparator = true;
                }
                else
                {
                    previousByteWasSeparator = false;
                    
                    if (isOpeningBracket(sourceBytes[currentIndex]))
                    {
                        // a sub-element begins
                        insideSubElement = true;
                        indexSubElementBegin = currentIndex;
                        numOpenBrackets = 1;
                    }
                }
            }
        }
    }
    
    /**
     * If the constructor is invoked without hierarchy argument,
     * then top level is assumed.
     * 
     * @param s: EDIF string to be parsed
     */
    public EdifElement(String s)
    {
        this(s, 0);
    }

    /**
     * Check, if a given char is a separator
     */
    public static boolean isSeparator(byte b)
    {
        return b == ' ' || b == '\t' || b == '\n' || b == '\r';
    }
    
    public static boolean isOpeningBracket(byte b)
    {
        return b == '(';
    }
    
    public static boolean isClosingBracket(byte b)
    {
        return b == ')';
    }
    
    public static String getIdentationVariant()
    {
        return "\t";
    }
    
    /**
     * Export this element and it's subelements to JSON 
     * 
     * @return: String
     */
    public String toJson()
    {
        String s = getIdentation() + "{" + System.lineSeparator();

        if (getAttributes().size() > 0)
            s += getIdentation() + getIdentationVariant() + "\"attributes\": [" + String.join(", ", getAttributes(true)) + "]," + System.lineSeparator();
        
        for (EdifElement subElement : getSubElements())
        {
            s += getIdentation() + getIdentationVariant() + "\"" + subElement.getName() + "\": " + System.lineSeparator();
            s += subElement.toJson();
        }
        
        s += getIdentation() + "}," + System.lineSeparator();
        return s;
    }
    
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        //System.out.println("Element name: \""+name+"\"");
        this.name = name;
    }

    public int getHierarchyLevel()
    {
        return hierarchyLevel;
    }
    
    public void setHierarchyLevel(int level)
    {
        hierarchyLevel = level;
    }
    
    public String getIdentation()
    {
        if (identation != null)
            return identation;
        
        identation = "";
        for (int i=0; i<getHierarchyLevel(); i++)
            identation += getIdentationVariant();
        return identation;
    }
    
    public List<String> getAttributes()
    {
        return attributes;
    }
    
    public List<String> getAttributes(boolean quoted)
    {
        if (!quoted)
            return getAttributes();
        
        List<String> l = new ArrayList<>();
        for (String s : getAttributes())
        {
            l.add("\"" + s + "\"");
        }
        return l;
    }
    
    public boolean addAttribute(String s)
    {
        //System.out.printf("\tNew attribute: %s\n", s);
        return attributes.add(s);
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
    
    public boolean addSubElement(EdifElement e)
    {
        return subElements.add(e);
    }
}

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
     * Parse an EDIF from String
     * 
     * @param s: EDIF string to be parsed
     */
    public EdifElement(String s)
    {
        this(s, 0);
    }
    
    /**
     * Initialize an EDIF element,
     * parse element properties from String
     * and insert at specified hierarchy level
     * 
     * @param s: String from which to parse EDIF objects
     */
    public EdifElement(String s, int level)
    {
        setHierarchyLevel(level);
        
        // find first opening bracket
        int indexOpen = s.indexOf('(');
        
        // find corresponding closing bracket
        int indexClose = indexOfClosingBracket(s, indexOpen);

        // closing bracket was not found
        if (indexClose < 0)
        {
            System.out.println("EDIF Element parsing failed: Closing bracket not found");
            return;
        }
        
        // parse element without brackets
        String content = s.substring(indexOpen+1, indexClose);
        //System.out.printf("Begin: %d, End: %d, String: %s\n", indexOpen, indexClose, content);
        parseBracketEnclosedString(content);
    }

    /**
     * 
     * @param content
     */
    private void parseBracketEnclosedString(String content)
    {
        int indexPreviousWhitespaceCharacter = -1;
        boolean previousCharacterWasWhitespace = false;

        int numOpenBrackets = 0;
        boolean insideSubElement = false;
        // index of the sub-element's opening bracket
        int indexSubElementBegin = 0;
        
        for (int currentIndex=0; currentIndex < content.length(); currentIndex++)
        {
            char c = content.charAt(currentIndex);
            //System.out.printf("Char: %c, ", c);

            if (insideSubElement)
            {
                // possibly end of sub-element reached
                if (isClosingBracket(c))
                {
                    numOpenBrackets--;

                    // was it the last open bracket?
                    if (numOpenBrackets == 0)
                    {
                        // the sub-element ends here
                        // including the byte at currentIndex => currentIndex+1
                        String subElement = content.substring(indexSubElementBegin, currentIndex+1);

                        // parse recursively
                        addSubElement( new EdifElement(subElement, getHierarchyLevel()+1) );
                        
                        // treat the closing bracket as a separator
                        insideSubElement = false;
                        previousCharacterWasWhitespace = true;
                        indexPreviousWhitespaceCharacter = currentIndex;
                    }
                }
                else if (isOpeningBracket(c))
                {
                    // the sub-element has sub-elements of it's own
                    // disregard them, they will be parsed below the EdifElement constructor,
                    // which is invoked at the closing bracket of the current sub-element
                    numOpenBrackets++;
                }
            }
            else
            {
                if (isWhitespace(c) && previousCharacterWasWhitespace)
                {
                    // disregard multiple whitespace characters in a row
                }
                else if (isOpeningBracket(c))
                {
                    // a sub-element begins here
                    numOpenBrackets = 1;
                    insideSubElement = true;
                    indexSubElementBegin = currentIndex;
                }
                else if (isWhitespace(c) || currentIndex == content.length()-1)
                {
                    String attribute;

                    // is this the last character of the content string?
                    if (currentIndex == content.length()-1)
                    {
                        // extract string since last whitespace character
                        attribute = content.substring(indexPreviousWhitespaceCharacter+1, currentIndex+1).trim();
                    }
                    else
                    {
                        // extract string between the last and the current whitespace character
                        attribute = content.substring(indexPreviousWhitespaceCharacter+1, currentIndex).trim();
                    }
                    //System.out.println("Attribute: "+attribute);

                    // disregard empty spaces after the sub-element's closing brackets
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

                // remember, whether the current character was whitespace or not
                if (isWhitespace(c))
                {
                    previousCharacterWasWhitespace = true;
                    indexPreviousWhitespaceCharacter = currentIndex;
                }
                else
                {
                    previousCharacterWasWhitespace = false;
                }
            }
        }
    }


    /**
     * Find the index of the corresponding closing bracket
     * 
     * @param s: Haystack
     * @param indexOpen: Index of opening bracket, for which the closing bracket shall be found
     * @return: Index of closing bracket
     */
    protected static int indexOfClosingBracket(String s, int indexOpen)
    {
        // -1 = closing bracket not found
        int indexClose = -1;
        
        // keep count of opened brackets 
        int numOpenBrackets = 1;
        
        // search for closing bracket
        byte[] sourceBytes = s.getBytes();
        for (int i=indexOpen+1; i<s.length(); i++)
        {
            if (isOpeningBracket(sourceBytes[i]))
            {
                // a sub-element bracket was openend
                numOpenBrackets++;
            }
            else if (isClosingBracket(sourceBytes[i]))
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
        
        return indexClose;
    }

    protected static boolean isWhitespace(byte b)
    {
        return b == ' ' || b == '\t' || b == '\n' || b == '\r';
    }

    protected static boolean isWhitespace(char c)
    {
        return isWhitespace((byte) c);
    }

    protected static boolean isOpeningBracket(byte b)
    {
        return b == '(';
    }

    protected static boolean isOpeningBracket(char c)
    {
        return isOpeningBracket((byte) c);
    }

    protected static boolean isClosingBracket(byte b)
    {
        return b == ')';
    }

    protected static boolean isClosingBracket(char c)
    {
        return isClosingBracket((byte) c);
    }

    protected static String getIdentationCharacter()
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
        // has neither sub-elements, nor attributes: empty
        String s = "\"\"," + System.lineSeparator();
        
        // has sub-elements: export as dictionary
        if (getSubElements().size() > 0)
        {
            // begin JSON element
            s = System.lineSeparator() + getIdentationString() + "{" + System.lineSeparator();

            // append more than attribute
            if (getAttributes().size() > 1)
            {
                s += getIdentationString() + getIdentationCharacter() + "\"attributes\": [" + String.join(", ", getAttributes(true)) + "]," + System.lineSeparator();
            }
            // append one attribute
            else if (getAttributes().size() == 1)
            {
                s += getIdentationString() + getIdentationCharacter() + "\"attribute\": " + getAttributes(true).get(0) + "," + System.lineSeparator();
            }

            // append sub-elements
            for (EdifElement subElement : getSubElements())
            {
                s += getIdentationString() + getIdentationCharacter() + "\"" + subElement.getName() + "\": ";
                s += subElement.toJson();
            }
            
            // end JSON element
            s += getIdentationString() + "}," + System.lineSeparator();
        }

        // has no sub-elements, but several attributes: export as array
        else if (getAttributes().size() > 1)
        {
            // begin array
            s = "[";

            // append attributes
            if (getAttributes().size() > 0)
            {
                s += String.join(", ", getAttributes(true));
            }

            // end array
            s += "]," + System.lineSeparator();
        }

        // has no sub-elements, but one attribute: export as string
        else if (getAttributes().size() == 1)
        {
            s = getAttributes(true).get(0) + "," + System.lineSeparator();
        }

        return s;
    }

    
    /*
     * Getter and Setter
     */

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
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
    
    public String getIdentationString()
    {
        if (identation != null)
            return identation;
        
        identation = "";
        for (int i=0; i<getHierarchyLevel(); i++)
            identation += getIdentationCharacter();
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

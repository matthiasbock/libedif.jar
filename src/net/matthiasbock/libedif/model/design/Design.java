package net.matthiasbock.libedif.model.design;

import net.matthiasbock.libedif.model.parser.EdifElement;

/**
 * Implements a design work for an FPGA
 * including placing and routing information
 */
public class Design
{
    private String name = null;
    private String part = null;

    private Library libraryRef = null;
    private Cell cellRef = null;

    public Design(EdifElement edif)
    {
        name = edif.getSubElementByName("rename").getFirstAttribute();
        System.out.println("Design name: "+name);

        part = edif.getSubElementByAttribute("property", "PART").getSubElementByName("string").getFirstAttribute();
        System.out.println("Target FPGA: "+part);

        EdifElement c = edif.getSubElementByName("cellRef");
        String libraryRefName = c.getSubElementByName("libraryRef").getFirstAttribute();
        String cellRefName = c.getSubElementByName("rename").getFirstAttribute();
        System.out.printf("Implementation is stored in library \"%s\", cell \"%s\"\n ", libraryRefName, cellRefName);
    }
}

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

    public Design(EdifElement subElementByName)
    {
        // TODO Auto-generated constructor stub
    }
}

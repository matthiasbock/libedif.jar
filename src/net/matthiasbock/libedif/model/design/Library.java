package net.matthiasbock.libedif.model.design;

import java.util.*;

import net.matthiasbock.libedif.model.parser.EdifElement;

/**
 * Implements a library,
 * which can either be a design library defining the available logical cell types
 * or a work library with instantiations of such cells,
 * including netlist and placement on the FPGA
 */
public class Library
{
    private String name = null;
    private List<Cell> cells = null;

    public Library(EdifElement e)
    {
        // TODO Auto-generated constructor stub
    }
}

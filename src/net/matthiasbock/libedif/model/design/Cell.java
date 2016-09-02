package net.matthiasbock.libedif.model.design;

import java.util.*;

/**
 * Implements one logical cell,
 * as they are defined in the design library
 * and used (instantiated) in the work
 */
public class Cell
{
    private String name = null;
    private String cellType = null;
    private List<Port> ports = null; // "interface"
    private List<Net> nets = null; // "contents"
}

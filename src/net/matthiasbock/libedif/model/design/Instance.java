package net.matthiasbock.libedif.model.design;

import java.util.HashMap;

/**
 * Implements an instance of a design library cell
 */
public class Instance
{
    String name = null;
    Library libraryRef = null;
    Cell cellRef = null;
    HashMap<String, String> properties = null;
}

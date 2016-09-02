package net.matthiasbock.libedif.model.parser;

import java.util.*;

import net.matthiasbock.libedif.model.design.*;

/**
 * Handles EDIF files, which represent an FPGA design
 * as they are created by the iCEcube/SBT Place & Route process
 */
public class LatticeEdif
{
    Design design = null;
    List<Library> libraries = null;

    /**
     * Construct LatticeEdif by nominating design and libraries
     */
    public LatticeEdif(EdifElement edif)
    {
        design = new Design( edif.getSubElementByName("design") );

        libraries = new ArrayList<>();
        for (EdifElement e : edif.getSubElementsByName("library"))
        {
            libraries.add( new Library(e) );
        }
    }
}

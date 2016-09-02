package net.matthiasbock.libedif.model.design;

import java.util.*;

import net.matthiasbock.libedif.model.parser.EdifElement;

/**
 * Implements an instance of a design library cell
 */
public class Instance
{
    String name = null;
    String libraryRef = null;
    String cellRef = null;
    HashMap<String, String> properties = null;

    public Instance(EdifElement edifInstance)
    {
        name = edifInstance.getSubElementByName("rename").getFirstAttribute();

        EdifElement c = edifInstance.getSubElementByName("cellRef");
        if (c != null)
        {
            libraryRef = c.getFirstAttribute();
            cellRef = c.getSubElementByName("libraryRef").getFirstAttribute();
        }
        
        properties = new HashMap<>();
        for (EdifElement edifProperty : edifInstance.getSubElementsByName("property"))
        {
            properties.put( edifProperty.getFirstAttribute(), edifProperty.getSubElementByName("string").getFirstAttribute() );
        }
    }
}

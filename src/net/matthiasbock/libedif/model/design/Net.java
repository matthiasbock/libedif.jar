package net.matthiasbock.libedif.model.design;

import java.util.*;

import net.matthiasbock.libedif.model.parser.EdifElement;

/**
 * Implements networks, as they are used in a work library,
 * defining, which cell ports are connected to each other  
 */
public class Net
{
    private String name = null;
    private List<PortRef> portRefs = null;

    public Net(EdifElement edifNet)
    {
        name = edifNet.getSubElementByName("rename").getFirstAttribute();

        portRefs = new ArrayList<>();
        for (EdifElement edifPortRef : edifNet.getSubElementByName("joined").getSubElementsByName("portRef"))
        {
            portRefs.add( new PortRef(edifPortRef) );
        }

        System.out.printf("\t\tNet \"%s\" connects %d ports: ", name, portRefs.size());
        for (PortRef p : portRefs)
        {
            System.out.printf("%s.%s, ", p.getInstanceRef(), p.getPortRef());
        }
        System.out.println();
    }
}

package net.matthiasbock.libedif.model.design;

import java.util.*;

import net.matthiasbock.libedif.model.parser.EdifElement;

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
    private List<Instance> instances = null;
    private List<Net> nets = null; // "contents"

    public Cell(EdifElement edifCell)
    {
        name = edifCell.getSubElementByName("rename").getFirstAttribute();

        cellType = edifCell.getSubElementByName("cellType").getFirstAttribute();
        
        ports = new ArrayList<>();
        EdifElement i = edifCell.getSubElementByName("view").getSubElementByName("interface");
        if (i != null)
        {
            for (EdifElement edifPort : i.getSubElementsByName("port"))
            {
                ports.add( new Port(edifPort) );
            }
        }

        instances = new ArrayList<>();
        nets = new ArrayList<>();   
        EdifElement c = edifCell.getSubElementByName("view").getSubElementByName("contents");
        if (c != null)
        {
            for (EdifElement edifInstance : c.getSubElementsByName("instance"))
            {
                instances.add( new Instance(edifInstance) );
            }

            for (EdifElement edifNet : c.getSubElementsByName("net"))
            {
                nets.add( new Net(edifNet) );
            }
        }

        System.out.printf("\tCell \"%s\": type \"%s\", %d port(s), %d net(s)\n", name, cellType, ports.size(), nets.size());
    }
}

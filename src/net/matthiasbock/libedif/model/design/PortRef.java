package net.matthiasbock.libedif.model.design;

import net.matthiasbock.libedif.model.parser.EdifElement;

/**
 * Implements port references,
 * as they are used in netlists to declare,
 * which cell ports are connected to each other
 */
public class PortRef
{
    private String instanceRef = null;
    private String portRef = null;

    public PortRef(EdifElement edifPortRef)
    {
        portRef = edifPortRef.getFirstAttribute();
        
        EdifElement i = edifPortRef.getSubElementByName("instanceRef");
        if (i != null)
        {
            instanceRef = i.getFirstAttribute();
        }
        else
        {
            instanceRef = "";
        }
    }

    public String getInstanceRef()
    {
        return instanceRef;
    }

    public void setInstanceRef(String instanceRef)
    {
        this.instanceRef = instanceRef;
    }

    public String getPortRef()
    {
        return portRef;
    }

    public void setPortRef(String portRef)
    {
        this.portRef = portRef;
    }
}

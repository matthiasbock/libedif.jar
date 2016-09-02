package net.matthiasbock.libedif.model.design;

import net.matthiasbock.libedif.model.parser.EdifElement;

/**
 * Implements ports as they are port of every cell
 */
public class Port
{
    private String name = null;
    private PortDirection direction = null;

    public Port(EdifElement edifPort)
    {
        name = edifPort.getFirstAttribute();

        String d = edifPort.getSubElementByName("direction").getFirstAttribute();
        switch (d)
        {
            case "INPUT":
                direction = PortDirection.INPUT;
                break;

            case "OUTPUT":
                direction = PortDirection.OUTPUT;
                break;

            case "INOUT":
                direction = PortDirection.INOUT;
                break;

            default:
                direction = null;
        }
    }
}

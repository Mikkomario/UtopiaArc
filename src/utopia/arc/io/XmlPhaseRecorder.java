package utopia.arc.io;

import java.io.OutputStream;
import java.util.Collection;

import utopia.arc.generics.ArcDataType;
import utopia.arc.resource.Phase;
import utopia.flow.generics.ValueList;

/**
 * This static class may be used for reading and writing phases into xml data
 * @author Mikko Hilpinen
 * @since 14.5.2016
 */
public class XmlPhaseRecorder
{
	// CONSTRUCTOR	---------------
	
	private XmlPhaseRecorder()
	{
		// Static interface
	}

	
	// OTHER METHODS	----------
	
	public static void writePhases(Collection<? extends Phase> phases, OutputStream targetStream)
	{
		// Wraps the phases into a list, then writes it
		ValueList list = new ValueList(ArcDataType.PHASE);
	}
}

package utopia.arc.io;

import java.io.OutputStream;
import java.util.Collection;

import utopia.arc.resource.Phase;

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
		
	}
}

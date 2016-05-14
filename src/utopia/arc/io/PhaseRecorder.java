package utopia.arc.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import utopia.arc.generics.ArcDataType;
import utopia.arc.resource.Phase;
import utopia.flow.generics.DataTypeException;
import utopia.flow.generics.Value;
import utopia.flow.generics.ValueList;
import utopia.flow.io.XmlElementReader;
import utopia.flow.io.XmlElementReader.ElementParseException;
import utopia.flow.io.XmlElementWriter;
import utopia.flow.structure.Element;
import utopia.flow.structure.TreeNode;

/**
 * This static class may be used for reading and writing phases into xml data
 * @author Mikko Hilpinen
 * @since 14.5.2016
 */
public class PhaseRecorder
{
	// CONSTRUCTOR	---------------
	
	private PhaseRecorder()
	{
		// Static interface
	}

	
	// OTHER METHODS	----------
	
	/**
	 * Writes the provided phases into an xml stream
	 * @param phases The phases that are written
	 * @param targetStream The stream the elements are written into
	 * @param encodeElementContents Should the element contents be encoded in UTF-8
	 * @throws XMLStreamException If the writing failed
	 */
	public static void writePhasesWithXml(Collection<? extends Phase> phases, 
			OutputStream targetStream, boolean encodeElementContents) throws XMLStreamException
	{
		XmlElementWriter.writeElementIntoStream(new TreeNode<>(phasesToElement(phases)), 
				targetStream, encodeElementContents);
	}
	
	/**
	 * Writes the provided phases into an xml file
	 * @param phases The phases that are written
	 * @param targetFile The file the phases are written into
	 * @param encodeElementContents Should the element contents be encoded in UTF-8
	 * @throws IOException If the file couldn't be opened or closed
	 * @throws XMLStreamException If the writing failed
	 */
	public static void writePhasesWithXml(Collection<? extends Phase> phases, File targetFile, 
			boolean encodeElementContents) throws IOException, XMLStreamException
	{
		XmlElementWriter.writeElementIntoFile(new TreeNode<>(phasesToElement(phases)), 
				targetFile, encodeElementContents);
	}
	
	/**
	 * Reads phases from an xml stream written with {@link #writePhasesWithXml(Collection, OutputStream, boolean)}
	 * @param stream The stream the phases are read from
	 * @param decodeElementContents Are the element contents encoded in UTF-8
	 * @return The phases parsed from the stream
	 * @throws XMLStreamException If the read failed
	 * @throws ElementParseException If the elements couldn't be properly parsed
	 */
	public static List<Phase> readPhasesFromXml(InputStream stream, 
			boolean decodeElementContents) throws XMLStreamException, ElementParseException
	{
		return elementToPhases(XmlElementReader.parseStream(stream, decodeElementContents).getContent());
	}
	
	/**
	 * Reads phases from an xml file written with {@link #writePhasesWithXml(Collection, File, boolean)}
	 * @param file The xml file the phases are read from
	 * @param decodeElementContents Are the element contents encoded in UTF-8
	 * @return The phases parsed from the file
	 * @throws IOException If the file couldn't be opened or closed
	 * @throws XMLStreamException If the read failed
	 * @throws ElementParseException If the elements couldn't be properly parsed
	 */
	public static List<Phase> readPhasesFromXml(File file, boolean decodeElementContents) 
			throws IOException, XMLStreamException, ElementParseException
	{
		return elementToPhases(XmlElementReader.parseFile(file, decodeElementContents).getContent());
	}
	
	/**
	 * Parses numerous phases into an element
	 * @param phases The phases that are parsed
	 * @return The element parsed from the phases
	 */
	public static Element phasesToElement(Collection<? extends Phase> phases)
	{
		// Wraps the phases into a list, then writes it
		ValueList list = new ValueList(ArcDataType.PHASE);
		for (Phase phase : phases)
		{
			list.add(ArcDataType.Phase(phase));
		}
		
		return new Element("phases", Value.List(list));
	}
	
	/**
	 * Parses an element into phases
	 * @param element The root element
	 * @return The phases read from the element
	 * @throws ElementParseException If the element contents couldn't be parsed into phases
	 */
	public static List<Phase> elementToPhases(Element element) throws ElementParseException
	{
		try
		{
			// Reads a list from the element
			ValueList list = element.getContent().toList();
			List<Phase> phases = new ArrayList<>();
			
			for (Value phaseValue : list)
			{
				phases.add(ArcDataType.valueToPhase(phaseValue));
			}
			
			return phases;
		}
		catch (DataTypeException e)
		{
			throw new ElementParseException("Couldn't parse element contents into phases", e);
		}
	}
}

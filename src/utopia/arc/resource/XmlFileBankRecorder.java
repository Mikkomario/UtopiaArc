package utopia.arc.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.stream.XMLStreamException;

import utopia.flow.generics.Variable;
import utopia.flow.io.XmlElementReader;
import utopia.flow.io.XmlElementReader.ElementParseException;
import utopia.flow.io.XmlElementReader.EndOfStreamReachedException;
import utopia.flow.io.XmlElementWriter;
import utopia.flow.structure.Element;
import utopia.flow.structure.TreeNode;

/**
 * This class keeps track of a bank's data in a single xml file
 * @author Mikko Hilpinen
 * @since 8.5.2016
 */
public class XmlFileBankRecorder implements BankRecorder
{
	// ATTRIBUTES	------------------
	
	private File targetFile;
	
	
	// CONSTRUCTOR	------------------
	
	/**
	 * Creates a new bank recorder which uses the provided file to store bank data
	 * @param targetFile The file the bank data is / will be stored to
	 */
	public XmlFileBankRecorder(File targetFile)
	{
		this.targetFile = targetFile;
	}
	
	
	// IMPLEMENTED METHODS	----------

	@Override
	public void writeBank(Collection<? extends Variable> contents) throws RecordingFailedException
	{
		// Parses the content into elements first
		TreeNode<Element> root = new TreeNode<>(new Element("root"));
		for (Variable var : contents)
		{
			root.addChild(new TreeNode<>(new Element(var.getName(), var.getValue())));
		}
		
		// Writes the element data to the file
		try
		{
			XmlElementWriter.writeElementIntoFile(root, this.targetFile, true);
		}
		catch (IOException | XMLStreamException e)
		{
			throw new RecordingFailedException("Failed to save the bank data to file", e);
		}
	}

	@Override
	public Collection<Variable> readBank() throws RecordingFailedException
	{
		// If there is no file, there is no data
		if (!this.targetFile.exists())
			return new ArrayList<>();
		
		// Reads the bank data from the file
		Collection<Variable> data = new ArrayList<>();
		InputStream stream = null;
		XmlElementReader reader = null;
		try
		{
			stream = new FileInputStream(this.targetFile);
			reader = new XmlElementReader(stream, true);
			
			// Reads each element under the root element
			reader.skipToNextElement();
			while (reader.hasNext())
			{
				Element resourceElement = reader.toNextSibling();
				data.add(new Variable(resourceElement.getName(), resourceElement.getContent()));
			}
		}
		catch (EndOfStreamReachedException e)
		{
			// Ignored
		}
		catch (FileNotFoundException | XMLStreamException | ElementParseException e)
		{
			throw new RecordingFailedException("Failed to read bank data", e);
		}
		finally
		{
			if (reader != null)
				reader.closeQuietly();
			if (stream != null)
			{
				try
				{
					stream.close();
				}
				catch (IOException e)
				{
					// Ignored
				}
			}
		}
		
		return data;
	}
}

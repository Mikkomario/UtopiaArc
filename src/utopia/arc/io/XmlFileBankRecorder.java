package utopia.arc.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import utopia.arc.resource.BankRecorder;
import utopia.flow.generics.DataType;
import utopia.flow.generics.Variable;
import utopia.flow.io.FileUtils;
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
	
	private Path bankDirectory;
	
	
	// CONSTRUCTOR	------------------
	
	/**
	 * Creates a new bank recorder which uses the provided file to store bank data
	 * @param bankDirectory The directory that contains all bank data (which then is stored 
	 * in multiple separate directories and files)
	 */
	public XmlFileBankRecorder(Path bankDirectory)
	{
		this.bankDirectory = bankDirectory;
	}
	
	
	// IMPLEMENTED METHODS	----------

	@Override
	public void writeBank(String bankName, DataType bankType, 
			Collection<? extends Variable> contents) throws RecordingFailedException
	{
		// Determines the target file based on the bank content type and bank name
		File targetFile = getTargetFile(bankName, bankType);
		
		// Tries to generate the directories for the target file
		if (!targetFile.exists())
			targetFile.mkdirs();
		
		// Parses the content into elements first
		TreeNode<Element> root = new TreeNode<>(new Element(bankName));
		for (Variable var : contents)
		{
			root.addChild(new TreeNode<>(new Element(var.getName(), var.getValue())));
		}
		
		// Writes the element data to the file
		try
		{
			XmlElementWriter.writeElementIntoFile(root, targetFile, true);
		}
		catch (IOException | XMLStreamException e)
		{
			throw new RecordingFailedException("Failed to save the bank data to file", e);
		}
	}

	@Override
	public Collection<Variable> readBank(String bankName, DataType bankType) throws RecordingFailedException
	{
		// Parses the target file
		File targetFile = getTargetFile(bankName, bankType);
		
		// If there is no file, there is no data
		if (!targetFile.exists())
			return new ArrayList<>();
		
		// Reads the bank data from the file
		Collection<Variable> data = new ArrayList<>();
		InputStream stream = null;
		XmlElementReader reader = null;
		try
		{
			stream = new FileInputStream(targetFile);
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
	
	@Override
	public List<String> readBankNames(DataType resourceType) throws RecordingFailedException
	{
		Path resourcePath = this.bankDirectory.resolve(resourceType.getName());
		File resourceDirectory = resourcePath.toFile();
		
		List<String> bankNames = new ArrayList<>();
		if (resourceDirectory.exists() && resourceDirectory.isDirectory())
		{
			String[] bankFileNames = FileUtils.findFileNamesIn(resourceDirectory, "xml");
			if (bankFileNames == null)
				throw new RecordingFailedException("Couldn't read file names under " + resourcePath);
			for (String fileName : bankFileNames)
			{
				bankNames.add(fileName.substring(0, fileName.lastIndexOf('.')));
			}
		}
		
		return bankNames;
	}
	
	private File getTargetFile(String bankName, DataType bankType)
	{
		return this.bankDirectory.resolve(Paths.get(bankType.getName(), 
				bankName + ".xml")).toFile();
	}
}

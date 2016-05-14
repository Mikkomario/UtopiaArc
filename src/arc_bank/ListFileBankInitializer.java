package arc_bank;

import java.io.FileNotFoundException;

import flow_io.ListFileReader;
import genesis_event.Handled;
import utopia.arc.io.XmlFileBankRecorder;
import utopia.arc.resource.BankRecorder;

/**
 * This type of BankInitializer uses a file to read the necessary data from. The subclasses 
 * must be able to create objects based on the data.
 * 
 * @author Mikko Hilpinen
 * @since 27.11.2014
 * @param <T> The type of resource this initializer is able to produce
 * @deprecated Replaced with {@link BankRecorder} and {@link XmlFileBankRecorder}
 */
public class ListFileBankInitializer<T extends Handled> implements BankInitializer<T>
{
	// ATTRIBUTES	---------------------------------
	
	private String fileName;
	private BankObjectConstructor<T> constructor;
	
	
	// CONSTRUCTOR	---------------------------------
	
	/**
	 * Creates a new initializer.
	 * 
	 * @param fileName The file that contains the data needed in the initialization 
	 * ("data/" automatically included). "*" is used as a general comment indicator in the 
	 * file.
	 * @param constructor The constructor that can construct the objects based on commands
	 */
	public ListFileBankInitializer(String fileName, BankObjectConstructor<T> constructor)
	{
		this.fileName = fileName;
		this.constructor = constructor;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------
	
	@Override
	public void initialize(Bank<T> bank)
	{
		// Reads the instructions from a file
		try
		{
			ListFileReader reader = new ListFileReader();
			reader.readFile(this.fileName, "*");
			
			// Creates objects based on the data and adds them to the bank
			for (String line : reader.getLines())
			{
				this.constructor.construct(line, bank);
			}
		}
		catch (FileNotFoundException e)
		{
			throw new ResourceInitializationException("Couldn't find file " + this.fileName);
		}
	}
}

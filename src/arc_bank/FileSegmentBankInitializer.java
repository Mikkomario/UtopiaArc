package arc_bank;

import java.io.FileNotFoundException;
import java.util.List;

import flow_io.ModeUsingFileReader;
import genesis_event.Handled;

/**
 * These initializers read object data from a file. The file may contain other kind of data 
 * as well since this initializer only reads lines under a certain mode (= bankName)
 * 
 * @author Mikko Hilpinen
 * @since 6.12.2014
 * @param <T> The type of content initialized by this initializer
 */
public class FileSegmentBankInitializer<T extends Handled> implements BankInitializer<T>
{
	// ATTRIBUTES	-------------------------------
	
	private String fileName, acceptedMode;
	private BankObjectConstructor<T> constructor;
	
	
	// CONSTRUCTOR	-------------------------------
	
	/**
	 * Creates a new initializer
	 * @param fileName The name of the file that contains object data ("data/" 
	 * automatically included)
	 * @param acceptedMode The name of the mode that contains the object data (modes should 
	 * be marked with "&")
	 * @param constructor The constructor that is able to construct objects
	 */
	public FileSegmentBankInitializer(String fileName, String acceptedMode, 
			BankObjectConstructor<T> constructor)
	{
		// Initializes attributes
		this.fileName = fileName;
		this.acceptedMode = acceptedMode;
		this.constructor = constructor;
	}
	
	
	// IMPLEMENTED METHODS	------------------------

	@Override
	public void initialize(Bank<T> bank)
	{
		FileSegmentReader<T> reader = new FileSegmentReader<>(this.acceptedMode, 
				this.constructor, bank);
		try
		{
			reader.readFile(this.fileName, "*");
		}
		catch (FileNotFoundException e)
		{
			throw new ResourceInitializationException("Can't find file " + this.fileName);
		}
	}

	
	// SUBCLASSES	--------------------------------
	
	private static class FileSegmentReader<T extends Handled> extends ModeUsingFileReader
	{
		// ATTRIBUTES	----------------------------
		
		private String acceptedMode;
		private BankObjectConstructor<T> constructor;
		private boolean inCorrectMode;
		private Bank<T> bank;
		
		
		// CONSTRUCTOR	----------------------------
		
		public FileSegmentReader(String acceptedMode, BankObjectConstructor<T> constructor, 
				Bank<T> bank)
		{
			super("&");
			
			// Initializes attributes
			this.acceptedMode = acceptedMode;
			this.constructor = constructor;
			this.inCorrectMode = false;
			this.bank = bank;
		}
		
		
		// IMPLEMENTED METHODS	--------------------
		
		@Override
		protected void onLine(String line, List<String> modes)
		{
			if (this.inCorrectMode)
				this.constructor.construct(line, this.bank);
		}

		@Override
		protected void onMode(String newMode, List<String> modes)
		{
			this.inCorrectMode = newMode.equals(this.acceptedMode);
		}
	}
}

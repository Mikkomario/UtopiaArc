package arc_bank;

import java.io.FileNotFoundException;
import java.util.List;

import flow_io.ModeUsingFileReader;
import genesis_event.Handled;

/**
 * This initializer can be used in BankBanks since it can construct whole banks. 
 * This initializer uses files with special text formatting in order to construct the banks
 * 
 * @author Mikko Hilpinen
 * @since 27.11.2014
 * @param <T> The type of handled held in the banks created by this initializer
 */
public class BankBankInitializer<T extends Handled> implements BankInitializer<Bank<T>>
{
	// ATTRIBUTES	-------------------------------
	
	private String fileName;
	private BankObjectConstructor<T> constructor;
	private BankObjectConstructor<Bank<T>> bankConstructor;
	
	
	// CONSTRUCTOR	-------------------------------
	
	/**
	 * Creates a new initializer
	 * @param fileName The name of the file used for creating the banks ("data/" automatically 
	 * included). The file should be written as follows:
	 * <p>
	 * &bankname<br>
	 * ...<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 * @param bankConstructor A constructor that is able to construct empty banks of the correct type
	 * @param constructor The constructor that can create objects based on commands
	 */
	public BankBankInitializer(String fileName, 
			BankObjectConstructor<Bank<T>> bankConstructor, BankObjectConstructor<T> constructor)
	{
		this.fileName = fileName;
		this.constructor = constructor;
		this.bankConstructor = bankConstructor;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------

	@Override
	public void initialize(Bank<Bank<T>> bank)
	{
		// Initializes the bankBank and provides initializers for the created banks
		BankFileReader<T> reader = new BankFileReader<>(this.fileName, bank, 
				this.bankConstructor, this.constructor);
		try
		{
			reader.readFile(this.fileName, "*");
		}
		catch (FileNotFoundException e)
		{
			throw new ResourceInitializationException("Cannot find file " + this.fileName);
		}
	}
	
	
	// SUBCLASSES	-------------------------
	
	private static class BankFileReader<T2 extends Handled> extends ModeUsingFileReader
	{
		// ATTRIBUTES	----------------------------------
		
		private Bank<Bank<T2>> bank;
		private BankObjectConstructor<T2> constructor;
		private BankObjectConstructor<Bank<T2>> bankConstructor;
		private String fileName;
		
		
		// CONSTRUCTOR	----------------------------------
		
		public BankFileReader(String fileName, Bank<Bank<T2>> bank, BankObjectConstructor<Bank<T2>> 
				bankConstructor, BankObjectConstructor<T2> constructor)
		{
			super("&");
			
			this.bank = bank;
			this.constructor = constructor;
			this.bankConstructor = bankConstructor;
			this.fileName = fileName;
		}
		
		
		// IMPLEMENTED METHODS	--------------------------
		
		@Override
		protected void onLine(String line, List<String> modes)
		{
			// Doesn't initialize the bank content but leaves it to the created initializers 
			// (see onMode(String, List<String>)
		}

		@Override
		protected void onMode(String newMode, List<String> modes)
		{
			// Creates a new bank and an initializer for it
			Bank<T2> newBank = this.bankConstructor.construct(newMode, this.bank);
			newBank.setInitializer(new FileSegmentBankInitializer<>(this.fileName, newMode, 
					this.constructor));
		}
	}
}

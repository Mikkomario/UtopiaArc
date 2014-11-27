package arc_bank;

import genesis_event.Handled;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import arc_resource.Resource;

/**
 * OpenBankHolder is an abstract class which creates and stores various OpenBanks
 * and gives access to them to it's subclasses.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * @param <T> The type of object held in the banks in this bank
 * @since 29.8.2013
 */
public abstract class BankBank<T extends Handled> extends Bank<Bank<T>> implements StateOperatorListener
{
	// ATTRIBUTES	--------------------------
	
	private Resource resourceType;
	
	
	// CONSTRUCTOR -----------------------------------------------------
	
	/**
	 * Creates a new bank that uses the given initializer to initialize its contents
	 * @param initializer The initializer that can construct the banks used in this bank
	 * @param resourceType The resourceType associated with this bank
	 */
	public BankBank(BankInitializer<Bank<T>> initializer, Resource resourceType)
	{
		super(initializer);
		
		getIsDeadStateOperator().getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS ----------------------------------------------

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		// Reacts to own state changes by uninitializing all the banks in this bank
		if (source == getIsDeadStateOperator() && newState == true)
			uninitializeBanks();
	}
	
	/* TODO: To be used in the initializer
	@SuppressWarnings("unchecked")
	@Override
	protected void onLine(String line)
	{
		// If the line starts with '&' ends the last bank and starts a new bank
		if (line.startsWith("&")) {
			if (this.lastbankname != null) {
				// System.out.println("Puts " + this.lastcommands.size() +
				// " objects to the bank " + this.lastbankname);
				this.banks.put(this.lastbankname, this.createBank(
						(ArrayList<String>) this.lastcommands.clone()));
			}
			this.lastbankname = line.substring(1);
			this.lastcommands.clear();
			return;
		}
		// Otherwise, tries to add a new command to the lastcommands
		this.lastcommands.add(line);
	}
	*/
	
	
	// GETTERS & SETTERS	-----------------------
	
	/**
	 * @return What kind of resource is held in this bank holder
	 */
	public Resource getHeldResourceType()
	{
		return this.resourceType;
	}

	
	// OTHER METHODS ---------------------------------------------------

	/**
	 * Uninitializes all the banks held by this object
	 */
	public void uninitializeBanks()
	{
		// Goes through all the banks and uninitializes them
		for (String bankName : getContentNames())
		{
			get(bankName).uninitialize();
		}
	}
	
	/**
	 * Initializes the BankHolder if it hasn't been initialized already
	 * @throws FileNotFoundException If the file couldn't be found
	 */
	/* TODO: To be used in the initializer
	@SuppressWarnings("unchecked")
	protected void initialize() throws FileNotFoundException
	{
		if (this.initialized)
			return;
		
		this.initialized = true;
		
		// Reads the file
		readFile(this.filename, "*");
		// Adds the last Bank and releases the memory
		if (this.lastcommands.size() > 0)
		{
			// System.out.println("Puts " + this.lastcommands.size() +
			// " objects to the bank " + this.lastbankname);
			this.banks.put(this.lastbankname, this.createBank(
					(ArrayList<String>) this.lastcommands.clone()));
		}
			
		this.lastcommands.clear();
		this.lastbankname = null;
	}
	*/
}

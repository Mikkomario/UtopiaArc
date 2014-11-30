package arc_bank;

import genesis_event.Handled;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import arc_resource.ResourceType;

/**
 * OpenBankHolder is an abstract class which creates and stores various OpenBanks
 * and gives access to them to it's subclasses.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * @param <T> The type of object held in the banks in this bank
 * @since 29.8.2013
 */
public class BankBank<T extends Handled> extends Bank<Bank<T>> implements StateOperatorListener
{
	// ATTRIBUTES	--------------------------
	
	private ResourceType resourceType;
	
	
	// CONSTRUCTOR -----------------------------------------------------
	
	/**
	 * Creates a new bank that uses the given initializer to initialize its contents
	 * @param initializer The initializer that can construct the banks used in this bank
	 * @param resourceType The resourceType associated with this bank
	 */
	public BankBank(BankInitializer<Bank<T>> initializer, ResourceType resourceType)
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
	
	
	// GETTERS & SETTERS	-----------------------
	
	/**
	 * @return What kind of resource is held in this bank holder
	 */
	public ResourceType getHeldResourceType()
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
}

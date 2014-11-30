package arc_bank;

import genesis_event.Handled;
import genesis_util.StateOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Abstractbank is the superclass of all the bank objects providing some 
 * necessary methods for the subclasses. The class also handles necessary 
 * data handling.
 *
 * @author Mikko Hilpinen.
 * @param <T> The type of object held in this bank
 * @since 17.8.2013.
 */
public class Bank<T extends Handled> implements Handled
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Map<String, T> bank;
	private boolean initialized;
	private BankInitializer<T> initializer;
	private StateOperator isDeadOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new uninitialized abstractbank. The bank will be initialized 
	 * when an object tries to get something from it.
	 * @param initializer The initializer that will initialize the bank's contents when 
	 * necessary
	 */
	public Bank(BankInitializer<T> initializer)
	{
		// Initializes attributes
		this.bank = new HashMap<>();
		this.initialized = false;
		this.initializer = initializer;
		this.isDeadOperator = new StateOperator(false, true);
	}
	
	/**
	 * Creates a new empty bank. The contents must be inserted manually
	 */
	public Bank()
	{
		// Initializes attributes
		this.bank = new HashMap<>();
		this.initialized = false;
		this.initializer = null;
		this.isDeadOperator = new StateOperator(false, true);
	}
	
	
	// IMPLEMENTED METHODS	-------------------------
	
	@Override
	public StateOperator getIsDeadStateOperator()
	{
		return this.isDeadOperator;
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return A set containing all the names of the objects held in the bank. 
	 * Notice that this is empty if the bank has not been initialized
	 */
	public Set<String> getContentNames()
	{
		return this.bank.keySet();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Tries to retrieve an object from the bank. Calling this method initializes 
	 * the bank if it hasn't yet been initialized
	 *
	 * @param objectName The name of the object in the bank
	 * @return The object in the bank or null if no object with the given name 
	 * was found
	 */
	public T get(String objectName)
	{
		// Initializes the bank if it hasn't already
		initialize();
		
		// Checks the parameter
		if (objectName == null)
			return null;
		
		// Tries to get the object from the map and return it
		if (this.bank.containsKey(objectName))
		{
			// Only returns alive objects, others are removed from the bank
			T object = this.bank.get(objectName);
			
			if (object.getIsDeadStateOperator().getState())
			{
				this.bank.remove(objectName);
				return null;
			}
			else
				return object;
		}
		else
			throw new ResourceUnavailableException(getClass().getName() + 
					" doesn't hold an object named " + objectName);
	}
	
	/**
	 * @return How many objects are currently stored in the bank
	 */
	public int size()
	{
		return this.bank.size();
	}
	
	/**
	 * Adds a new object to the bank
	 *
	 * @param object The object to be added
	 * @param name The name of the object in the bank
	 */
	public void put(String name, T object)
	{
		// Checks the given name and object state
		if (name == null || object.getIsDeadStateOperator().getState())
			return;
		
		// Adds the object to the bank
		this.bank.put(name, object);
	}
	
	/**
	 * Uninitializes the contents of the bank. The bank will be reinitialized 
	 * when something is tried to retrieve from it
	 * 
	 * @warning calling this method while the objects in the bank are in use 
	 * may crash the program depending on the circumstances. It would be safe 
	 * to uninitialize the bank only when the content is not in use.
	 */
	public void uninitialize()
	{
		// If the object wasn't initialized yet, doesn't do anything
		if (!this.initialized)
			return;
		
		// Goes through all the objects in the bank and kills them
		for (T object: this.bank.values())
		{
			object.getIsDeadStateOperator().setState(true);
		}
		
		// Clears the bank
		this.bank.clear();
		this.initialized = false;
	}
	
	/**
	 * Initializes the bank so it can be used immediately
	 */
	public void initialize()
	{
		// If the bank hasn't yet been initialized, initializes it
		if (!this.initialized)
		{
			this.initialized = true;
			if (this.initializer != null)
				this.initializer.initialize(this);
		}
	}
	
	/**
	 * @return Is the bank currently initialized
	 */
	protected boolean isInitialized()
	{
		return this.initialized;
	}
}

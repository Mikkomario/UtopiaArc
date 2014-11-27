package arc_bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import arc_resource.Resource;

/**
 * Multimediaholder keeps track all kinds of resources 
 * and provides them for other objects. The resources can be 
 * activated and deactivated at-will. The class is wholly static since no 
 * copies of the resources should be made and they should be accessessible from 
 * anywhere.<br>
 * Please note that different resource data has to be initialized 
 * before they can be used.
 * 
 * @author Mikko Hilpinen. 
 * @since 14.2.2014
 * @see #initializeResourceDatabase(BankBank)
 */
public class MultiMediaHolder
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private static Map<Resource, Map<String, Bank<?>>> activebanks 
			= new HashMap<Resource, Map<String, Bank<?>>>();
	private static Map<Resource, BankBank<?>> bankholders = 
			new HashMap<Resource, BankBank<?>>();
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	private MultiMediaHolder()
	{
		// Constructor hidden from other classes since only static 
		// interfaces are allowed
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Initializes the database of the given type. The necessary information is 
	 * read from a file.
	 * @param resourceData The OpenBankHolder that will hold the resource data of a certain type
	 */
	public static void initializeResourceDatabase(BankBank<?> resourceData)
	{
		if (resourceData == null)
			return;
		
		Resource type = resourceData.getHeldResourceType();
		
		if (bankholders.containsKey(type))
		{
			// TODO: Throw exception instead
			System.err.println("The resource database with type " + type + 
					" has already been initialized");
			return;
		}
		
		// Remembers the new holder
		bankholders.put(type, resourceData);
		
		// Initializes the map
		activebanks.put(type, new HashMap<String, Bank<?>>());
	}
	
	/**
	 * Activates the bank with the given resourceType and name. The bank remains 
	 * active until deactivateBank() is called for it
	 * 
	 * @param type The resource type of the bank
	 * @param bankname The name of the resource bank
	 * @param preinitialize Should all the resources in the bank be initialized 
	 * right away (true) or at the first time the bank is used (false)
	 * @see #deactivateBank(Resource, String)
	 */
	public static void activateBank(Resource type, String bankname, boolean preinitialize)
	{
		if (!activebanks.containsKey(type))
		{
			// TODO: Throw exception
			System.err.println(type + 
					" database hasn't been initialized and can't be used yet");
			return;
		}
		
		// If the bank was already activated, does nothing
		if (activebanks.get(type).containsKey(bankname))
			return;
		
		Bank<?> newbank = bankholders.get(type).get(bankname);
		
		if (newbank == null)
			return;
		
		if (preinitialize)
			newbank.initialize();
		activebanks.get(type).put(bankname, newbank);
	}
	
	/**
	 * Deactivates a bank with the given resource type and name. The bank can 
	 * be later reactivated using the activateBank method.
	 * 
	 * @param type The resource type of the bank
	 * @param bankname The name of the bank
	 * @see #activateBank(Resource, String, boolean)
	 */
	public static void deactivateBank(Resource type, String bankname)
	{
		// If the bank isn't active or if the resource type hasn't been 
		// initialized, does nothing
		if (!activebanks.containsKey(type) || 
				!activebanks.get(type).containsKey(bankname))
			return;
		
		// Uninitializes the bank and then removes it from the active banks
		activebanks.get(type).get(bankname).uninitialize();
		activebanks.get(type).remove(bankname);
	}
	
	/**
	 * Gives access to a bank stored within the multiMediaHolder. This should mostly be used 
	 * by the bank classes to provide static access.
	 * 
	 * @param type The type of bank being retrieved
	 * @param bankname The name of the bank being retrieved
	 * @return A bank in the multiMediaHolder with the given type and name or null if no such 
	 * bank exists
	 */
	public static Bank<?> getBank(Resource type, String bankname)
	{
		if (!activebanks.containsKey(type))
		{
			// TODO: Exception...
			System.err.println(type + " database has not been initialized yet!");
			return null;
		}
		
		if (!activebanks.get(type).containsKey(bankname))
		{
			// TODO: Exception
			System.err.println(type + "bank named " + bankname + 
					" is not active!");
			return null;
		}
		
		return activebanks.get(type).get(bankname);
	}
	
	/**
	 * @return The types of resources initialized to this holder
	 */
	public static Set<Resource> getHeldResourceTypes()
	{
		return bankholders.keySet();
	}
	
	/**
	 * This method tries to parse a resource type from a string. Only resources introduced to 
	 * the holder are considered.
	 * 
	 * @param resourceName The name of the resource type
	 * @return A resource with the given name
	 */
	public static Resource getResourceType(String resourceName)
	{
		for (Resource resource : getHeldResourceTypes())
		{
			if (resource.toString().equalsIgnoreCase(resourceName))
				return resource;
		}
		
		return null;
	}
}

package arc_bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import arc_resource.ResourceType;

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
 * @deprecated Replaced with {@link utopia.arc.resource.BankBank}
 */
public class MultiMediaHolder
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private static Map<ResourceType, Map<String, Bank<?>>> activebanks 
			= new HashMap<ResourceType, Map<String, Bank<?>>>();
	private static Map<ResourceType, BankBank<?>> bankholders = 
			new HashMap<ResourceType, BankBank<?>>();
	
	
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
		
		ResourceType type = resourceData.getHeldResourceType();
		
		if (bankholders.containsKey(type))
			throw new ResourceStateException("ResourceType " + type + 
					" has already been initialized");
		
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
	 * @see #deactivateBank(ResourceType, String)
	 */
	public static void activateBank(ResourceType type, String bankname, boolean preinitialize)
	{
		if (!activebanks.containsKey(type))
			throw new ResourceUnavailableException(
					"The resource type " + type + " hasn't been initialized yet");
		
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
	 * @see #activateBank(ResourceType, String, boolean)
	 */
	public static void deactivateBank(ResourceType type, String bankname)
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
	public static Bank<?> getBank(ResourceType type, String bankname)
	{
		if (!activebanks.containsKey(type))
			throw new ResourceUnavailableException(
					"The resource type " + type + " hasn't been initialized yet");
		
		if (!activebanks.get(type).containsKey(bankname))
			throw new ResourceUnavailableException("The bank " + bankname + " insn't active");
		
		return activebanks.get(type).get(bankname);
	}
	
	/**
	 * @return The types of resources initialized to this holder
	 */
	public static Set<ResourceType> getHeldResourceTypes()
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
	public static ResourceType getResourceType(String resourceName)
	{
		for (ResourceType resource : getHeldResourceTypes())
		{
			if (resource.toString().equalsIgnoreCase(resourceName))
				return resource;
		}
		
		return null;
	}
	
	/**
	 * Finds out the active bank names of a single resource type
	 * @param type A resource type initialized in this holder
	 * @return All the bank names that can have been activated in that given type.
	 */
	public static Set<String> getActiveBankNames(ResourceType type)
	{
		if (!bankholders.containsKey(type))
			return null;
		return bankholders.get(type).getContentNames();
	}
}

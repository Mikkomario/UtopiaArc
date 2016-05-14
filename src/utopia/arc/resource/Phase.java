package utopia.arc.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utopia.flow.generics.DataType;
import utopia.flow.util.Filter;
import utopia.flow.util.StringFilter;

/**
 * Phases represent different phases in a program's life cycle. Different resources are 
 * connected to each phase
 * @author Mikko Hilpinen
 * @since 10.5.2016
 */
public class Phase
{
	// ATTRIBUTES	-----------------
	
	private String name;
	private Map<DataType, Set<String>> activeBankNames = new HashMap<>();
	
	
	// CONSTRUCTOR	-----------------
	
	/**
	 * Creates a new phase
	 * @param name The name of the phase
	 */
	public Phase(String name)
	{
		this.name = name;
	}
	
	
	// IMPLEMENTED METHODS	---------
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	
	// ACCESSORS	-----------------
	
	/**
	 * @return The name of the phase
	 */
	public String getName()
	{
		return this.name;
	}
	
	
	// OTHER METHODS	-------------
	
	/**
	 * @return The resource types associated with this phase. Those resource types have 
	 * active banks in this phase.
	 */
	public Set<DataType> getResourceTypes()
	{
		return new HashSet<>(this.activeBankNames.keySet());
	}
	
	/**
	 * Finds the names of the banks of a certain type, that are used during this phase
	 * @param resourceType The type of resource in question
	 * @return The banks of the provided resource type that should be kept available while 
	 * the phase persists. The returned list is a copy and changes made to it won't affect 
	 * this phase.
	 */
	public Set<String> getActiveBankNames(DataType resourceType)
	{
		if (!this.activeBankNames.containsKey(resourceType))
			this.activeBankNames.put(resourceType, new HashSet<>());
			
		return new HashSet<>(this.activeBankNames.get(resourceType));
	}
	
	/**
	 * Updates the set of active bank names of a single resource type
	 * @param resourceType The type of resource in question
	 * @param activeBankNames The names of the banks of that resource type that are used 
	 * during this phase
	 */
	public void setActiveBankNames(DataType resourceType, Collection<String> activeBankNames)
	{
		this.activeBankNames.put(resourceType, new HashSet<>(activeBankNames));
	}
	
	/**
	 * Adds a new bank to the set of active banks
	 * @param resourceType The type of the bank
	 * @param bankName The name of the bank
	 */
	public void addActiveBank(DataType resourceType, String bankName)
	{
		Set<String> bankNames = this.activeBankNames.get(resourceType);
		
		if (bankNames == null)
		{
			bankNames = new HashSet<>();
			this.activeBankNames.put(resourceType, bankNames);
		}
		
		bankNames.add(bankName);
	}
	
	/**
	 * Checks whether a bank is active during this phase
	 * @param resourceType The resource type of the bank
	 * @param bankName The name of the bank
	 * @return Should the bank be available during this phase
	 */
	public boolean bankIsActive(DataType resourceType, String bankName)
	{
		if (!this.activeBankNames.containsKey(resourceType))
			return false;
		
		return Filter.findFirst(getActiveBankNames(resourceType), new StringFilter(bankName)) != null;
	}
	
	/**
	 * Checks whether a bank is active during this phase
	 * @param bank The bank that may be active
	 * @return Should the bank be available during this phase
	 */
	public boolean bankIsActive(Bank<?> bank)
	{
		return bankIsActive(bank.getContentType(), bank.getName());
	}
}

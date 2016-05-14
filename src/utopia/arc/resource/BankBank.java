package utopia.arc.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utopia.arc.resource.BankRecorder.RecordingFailedException;
import utopia.flow.generics.DataType;

/**
 * This class contains multiple banks of a single type
 * @author Mikko Hilpinen
 * @since 9.5.2016
 * @param <ResourceType> The type of resource held within the banks
 */
public class BankBank<ResourceType>
{
	// ATTRIBUTES	---------------
	
	private Map<String, Bank<ResourceType>> banks = new HashMap<>();
	private DataType type;
	private BankRecorder recorder;
	
	
	// CONSTRUCTOR	---------------
	
	/**
	 * Creates a new bank bank
	 * @param contentType The type of content in the banks. Must match the bank's class type
	 * @param recorder The recorder used for reading and writing bank data
	 * @param generateBanks Should the banks be generated as well by using the recorder to read 
	 * the bank names
	 * @throws RecordingFailedException If, when trying to generate the banks, the operation 
	 * fails for some reason
	 */
	public BankBank(DataType contentType, BankRecorder recorder, boolean generateBanks) 
			throws RecordingFailedException
	{
		this.type = contentType;
		this.recorder = recorder;
		
		if (generateBanks)
		{
			for (String bankName : this.recorder.readBankNames(contentType))
			{
				put(bankName);
			}
		}
	}
	
	
	// ACCESSORS	---------------
	
	/**
	 * @return The type of content inside the banks
	 */
	public DataType getContentType()
	{
		return this.type;
	}
	
	
	// OTHER METHODS	-----------
	
	/**
	 * Checks whether the bank bank contains a bank with the provided name (case-insensitive)
	 * @param bankName The name of the bank
	 * @return Does the bank bank contain a bank with the provided name
	 */
	public boolean containsBankWithName(String bankName)
	{
		return this.banks.containsKey(bankName.toLowerCase());
	}
	
	/**
	 * Adds a new bank to the bank bank
	 * @param bank The bank that is added
	 */
	public void put(Bank<ResourceType> bank)
	{
		this.banks.put(bank.getName().toLowerCase(), bank);
	}
	
	/**
	 * Adds a new bank to the bank bank
	 * @param bankName The name of the new bank
	 * @return The bank that was added
	 */
	public Bank<ResourceType> put(String bankName)
	{	
		Bank<ResourceType> bank = new Bank<>(bankName, getContentType(), this.recorder);
		put(bank);
		return bank;
	}
	
	/**
	 * Adds multiple banks to the bank bank (in case they don't exist there yet)
	 * @param bankNames The names of the new banks
	 */
	public void generateBanks(String... bankNames)
	{
		for (String bankName : bankNames)
		{
			if (!containsBankWithName(bankName))
				put(bankName);
		}
	}
	
	/**
	 * Adds a new resource to one of the banks inside this bank bank
	 * @param bankName The name of the target bank. If no bank exists with this name, one is 
	 * created
	 * @param resourceName The name of the resource inside the bank
	 * @param resource The resource put to the bank
	 */
	public void put(String bankName, String resourceName, ResourceType resource)
	{
		Bank<ResourceType> bank = get(bankName);
		// If there is no bank, creates one
		if (bank == null)
			bank = put(bankName);
		
		bank.put(resourceName, resource);
	}
	
	/**
	 * Finds a bank from the bank bank
	 * @param bankName The name of the bank
	 * @return The bank from this bank bank or null if no such bank existed
	 */
	public Bank<ResourceType> get(String bankName)
	{
		return this.banks.get(bankName.toLowerCase());
	}
	
	/**
	 * Finds a resource from one of the banks inside this bank bank
	 * @param bankName The name of the bank
	 * @param resourceName The name of the resource in the bank
	 * @return The resource from the bank or null if the bank didn't exist or it didn't contain 
	 * the resource
	 */
	public ResourceType get(String bankName, String resourceName)
	{
		Bank<ResourceType> bank = get(bankName);
		if (bank == null)
			return null;
		else
			return bank.get(resourceName);
	}
	
	/**
	 * @return The banks in this bank bank. The list is a copy and changes made to it 
	 * won't affect the bank.
	 */
	public List<Bank<ResourceType>> getBanks()
	{
		return new ArrayList<>(this.banks.values());
	}
	
	// TODO: Add methods for initialisation and uninitialisation if need be
}

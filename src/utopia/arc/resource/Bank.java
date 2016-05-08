package utopia.arc.resource;

import utopia.arc.resource.BankRecorder.RecordingFailedException;
import utopia.flow.generics.DataType;
import utopia.flow.generics.Model;
import utopia.flow.generics.Value;
import utopia.flow.generics.Variable;

/**
 * Banks contain resources of a type. Banks can be initialised and cleared at will. They are 
 * saved and loaded when necessary.
 * @author Mikko Hilpinen
 * @since 7.5.2016
 * @param <ResourceType> The type of resource held by this bank must reflect the bank's 
 * used data type!
 */
public class Bank<ResourceType> extends Model<Variable>
{
	// ATTRIBUTES	----------------------
	
	private DataType type;
	private BankRecorder recorder;
	private boolean initialised = false;
	
	
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new bank
	 * @param contentType The type of content held by this bank. Must match the object class 
	 * associated with this bank.
	 * @param recorder The object used for writing and reading the bank data
	 */
	public Bank(DataType contentType, BankRecorder recorder)
	{
		super(SingleTypeVariableParser.createBasicSingleTypeVariableParser(contentType));
		
		this.type = contentType;
		this.recorder = recorder;
	}
	
	/**
	 * Copies another bank
	 * @param other Another bank
	 */
	public Bank(Bank<ResourceType> other)
	{
		super(other);
		this.type = other.type;
		this.recorder = other.recorder;
	}
	
	
	// ACCESSORS	----------------------
	
	/**
	 * @return The data type of the bank's contents
	 */
	public DataType getContentType()
	{
		return this.type;
	}
	
	
	// OTHER METHODS	------------------
	
	/**
	 * Fetches a resource from the bank
	 * @param resourceName The name of the requested resource
	 * @return The resource with the provided name or null if there was no such resource.
	 */
	@SuppressWarnings("unchecked")
	public ResourceType get(String resourceName)
	{
		// TODO: Throw an exception if there is no such resource
		return (ResourceType) getAttribute(resourceName).getObjectValue(getContentType());
	}
	
	/**
	 * Adds a new resource to the bank
	 * @param resourceName The name of the resource
	 * @param resource The resource
	 */
	public void put(String resourceName, ResourceType resource)
	{
		addAttribute(resourceName, new Value(resource, getContentType()), true);
	}
	
	/**
	 * Saves the bank's current state
	 * @throws RecordingFailedException If bank writing failed
	 */
	public void save() throws RecordingFailedException
	{
		this.recorder.writeBank(getAttributes());
	}
	
	/**
	 * Initialises the bank, reading its data
	 * @throws RecordingFailedException If the bank read failed
	 */
	public void initialise() throws RecordingFailedException
	{
		if (!this.initialised)
		{
			this.initialised = true;
			addAttributes(this.recorder.readBank(), true);
		}
	}
	
	/**
	 * Clears the bank. The data may be restored by calling {@link #initialise()}
	 */
	public void uninitialise()
	{
		if (this.initialised)
		{
			this.initialised = false;
			for (Variable attribute : getAttributes())
			{
				removeAttribute(attribute);
			}
		}
	}
}

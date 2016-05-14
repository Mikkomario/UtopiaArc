package utopia.arc.resource;

import java.util.ArrayList;
import java.util.List;

import utopia.arc.resource.BankRecorder.RecordingFailedException;
import utopia.flow.generics.DataType;
import utopia.flow.generics.Model;
import utopia.flow.generics.SingleTypeVariableParser;
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
	
	private String name;
	private DataType type;
	private BankRecorder recorder;
	private boolean initialised = false;
	
	
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new bank
	 * @param name The name of the bank
	 * @param contentType The type of content held by this bank. Must match the object class 
	 * associated with this bank.
	 * @param recorder The object used for writing and reading the bank data
	 */
	public Bank(String name, DataType contentType, BankRecorder recorder)
	{
		super(SingleTypeVariableParser.createBasicSingleTypeVariableParser(contentType));
		
		this.name = name;
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
	 * @return The name of the bank
	 */
	public String getName()
	{
		return this.name;
	}
	
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
	 * @throws NoSuchAttributeException If the bank didn't contain a resource with the provided name
	 */
	public ResourceType get(String resourceName) throws NoSuchAttributeException
	{
		return attributeToResource(getAttribute(resourceName));
	}
	
	/**
	 * @return This bank's contents
	 */
	public List<ResourceType> listContents()
	{
		List<ResourceType> list = new ArrayList<>();
		for (Variable var : getAttributes())
		{
			list.add(attributeToResource(var));
		}
		
		return list;
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
		this.recorder.writeBank(getName(), getContentType(), getAttributes());
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
			addAttributes(this.recorder.readBank(getName(), getContentType()), true);
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
	
	@SuppressWarnings("unchecked")
	private ResourceType attributeToResource(Variable attribute)
	{
		return (ResourceType) attribute.getObjectValue(getContentType());
	}
}

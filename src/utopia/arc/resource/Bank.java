package utopia.arc.resource;

import utopia.flow.generics.DataType;
import utopia.flow.generics.Model;
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
	
	
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new bank
	 * @param contentType The type of content held by this bank. Must match the object class 
	 * associated with this bank.
	 */
	public Bank(DataType contentType)
	{
		super(SingleTypeVariableParser.createBasicSingleTypeVariableParser(contentType));
		
		this.type = contentType;
	}
	
	/**
	 * Copies another bank
	 * @param other Another bank
	 */
	public Bank(Bank<ResourceType> other)
	{
		super(other);
		this.type = other.type;
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
		return (ResourceType) getAttribute(resourceName).getObjectValue(getContentType());
	}
}

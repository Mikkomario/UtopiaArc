package arc_bank;

import utopia.flow.generics.Model.NoSuchAttributeException;

/**
 * ResourceUnavailableExceptions are thrown when resource retrieval fails.
 * 
 * @author Mikko Hilpinen
 * @since 30.11.2014
 * @deprecated Replaced with {@link NoSuchAttributeException}
 */
public class ResourceUnavailableException extends ResourceStateException
{
	// ATTRIBUTES	---------------------------
	
	private static final long serialVersionUID = 977529570759793100L;
	
	
	// CONSTRUCTOR	---------------------------
	
	/**
	 * Creates a new exception
	 * @param message The message sent with the exception
	 */
	public ResourceUnavailableException(String message)
	{
		super(message);
	}
}
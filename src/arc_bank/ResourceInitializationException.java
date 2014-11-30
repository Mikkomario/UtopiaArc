package arc_bank;

/**
 * ResourceInitializationExceptions are thrown when resource initialization fails.
 * 
 * @author Mikko Hilpinen
 * @since 30.11.2014
 */
public class ResourceInitializationException extends ResourceStateException
{
	// ATTRIBUTES	-------------------------
	
	private static final long serialVersionUID = 6992893996495969087L;

	
	// CONSTRUCTOR	-------------------------
	
	/**
	 * Creates a new exception
	 * @param message The message sent with the exception
	 */
	public ResourceInitializationException(String message)
	{
		super(message);
	}
}
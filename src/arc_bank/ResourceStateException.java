package arc_bank;

/**
 * ResourceStateExceptions are thrown when the resource system is misused. Often this is done 
 * in the instruction files and / or written code so they are considered non-recoverable errors.
 * 
 * @author Mikko Hilpinen
 * @since 30.11.2014
 */
public class ResourceStateException extends RuntimeException
{
	// ATTRIBUTES	-------------------------------
	
	private static final long serialVersionUID = 352164441181005190L;
	
	
	// CONSTRUCTOR	------------------------------------

	/**
	 * Creates a new exception with the given message
	 * @param message The message sent with the exception
	 */
	public ResourceStateException(String message)
	{
		super(message);
	}
}

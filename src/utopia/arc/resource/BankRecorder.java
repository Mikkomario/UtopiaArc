package utopia.arc.resource;

import java.util.Collection;
import java.util.List;

import utopia.flow.generics.DataType;
import utopia.flow.generics.Variable;

/**
 * These recorders are used for writing and reading bank data
 * @author Mikko Hilpinen
 * @since 8.5.2016
 */
public interface BankRecorder
{
	/**
	 * Writes bank contents somewhere
	 * @param bankName The name of the written bank
	 * @param bankType The type of the bank's contents
	 * @param contents The contents that need to be written
	 * @throws RecordingFailedException If the writing failed
	 */
	public void writeBank(String bankName, DataType bankType, 
			Collection<? extends Variable> contents) throws RecordingFailedException;
	
	/**
	 * Reads the bank contents from somewhere
	 * @param bankName The name of the read bank
	 * @param bankType The type of the read content
	 * @return The bank contents that were read
	 * @throws RecordingFailedException If the read failed
	 */
	public Collection<Variable> readBank(String bankName, DataType bankType) throws RecordingFailedException;
	
	/**
	 * Reads the available bank names for a single resource type
	 * @param resourceType The type of resource which's bank names are read
	 * @return The available bank names, which may be used in the {@link #readBank(String, DataType)} method
	 * @throws RecordingFailedException If the bank names couldn't be read
	 */
	public List<String> readBankNames(DataType resourceType) throws RecordingFailedException;
	
	
	// NESTED CLASSES	-----------------
	
	/**
	 * These exceptions are thrown when bank writing / reading fails for some reason
	 * @author Mikko Hilpinen
	 * @since 8.5.2016
	 */
	public static class RecordingFailedException extends Exception
	{
		private static final long serialVersionUID = -4203761127745189378L;

		/**
		 * Creates a new exception
		 * @param message The message sent along with the exception
		 */
		public RecordingFailedException(String message)
		{
			super(message);
		}
		
		/**
		 * Creates a new exception
		 * @param message The message sent along with the exception
		 * @param cause The cause of the exception
		 */
		public RecordingFailedException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}
}

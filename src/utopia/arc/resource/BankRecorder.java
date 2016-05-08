package utopia.arc.resource;

import java.util.Collection;

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
	 * @param contents The contents that need to be written
	 * @throws RecordingFailedException If the writing failed
	 */
	public void writeBank(Collection<? extends Variable> contents) throws RecordingFailedException;
	
	/**
	 * Reads the bank contents from somewhere
	 * @return The bank contents that were read
	 * @throws RecordingFailedException If the read failed
	 */
	public Collection<Variable> readBank() throws RecordingFailedException;
	
	
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

package arc_bank;

import genesis_event.Handled;

/**
 * ObjectConstructors are able to construct objects based on a command given on a single line
 * 
 * @author Mikko Hilpinen
 * @since 27.11.2014
 * @param <T> The type of object constructed by this constructor
 */
public abstract interface BankObjectConstructor<T extends Handled>
{
	/**
	 * Constructs an object from the information of the given line. The object should be 
	 * put to the given bank as well.
	 * @param line The line that should contain all the necessary information for constructing 
	 * the object
	 * @param bank The bank the constructed object should be put to
	 * @return The constructed object (for reference)
	 */
	public T construct(String line, Bank<T> bank);
}

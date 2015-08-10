package arc_bank;

import genesis_util.Killable;

/**
 * BankInitializers are able to provide initial content for a bank. Most preferably by 
 * reading the content from a file or something
 * 
 * @author Mikko Hilpinen
 * @param <T> The type of resource created by this initializer
 * @since 27.11.2014
 */
public interface BankInitializer<T extends Killable>
{
	/**
	 * Initializes the given bank
	 * @param bank The bank that requires initializing
	 */
	public void initialize(Bank<T> bank);
}

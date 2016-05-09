package arc_bank;

import java.util.List;

import genesis_event.Handled;
import utopia.arc.resource.BankRecorder;

/**
 * This bankInitializer initializes bank content based on a special list of commands
 * 
 * @author Mikko Hilpinen
 * @since 27.11.2014
 * @param <T> The type of resource produced by this initializer
 * @deprecated Replaced with {@link BankRecorder}
 */
public class ListBankInitializer<T extends Handled> implements BankInitializer<T>
{
	// ATTRIBUTES	----------------------------
	
	private List<String> commands;
	private BankObjectConstructor<T> constructor;
	
	
	// CONSTRUCTOR	----------------------------
	
	/**
	 * Creates a new initializer
	 * @param commands The commands used in the initializing process
	 * @param constructor The constructor that can construct the objects that will be put 
	 * to the bank
	 */
	public ListBankInitializer(List<String> commands, BankObjectConstructor<T> constructor)
	{
		this.commands = commands;
		this.constructor = constructor;
	}
	
	
	// IMPLEMENTED METHODS	---------------------

	@Override
	public void initialize(Bank<T> bank)
	{
		// Creates content for each line
		for (String command : this.commands)
		{
			this.constructor.construct(command, bank);
		}
	}
}

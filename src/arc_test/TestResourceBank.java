package arc_test;

import arc_bank.Bank;
import arc_bank.BankBank;
import arc_bank.BankBankInitializer;
import arc_bank.BankObjectConstructor;
import arc_bank.MultiMediaHolder;

/**
 * Creates banks for testing
 * 
 * @author Mikko Hilpinen
 * @since 30.11.2014
 * @deprecated Replaced with {@link utopia.arc.resource.Bank}
 */
public class TestResourceBank
{
	// CONSTRUCTOR	-------------------------
	
	private TestResourceBank()
	{
		// The interface is static
	}
	
	
	// OTHER METHODS	---------------------
	
	/**
	 * Initializes testResource database in the multiMediaHolder
	 * @param fileName The name of the file that contains testResource construction 
	 * information ("data/" automatically included).
	 */
	public static void initializeTestResources(String fileName)
	{
		MultiMediaHolder.initializeResourceDatabase(new BankBank<TestResource>(
				new BankBankInitializer<TestResource>(fileName, 
				new TestResourceBankConstructor(), new TestResourceConstructor()), 
				ArcTestResourceType.TESTRESOURCE));
	}
	
	/**
	 * Finds an active bank with the given name
	 * @param bankName The name of the test resource bank
	 * @return An active testResourceBank with the given name
	 */
	@SuppressWarnings("unchecked")
	public static Bank<TestResource> getTestResourceBank(String bankName)
	{
		return (Bank<TestResource>) MultiMediaHolder.getBank(ArcTestResourceType.TESTRESOURCE, 
				bankName);
	}
	
	
	// SUBCLASSES	-------------------------
	
	private static class TestResourceConstructor implements BankObjectConstructor<TestResource>
	{
		// IMPLEMENTED METHODS	-------------------------------------
		
		@Override
		public TestResource construct(String line, Bank<TestResource> bank)
		{
			System.out.println("Constructs a testResource from " + line);
			String[] arguments = line.split("#");
			TestResource newResource = new TestResource(arguments[1]);
			bank.put(arguments[0], newResource);
			return newResource;
		}
	}
	
	private static class TestResourceBankConstructor implements BankObjectConstructor<Bank<TestResource>>
	{
		@Override
		public Bank<TestResource> construct(String line, Bank<Bank<TestResource>> bank)
		{
			Bank<TestResource> newBank = new Bank<>();
			bank.put(line, newBank);
			return newBank;
		}	
	}
}

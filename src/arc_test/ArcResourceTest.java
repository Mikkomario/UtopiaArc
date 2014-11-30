package arc_test;

import arc_bank.Bank;
import arc_bank.GamePhaseBank;
import arc_resource.ResourceActivator;

/**
 * This class tests the resource initiation and request functions of Arc
 * @author Mikko Hilpinen
 * @since 30.11.2014
 */
public class ArcResourceTest
{
	// CONSTRUCTOR	--------------------------
	
	private ArcResourceTest()
	{
		// The constructor is hidden since the interface is static
	}

	
	// MAIN METHOD	--------------------------
	
	/**
	 * Starts the test
	 * @param args Not used
	 */
	public static void main(String[] args)
	{
		// Initializes resources
		TestResourceBank.initializeTestResources("testing/testResources.txt");
		GamePhaseBank.initializeGamePhaseResources("testing/testGamePhases.txt", "test");
		
		// Activates a phase
		ResourceActivator.startPhase(GamePhaseBank.getGamePhase("phase1"));
		
		printBankContents("bank1");
		printBankContents("bank2");
		
		ResourceActivator.startPhase(GamePhaseBank.getGamePhase("phase2"));
		
		printBankContents("bank2");
		printBankContents("bank3");
		
		ResourceActivator.startPhase(GamePhaseBank.getGamePhase("phase3"));
		
		printBankContents("bank3");
	}
	
	
	// OTHER METHODS	---------------------
	
	private static void printBankContents(String bankName)
	{
		System.out.println(bankName + " --------------------------------");
		
		Bank<TestResource> bank = TestResourceBank.getTestResourceBank(bankName);
		for (String name : bank.getContentNames())
		{
			System.out.println(name + ": " + bank.get(name));
		}
	}
}

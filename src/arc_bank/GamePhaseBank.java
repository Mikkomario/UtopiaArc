package arc_bank;

import java.util.HashMap;
import java.util.Map;

import arc_resource.GamePhase;
import arc_resource.MetaResource;
import arc_resource.Resource;

/**
 * This is a static factor for banks that are contain GamePhases
 * 
 * @author Mikko Hilpinen
 * @since 27.11.2014
 */
public class GamePhaseBank
{
	// CONSTRUCTOR	------------------------------
	
	private GamePhaseBank()
	{
		// The interface is static
	}
	
	
	// OTHER METHODS	---------------------------
	
	/**
	 * Initializes the program ready to use the GamePhases introduced in the given file
	 * @param fileName The name of the file that contains gamePhase data 
	 * ("data/" automatically included).<br> 
	 * The file should have the following format:<br>
	 * &bankName<br>
	 * GamePhaseName#ResourceType:ResourceBankName1,ResourceBankName2,...#ResourceType2:...#...<br>
	 * GamePhaseName2#...<br>
	 * ...
	 */
	public static void initializeGamePhaseResources(String fileName)
	{
		MultiMediaHolder.initializeResourceDatabase(createGamePhaseBankBank(fileName));
	}
	
	/**
	 * Creates a new GamePhaseBankBank that can handle all gamephase banks
	 * @param fileName The name of the file that contains gamePhase data 
	 * ("data/" automatically included).<br> 
	 * The file should have the following format:<br>
	 * &bankName<br>
	 * GamePhaseName#ResourceType:ResourceBankName1,ResourceBankName2,...#ResourceType2:...#...<br>
	 * GamePhaseName2#...<br>
	 * ...
	 * @return A gamePhaseBankBank A BankBank that contains GamePhaseBanks and their contents
	 */
	public static BankBank<GamePhase> createGamePhaseBankBank(String fileName)
	{
		return new BankBank<GamePhase>(new BankBankInitializer<>(fileName, 
				createBankConstructor(), createGamePhaseConstructor()), 
				MetaResource.GAMEPHASE);
	}
	
	/**
	 * @return A new bankConstructor that works with GamePhases
	 */
	private static GamePhaseBankConstructor createBankConstructor()
	{
		return new GamePhaseBankConstructor();
	}
	
	/**
	 * @return A new Constructor that creates GamePhases
	 */
	private static GamePhaseConstructor createGamePhaseConstructor()
	{
		return new GamePhaseConstructor();
	}
	
	/**
	 * A GamePhaseBank with the given name. The bank must first be activated through the 
	 * MultiMediaHolder.
	 * 
	 * @param bankName The name of the bank
	 * @return A GamePhaseBank with the given name
	 */
	@SuppressWarnings("unchecked")
	public static Bank<GamePhase> getGamePhaseBank(String bankName)
	{
		Bank<?> bank = MultiMediaHolder.getBank(MetaResource.GAMEPHASE, bankName);
		
		return (Bank<GamePhase>) bank;
	}

	
	// SUBCLASSES	-------------------------------
	
	/**
	 * This constructor is able to create GamePhaseBanks
	 * 
	 * @author Mikko Hilpinen
	 * @since 27.11.2014
	 */
	public static class GamePhaseBankConstructor implements BankObjectConstructor<Bank<GamePhase>>
	{
		// IMPLEMENTED METHODS	-----------------------------------
		
		@Override
		public void construct(String line, Bank<Bank<GamePhase>> bank)
		{
			bank.put(line, new Bank<GamePhase>());
		}
	}
	
	/**
	 * GamePhaseBankInitializer is able to produce new gamePhases based on a list of commands
	 * 
	 * @author Mikko Hilpinen
	 * @since 27.11.2014
	 */
	public static class GamePhaseConstructor implements BankObjectConstructor<GamePhase>
	{	
		// IMPLEMENTED METHODS	---------------

		@Override
		public void construct(String line, Bank<GamePhase> bank)
		{
			// TODO: Add exceptions
			
			String[] arguments = line.split("#");
			
			// Checks that there are enough arguments
			if (arguments.length < 1)
			{
				System.err.println("Couldn't load a GamePhase. Line " + line + 
						"doensn't have enough arguments");
				return;
			}
			
			Map<Resource, String[]> banknames = new HashMap<>();
					
			// The first argument is the name of the phase
			// Reads the banknames and resourcetypes from arguments 2 forwards
			for (int argumentindex = 1; argumentindex < arguments.length; argumentindex++)
			{
				// The banknames are listed in the second part of the argument
				String[] resourceparts = arguments[argumentindex].split(":");
				String[] nameslist = resourceparts[1].split(",");
				
				// Adds the names to the map
				banknames.put(MultiMediaHolder.getResourceType(resourceparts[0]), nameslist);
			}
			
			// Creates a new phase, adds it to the bank and updates it according 
			// to the findings
			GamePhase newPhase = new GamePhase(arguments[0]);
			
			for (Resource type : banknames.keySet())
			{
				newPhase.connectResourceBankNames(type,  banknames.get(type));
			}
			
			bank.put(arguments[0], newPhase);
		}
	}
}

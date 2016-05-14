package arc_bank;

import java.util.HashMap;
import java.util.Map;

import arc_resource.GamePhase;
import arc_resource.MetaResourceType;
import arc_resource.ResourceType;
import utopia.arc.io.PhaseRecorder;
import utopia.arc.resource.ResourceManager;

/**
 * This is a static factor for banks that are contain GamePhases
 * 
 * @author Mikko Hilpinen
 * @since 27.11.2014
 * @deprecated Replaced with {@link ResourceManager} and {@link PhaseRecorder}
 */
public class GamePhaseBank
{
	// ATTRIBUTES	------------------------------
	
	private static String defaultPhaseBankName;
	
	
	// CONSTRUCTOR	------------------------------
	
	private GamePhaseBank()
	{
		// The interface is static
	}
	
	
	// OTHER METHODS	---------------------------
	
	/**
	 * Initializes the program ready to use the GamePhases introduced in the given file. This 
	 * should be called after the other resource types have been initialized.
	 * @param fileName The name of the file that contains gamePhase data 
	 * ("data/" automatically included).<br> 
	 * The file should have the following format:<br>
	 * &bankName<br>
	 * GamePhaseName#ResourceType:ResourceBankName1,ResourceBankName2,...#ResourceType2:...#...<br>
	 * GamePhaseName2#...<br>
	 * ...
	 * @param bankName The name of the GamePhaseBank that will be activated initially
	 */
	public static void initializeGamePhaseResources(String fileName, String bankName)
	{
		initializeGamePhaseResources(fileName);
		MultiMediaHolder.activateBank(MetaResourceType.GAMEPHASE, bankName, false);
		defaultPhaseBankName = bankName;
	}
	
	/**
	 * Initializes the program ready to use the GamePhases introduced in the given file.
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
				MetaResourceType.GAMEPHASE);
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
		Bank<?> bank = MultiMediaHolder.getBank(MetaResourceType.GAMEPHASE, bankName);
		
		return (Bank<GamePhase>) bank;
	}
	
	/**
	 * Retrieves a gamePhase from the a gamePhaseBank
	 * @param bankName The name of the bank the phase is retrieved from
	 * @param gamePhaseName The name of the GamePhase in the bank
	 * @return A gamePhase from the bank
	 */
	public static GamePhase getGamePhase(String bankName, String gamePhaseName)
	{
		return getGamePhaseBank(bankName).get(gamePhaseName);
	}
	
	/**
	 * Retrieves a GamePhase from the default gamePhaseBank introduced upon resource initiation.
	 * @param gamePhaseName The name of the GamePhase in the default bank.
	 * @return A gamePhase with the given name.
	 */
	public static GamePhase getGamePhase(String gamePhaseName)
	{
		if (defaultPhaseBankName == null)
			throw new ResourceUnavailableException(
					"The name of the default GamePhaseBank hasn't been introduced.");
		
		return getGamePhase(defaultPhaseBankName, gamePhaseName);
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
		public Bank<GamePhase> construct(String line, Bank<Bank<GamePhase>> bank)
		{
			Bank<GamePhase> newBank = new Bank<>();
			bank.put(line, newBank);
			return newBank;
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
		public GamePhase construct(String line, Bank<GamePhase> bank)
		{
			String[] arguments = line.split("#");
			
			// Checks that there are enough arguments
			if (arguments.length < 1)
				throw new ResourceInitializationException("Couldn't load a GamePhase. Line " + 
						line + "doensn't have enough arguments");
			
			Map<ResourceType, String[]> banknames = new HashMap<>();
					
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
			
			for (ResourceType type : banknames.keySet())
			{
				newPhase.connectResourceBankNames(type,  banknames.get(type));
			}
			
			bank.put(arguments[0], newPhase);
			
			return newPhase;
		}
	}
}

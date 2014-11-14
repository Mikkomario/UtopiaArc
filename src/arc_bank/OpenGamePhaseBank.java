package arc_bank;

import java.util.ArrayList;
import java.util.HashMap;

import arc_resource.GamePhase;
import arc_resource.MetaResource;
import arc_resource.Resource;

/**
 * OpenGamePhaseBank is a gamePhaseBank that creates its contents using a list 
 * of commands.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class OpenGamePhaseBank extends GamePhaseBank implements OpenBank
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<String> creationCommands;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new GamePhaseBank.
	 * @param creationCommands A list of commands the bank will use to 
	 * initialize itself. The commands should follow the following 
	 * style:<br>
	 * phasename#resourcetypename1:bankname1,bankname2,bankname3, ... #resourcetypename2: ... # ...
	 */
	public OpenGamePhaseBank(ArrayList<String> creationCommands)
	{
		// Initializes attributes
		this.creationCommands = creationCommands;
	}
	
	
	// IMPLEMENTED METHODS	-------------------------------------------------

	@Override
	protected void initialize()
	{
		// Creates the gamephases by going through the commands
		for (int i = 0; i < this.creationCommands.size(); i++)
		{
			String command = this.creationCommands.get(i);
			String[] arguments = command.split("#");
			
			// Checks that there are enough arguments
			if (arguments.length < 1)
			{
				System.err.println("Couldn't load a GamePhase. Line " + command + 
						"doensn't have enough arguments");
				continue;
			}
			
			HashMap<Resource, String[]> banknames = 
					new HashMap<Resource, String[]>();
					
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
			
			addObject(newPhase, arguments[0]);
		}
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Returns an GamePhaseBank if it has been initialized
	 *
	 * @param bankname The name of the needed bank
	 * @return The GamePhaseBank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static GamePhaseBank getGamePhaseBank(String bankname)
	{
		OpenBank maybegamephasebank = MultiMediaHolder.getBank(MetaResource.GAMEPHASE, bankname);
		
		if (maybegamephasebank instanceof GamePhaseBank)
			return (GamePhaseBank) maybegamephasebank;
		else
			return null;
	}
}

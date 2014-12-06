package arc_resource;

import java.util.ArrayList;
import java.util.List;

import arc_bank.MultiMediaHolder;

/**
 * ResourceActivator activates resources from certain gamePhases at a time. 
 * Resources that aren't used in the current gamePhase are deactivated until 
 * they are needed again. The class is completely static so no instance is 
 * needed.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class ResourceActivator
{
	// ATTRIBUTES	------------------------------------------------------
	
	private static List<GamePhase> currentPhases = new ArrayList<>();
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	private ResourceActivator()
	{
		// the Constuctor is hidden since the class is completely static
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Starts a new Gamephase, activating new resources in the process. Previous phases can 
	 * be deactivated here as well.
	 * 
	 * @param phase The new gamePhase to be started
	 * @param endPreviousPhases Should the previous phase(s) be ended
	 */
	public static void startPhase(GamePhase phase, boolean endPreviousPhases)
	{
		List<GamePhase> newPhases = new ArrayList<>();
		
		if (!endPreviousPhases)
		{
			newPhases.addAll(currentPhases);
			
			// If the phase was already active, no change is required
			if (newPhases.contains(phase))
				return;
		}
		
		newPhases.add(phase);
		
		updatePhaseList(newPhases);
	}
	
	/**
	 * Ends the given gamePhase, releasing resources wherever possible
	 * @param phase The phase that is deactivated / ended
	 */
	public static void endPhase(GamePhase phase)
	{
		if (!currentPhases.contains(phase))
			return;
		
		List<GamePhase> newPhases = new ArrayList<>();
		newPhases.addAll(currentPhases);
		newPhases.remove(phase);
		
		updatePhaseList(newPhases);
	}
	
	private static void updatePhaseList(List<GamePhase> newPhases)
	{
		// Updates the loaded resources
		for (ResourceType type : MultiMediaHolder.getHeldResourceTypes())
		{
			updateResourceBanks(newPhases, type);
		}
		
		// Remembers the new active phase
		currentPhases = newPhases;
	}
	
	private static void updateResourceBanks(List<GamePhase> newPhases, 
			ResourceType resourceType)
	{
		List<String> newBankNames = getBankNames(newPhases, resourceType);
		
		// Skips the removal process if there was no previous phase
		if (!currentPhases.isEmpty())
		{
			List<String> oldBankNames = getBankNames(currentPhases, resourceType);
			
			// Removes the old banks that aren't active in the new phase
			for (String oldBankName : oldBankNames)
			{
				if (!newBankNames.contains(oldBankName))
					MultiMediaHolder.deactivateBank(resourceType, oldBankName);
			}
		}
		
		// Adds all the new banks that weren't active already
		// (Checking done in the MultiMediaHolder)
		for (String newBankName : newBankNames)
		{
			// TODO: True or false, now there is a question
			MultiMediaHolder.activateBank(resourceType, newBankName, false);
		}
	}
	
	private static List<String> getBankNames(List<GamePhase> phases, ResourceType resourceType)
	{
		List<String> bankNames = new ArrayList<>();
		
		for (GamePhase phase : phases)
		{
			for (int i = 0; i < phase.getConnectedResourceBankNames(resourceType).length; i++)
			{
				bankNames.add(phase.getConnectedResourceBankNames(resourceType)[i]);
			}
		}
		
		return bankNames;
	}
}

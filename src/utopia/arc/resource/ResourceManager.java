package utopia.arc.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utopia.arc.resource.BankRecorder.RecordingFailedException;

/**
 * This class manages multiple resource types, activating and deactivating the banks when 
 * certain phases come into play. This class is not used for accessing the resource banks, though.
 * @author Mikko Hilpinen
 * @since 12.5.2016
 */
public class ResourceManager
{
	// ATTRIBUTES	-------------------
	
	private List<BankBank<?>> banks = new ArrayList<>();
	private List<Phase> currentPhases = new ArrayList<>();
	private Map<String, Phase> knownPhases = new HashMap<>();
	
	
	// CONSTRUCTOR	-------------------
	
	/**
	 * Creates a new empty resource manager. The banks and phases need to be introduced separately
	 */
	public ResourceManager()
	{
		// Empty constructor
	}

	/**
	 * Creates a new resource manager with set banks and phases
	 * @param banks The banks managed by the resource manager
	 * @param phases The phases known by the resource manager
	 */
	public ResourceManager(Collection<? extends BankBank<?>> banks, Collection<? extends Phase> phases)
	{
		introduceBanks(banks);
		introducePhases(phases);
	}
	
	
	// OTHER METHODS	---------------
	
	/**
	 * Adds a new bank to be managed by this resource manager
	 * @param bank The bank that will be managed by the manager
	 */
	public void introduceBank(BankBank<?> bank)
	{
		if (!this.banks.contains(bank))
			this.banks.add(bank);
	}
	
	/**
	 * Adds multiple banks to be managed by this resource manager
	 * @param banks The banks that will be managed by this manager
	 */
	public void introduceBanks(Collection<? extends BankBank<?>> banks)
	{
		for (BankBank<?> bank : banks)
		{
			introduceBank(bank);
		}
	}
	
	/**
	 * Introduces a phase to this manager
	 * @param phase The phase introduced to this manager
	 */
	public void introducePhase(Phase phase)
	{
		if (!this.knownPhases.containsKey(phase.getName().toLowerCase()))
			this.knownPhases.put(phase.getName().toLowerCase(), phase);
	}
	
	/**
	 * Introduces multiple phases to this manager
	 * @param phases The phases introduced to this manager
	 */
	public void introducePhases(Collection<? extends Phase> phases)
	{
		for (Phase phase : phases)
		{
			introducePhase(phase);
		}
	}
	
	/**
	 * Finds a phase with the provided name. Only works with phases introduced to the manager
	 * @param phaseName The name of the phase
	 * @return The phase with the provided name (case-insensitive)
	 * @throws PhaseNotIntroducedException If the phase hasn't been introduced to the manager
	 */
	public Phase getPhase(String phaseName) throws PhaseNotIntroducedException
	{
		if (!this.knownPhases.containsKey(phaseName.toLowerCase()))
			throw new PhaseNotIntroducedException(phaseName);
		return this.knownPhases.get(phaseName.toLowerCase());
	}
	
	public void startPhase(Phase phase, boolean endOtherPhases) throws RecordingFailedException
	{
		if (endOtherPhases)
			this.currentPhases.clear();
		this.currentPhases.add(phase);
		updateBanks();
	}
	
	public void startPhase(String phaseName, boolean endOtherPhases)
	{
		Phase newPhase = getPhase(phaseName);
		
	}
	
	public void switchPhase(Phase oldPhase, Phase newPhase) throws RecordingFailedException
	{
		this.currentPhases.remove(oldPhase);
		if (!this.currentPhases.contains(newPhase))
			this.currentPhases.add(newPhase);
		updateBanks();
	}
	
	public void endPhase(Phase phase) throws RecordingFailedException
	{
		if (this.currentPhases.contains(phase))
		{
			this.currentPhases.remove(phase);
			updateBanks();
		}
	}
	
	public void endPhase(String phaseName) throws RecordingFailedException
	{
		Phase phase = getPhase(phaseName);
		if (phase != null)
			endPhase(phase);
	}
	
	private void updateBanks() throws RecordingFailedException
	{
		// Goes through each bank and activates / deactivates it based on the current phases
		for (BankBank<?> bankbank : this.banks)
		{
			for (Bank<?> bank : bankbank.getBanks())
			{
				boolean shouldBeActive = false;
				for (Phase phase : this.currentPhases)
				{
					if (phase.bankIsActive(bank))
					{
						shouldBeActive = true;
						break;
					}
				}
				
				// Activates or deactivates the bank
				if (shouldBeActive)
					bank.initialise();
				else
					bank.uninitialise();
			}
		}
	}
	
	
	// NESTED CLASSES	-----------------
	
	/**
	 * These exceptions are thrown when trying to use a non-existing phase or one that hasn't 
	 * been introduced yet
	 * @author Mikko Hilpinen
	 * @since 12.5.2016
	 */
	public static class PhaseNotIntroducedException extends RuntimeException
	{
		private static final long serialVersionUID = 5003493962012116061L;
		
		/**
		 * Creates a new exception
		 * @param phaseName The name of the requested phase
		 */
		public PhaseNotIntroducedException(String phaseName)
		{
			super("Phase '" + phaseName + "' hasn't been introduced yet");
		}
	}
}
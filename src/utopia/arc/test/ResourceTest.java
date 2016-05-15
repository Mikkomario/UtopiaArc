package utopia.arc.test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import utopia.arc.generics.ArcDataType;
import utopia.arc.io.PhaseRecorder;
import utopia.arc.io.XmlFileBankRecorder;
import utopia.arc.resource.Bank;
import utopia.arc.resource.BankBank;
import utopia.arc.resource.BankRecorder;
import utopia.arc.resource.Phase;
import utopia.arc.resource.ResourceManager;
import utopia.flow.generics.BasicDataType;
import utopia.flow.generics.DataType;

/**
 * This class is used for testing the resource and io functions of the Arc project
 * @author Mikko Hilpinen
 * @since 15.5.2015
 */
class ResourceTest
{
	/**
	 * Runs the test
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		try
		{
			ArcDataType.initialise();
			
			// Creates the phases
			System.out.println("\nCREATING PHASES");
			ResourceManager manager1 = new ResourceManager();
			
			Phase phase1 = new Phase("phase1");
			phase1.addActiveBank(BasicDataType.STRING, "stringForAll");
			phase1.addActiveBank(BasicDataType.STRING, "stringFor1Only");
			phase1.addActiveBank(BasicDataType.INTEGER, "sharedInteger12");
			manager1.introducePhase(phase1);
			
			Phase phase2 = new Phase("phase2");
			phase2.addActiveBank(BasicDataType.STRING, "stringForAll");
			phase2.addActiveBank(BasicDataType.INTEGER, "integerFor2Only");
			phase2.addActiveBank(BasicDataType.INTEGER, "sharedInteger12");
			manager1.introducePhase(phase2);
			
			Phase phase3 = new Phase("phase3");
			phase3.addActiveBank(BasicDataType.STRING, "stringForAll");
			manager1.introducePhase(phase3);
			
			printPhases(manager1);
			
			// Adds the banks
			System.out.println("\nCREATING BANKS");
			Path bankPath = Paths.get("test", "resources");
			BankRecorder recorder = new XmlFileBankRecorder(bankPath);
			
			BankBank<String> strings = new BankBank<>(BasicDataType.STRING, recorder, false);
			BankBank<Integer> ints = new BankBank<>(BasicDataType.INTEGER, recorder, false);
			
			manager1.introduceBank(strings);
			manager1.introduceBank(ints);
			manager1.generateBanksBasedOnPhases();
			
			// Adds some resources to the banks
			strings.put("stringForAll", "string1", "Tämä on string1");
			strings.put("stringForAll", "xml", "<root><asd/></root>");
			strings.put("stringFor1Only", "secret", "Secret string for phase 1 only");
			
			ints.put("sharedInteger12", "width", 1920);
			ints.put("sharedInteger12", "height", 1080);
			ints.put("integerFor2Only", "secret", 70 * 7);
			
			printBanks(manager1);
			
			// Saves the banks and phases into a local folder
			System.out.println("\nWRITING BANKS");
			manager1.saveBanks();
			File phaseFile = bankPath.resolve("phases.xml").toFile();
			PhaseRecorder.writePhasesWithXml(manager1.getPhases(), phaseFile, true);
			
			// Reads the phases
			System.out.println("\nREADING PHASES");
			ResourceManager manager2 = new ResourceManager();
			manager2.introducePhases(PhaseRecorder.readPhasesFromXml(phaseFile, true));
			printPhases(manager2);
			
			// Reads the banks
			System.out.println("\nREADING BANKS (no phase active)");
			manager2.introduceBank(new BankBank<>(BasicDataType.STRING, recorder, true));
			manager2.introduceBank(new BankBank<>(BasicDataType.INTEGER, recorder, true));
			printBanks(manager2);
			
			// Starting phases
			System.out.println("\nSTARTING PHASE 1");
			manager2.startPhase("phase1", false);
			printBanks(manager2);
			
			System.out.println("\nSWITCHING TO PHASE 2");
			manager2.switchPhase("phase1", "phase2");
			printBanks(manager2);
			
			System.out.println("\nSTARTING PHASE 3");
			manager2.startPhase("phase3", false);
			printBanks(manager2);
			
			System.out.println("\nENDING PHASE 2");
			manager2.endPhase("phase2");
			printBanks(manager2);
		}
		catch (Exception e)
		{
			System.err.println("Failed");
			e.printStackTrace();
		}
	}
	
	private static void printPhases(ResourceManager manager)
	{
		for (Phase phase : manager.getPhases())
		{
			printPhase(phase);
		}
	}
	
	private static void printPhase(Phase phase)
	{
		System.out.println(phase.getName());
		for (DataType resourceType : phase.getResourceTypes())
		{
			StringBuilder s = new StringBuilder("\t");
			s.append(resourceType.getName());
			s.append(":");
			for (String bankName : phase.getActiveBankNames(resourceType))
			{
				s.append(" ");
				s.append(bankName);
			}
			System.out.println(s);
		}
	}
	
	private static void printBanks(ResourceManager manager)
	{
		for (BankBank<?> bank : manager.getBanks())
		{
			System.out.println("\n" + bank.getContentType());
			printBank(bank);
		}
	}
	
	private static void printBank(BankBank<?> bankbank)
	{
		for (Bank<?> bank : bankbank.getBanks())
		{
			System.out.println("Bank " + bank.getName());
			System.out.println(bank.toString());
		}
	}
}

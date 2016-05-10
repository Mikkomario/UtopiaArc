package utopia.arc.resource;

import java.util.Collection;

import utopia.flow.generics.DataType;

/**
 * Phases represent different phases in a program's life cycle. Different resources are 
 * connected to each phase
 * @author Mikko Hilpinen
 * @since 10.5.2016
 */
public interface Phase
{
	/**
	 * Finds the names of the banks of a certain type, that are used during this phase
	 * @param resourceType The type of resource in question
	 * @return The banks of the provided resource type that should be kept available while 
	 * the phase persists
	 */
	public Collection<String> getActiveBankNames(DataType resourceType);
}

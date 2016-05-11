package arc_resource;

import utopia.arc.resource.Phase;
import utopia.flow.generics.DataType;

/**
 * MetaResources are resource that are used in resource handling
 * 
 * @author Mikko Hilpinen
 * @since 27.7.2014
 * @deprecated Replaced with {@link DataType}, plus the new {@link Phase} is not a resource anymore
 */
public enum MetaResourceType implements ResourceType
{
	/**
	 * GamePhase represents a state in the program's flow. GamePhases contain a certain set 
	 * of resources.
	 * @see GamePhase
	 */
	GAMEPHASE;
}

package arc_resource;

import utopia.flow.generics.DataType;

/**
 * Resource represents an external resource used in the game. Resources are managed by 
 * specific banks and can be initialized with external files. The classes that implement 
 * this interface should be enumerations.
 * 
 * @author Mikko Hilpinen
 * @since 22.7.2014
 * @deprecated Replaced with {@link DataType}
 */
public interface ResourceType
{
	// Resource is used as a wrapper and doesn't limit classes that inherit it
}

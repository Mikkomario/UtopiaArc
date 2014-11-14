package arc_resource;

import java.util.ArrayList;

/**
 * ResourceHandler keeps track of all the different resource types used in the program
 * 
 * @author Mikko Hilpinen
 * @since 27.7.2014
 */
class ResourceHandler
{
	// ATTRIBUTES	--------------------------------------------------------------
	
	private static ArrayList<Resource> resources;
	
	
	// CONSTRUCTOR	----------------------------------------------------------
	
	private ResourceHandler()
	{
		// The constructor is hidden from other objects since the interface is static
	}

	
	// OTHER METHODS	------------------------------------------------------
	
	protected void addResource(Resource resource)
	{
		if (resource != null && !resources.contains(resource))
			resources.add(resource);
	}
}

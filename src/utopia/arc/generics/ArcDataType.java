package utopia.arc.generics;

import utopia.arc.resource.Phase;
import utopia.flow.generics.BasicDataType;
import utopia.flow.generics.DataType;
import utopia.flow.generics.DataTypeTreeNode;
import utopia.flow.generics.DataTypes;
import utopia.flow.generics.Value;

/**
 * These are the data type introduced in the Arc project
 * @author Mikko Hilpinen
 * @since 14.5.2016
 */
public enum ArcDataType implements DataType
{
	/**
	 * A phase represents a single phase in a program's run time
	 * @see Phase
	 */
	PHASE;
	
	
	// ATTRIBUTES	--------------
	
	private static boolean initialised = false;
	
	
	// IMPLEMENTED METHODS	------

	@Override
	public String getName()
	{
		return toString();
	}

	
	// OTHER METHODS	---------
	
	/**
	 * Initialises the data types introduced in the arc project. Before this method is called, 
	 * some resources and functions will not work properly
	 */
	public static void initialise()
	{
		if (!initialised)
		{
			initialised = true;
			
			// The phase data type is just under the object data type
			DataTypes dataTypes = DataTypes.getInstance();
			dataTypes.add(new DataTypeTreeNode(PHASE, dataTypes.get(BasicDataType.OBJECT)));
			
			// Adds parsing for the new type(s) as well
			dataTypes.addParser(ArcDataTypeParser.getInstance());
			// Plus element parsing
			dataTypes.introduceSpecialParser(new ArcElementValueParser());
		}
	}
	
	/**
	 * Wraps a phase into a value
	 * @param phase The phase that is wrapped
	 * @return The phase wrapped into value
	 */
	public static Value Phase(Phase phase)
	{
		return new Value(phase, PHASE);
	}
	
	/**
	 * Returns the phase value of a value
	 * @param value A value
	 * @return The phase value of that value
	 */
	public static Phase valueToPhase(Value value)
	{
		return (Phase) value.parseTo(PHASE);
	}
}

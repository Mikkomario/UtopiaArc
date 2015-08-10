package arc_test;

import genesis_util.Killable;
import genesis_util.StateOperator;

/**
 * Used as a simple test entity
 * 
 * @author Mikko Hilpinen
 * @since 30.11.2014
 */
public class TestResource implements Killable
{
	// ATTRIBUTES	--------------------------
	
	private String data;
	private StateOperator isDeadOperator;
	
	
	// CONSTRUCTOR	----------------------------
	
	/**
	 * Creates a new resource with the given data
	 * @param data The content of the object
	 */
	public TestResource(String data)
	{
		this.data = data;
		this.isDeadOperator = new StateOperator(false, true);
	}
	
	
	// IMPLEMENTED METHODS	-------------------
	
	@Override
	public String toString()
	{
		return this.data;
	}
	
	
	// GETTERS & SETTERS	-------------------
	
	/**
	 * @return The content of this object
	 */
	public String getData()
	{
		return this.data;
	}
	
	
	// OTHER METHODS	-----------------------

	@Override
	public StateOperator getIsDeadStateOperator()
	{
		return this.isDeadOperator;
	}
}

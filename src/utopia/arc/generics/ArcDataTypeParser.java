package utopia.arc.generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import utopia.arc.resource.Phase;
import utopia.flow.generics.BasicDataType;
import utopia.flow.generics.Conversion;
import utopia.flow.generics.ConversionReliability;
import utopia.flow.generics.DataType;
import utopia.flow.generics.Model;
import utopia.flow.generics.Value;
import utopia.flow.generics.ValueList;
import utopia.flow.generics.ValueParser;
import utopia.flow.generics.Variable;

/**
 * This parser is able to parse data types introduced in the Arc project
 * @author Mikko Hilpinen
 * @since 14.5.2016
 */
public class ArcDataTypeParser implements ValueParser
{
	// ATTRIBUTES	----------------
	
	private static ArcDataTypeParser instance = null;
	private List<Conversion> conversions = new ArrayList<>();
	
	
	// CONSTRUCTOR	----------------
	
	private ArcDataTypeParser()
	{
		// Adds supported conversions
		// Perfect cast from phase to model and nothing else
		this.conversions.add(new Conversion(ArcDataType.PHASE, BasicDataType.MODEL, 
				ConversionReliability.PERFECT));
	}
	
	/**
	 * @return The singular parser instance
	 */
	public static ArcDataTypeParser getInstance()
	{
		if (instance == null)
			instance = new ArcDataTypeParser();
		return instance;
	}
	
	
	// IMPLEMENTED METHODS	-------

	@Override
	public Value cast(Value value, DataType to) throws ValueParseException
	{	
		// Can only cast from PHASE into a model. Other conversions aren't supported
		// (except  object to string provided by the basic parser)
		if (value.getType().equals(ArcDataType.PHASE) && to.equals(BasicDataType.MODEL))
		{
			Phase phase = ArcDataType.valueToPhase(value);
			Model<Variable> model = Model.createBasicModel();
			
			for (DataType resourceType : phase.getResourceTypes())
			{
				ValueList bankNameList = new ValueList(BasicDataType.STRING);
				for (String bankName : phase.getActiveBankNames(resourceType))
				{
					bankNameList.add(Value.String(bankName));
				}
				model.addAttribute(resourceType.getName(), Value.List(bankNameList), true);
			}
			
			return Value.Model(model);
		}
		
		throw new ValueParseException(value, to);
	}

	@Override
	public Collection<? extends Conversion> getConversions()
	{
		return this.conversions;
	}
}

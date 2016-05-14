package utopia.arc.generics;

import utopia.arc.resource.Phase;
import utopia.flow.generics.DataType;
import utopia.flow.generics.DataTypes;
import utopia.flow.generics.Value;
import utopia.flow.io.ElementValueParser;
import utopia.flow.structure.Element;
import utopia.flow.structure.TreeNode;

/**
 * This class handles the exceptions in value parsing when it comes to Arc specific data types
 * @author Mikko Hilpinen
 * @since 14.5.2016
 */
public class ArcElementValueParser implements ElementValueParser
{
	@Override
	public DataType[] getParsedTypes()
	{
		return new DataType[]{ArcDataType.PHASE};
	}

	@Override
	public TreeNode<Element> writeValue(Value value) throws ElementValueParsingFailedException
	{
		if (value.getType().equals(ArcDataType.PHASE))
		{
			Phase phase = ArcDataType.valueToPhase(value);
			
			// Phases are written in 3 layer elements (name / data type / bank name)
			TreeNode<Element> root = new TreeNode<>(new Element(phase.getName()));
			for (DataType resourceType : phase.getResourceTypes())
			{
				TreeNode<Element> typeElement = new TreeNode<>(
						new Element(resourceType.getName()), root);
				for (String bankName : phase.getActiveBankNames(resourceType))
				{
					typeElement.addChild(new TreeNode<>(new Element(bankName)));
				}
			}
			
			return root;
		}
		
		throw new ElementValueParsingFailedException("Unsupported data type " + 
				value.getType().getName());
	}

	@Override
	public Value readValue(TreeNode<Element> element, DataType targetType)
			throws ElementValueParsingFailedException
	{
		if (targetType.equals(ArcDataType.PHASE))
		{
			Phase phase = new Phase(element.getContent().getName());
			for (TreeNode<Element> typeElement : element.getChildren())
			{
				DataType resourceType = DataTypes.parseType(typeElement.getContent().getName());
				for (TreeNode<Element> bankElement : typeElement.getChildren())
				{
					phase.addActiveBank(resourceType, bankElement.getContent().getName());
				}
			}
			
			return ArcDataType.Phase(phase);
		}
		
		throw new ElementValueParsingFailedException("Unsupported target type " + targetType.getName());
	}

}

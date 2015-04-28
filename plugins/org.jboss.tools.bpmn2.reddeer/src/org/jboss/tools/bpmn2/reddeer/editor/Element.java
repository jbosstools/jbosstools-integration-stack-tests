package org.jboss.tools.bpmn2.reddeer.editor;

import static org.hamcrest.Matchers.allOf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.hamcrest.Matcher;
import org.jboss.reddeer.gef.GEFLayerException;
import org.jboss.reddeer.gef.impl.editpart.AbstractEditPart;
import org.jboss.reddeer.gef.impl.editpart.LabeledEditPart;
import org.jboss.reddeer.graphiti.impl.graphitieditpart.LabeledGraphitiEditPart;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.bpmn2.reddeer.AbsoluteEditPart;
import org.jboss.tools.bpmn2.reddeer.GEFProcessEditor;
import org.jboss.tools.bpmn2.reddeer.JBPM6ComplexEnvironment;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.reddeer.PropertiesHandler;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.BoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructOfType;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructOnPoint;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructWithName;
import org.jboss.tools.bpmn2.reddeer.matcher.EditPartOfClassName;
import org.jboss.tools.bpmn2.reddeer.properties.setup.LabeledTextSetUp;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Represents an element in the editor canvas.
 */
public class Element {
	
	private boolean useGraphitiProperties = 
		JBPM6ComplexEnvironment.getInstance().useGraphiti();

	protected Logger log = Logger.getLogger(getClass());
	
	protected ProcessEditorView editor;
	protected PropertiesHandler propertiesHandler;
	
	protected String name;
	protected ElementType type;
	protected Element parent;
	protected AbsoluteEditPart containerShapeEditPart;
	protected GEFProcessEditor processEditor;
	
	protected Element(){
		
	}
	
	/**
	 * @param name
	 * @param type
	 */
	public Element(String name, ElementType type) {
		this(name, type, null);
	}

	/**
	 * @param name
	 * @param type
	 * @param parent
	 */
	public Element(String name, ElementType type, Element parent) {
		this(name, type, parent, 0, true);
	}
	
	/**
	 * Creates a new instance of Construct. The Construct must already be present
	 * in the editor. If not found base either based on ${name} or ${type} then a
	 * RuntimeException will be thrown. 
	 * 
	 * @param name
	 * @param type
	 * @param parent
	 * @param index
	 * @param select
	 */
	public Element(String name, ElementType type, Element parent, int index, boolean select) {
		this.name = name;
		this.type = type;
		this.parent = parent;
		
		setUp(name, type, parent, index, select);
	}
	
	protected Element(EditPart containerShapeEditPart, ElementType type) {
		this.type = type;
		this.containerShapeEditPart = new AbsoluteEditPart(containerShapeEditPart);
		processEditor = new GEFProcessEditor();
		editor = new ProcessEditorView();
		propertiesHandler = new PropertiesHandler(containerShapeEditPart, useGraphitiProperties);
	}
	
	protected Element(Element copyFrom) {
		this.name = copyFrom.name;
		this.type = copyFrom.type;
		this.parent = copyFrom.parent;
		this.editor = copyFrom.editor;
		this.containerShapeEditPart = copyFrom.containerShapeEditPart;
		this.processEditor = copyFrom.processEditor;
		this.propertiesHandler = copyFrom.propertiesHandler;
		select();
	}
	
	private void setUp(String name, ElementType type, Element parent, int index, boolean select) {
		processEditor = new GEFProcessEditor();
		editor = new ProcessEditorView();
		
		List<Matcher<? super EditPart>> matcherList = new ArrayList<Matcher<? super EditPart>>();
		if (name != null) {
			matcherList.add(new ConstructWithName<EditPart>(name));
		}
		if (type != null) {
			matcherList.add(new ConstructOfType<EditPart>(type));
		}
		
		if(type != ElementType.PROCESS) {
			matcherList.add(new EditPartOfClassName("ContainerShapeEditPart"));
			containerShapeEditPart = new AbsoluteEditPart(allOf(matcherList), index);
		} else {
			containerShapeEditPart = new AbsoluteEditPart(processEditor.getRootEditPart());
		}

		if (containerShapeEditPart == null) {
			throw new RuntimeException("Reddeer could not find construct with name '" + name + "' of type '" + type.name() + "'");
		}

		propertiesHandler = new PropertiesHandler(containerShapeEditPart.getEditPart(), useGraphitiProperties);
	
		if (select) {
			select();
		}
		
		if(type == ElementType.PROCESS) {
			containerShapeEditPart.click(0,0);
			if(name == null) {
				propertiesHandler.activatePropertiesView();
				this.name = propertiesHandler.getTitleOfPropertiesView();
			}
		}
	}
	
	/**
	 * 
	 */
	public void refresh() {
		setUp(name, type, parent, 0, true);
	}
	
	/**
	 * Set the name of this construct.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		propertiesHandler.setUp(new LabeledTextSetUp("General", "Name", name));
	}
	
	/**
	 * Select this construct. Select the pictogram not the label.
	 */
	public void select() {
		if(name != null && type != ElementType.PROCESS) {
			new LabeledEditPart(name).select();
		} else {
			containerShapeEditPart.select();
		}
	}

	/**
	 * Click to label of construct
	 */
	public void click() {
		containerShapeEditPart.click();
	}
	
	/**
	 * Add a boundary event to this construct.
	 *  
	 * @param name
	 * @param eventType
	 */
	protected <T extends BoundaryEvent> T addEvent(String name, ElementType eventType, Class<T> asType) {
		if (!eventType.name().endsWith("BOUNDARY_EVENT")) {
			throw new IllegalArgumentException("Can add only BOUNDARY_EVENT types.");
		}

		Point p = getBounds().getTopLeft();
		Element newEvent = putToCanvas(name, eventType, p, containerShapeEditPart.getEditPart().getParent());
		newEvent.name = name;
		
		try {
			return (T) asType.getConstructor(Element.class).newInstance(newEvent);
		} catch (Exception e) {
			throw new RuntimeException("Could not create an instance of type '" + asType + "'", e);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 */
	public Element append(String name, ElementType constructType) {
		return append(name, constructType, ConnectionType.SEQUENCE_FLOW, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param position
	 */
	public Element append(String name, ElementType constructType, Position position) {
		return append(name, constructType, ConnectionType.SEQUENCE_FLOW, position);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param connectionType
	 */
	public Element append(String name, ElementType constructType, ConnectionType connectionType) {
		return append(name, constructType, connectionType, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param connectionType
	 * @param relativePosition
	 */
	public Element append(String name, ElementType constructType, ConnectionType connectionType, Position relativePosition) {
		select();
		log.info("Appending construct name '" + name + "' of type '" + constructType + "' after construct with name '" + this.name + "'.");
		
		Point point = findPoint(parent, this, relativePosition);
		if (!isAvailable(point)) {
			throw new RuntimeException(point + " is not available ");
		}
		
		Element construct = putToCanvas(name, constructType, point, containerShapeEditPart.getEditPart().getParent());
		
		connectTo(construct, connectionType);
		
		Element specificInstance = null;
		
		try {
			specificInstance = (Element) constructType.getJavaClass().getConstructor(Element.class).newInstance(construct);
		} catch (Exception e) {
			// will be returned null
		}
		
		return specificInstance;
	}
	
	/**
	 * 
	 * @param construct
	 */
	public void connectTo(Element construct) {
		connectTo(construct, ConnectionType.SEQUENCE_FLOW);
	}
	
	/**
	 * 
	 * Connect this construct to another one using a connector
	 * of type ${type}.
	 * 
	 * @param toElement
	 * @param connectionType
	 */
	public void connectTo(Element toElement, ConnectionType connectionType) {
	    new WaitUntil(new ConnectionAdded(this, toElement, connectionType));
	}
	
	/**
	 * Delete this construct. Cannot be undone.
	 */
	public void delete() {
		select();
		new LabeledGraphitiEditPart(name).getContextButton("Delete").click();
	}
	
	/**
	 * 
	 * @return
	 */
	public ElementType getType() {
		return type;
	}
	
	/**
	 * Check if ${p} in the parent is not covered by another construct.
	 *  
	 * @param p
	 * @return
	 */
	protected boolean isAvailable(Point p) {
		// Check weather the point is not already taken by another child editPart.
		List<EditPart> result = processEditor.getAllChildContainerShapeEditParts(containerShapeEditPart.getEditPart().getParent(), new ConstructOnPoint<EditPart>(p));
		return result.isEmpty();
	}
	
	/**
	 * Get a point inside ${parent} and next to ${child} with relative
	 * ${position} to which another child can be added. 
	 * 
	 * @param parent
	 * @param nextToChild
	 * @param relativePosition
	 * @return
	 */
	protected Point findPoint(Element parent, Element nextToChild, Position relativePosition) {
		Rectangle childBounds = nextToChild.getBounds();
		
		int childStartX = childBounds.x();
		int childEndX = childBounds.right();
		
		int childStartY = childBounds.y();
		int childEndY = childBounds.bottom();
		
		int childCenterX = childBounds.getCenter().x();
		int childCenterY = childBounds.getCenter().y();
		
		int nearOffset  = 75; //nextToChild.type.isContainer() ? 2 * 75 : 75;
		int farOffset = 100; //nextToChild.type.isContainer() ? 2 * 100 : 100;
		
		Point point = new Point(-1, -1);
		switch (relativePosition) {
			case NORTH:
				point.x = childCenterX;
				point.y = childStartY - farOffset;
				break;
			case NORTH_EAST:
				point.x = childEndX + nearOffset;
				point.y = childStartY - farOffset;
				break;
			case EAST:
				point.x = childEndX + farOffset;
				point.y = childCenterY;
				break;
			case SOUTH_EAST:
				point.x = childEndX  + nearOffset;
				point.y = childEndY + farOffset;
				break;
			case SOUTH:
				point.x = childCenterX;
				point.y = childEndY + farOffset;
				break;
			case SOUTH_WEST:
				point.x = childStartX - nearOffset;
				point.y = childEndY + farOffset;
				break;
			case WEST:
				point.x = childStartX - farOffset;
				point.y = childCenterY;
				break;
			case NORTH_WEST:
				point.x = childStartX - nearOffset;
				point.y = childStartY - farOffset;
				break;
			default:
				throw new UnsupportedOperationException();
		}
		// Check parent bounds. In case the point (marked as '+') is out of bounds.
		// 	- '*'  signals a properly added construct.
		// 	- '->' marks a connection between to '*'
		// 
		//     + N
		//    ________
		// + |        |
		//   |        | +
		// W | *->*   | E
		//   |________|
		//       +
        //       S
		if (parent != null) {
			Rectangle parentBounds = parent.getBounds();
			
			int parentStartX = parentBounds.x();
			int parentEndX = parentBounds.right();
			
			int parentStartY = parentBounds.y();
			int parentEndY = parentBounds.bottom();
			
			if (point.x < parentStartX) point.x = parentStartX;
			if (point.x > parentEndX) point.x = parentEndX;
			if (point.y < parentStartY) point.y = parentStartY;
			if (point.y > parentEndY) point.y = parentEndY;
			
			// Transform the point so it appears to be relative to the activity given
			// by parent not the whole canvas.
			point.x = point.x() - parentBounds.x();
			point.y = point.y() - parentBounds.y();
		}

		return point;
	}
	
	/**
	 * Validate this construct. At this moment only attributes are validated. Element structure
	 * is validated using a XSD schema via the BPMN2Validator and or the JBPM6Validator.
	 * 
	 * @param attributes expected attribute names
	 * @param values     expected attribute values
	 * 
	 * @return
	 */
	public List<String> validate(String[] attributes, String[] values) {
		log.info("Validating construct '" + name + "' of type '" + type + "'");
		/*
		 * check bounds.
		 */
		if (attributes.length != values.length) {
			throw new RuntimeException("Attribute names size '" + attributes.length + "' is not equal to attribute values size '" + values.length + "'");
		}
		// Validate attributes
		List<String> errors = new ArrayList<String>();
		org.w3c.dom.Element e = getEditPartElement();
		for (int i=0; i<attributes.length; i++) {
			String attributeName = attributes[i];
			String actualValue = e.getAttribute(attributeName);
			String expectedValue = values[i];
			
			if (actualValue == null || actualValue.isEmpty()) {
				errors.add("Missing attribute '" + attributeName + "'");
			}
			
			if (!expectedValue.equals(actualValue)) {
				errors.add("Expected atribute '" + attributeName + "' to have value '" + expectedValue + "'." +
						"Found value '" + actualValue + "' instead.");
			}
		}
		
		return errors;
	}

	/**
	 * 
	 * @return
	 */
	public org.w3c.dom.Element getEditPartElement() {
		// Required to store the code to xml
		editor.save();
		// Find the element
		try {
			InputStream inputStream = new ByteArrayInputStream(editor.getSourceText().getBytes());		
			Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
					
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression xPathExpression = xPath.compile("//*[@name='" + name + "']");
			
			NodeList nodeList = (NodeList) xPathExpression.evaluate(xmlDocument, XPathConstants.NODESET);
			
			if (nodeList.getLength() == 0 || nodeList.getLength() > 1) {
				throw new RuntimeException("Found '" + nodeList.getLength() + "' nodes with name '" + name + "'. Expected exactly '1'."); 
			}

			return (org.w3c.dom.Element) nodeList.item(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	public AbsoluteEditPart getAbsoluteEditPart() {
		return containerShapeEditPart;
	}
	
	/**
	 * 
	 * @return
	 */
	public Rectangle getBounds() {
		return containerShapeEditPart.getBounds();
	}
	
	protected Element putToCanvas(String name, ElementType type, Point point, EditPart parent) {
		AbstractEditPart added = (AbstractEditPart) processEditor.addElementFromPalette(
										type, 
										point.x, 
										point.y, 
										parent); 
		return addedElement(added, name, type);
	}
	
	private Element addedElement(AbstractEditPart added, String label, ElementType type) {
		List<EditPart> selected = processEditor.getViewer().getSelectedEditParts();
		
		if(selected.size() != 1) {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < selected.size(); i++) {
				builder.append(selected.get(i).toString());
				builder.append(", ");
			}
			throw new IllegalStateException("Adding element caused selecting more elements: " + builder.toString());
		}
		
		Element addedElement = new Element(selected.get(0), type);
		try{
			if("Tasks".compareTo(type.getSectionName()) == 0) {
				added.setLabel(label);
			} else {
				addedElement.setName(label);
			}
			
		} catch (SWTLayerException e) {
			addedElement.setName(label);
		}
		
		return addedElement;
	}
	
	private class ConnectionAdded implements WaitCondition {

	    private Element from;
	    private Element to;
	    private ConnectionType type;
	    
	    public ConnectionAdded(Element from, Element to, ConnectionType type) {
            this.from = from;
            this.to = to;
            this.type = type;
        }
	    
        @Override
        public boolean test() {
            try {
                processEditor.addConnectionFromPalette(type, from, to);
            }catch(GEFLayerException e) {
                return false;
            }
            
            return true;
        }

        @Override
        public String description() {
            return "test if connection was added";
        }
	    
	}
}

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
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.Matcher;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.reddeer.ProcessPropertiesView;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructOfType;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructOnPoint;
import org.jboss.tools.bpmn2.reddeer.editor.matcher.ConstructWithName;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.DescriptionTab;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Represents an element in the editor canvas.
 */
public class Element {

	protected Logger log = Logger.getLogger(getClass());
	
	protected ProcessEditorView editor = new ProcessEditorView();
	protected ProcessPropertiesView properties = new ProcessPropertiesView();
	protected SWTBot bot = new SWTBot();
	
	protected String name;
	protected ElementType type;
	protected Element parent;
	protected SWTBotGefEditPart editPart;
	
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
	
	private void setUp(String name, ElementType type, Element parent, int index, boolean select) {
		List<Matcher<? super EditPart>> matcherList = new ArrayList<Matcher<? super EditPart>>();
		if (name != null) {
			matcherList.add(new ConstructWithName<EditPart>(name));
		}
		if (type != null) {
			matcherList.add(new ConstructOfType<EditPart>(type));
		}

		Matcher<EditPart> allMatcher = allOf(matcherList);
		if (parent != null) {
			List<SWTBotGefEditPart> editParts = editor.getEditParts(parent.editPart, allMatcher);
			if (!editParts.isEmpty()) {
				this.editPart = editParts.get(index);
			}
		} else {
			this.editPart = editor.getEditPart(allMatcher, index);
		}

		if (editPart == null) {
			throw new RuntimeException("Could not find construct with name '" + name + "' of type '" + type.name() + "'");
		}
		if (name == null) {
			this.name = editor.getName(editPart);
		}
		
		if (select) {
			select();
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
		this.properties.getTab("Description", DescriptionTab.class).setName(name);
	}
	
	/**
	 * Select this construct. Select the pictogram not the label.
	 */
	public void select() {
		editor.selectEditPart(editPart);
	}
	
	/**
	 * Add a boundary event to this construct.
	 *  
	 * @param name
	 * @param eventType
	 */
	protected void addEvent(String name, ElementType eventType) {
		if (!eventType.name().endsWith("BOUNDARY_EVENT")) {
			throw new IllegalArgumentException("Can add only BOUNDARY_EVENT types.");
		}

		// Add the event
		Point p = getBounds().getTopLeft();
		editor.activateTool(eventType.toToolPath()[0], eventType.toToolPath()[1]);
		editor.click(p.x(), p.y());

		// Set the name of the event
		Element event = editor.getLastConstruct(eventType);
		event.setName(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 */
	public void append(String name, ElementType constructType) {
		append(name, constructType, ConnectionType.SEQUENCE_FLOW, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param position
	 */
	public void append(String name, ElementType constructType, Position position) {
		append(name, constructType, ConnectionType.SEQUENCE_FLOW, position);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param connectionType
	 */
	public void append(String name, ElementType constructType, ConnectionType connectionType) {
		append(name, constructType, connectionType, Position.EAST);
	}
	
	/**
	 * 
	 * @param name
	 * @param constructType
	 * @param connectionType
	 * @param relativePosition
	 */
	public void append(String name, ElementType constructType, ConnectionType connectionType, Position relativePosition) {
		log.info("Appending construct name '" + name + "' of type '" + constructType + "' after construct with name '" + this.name + "'.");
		
		Point point = findPoint(parent, this, relativePosition);
		if (!isAvailable(point)) {
			throw new RuntimeException(point + " is not available");
		}
		
		editor.activateTool(constructType.toToolPath()[0], constructType.toToolPath()[1]);
		editor.click(point.x(), point.y());
			
		Element construct = editor.getLastConstruct(constructType);
		if (construct == null) {
			throw new RuntimeException("Unexpected error. Could not find added construct.");
		}
		if (name != null) {
			construct.setName(name);
		}
		
		connectTo(construct, connectionType);
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
	 * @param construct
	 * @param connectionType
	 */
	public void connectTo(Element construct, ConnectionType connectionType) {
		log.info("Connecting construct '" + this.name + "' and construct '" + construct.getName() + "' using '" + connectionType + "'.");
		// Get the dimensions of the source (this) construct. 
		Rectangle rs = getBounds();
		// Get the dimensions of the target construct. 
		Rectangle rt = construct.getBounds();
		// Create the connection.
		// 
		// Bring forward elements in case they are covered by another bigger
		// element. Then perform the clicks.
		editor.activateTool(connectionType.toName());
		
		select();
		log.debug("\tConnecting points '" + rs.getCenter() + "' and '" + rt.getCenter() + "'");
		// ISSUE: Clicking center on a Lane|Subprocess|etc. has a good chance to select something in it!
		editor.click(rs.getCenter().x(), rs.getCenter().y());
		
		construct.select();
		editor.click(rt.getCenter().x(), rt.getCenter().y());
		editor.activateTool("Select");
	}
	
	/**
	 * Delete this construct. Cannot be undone.
	 */
	public void delete() {
		select();
		editor.clickContextMenu("Delete");
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
		return editor.getEditParts(editPart.parent(), new ConstructOnPoint<EditPart>(p)).isEmpty();
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
		
		int nearOffset  = 75; // newChildType.isContainer() ? 2 * 75 : 75;
		int farOffset = 100; // newChildType.isContainer() ? 2 * 100 : 100;
		
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
			Rectangle parentBounds = editor.getBounds(parent.getEditPart());
			
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
	public SWTBotGefEditPart getEditPart() {
		return editPart;
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
	
	/**
	 * 
	 * @return
	 */
	public ProcessEditorView getEditor() {
		return editor;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProcessPropertiesView getProperties() {
		return properties;
	}
	
	/**
	 * 
	 * @return
	 */
	public Rectangle getBounds() {
		return editor.getBounds(editPart);
	}
	
}

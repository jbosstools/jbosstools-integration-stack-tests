package org.jboss.tools.bpmn2.reddeer.editor;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.jboss.tools.bpmn2.reddeer.matcher.ConstructOnPoint;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ContainerConstruct extends Construct {

	/**
	 * @see Construct(String, ConstructType, Construct, int, boolean)
	 * @param name
	 * @param type
	 */
	public ContainerConstruct(String name, ConstructType type) {
		this(name, type, null, -1, true);
	}
	
	/**
	 * @see Construct(String, ConstructType, Construct, int, boolean)
	 * @param name
	 * @param type
	 * @param parent
	 * @param index
	 * @param select
	 */
	public ContainerConstruct(String name, ConstructType type, Construct parent, int index, boolean select) {
		super(name, type, parent, index, select);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public void add(String name, ConstructType type) {
		String sectionName = type.toToolPath()[0];
		
		Rectangle bounds = editor.getBounds(editPart);
		int x, y = 0;
		
		if ("Boundary Events".equals(sectionName)) {
			/*
			 * Upper left corner
			 */
			x = y = 5; 
		} else {
			x = bounds.width() / 8;
			// TODO: Creates issues in the AdHocSubProcessTest. The click hits a connection.
			y = bounds.height() / 10;
		}
		Point placeTo = new Point(x, y);
		add(name, type, placeTo);
	}
	
	/**
	 * 
	 * @param nextTo
	 * @param name
	 * @param type
	 * @param position
	 */
	public void add(String name, ConstructType type, Construct nextTo, Position position) {
		add(name, type, findPoint(this, nextTo, position));
	}
	
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param point Represents a position in the canvas relative to this construct. Meaning that
	 *              this construct's upper left corner is the staring position. 
	 */
	public void add(String name, ConstructType type, Point point) {
		Rectangle bounds = editor.getBounds(editPart);
		
		String sectionName = type.toToolPath()[0];
		/*
		 * Make sure that the point is available.
		 */
		if (!"Boundary Events".equals(sectionName)) {
			if (isInternalAvailable(point)) {
				throw new RuntimeException("'" + point + "' is not available");
			}
		}

		/*
		 * Add the construct using the tool in the palette.
		 */
		select();
		log.info("Adding consturct '" + name + "' of type '" + type + "' to '" + point + "'");
		editor.activateTool(type.toToolPath()[0], type.toToolPath()[1]);
		editor.click(bounds.x() + point.x(), bounds.y() + point.y());
		/*
		 * Set name
		 */
		Construct c = editor.getLastConstruct(type);
		c.select();
		c.setName(name);
	}
	
	/**
	 * Check weather ${p} located in this container construct is not covered by another 
	 * construct. 
	 * 
	 * @param point a relative position in this container construct
	 * @return
	 */
	protected boolean isInternalAvailable(Point p) {
		/*
		 * The point must be inside this edit part.
		 */
		Rectangle bounds = getBounds();
		Point point = new Point(p.x() + bounds.width(), p.y() + bounds.height());
		if (bounds.contains(point)) {
			/*
			 * Check weather the point is not already taken by another child editPart.
			 */
			return editor.getEditParts(editPart, new ConstructOnPoint<EditPart>(point)).isEmpty();
		}
		/*
		 * Out of bounds.
		 */
		return false;
	}
	
	/**
	 * 
	 */
	public void pushDown() {
		editor.clickContextMenu("Push down");
	}
	
	/**
	 * 
	 */
	public void undoPushDown() {
		editor.clickContextMenu("Undo Push down");
	}
}

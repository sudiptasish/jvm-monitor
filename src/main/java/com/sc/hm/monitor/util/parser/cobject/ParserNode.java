/**
 * 
 */

package com.sc.hm.monitor.util.parser.cobject;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.sc.hm.monitor.util.Logger;

public class ParserNode implements CustomizedXMLObject {

	private String nodeName = "";
	private String nodeValue = "";
	private boolean hasChild = false;
	private Vector<CustomizedXMLObject> children = null;
	private Map<String, String> attributeMap = null;
	
	public ParserNode(String name) {
		this(name, "");
	}
	
	public ParserNode(String name, String value) {
		this.nodeName = name;
		this.nodeValue = value;
		this.children = new Vector<CustomizedXMLObject>();
		this.attributeMap = new LinkedHashMap<String, String>();
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public void setNodeValue(String value) {
		nodeValue = value;
	}
	
	public String getNodeValue() {
		return nodeValue;
	}
	
	public boolean hasChild() {
		return this.hasChild;
	}
	
	public void hasChild(boolean flag) {
		this.hasChild = flag;
	}
	
	public void addChild(CustomizedXMLObject child) {
		if (child == this) {
			Logger.log("Can not Add Parent To Itself");
			return;
		}
		this.children.addElement(child);
	}
	
	public CustomizedXMLObject getFirstChild() {
		return getChildAt(0);
	}
	
	public CustomizedXMLObject getChildAt(int index) {
		return this.children.elementAt(index);
	}
	
	public Enumeration<CustomizedXMLObject> getAllChildren() {
		return this.children.elements();
	}
	
	public void addAttribute(String attributeName, String attributeValue) {
		this.attributeMap.put(attributeName, attributeValue);
	}
	
	public void addAttributeNames(String[] attributeNames, String[] attributeValues) {
		for (int i = 0; i < attributeNames.length; i ++) {
			this.attributeMap.put(attributeNames[i], attributeValues[i]);
		}
	}
	
	public Iterator<String> getAllAttributeNames() {
		return this.attributeMap.keySet().iterator();
	}
	
	public Iterator<String> getAllAttributeValues() {
		return this.attributeMap.values().iterator();
	}
	
	public String getAttribute(String attributeName) {
		if (attributeMap.containsKey(attributeName)) {
			return attributeMap.get(attributeName);
		}
		return null;
	}
	
	public Vector<CustomizedXMLObject> getChildrenByName(String childName) {
		Vector<CustomizedXMLObject> childElements = new Vector<CustomizedXMLObject>();
		for (Enumeration<CustomizedXMLObject> enm = this.getAllChildren(); enm.hasMoreElements();) {
			_getChildInternal(enm.nextElement(), childName, childElements);
		}
		return childElements;
	}
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	private void _getChildInternal(CustomizedXMLObject object, String childName, Vector<CustomizedXMLObject> childElements) {
		if (childName.equals(object.getNodeName())) {
			childElements.addElement(object);
		}
		else {
			for (Enumeration<CustomizedXMLObject> enm = object.getAllChildren(); enm.hasMoreElements();) {
				_getChildInternal(enm.nextElement(), childName, childElements);
			}
		}
	}
	
	public String toString() {
		return this.nodeName;
	}
}

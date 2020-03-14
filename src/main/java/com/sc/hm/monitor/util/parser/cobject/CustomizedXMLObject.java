/**
 * 
 */

package com.sc.hm.monitor.util.parser.cobject;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public interface CustomizedXMLObject {
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public String getNodeName();
	
	public void setNodeValue(String value);
	
	public String getNodeValue();
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public boolean hasChild();
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public void hasChild(boolean flag);
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public void addChild(CustomizedXMLObject child);

	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public CustomizedXMLObject getFirstChild();
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public CustomizedXMLObject getChildAt(int index);
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public Enumeration<CustomizedXMLObject> getAllChildren();
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public void addAttribute(String attributeName, String attributeValue);
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public void addAttributeNames(String[] attributeNames, String[] attributeValues);
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public String getAttribute(String attributeName);
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public Iterator<String> getAllAttributeNames();	
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public Iterator<String> getAllAttributeValues();
	
	/**
	 * 
	 *
	 * @param		
	 * @return		
	 * @exception
	 */
	public Vector<CustomizedXMLObject> getChildrenByName(String childName);
}

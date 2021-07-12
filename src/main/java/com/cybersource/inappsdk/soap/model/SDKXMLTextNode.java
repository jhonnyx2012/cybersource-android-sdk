package com.cybersource.inappsdk.soap.model;

/**
 * Class representing single XML text node.
 * 
 * @author fzubair
 */
public class SDKXMLTextNode extends SDKXMLNode {

	private final String value;

	/**
	 * Creates a new XML Element.
	 * 
	 * @param namespace The namespace of the element (can be null).
	 * @param name The name of the element.
	 */
	public SDKXMLTextNode(String namespace, String name, String value) {
		super(namespace, name);
		this.value = value;
	}

	/**
	 * @return Text value of this text node XML element.
	 */
	public String getValue() {
		return value;
	}
}

package com.team5.contact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.widget.Toast;

/**
 * Contains data of a Contact. Also reads the contacts.xml file.
 * @author Nick
 *
 */
class Contact {
	public String name;
	public String phoneNumber;
	public String email;
	public float latitude;
	public float longitude;
	public String address;
	public String postCode;
	public String iconName;
	Context c;
	
	/**
	 * Create the Contact with all values determined
	 * @author Nick
	 *
	 */
	public Contact(String name, String phoneNumber, String email,
			float latitude, float longitude, String address, String postCode, String iconName, Context c) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.postCode = postCode;
		this.iconName = iconName;
		this.c = c;
	}
	
	/**
	 * 
	 * @author Fetch the contacts.xml and parse it, returning an array of all contacts
	 *
	 */
	public static List<Contact> getAll(Context c)	{
		List<Contact> contacts = new ArrayList<Contact>();
		
		// Read all attributes from the XML file
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(c.getAssets().open("Contacts.xml"));
			
			NodeList nodeList = document.getElementsByTagName("contact");
			
			int size = nodeList.getLength();

			// Extract text content from each tag
			for (int i = 0; i < size; i++) {
				Element node = (Element) nodeList.item(i);
				
				String name = node.getElementsByTagName("name").item(0).getTextContent();
				String phoneNumber = node.getElementsByTagName("phone").item(0).getTextContent();
				String email = node.getElementsByTagName("email").item(0).getTextContent();
				Float latitude = Float.parseFloat(node.getElementsByTagName("latitude").item(0).getTextContent());
				Float longitude = Float.parseFloat(node.getElementsByTagName("longitude").item(0).getTextContent());
				String address = node.getElementsByTagName("address").item(0).getTextContent();
				String postCode = node.getElementsByTagName("postcode").item(0).getTextContent();
				String icon = node.getElementsByTagName("logo").item(0).getTextContent();
				
				contacts.add(new Contact(name, phoneNumber, email, latitude, longitude, address, postCode, icon, c));
			}
		} catch (Exception e) {
			return null;
		}
				
		return contacts;
	}
	
	/**
	 * Parse the contacts.xml and return a specific contact
	 * @author Nick
	 *
	 */
	public static Contact getOne(Context c, int position)	{		
		// Read all attributes from the XML file
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(c.getAssets().open("Contacts.xml"));
			
			NodeList nodeList = document.getElementsByTagName("contact");

			// Extract text content from each tag
			Element node = (Element) nodeList.item(position);
			
			String name = node.getElementsByTagName("name").item(0).getTextContent();
			String phoneNumber = node.getElementsByTagName("phone").item(0).getTextContent();
			String email = node.getElementsByTagName("email").item(0).getTextContent();
			Float latitude = Float.parseFloat(node.getElementsByTagName("latitude").item(0).getTextContent());
			Float longitude = Float.parseFloat(node.getElementsByTagName("longitude").item(0).getTextContent());
			String address = node.getElementsByTagName("address").item(0).getTextContent();
			String postCode = node.getElementsByTagName("postcode").item(0).getTextContent();
			String icon = node.getElementsByTagName("logo").item(0).getTextContent();
			
			return new Contact(name, phoneNumber, email, latitude, longitude, address, postCode, icon, c);
		} catch (Exception e) {
			return null;
		}
	}
}
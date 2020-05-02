import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlParser {

	/**
	 * 
	 * @param filePath 
	 * @param theFileStructure eg : file have column name as tagName,attributeName,attributeValue
	 * @param columnNames : 
	 * @param rootNode : Root node path eg : "/results/row"
	 * @return Map of ArrayList
	 * @throws Exception
	 */
	public HashMap<String,ArrayList<String>> xmlDataForDumping(final String filePath, final String theFileStructure, final ArrayList<String> columnNames, final String rootNode) throws Exception{

		if(theFileStructure.equalsIgnoreCase("tagName")) {

			try {
				String suffixXpression = "//_column_/text()";
				String xpression = suffixXpression;
				return getValuesFromTag(readFile(filePath),columnNames,xpression);
			} catch (Exception e) {
				throw e;
			}
		}
		else if (theFileStructure.equalsIgnoreCase("attributeName")) {

			try {
				String suffixXpression = "";
				String xpression = rootNode + suffixXpression;
				return getValuesFromAttribute(readFile(filePath),columnNames,xpression);
			} catch (Exception e) {
				throw e;
			}
		}
		else if (theFileStructure.equalsIgnoreCase("attributeValue")) {

			try {
				String suffixXpression = "[@*='_column_']/text()";
				String xpression = rootNode+suffixXpression;
				return getValuesFromAttributeValueTag(readFile(filePath),columnNames,xpression);
			} catch (Exception e) {
				throw e;
			}
		}
		else {
			System.err.print("Report : No structure found for this file so this file comes under invalid file according to this code");
			 throw new Exception("No Structure found"); 
		}
	}


	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private  Document readFile(String fileName) throws Exception 
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = null;
		try {
			doc = factory.newDocumentBuilder().parse(fileName);
		} catch (SAXException  | IOException | ParserConfigurationException e) {
			throw  e;
		} 
		return doc;
	}


	/**
	 * 
	 * @param document
	 * @param columnNames
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	private  HashMap<String,ArrayList<String>> getValuesFromTag(Document document,ArrayList<String> columnNames,String xpathExpression) throws Exception 
	{
		HashMap<String,ArrayList<String>> xmlValues= new HashMap<String, ArrayList<String>>();

		ArrayList<String> values =null;
		for(String column:columnNames) {
			XPathFactory xpathFactory = XPathFactory.newInstance();              
			XPath xpath = xpathFactory.newXPath(); 
			values = new ArrayList<String>();
			try
			{          
				XPathExpression expr = xpath.compile(xpathExpression.replace("_column_", column)); 
				NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					values.add(nodes.item(i).getNodeValue());
				}
			} catch (XPathExpressionException e) {
				throw  e;
			}
			xmlValues.put(column, values);
		}			
		return xmlValues;
	}


	/**
	 * 
	 * @param document
	 * @param columnNames
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	private  HashMap<String,ArrayList<String>> getValuesFromAttribute(Document document, ArrayList<String> columnNames,String xpathExpression) throws Exception 
	{
		XPathFactory xpathFactory = XPathFactory.newInstance();              
		XPath xpath = xpathFactory.newXPath(); 
		ArrayList<String> values ;
		HashMap<String,ArrayList<String>> xmlValues= new HashMap<String, ArrayList<String>>();
		try
		{          
			XPathExpression expr = xpath.compile(xpathExpression); 
			for(String attribute : columnNames) {
				values = new ArrayList<String>();
				NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					values.add(nodes.item(i).getAttributes().getNamedItem(attribute).getNodeValue());
				}
				xmlValues.put(attribute, values);
			}
		} catch (XPathExpressionException e) {
			throw  e;
		}
		return xmlValues;
	}

	/**
	 * 
	 * @param document
	 * @param columnNames
	 * @param xpathExpression
	 * @return
	 * @throws Exception
	 */
	private  HashMap<String,ArrayList<String>> getValuesFromAttributeValueTag(Document document,ArrayList<String> columnNames,String xpathExpression) throws Exception 
	{
		HashMap<String,ArrayList<String>> xmlValues= new HashMap<String, ArrayList<String>>();	 
		ArrayList<String> values =null;
		for(String column:columnNames) {
			XPathFactory xpathFactory = XPathFactory.newInstance();              
			XPath xpath = xpathFactory.newXPath(); 
			values = new ArrayList<String>();
			try
			{          
				XPathExpression expr = xpath.compile(xpathExpression.replace("_column_", column)); 
				NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
				for (int i = 0; i < nodes.getLength(); i++) {
					values.add(nodes.item(i).getNodeValue());
				}
			} catch (XPathExpressionException e) {
				throw  e;
			}
			xmlValues.put(column, values);
		}			
		return xmlValues;
	}
}

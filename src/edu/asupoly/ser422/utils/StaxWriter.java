package edu.asupoly.ser422.utils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import edu.asupoly.ser422.model.Person;

public class StaxWriter {
    private String configFile;

    public void setFile(String configFile) {
        this.configFile = configFile;
    }

    public void saveConfig(List<Person> people) throws Exception {
        // create an XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        System.out.println("Opening File OutputStream...");
        FileOutputStream out = new FileOutputStream(configFile);
        System.out.println("Open");
        // create XMLEventWriter
        System.out.println("Opening XMLEventWriter...");
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(out);
        System.out.println("Open");
        // create an EventFactory
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        eventWriter.add(end);
        StartElement peopleElement = eventFactory.createStartElement("", "", "people");
        eventWriter.add(peopleElement);
        eventWriter.add(end);

        Iterator<Person> it = people.iterator();
        while (it.hasNext()){
        	Person p = it.next();
	        // create person open tag
	        StartElement configStartElement = eventFactory.createStartElement("",
	                "", "person");
	        
	        
	        eventWriter.add(tab);
	        eventWriter.add(configStartElement);
	        eventWriter.add(end);
	        // Write the different nodes
	        createNode(eventWriter, "firstname", p.getFirstName());
	        createNode(eventWriter, "lastname", p.getLastName());
	        createNode(eventWriter, "languages", p.getLanguages());
	        createNode(eventWriter, "days", p.getDays());
	        createNode(eventWriter, "haircolor", p.getHairColor());
	        createNode(eventWriter, "creator", p.getCreator());
	        createNode(eventWriter, "id", p.getID());
	
	        eventWriter.add(tab);
	        eventWriter.add(eventFactory.createEndElement("", "", "person"));
	        eventWriter.add(end);
        }
        
        eventWriter.add(eventFactory.createEndElement("", "", "people"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        System.out.println("Closing XMLEventWriter...");
        eventWriter.close();
        System.out.println("Closed");
        try {
        	System.out.println("Closing FileOutputStream");
        	out.close();
        	System.out.println("Closed");
        }catch (Exception e){
        	e.printStackTrace();
        }
    }

    private void createNode(XMLEventWriter eventWriter, String name,
            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }

}
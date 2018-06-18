package edu.asupoly.ser422.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import edu.asupoly.ser422.model.Person;

public class StaxParser {
    
    static final String PERSON = "person";
    static final String FIRSTNAME = "firstname";
    static final String LASTNAME = "lastname";
    static final String LANGUAGES = "languages";
    static final String DAYS = "days";
    static final String HAIRCOLOR = "haircolor";
    static final String CREATOR = "creator";
    static final String ID = "id";

    @SuppressWarnings({ "unchecked", "null" })
    public List<Person> readConfig(String configFile) {
        List<Person> people = new ArrayList<Person>();
        InputStream in = null;
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            System.out.println("Opening file input stream...");
            File file = new File(configFile);
            in = new FileInputStream(file);
            System.out.println("Open");
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Person person = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have an person element, we create a new person
                    if (startElement.getName().getLocalPart().equals(PERSON)) {
                        person = new Person();
                        // Read in the attributes
//                        Iterator<Attribute> attributes = startElement.getAttributes();
//                        while (attributes.hasNext()){
//                        	Attribute attribute = attributes.next();
//                        	if (attribute.getName().toString().equals(CREATOR)){
//                        		person.setCreator(attribute.getValue());
//                        	}
//                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(FIRSTNAME)) {
                            event = eventReader.nextEvent();
                            person.setFirstName(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(LASTNAME)) {
                        event = eventReader.nextEvent();
                        person.setLastName(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(LANGUAGES)) {
                        event = eventReader.nextEvent();
                        person.setLanguages(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(DAYS)) {
                        event = eventReader.nextEvent();
                        person.setDays(event.asCharacters().getData());
                        continue;
                    }
                    
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(HAIRCOLOR)) {
                        event = eventReader.nextEvent();
                        person.setHairColor(event.asCharacters().getData());
                        continue;
                    }
                    
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(CREATOR)) {
                        event = eventReader.nextEvent();
                        person.setCreator(event.asCharacters().getData());
                        continue;
                    }
                    
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(ID)) {
                        event = eventReader.nextEvent();
                        person.setID(event.asCharacters().getData());
                        continue;
                    }
                }
                // If we reach the end of an item element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(PERSON)) {
                        people.add(person);
                    }
                }  
            }
            
            System.out.println("Closing Input Stream...");
			in.close();
			System.out.println("Closed");
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
			System.out.println("Error Closing Input Stream");
			e.printStackTrace();
		} finally{
//			try {
//				System.out.println("Closing Input Stream...");
//				in.close();
//				System.out.println("Closed");
//			}catch (Exception e){
//				System.out.println("Error Closing Input Stream 2");
//				e.printStackTrace();
//			}
		}
        return people;
    }
}
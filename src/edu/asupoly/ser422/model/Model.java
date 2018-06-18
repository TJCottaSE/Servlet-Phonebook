package edu.asupoly.ser422.model;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.asupoly.ser422.utils.StaxParser;
import edu.asupoly.ser422.utils.StaxWriter;

public class Model {

	private String FILE_PATH;
	private String LOCK_FILE;
	private int SLEEP_DELAY;
	
	public Model(){
		FILE_PATH = "";
		LOCK_FILE = "";
		SLEEP_DELAY = 250;
	}
	
	public Model(String _filepath, String _lockfile, int _sleepdelay){
		FILE_PATH = _filepath;
		LOCK_FILE = _lockfile;
		SLEEP_DELAY = _sleepdelay;
	}
	
	public Person doGet(String id){
		ArrayList<Person> people = null;
		Person thePerson = null;
		// Set up edit env
		FileOutputStream out = null;
		FileLock lock = null;
		
		try {
			File lockFile = new File(LOCK_FILE);
			lockFile.createNewFile();
			out = new FileOutputStream(LOCK_FILE);
			// Try to lock the lock file
			lock = out.getChannel().tryLock();
			while(lock == null){
				try {
					Thread.sleep(SLEEP_DELAY);
					lock = out.getChannel().tryLock();
				}catch(Exception errLock){
					// Do Nothing
				}
			}
			// Now that lock has been acquired
			
			// Read in the existing list
			System.out.println("Reading Stored Data...");
			StaxParser parser = new StaxParser();
			people = (ArrayList<Person>) parser.readConfig(FILE_PATH);
			System.out.println("Made it this far...");
			for (Person person : people){
				if (person.getID().equals(id)){
					System.out.println("The Person was found");
					thePerson = person;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (lock != null){lock.release();}
				if (out != null){out.close();}
			}catch (Exception c){
				c.printStackTrace();
			}
		}
		return thePerson;
	}
	
	public void doRemove(String id){
		FileOutputStream out = null;
		FileLock lock = null;
		ArrayList<Person> people = null;
		try {
			File lockFile = new File(LOCK_FILE);
			lockFile.createNewFile();
			out = new FileOutputStream(LOCK_FILE);
			// Try to lock the lock file
			lock = out.getChannel().tryLock();
			while(lock == null){
				try {
					Thread.sleep(SLEEP_DELAY);
					lock = out.getChannel().tryLock();
				}catch(Exception errLock){
					// Do Nothing
				}
			}
			// Now that lock has been acquired
			
			// Read in the existing list
			System.out.println("Reading Stored Data...");
			StaxParser parser = new StaxParser();
			people = (ArrayList<Person>) parser.readConfig(FILE_PATH);
			System.out.println("Made it this far...");
			int count = 0;
			int position = 0;
			boolean found = false;
			for (Person person : people){
				if (person.getID().equals(id)){
					System.out.println("The Person was found ... being removed");
					position = count;
					found = true;
				}
				count++;
			}
			if (found) { people.remove(position); }
			// Writing out the file
			System.out.println("Writing out the list");
			StaxWriter writer = new StaxWriter();
			writer.setFile(FILE_PATH);
			try {
				File file = new File(FILE_PATH);
				file.createNewFile();
				writer.saveConfig(people);
			} catch (Exception v) {
				v.printStackTrace();
			} 
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (lock != null){lock.release();}
				if (out != null){out.close();}
			}catch (Exception c){
				c.printStackTrace();
			}
		}
		
	}
	
	public String readData(HttpSession session, HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		FileOutputStream out = null;
		FileLock lock = null;
		ArrayList<Person> people = null;
		try {
			File lockFile = new File(LOCK_FILE);
			lockFile.createNewFile();
			out = new FileOutputStream(LOCK_FILE);
			// Try to lock the lock file
			lock = out.getChannel().tryLock();
			while(lock == null){
				try {
					Thread.sleep(SLEEP_DELAY);
					lock = out.getChannel().tryLock();
				}catch(Exception errLock){
					// Do Nothing
				}
			}
			// Now that lock has been acquired
			
			// Read in the existing list
			System.out.println("Reading Stored Data...");
			StaxParser parser = new StaxParser();
			people = (ArrayList<Person>) parser.readConfig(FILE_PATH);		
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (lock != null){lock.release();}
				if (out != null){out.close();}
			}catch (Exception c){
				c.printStackTrace();
			}
		}
		
		// Build the Result Table
		int count = 0;
		for (Person person : people){
			if (person.getCreator().equals(session.getAttribute("firstname") + " " + session.getAttribute("lastname"))){
				count++;
			}
		}
		// Build Users Entries Table
		if (count > 0){
			sb.append("<p>Your Entries</p>");
			// Build the Table
			sb.append("<table>"
						+ "<thead>"
							+ "<tr>"
								+ "<th>First Name</th>"
								+ "<th>Last Name</th>"
								+ "<th>Languages</th>"
								+ "<th>Days</th>"
								+ "<th>Hair Color</th>"
								+ "<th>Edit</th>"
							+ "</tr>"
						+ "</thead>"
						+ "<tbody>");
			for (Person person : people){
				if (person.getCreator().equals(session.getAttribute("firstname") + " " + session.getAttribute("lastname"))){
					String link = request.getContextPath() + "/controller?id=" + person.getID();
					String details = request.getContextPath() + "/usercontroller?id=" + person.getID();
					sb.append(	"<tr>"
									+ "<td><a href=\"" + details + "\">" + person.getFirstName() + "</a></td>"
									+ "<td><a href=\"" + details + "\">" + person.getLastName() + "</a></td>"
									+ "<td>" + person.getLanguages() + "</td>"
									+ "<td>" + person.getDays() + "</td>"
									+ "<td>" + person.getHairColor() + "</td>"
									+ "<td><a href=\"" + link + "\">Edit</a></td></tr>"	);
				}
			}
			sb.append("</tbody></table>");
		}
		else {
			// Show Message
			sb.append("<p>It looks like you haven't entered any data yet. Please use the Create User option to add data.</p>");
		}
		
		// Build Best Matches Table
		sb.append("<br><br><p>Best Matches</p>");
		try {
			// Build the Table
			sb.append("<table>"
						+ "<thead>"
							+ "<tr>"
								+ "<th>First Name</th>"
								+ "<th>Last Name</th>"
								+ "<th>Languages</th>"
								+ "<th>Days</th>"
								+ "<th>Hair Color</th>"
								+ "<th>Edit</th>"
							+ "</tr>"
						+ "</thead>"
						+ "<tbody>");
			Person best[] = getBest(people);
			for (Person person : best){
				if (!person.getID().equals("-1")){
					String link = request.getContextPath() + "/controller?id=" + person.getID();
					String details = request.getContextPath() + "/usercontroller?id=" + person.getID();
					sb.append(	"<tr>"
									+ "<td><a href=\"" + details + "\">" + person.getFirstName() + "</a></td>"
									+ "<td><a href=\"" + details + "\">" + person.getLastName() + "</a></td>"
									+ "<td>" + person.getLanguages() + "</td>"
									+ "<td>" + person.getDays() + "</td>"
									+ "<td>" + person.getHairColor() + "</td>"
									+ "<td><a href=\"" + link + "\">Edit</a></td></tr>"	);
				}
			}
			sb.append("</tbody></table>");
			
			
			
			
		}catch (Exception e){
			e.printStackTrace();
			sb.append("<p>Not enough entries in the data to list best matches.</p>");
		}
		
		
		return sb.toString();
	}
	
	private Person[] getBest(List<Person> people){
		Person best[] = new Person[3];
		Person p = new Person("T", "C", "", "", "", "", "-1");
		best[0] = best[1] = best[2] = p;
		for (Person person : people){
			String langs[] = person.getLanguages().split(",");
			if (langs.length >= best[0].getLanguages().split(",").length){
				Person temp = best[0];
				best[0] = person;
				if (temp.getLanguages().split(",").length >= best[1].getLanguages().split(",").length){
					Person temp2 = best[1];
					best[1] = temp;
					if (temp2.getLanguages().split(",").length >= best[2].getLanguages().split(",").length){
						best[2] = temp2;
					}
				}
			}else if (langs.length >= best[1].getLanguages().split(",").length){
				Person temp = best[1];
				best[1] = person;
				if(temp.getLanguages().split(",").length >= best[2].getLanguages().split(",").length){
					best[2] = temp;
				}
			}else if (langs.length >= best[2].getLanguages().split(",").length){
				best[2] = person;
			}
		}
		
		return best;
	}
	
	public boolean setUpEdit(HttpSession session, String id){
		// Set up edit env
		boolean found = false;
		FileOutputStream out = null;
		FileLock lock = null;
		ArrayList<Person> people = null;
		try {
			File lockFile = new File(LOCK_FILE);
			lockFile.createNewFile();
			out = new FileOutputStream(LOCK_FILE);
			// Try to lock the lock file
			lock = out.getChannel().tryLock();
			while(lock == null){
				try {
					Thread.sleep(SLEEP_DELAY);
					lock = out.getChannel().tryLock();
				}catch(Exception errLock){
					// Do Nothing
				}
			}
			// Now that lock has been acquired
			
			// Read in the existing list
			System.out.println("Reading Stored Data...");
			StaxParser parser = new StaxParser();
			people = (ArrayList<Person>) parser.readConfig(FILE_PATH);
			System.out.println("Made it this far...");
			for (Person person : people){
				if (person.getID().equals(id)){
					session.setAttribute("enteredFirstName", person.getFirstName());
					session.setAttribute("enteredLastName", person.getLastName());
					session.setAttribute("enteredLanguages", person.getLanguages());
					session.setAttribute("enteredDays", person.getDays());
					session.setAttribute("enteredHair", person.getHairColor());
					session.setAttribute("action", "create");
					found = true;
				}
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (lock != null){lock.release();}
				if (out != null){out.close();}
			}catch (Exception c){
				c.printStackTrace();
			}
		}
		
		return found;
	}

	public void write(HttpSession session) {
		// Acquire File Lock
		FileOutputStream out = null;
		FileLock lock = null;

		try {
			File lockFile = new File(LOCK_FILE);
			lockFile.createNewFile();
			out = new FileOutputStream(LOCK_FILE);
			// Try to lock the lock file
			lock = out.getChannel().tryLock();
			while(lock == null){
				try {
					Thread.sleep(SLEEP_DELAY);
					lock = out.getChannel().tryLock();
				}catch(Exception errLock){
					// Do Nothing
				}
			}
			// Now that lock has been acquired
			
			// Read in the existing list
			System.out.println("Reading Stored Data...");
			StaxParser parser = new StaxParser();
			ArrayList<Person> people = (ArrayList<Person>) parser.readConfig(FILE_PATH);
			// Add the new entry
			System.out.println("Adding new Person");
			people.add(new Person(session.getAttribute("enteredFirstName").toString(),
									session.getAttribute("enteredLastName").toString(),
									session.getAttribute("enteredLanguages").toString(),
									session.getAttribute("enteredDays").toString(),
									session.getAttribute("enteredHair").toString(),
				session.getAttribute("firstname").toString() + " " + session.getAttribute("lastname").toString(),
									String.valueOf(people.size()+1)));
			// Write out the result
			System.out.println("Writing out the list");
			StaxWriter writer = new StaxWriter();
			writer.setFile(FILE_PATH);
			try {
				File file = new File(FILE_PATH);
				file.createNewFile();
				writer.saveConfig(people);
			} catch (Exception v) {
				v.printStackTrace();
			} 
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (lock != null){lock.release();}
				if (out != null){out.close();}
			}catch (Exception c){
				c.printStackTrace();
			}
		}
	}
}

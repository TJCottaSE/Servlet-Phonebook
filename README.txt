Tony Cotta
Lab 2 Submission
SER 422 Online B 2018
3/26/2018
Dr. Gary

This is my submission for lab 2. I would like to have this submission considered for the extra credit as well.
In the web.xml there are init params to the personfile, please note that more than one servlet uses the same
init params, meaning that they will have to be changed for all servlets. In addition to that, there is also 
a lock file that is being used which should be co-located with the personfile. This is also given in init
params for each of the servlets, and so changes should be reflected across all of those parameters as well.
This solution works behind an apache httpd server and does not corrupt data, with one caveat.  The lock 
mechanism that is being used means that replicated tomcat instances MUST be on the same OS architecture.
This likely isn't problem for this class, since I assume both tomcats are going to be running locally, but
for the sake of completeness, it should be noted that should one tomcat be deployed on a unix box, and one 
on a windows box, that the two respective operating systems locking  mechanisms are not respected by the other.
This leads to the possibility that multiple instances could be trying to write to the file at the same time
causing unpredictable results. This does however work as long as both of the tomcats reside on the same 
underlying OS architecture.

For part 2.b - the request was that all attributes and values be displayed. I have made the choice that doing
so clutters the screen, and does not add any additional descriptive value to the output, and if anything makes
it worse and more difficult to understand. For this reason, only the primary fields are shown on the main 
welcome screen. Namely firstname, lastname, languages, days, hair color, and an edit button. There are two 
additional hidden fields, which are id, and creator. These additional fields can be viewed by using the view
user hyperlink on firstname or lastname. This will take you to the view user screen which displays all attributes
including the hidden ones from the main screen. Again this was a design choice for viewing experience, but to be
clear, both of those fields are used to determine page placement.

One feature that doesn't work as you might expect is the edit feature. It complies with all of the requirements
laid out in the doc, but does not actually edit the user in the  persistent store. Instead it just adds an 
additional user to the store. It still does allow you to edit the selected user, and all of those values 
are pre-populated into the fields but confirmation of edits creates a new user instead of actually editing 
the selected choice. 

This program has been tested mostly in two versions of firefox and some in chrome. Other browsers have not 
been tested, but due to the simplistic nature of the html generated, shoud not have issues.

As for part of the extra credit, the lock mechanism uses a default wait time of 250ms. This value can be changed 
as a bit of a tuning mechanism, but requires changes at the top of several source files for a variable called
"SLEEP_DELAY". This can be set to longer or shorter times, to control how many cycles the cpu is using vs 
throughput access to the file. I chose 250ms as a rough guess as to what the middle of a scalable server might 
choose to do under somewhat heavy load.
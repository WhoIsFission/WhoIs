CloudChamber is used to retrieve IP whois information based on IP address.

Steps to run server
i) Pull CloudChamber project from Github
ii) Start mysql instance
iii) Configure Url,UserName, password in Configuration.yml located in project root folder.
iv) Run DB script for mysql instance located under ProjectDir->scripts->db
v) Build the project using "mvn clean build".
vi) Start jetty using "mvn compile exec:java".
vii) Open ipwhois.html located under ProjectDir->web.
viii) Test ip whois service by entering ip address.
ix) Output will be shown in form of xml.
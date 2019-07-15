# Curtin University Unit Information

This program allows you to search for units from Curtin University and display their information. 

## Purpose of this?
On all unit pages on the Curtin website there is no way of knowing what that specific unit can lead to without browsing course pages and connecting the dots. 

E.g. "Object Oriented Program Design" (OOPD) is a pre-requisite of "Data Structures and Algorithms" (DSA), so on OOPD's unit information, it will show under "Unit is a pre-requisite for:" DSA, along with any others.

## Database
Was created through web-scraping (requests and BeautifulSoup) with Python, and Java to serialize objects and store as a database. (Can be found in src/update_database)

## Usage
GUI Version (Requires Java/JRE)
>Double-click `curtin-unit-info.jar`

Terminal Version (Requires Java/JRE & Java JDK)
>Compile: `javac *.java`
>Run: `java CurtinUnits`

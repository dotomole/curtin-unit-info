from bs4 import BeautifulSoup
import re
import os
import requests
import unit_indexes
import sys
import time

headers = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36'}
cur_dir = os.getcwd()
src_dir = os.path.dirname(cur_dir)
main_dir = os.path.dirname(src_dir)
data_dir = os.path.join(main_dir, r'Data')
raw_dir = os.path.join(data_dir, r'raw')
dsv_dir = os.path.join(raw_dir, r'dsv')
html_dir = os.path.join(raw_dir, r'html')

#make directories
if not os.path.exists(data_dir):
    os.makedirs(data_dir)

if not os.path.exists(raw_dir):
    os.makedirs(raw_dir)

if not os.path.exists(dsv_dir):
    os.makedirs(dsv_dir)

if not os.path.exists(html_dir):
    os.makedirs(html_dir)

#get number of units/files
list = os.listdir(html_dir)
number_files = len(list)
dsvStrArr = []

def main():
    start = time.process_time()
    # print("SCRAPING HTML:")
    # print("(takes like 2 minutes)")
    # scrapeUnits()
    print("CONVERTING HTML TO DSV STRING:")
    rawToDsvP1()
    print()
    print("ADDING UNITLEADS AND WRITING DSV")
    rawToDsvP2()
    print()
    print("time elapsed: "+(time.process_time() - start)+"seconds")

def rawToDsvP1():
    for i in range(1, number_files+1):
        f = open(html_dir+"\\"+str(i)+".html", "r", encoding='utf-8')

        soup = BeautifulSoup(f.read(), 'html.parser')

        titleStr = soup.find('h1').string
        unitName = titleStr.split(") ")
        unitCode = titleStr.split(" (")

        dsvStr = ""
        #concecate dsvStr - dsv is Dollar seperated values
        #as csv (commas) are often within text, hehe (silly i know)
        dsvStr += unitCode[0].strip()+"$"+unitName[1].strip()+"$"
        
        dsvStr += "AREA"+"$"
        #Area
        for vals in soup.find_all('th', {'scope' : 'row'}):
            if vals.text == 'Area:':
                area = vals.find_next_sibling('td').text
                dsvStr +=area+"$"
        
        #Credits        
        creditFound = False
        dsvStr += "CREDITS"+"$"
        for vals in soup.find_all('th', {'scope' : 'row'}):
            if vals.text == 'Credits:':
                dsvStr += vals.find_next_sibling('td').text+"$"
                creditFound = True
        if not creditFound:
            dsvStr += 'N/A'+'$'

        #Contact hrs - added fix for online classes            
        contactFound = False
        dsvStr += "CONTACT"+"$"
        for vals in soup.find_all('th'):
            if vals.text == 'Contact Hours:':
                dsvStr += vals.find_next_sibling('td').text+" "
                contactFound = True
            elif vals.text == 'Online Class:':
                dsvStr += 'Online'+' '
                contactFound = True
            elif vals.text == 'Seminar:':
                dsvStr += 'Seminar'+' '
                contactFound = True
        if not contactFound:
            dsvStr += 'N/A'+' '

        #Here because multiple stuff can be found
        #ie. Seminar and contact hrs, so itd do 2 $'s
        dsvStr += "$" 

        #syllabus
        dsvStr += "SYLLABUS"+"$"
        try:
            syllabus = soup.find('th', text='Syllabus:').find_next_sibling('td').text
            dsvStr += syllabus.replace("\n"," ")+"$"#replace added, need to recompile
            syllabusFound = True
        except:
            dsvStr += 'N/A'+'$' 

        #Pre-reqs (spits out whole bit, can fix later maybe)
        dsvStr += "PREREQS"+"$"
        try:
            prereqs = soup.find('th', text='Prerequisite(s):').find_next_sibling('td').text.strip()
            dsvStr += re.sub('\s+',' ',prereqs)+"$"
            prereqFound = True
        except:
            dsvStr += 'N/A'+'$' 

        f.close()
        dsvStrArr.append(dsvStr)
        sys.stdout.write("\r%f%%" % float((i/number_files)*100))
        sys.stdout.flush()

#OBSOLETE as converted to Java (UnitLeads.java) 
#it reduced time from ~20 mins to ~12 mins
#Also tried it in C and was slower? :(
def rawToDsvP2():
        #checks ALL other units if pre-req of that (gonna take ages)
        #Eg. OOPD searches all units that require OOPD as prereq, ie UCP, DSA
        #reads current dsv from p1, checks area of school and only
        #searches from within that school for UTLT and appends file

        for i in range(1, number_files+1):
            fw = open(dsv_dir+"\\"+str(i)+".dsv", "w", encoding='utf-8')
            dsvI = str(dsvStrArr[i-1]).split("$")
            unitNameI = dsvI[1]
            #print(i)

            dsvStr = dsvStrArr[i-1]
            #UNITS TO LEAD TO (main emphasis of this program)
            dsvStr += "UNITLEADS"+"$"

            #Cross check with each unit in specific school
            for j in range(1, number_files+1):
                dsvJ = str(dsvStrArr[j-1]).split("$")
                unitNameJ = dsvJ[1]

                # #only search from area of school (old idea with soup)
                # if areaI == areaJ:
                try:
                    prereqStr = dsvJ[11]
                    if unitNameI in prereqStr:
                        dsvStr+=unitNameJ+"$"
                except:
                    pass
            fw.write(dsvStr)
            sys.stdout.write("\r%f%%" % float((i/3502)*100))
            sys.stdout.flush()


def scrapeUnits():
    i=1
    for index in unit_indexes.unitIdx:
        #grab html of each index page where all units are listed
        #Ie. UNITS BEGINNING WITH A, UNITS B...etc
        raw_idx = requests.get(str(index), headers=headers).text
        soup_idx = BeautifulSoup(raw_idx, 'html.parser')

        #finds all individual unit html pages and scrapes them
        for unit in soup_idx.find_all('a', href=True):
            if "/units/3" in unit['href']:
                unitCode = unit['href']
                unitCode = unitCode.split("../../")
                unit_url = "http://handbook.curtin.edu.au/"+unitCode[1]
                unit_html = requests.get(unit_url, headers=headers).text
                #write each unit's html to txt file (storage)
                f = open(html_dir+"\\"+str(i)+".html", "w")
                f.write(unit_html)
                f.close()
                i+=1    

if __name__ == '__main__':
    main()

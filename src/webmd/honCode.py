import sys 
import requests 
import lxml.html 
import re

def getSite(id):
	hxs = lxml.html.document_fromstring(requests.get(id).content)
	try:
		links = hxs.xpath('//a/@href')
	except IndexError:
		print "Error occoured"
		links = ""

	return links	


if __name__ == '__main__':
	urls = ["www.webmd.com","http://www.ncvc.org/","www.medscape.com","http://www.medicinenet.com/","http://www.drugs.com/","http://www.everydayhealth.com/","http://www.medhelp.org/","http://www.healthline.com/","http://labtestsonline.org/","www.intelihealth.com","http://my.clevelandclinic.org/","http://www.healthline.com/","exchanges.webmd.com","blogs.webmd.com"]	
	hon = "www.hon.ch"
	healthNet = "www.healthonnet"
	guidestar = "www.guidestar.org"
	truste = "privacy.truste.com"
	urac = "www.urac.org"
	for eachURL in urls:
		honCode = 0
		if "http://" not in eachURL:
			links = getSite("http://"+eachURL)	
		else:
			links = getSite(eachURL)
		print len(links)
		for link in links:
			print link
			if hon in link:
				honCode = honCode+1
			if truste in link:
				honCode = honCode+1
			if urac in link:
				honCode = honCode+1
			if guidestar in link:
				honCode = honCode+1	
		print eachURL		
		print honCode		
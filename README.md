# Memex

https://www.fbo.gov/index?s=opportunity&mode=form&id=426485bc9531aaccba1b01ea6d4316ee&tab=core&_cview=0

>The Defense Advanced Research Projects Agency (DARPA) is soliciting proposals for innovative research to **maintain technological superiority in the area of content indexing and web search on the Internet**.

https://www.darpa.mil/program/memex

>Memex would ultimately apply to any public domain content; initially, **DARPA plans to develop Memex to address a key Defense Department mission: fighting human trafficking**. Human trafficking is a factor in many types of military, law enforcement and intelligence investigations and has a significant web presence to attract customers. The use of forums, chats, advertisements, job postings, hidden services, etc., continues to enable a growing industry of modern slavery. An index curated for the counter-trafficking domain, along with configurable interfaces for search and analysis, would enable new opportunities to uncover and defeat trafficking enterprises.

>Memex plans to explore three technical areas of interest: domain-specific indexing, domain-specific search, and DoD-specified applications. **The program is specifically not interested in proposals for the following: attributing anonymous services, deanonymizing or attributing identity to servers or IP addresses, or accessing information not intended to be publicly available.** The program plans to use commodity hardware and emphasize creating and leveraging open source technology and architecture.

>The Memex program gets its name and inspiration from a hypothetical device described in “As We May Think,” a 1945 article for The Atlantic Monthly written by Vannevar Bush, director of the U.S. Office of Scientific Research and Development (OSRD) during World War II. Envisioned as an analog computer to supplement human memory, the memex (a combination of “memory” and “index”) would store and automatically cross-reference all of the user’s books, records and other information.

## Issues

### Link Path Truncation

When a link like `https://darrienamor.wixsite.com/mysite1` is extracted, the link appears as `https://darrienamor.wixsite.com`. This is an issue, because some links depend upon the path. Dropping the path may result in an HTTP 404 resonse from the website.

## Creating Release Builds

Eclipse -> File -> Export -> Runnable Jar File to produce the [YYYY-MM-DD].jar file

## Execution

Add Java 7 to the PATH. Run the following command from the builds directory

    C:\Users\peon\Desktop\projects\CMU_memex\Builds\2017-10-05\Build>java -jar 2017-10-05.jar Example_text.txt Out

.NET -> Backpage -> Call Java Jar -> Import unique file for the crawl set -> Export to unique csv file -> Dump into dynamo db

I can process 411 records in 10 seconds. That's 41.1 records per second. The current target is 150,000 records per day. `150,000 records / 41.1 seconds per record = 3,650 seconds = 60 minutes`. This is great, it means the trouble will be in the network. Luckily I happen to have spent the last few months focusing on .NET's http client doing bulk operations on live HTTP sites in my Slideshow Creator so the problem is right where I want it. It probably will not even have defenses like images.nga.gov, but I welcome any such challenges.

[Related Projecs](https://opencatalog.darpa.mil/MEMEX.html)

## TJBatchExtractor
This project is a regular expression based information extractor designed to operate on
text captured from female escort advertisements originating from {US}
sections of Backpage.com. The motivation is to extract domain specific
information that may be representative of individuals or groups
responsible for each advertisement. To that end, the information extracted
focuses on physical description and contact information.

The system attempts to extract occurrences of the following informational elements.

Perspective_1st: Count of 1st person pronouns

Perspective_3rd: Count of 3rd person pronouns

Name: Female first names

Age: Age

Cost: Dollar figure charged for various services. Notation is given as Dollar/Measure/Unit. Dollar represents a cost, Unit represents object of the cost (e.g. hours, minutes, short stay, special, etc.), Measure represents the number of units (e.g. 30 minutes, 1 hour, hhr, etc.)

Height_ft: Height in feet, multiple values correlate with multiple values of Height_in

Height_in: Remaining inches of height, correlates with Height_ft

Weight: Weight in lbs

Cup: Cup size

Chest: Chest measurement

Waist: Waist measurement

Hip: Hip measurement

Ethnicity: Country referenced ethnicity (e.g. Spanish, Russian, etc.)

SkinColor: Color of skin

EyeColor: Color of eyes

HairColor: Color of hair

Restriction_Type: One of [no, over]; the type of restriction, i.e. "no black men", or "only men over 45."

Restriction_Ethnicity: The ethnicity/ skin color restricted

Restriction_Age: The threshold age value for the over restrictions

PhoneNumber: Phone number

AreaCode_State: State associated with phone number's area code

AreaCode_Cities: Cities/ locations associated with phone number's area code

Email: Email address

Url: urls specifically referenced or linked to in the body

Media: iframes and other foreign sourced content

## Building

You must use Java version 7. If you use java version 8 you will get an error when loading the application.xgapp which I believe is the core config and has to be loaded: https://github.com/mille856/CMU_memex/issues/1. The java version may be a bigger issue with GATE. I did try to update the gate.jar file, but that didn't work. I've archived Java/Eclipse installs locally, github doesn't allow greater than 100mb files, so the project can always be built.

1. Download and install [Java - jdk1.7.0_80](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html)

2. Download and install [Eclipse - Kepler SR2 - eclipse-standard-kepler-SR2-win32-x86_64.zip](http://www.eclipse.org/downloads/packages/release/kepler/sr2)

3. From here you should just need to set the eclipse workspace to CMU_MEMEX\Eclipse
*I will have to test this on a new fresh checkout*

## Further Reading

Great paper on ad analysis which would come into play after crawling and indexing is complete: http://repository.cmu.edu/cgi/viewcontent.cgi?article=1153&context=hsshonors

For example, what images are worth analyzing. Financially I can't do image analysis on all images. At least not with AWS Rekognition. Also a very interesting point made was that ads listed as *19* were signals for being underage. The paper is also good in that it defines *human trafficking*.

The author, Emily Kennedy has a business creating anti-human trafficking software called [Traffic Jam](http://www.marinusanalytics.com/)

http://www.marinusanalytics.com/articles/2017/6/27/face-search-debut

>The combination of the new FaceSearch into the existing Traffic Jam platform will make it possible for detectives to start with a photo of a missing child, a social media photo, or a photo from a social worker, and quickly determine whether this potential victim has been advertised online for commercial sex.

Most interestingly it only incorporated image analysis in June 2017. That's very recent.
TJBatchExtractor
=================
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

## Revisions from Fork

Here's the plan for this project:
1. Build a crawler/indexer to pull this data every second for a specific location. **I want something fast enough to get pages which are taken down.**
2. I want to transiently index this data from live pages into dynamodb. As seen in my SlideShowCreator.
3. Once the process is setup, I want to scale it out
   3. I expect to have cdn/anti-ddos/rate limiting come into play here. I'll deal with the issue as it arises at whatever scale.
   1. State-wide
   2. Nation-wide
   3. Internationally (Only if things go extremely well, but it may be an insane amount of data for my limited resources)
4. From here it will get very interesting. I will have near real-time raw source data across the entire United States or Globe. This will be beautiful data, because I can do very basic things such as ask:
  - what city has the most amount of ads per capita?
  - what city has the least amount of ads per capita?
  - is there any relation between per capita income in a city to the number of ads?
  - is there any relation between crime rates to the number of ads?
5. Now I will be able to begin searching new sites to see what else is out there.

## Building

You must use Java version 7. If you use java version 8 you will get an error when loading the application.xgapp which I believe is the core config and has to be loaded: https://github.com/mille856/CMU_memex/issues/1. The java version may be a bigger issue with GATE. I did try to update the gate.jar file, but that didn't work. I've archived Java/Eclipse installs locally, github doesn't allow greater than 100mb files, so the project can always be built.

1. Download and install [Java - jdk1.7.0_80] (http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html)

2. Download and install [Eclipse - Kepler SR2 - eclipse-standard-kepler-SR2-win32-x86_64.zip] (http://www.eclipse.org/downloads/packages/release/kepler/sr2)

3. From here you should just need to set the eclipse workspace to CMU_MEMEX\Eclipse
*I will have to test this on a new fresh checkout*
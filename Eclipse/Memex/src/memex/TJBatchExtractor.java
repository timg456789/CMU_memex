package memex;

/*
File:        TJBatchExtractor.java
Author:      Kyle Miller
Created:     July, 2014
Description: a GATE application wrapper for TJInfoExtractor

Copyright (C) 2014, Carnegie Mellon University
*/

/* 
* This project is distributed under the MIT license below.
* The project links to the TJInfoExtractor and gate libraries distributed under the 
* GNU Lesser General Public License, Version 3, June 2007
* (see https://gate.ac.uk/gate/licence.html)
*
The MIT License (MIT)

Copyright (c) <year> <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

* 
*/

/*

compile with:

javac -classpath '.:./dependencies/*' TJBatchExtractor.java

run with:
java -classpath '.:./dependencies/*' TJBatchExtractor [num_threads] [textfile] [outfile] [chunk_size]

*/

import gate.CorpusController;
import gate.Gate;
import gate.Factory;
import gate.util.persistence.PersistenceManager;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;

class TJBatchExtractor {  

	public static void main(String[] args) throws Exception {
		System.out.println("starting");
		
		// Here we go, get ready.
		// I can't even do unit tests yet.
		// Here is the poor mans unit tests.
		// Watch closely.
	
	int num_threads = 2;
 int chunk_size = 100000;
 int total_lines_read = 0;
 if( args.length>4 ) chunk_size = Integer.parseInt(args[4]);
	System.out.println("Initializing Gate");
 Gate.init();
	System.out.println("Loading setup file.");
	
	File setupFile = new File("TJInfoExtractor/application.xgapp");
	System.out.println("Setup file exists: " + setupFile.exists());
	
	System.out.println("Loading setup file.");
	Object applicationObject = PersistenceManager.loadObjectFromFile(setupFile);
	System.out.println("Casting setup file to corpus application.");
 CorpusController application = (CorpusController)applicationObject;

 System.out.println("setting input file.");;
 
 List<CorpusController> applicationList = new ArrayList<CorpusController>();    
 for(int i=0;i<num_threads;++i) applicationList.add( (CorpusController)Factory.duplicate(application) );

 System.out.println("Setting output file.");
 String outfile = "Out.csv";
 if( args.length>2 ) outfile = args[2];
 PrintWriter writer = new PrintWriter(outfile,"UTF-8");
 writer.println("Perspective_1st,Perspective_3rd,Name,Age,Cost,Height_ft,Height_in,Weight,Cup,Chest,Waist,Hip,Ethnicity,SkinColor,EyeColor,HairColor,Restriction_Type,Restriction_Ethnicity,Restriction_Age,PhoneNumber,AreaCode_State,AreaCode_Cities,Email,Url,Media");
 
 outfile = "Out.txt";
 if( args.length>3 ) outfile = args[3];
 PrintWriter writer2 = new PrintWriter(outfile,"UTF-8");
 
 System.out.println("Loading input file.");
 
 
 String inputFile = "Example_text.txt";
 System.out.println("Reading document " + inputFile + "...");
 BufferedReader br = new BufferedReader(new FileReader(inputFile));
 Boolean done = false;
 
 System.out.println("run run run all the way home.");
 while( !done ){
   List<String> FileLines = new ArrayList<String>();
   // Create container for results
   List<String> AnnotationResults = new ArrayList<String>();  
   List<String> AnnotationText = new ArrayList<String>();  
	      
   int LinesRead = 0;
   String fileline;
   // read the file
   while( true ){
	if(LinesRead >= chunk_size) break;
	if((fileline = br.readLine()) == null){
	  done = true;
	  break;
	}
	FileLines.add(fileline);
	LinesRead++;
	total_lines_read++;
   }

   //launch threads to process each chunk    
   int step = (int) Math.ceil(((double) FileLines.size())/((double) num_threads));
   List<ExtractorThread> pool = new ArrayList<ExtractorThread>();
   for(int i=0;i<num_threads;++i){
	pool.add( new ExtractorThread(
	    FileLines.subList(i*step,Math.min((i+1)*step,FileLines.size())),
	    applicationList.get(i),
	    i) );       
   }
   for(int i=0;i<num_threads;++i){ 
	pool.get(i).t.join();
	if(pool.get(i).results!=null) AnnotationResults.addAll(pool.get(i).results);
	if(pool.get(i).text!=null) AnnotationText.addAll(pool.get(i).text);
   }
   
   for(String l : AnnotationResults) writer.println(l);        
   for(String l : AnnotationText) writer2.println(l);
   System.out.println("Processed "+total_lines_read+" lines...");
 }
 
 br.close();
 writer.close();
 writer2.close();
 System.out.println("All done");
} 
}  

class ExtractorThread implements Runnable {
Thread t;
List<String> recs;
List<String> results;
List<String> text;
CorpusController application;
Integer threadID;

ExtractorThread(List<String> r,CorpusController a,Integer id) {
   // Create a new thread
   t = new Thread(this, "Thread"+id);
   recs = r;
   application = a;
   threadID = id;
   results = null;
   text = null;
   t.start(); // Start the thread
}



public static boolean isInteger(String s) {
   try { 
     Integer.parseInt(s); 
   } catch(NumberFormatException e) { 
     return false; 
   }
   return true;
 }   

	@Override
	public void run() {
	     System.out.println("Launching thread " + this.threadID);
	 try {
		 GateOutputModel r = new GateWrapper().ProcessRecords(application, recs);         
	     results = r.results;
	     text = r.text;
	 } catch (InterruptedException e) {
		 System.out.println("Child interrupted.");
	 } catch (Exception e){
		 System.out.println("Child caught exception " + e);
	 }
	 	System.out.println("Thread " + this.threadID + " finished.");
	}


}

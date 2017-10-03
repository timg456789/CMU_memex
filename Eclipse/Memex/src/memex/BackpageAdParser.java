package memex;

import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BackpageAdParser {
	
	public GateOutputModel Parse(
			int numberOfThreads,
			BufferedReader bufferedReader) throws IOException, InterruptedException, GateException {
		GateOutputModel result = new GateOutputModel();
		
		Gate.init();

		CorpusController application = GetGateConfiguration();
		List<CorpusController> applicationList = new ArrayList<CorpusController>();
		for (int i=0; i < numberOfThreads; ++i) {
			applicationList.add((CorpusController)Factory.duplicate(application));
		}
		
		List<String> fileLines;
		do {
			int chunkSize = 100000;
			fileLines = GetFileChunk(chunkSize, bufferedReader);
			List<String> annotationResults = new ArrayList<String>();
			List<String> annotationText = new ArrayList<String>();
			
			//launch threads to process each chunk
			int step = (int) Math.ceil(((double) fileLines.size())/((double) numberOfThreads));
			List<GateWrapperThread> pool = new ArrayList<GateWrapperThread>();
			
			for (int i=0;i<numberOfThreads;++i) {
				List<String> fileChunk = fileLines.subList(i*step,Math.min((i+1)*step, fileLines.size())); 
				GateWrapperThread currentThread = new GateWrapperThread(fileChunk,applicationList.get(i),i);
				currentThread.startThread();
				pool.add(currentThread);
			}
			for(int i=0;i<numberOfThreads;++i){
				pool.get(i).t.join();
				if(pool.get(i).results!=null) {
					annotationResults.addAll(pool.get(i).results);
				}
				if(pool.get(i).text!=null) {
					annotationText.addAll(pool.get(i).text);
				}
			}
			
			result.results.addAll(annotationResults);
			result.text.addAll(annotationText);
			
			System.out.println("Processed " + result.results.size() + " lines...");
		} while (fileLines.size() > 0);
	
		bufferedReader.close();
		
		return result;
	}
	
	private List<String> GetFileChunk(int chunkSize, BufferedReader bufferedFileReader) throws IOException {
		int linesRead = 0;
		List<String> fileLines = new ArrayList<String>();
		String fileLine;
		do {
			fileLine = bufferedFileReader.readLine();
			
			if (fileLine != null) {
				fileLines.add(fileLine);
				linesRead++;
			}
		} while (fileLine != null && linesRead <= chunkSize);
		return fileLines;
	}
	
	private CorpusController GetGateConfiguration() throws PersistenceException, ResourceInstantiationException, IOException {
		File setupFile = new File("TJInfoExtractor/application.xgapp");
		Object applicationObject = PersistenceManager.loadObjectFromFile(setupFile);
		CorpusController application = (CorpusController)applicationObject;
		return application;
	}
	
}

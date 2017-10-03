package memex;

import gate.CorpusController;

import java.util.List;

public class GateWrapperThread implements Runnable {
	Thread t;
	List<String> recs;
	List<String> results;
	List<String> text;
	CorpusController application;
	Integer threadID;

	public GateWrapperThread(List<String> r,CorpusController a,Integer id) {
		t = new Thread(this, "Thread"+id);
		recs = r;
		application = a;
		threadID = id;
		results = null;
		text = null;
	}
	
	public void startThread() {
		t.start();
	}

	@Override
	public void run() {
		System.out.println("Launching thread " + this.threadID);
		try {
			GateOutputModel r = new GateWrapper().processRecords(application, recs);
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
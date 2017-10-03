package memex;

import java.util.ArrayList;
import java.util.List;

public class GateOutputModel {
	
	public List<String> text;
	public List<String> results;

	public GateOutputModel() {
        this.results = new ArrayList<String>();
        this.text = new ArrayList<String>();
    }
	
}

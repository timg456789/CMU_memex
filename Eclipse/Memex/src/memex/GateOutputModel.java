package memex;

import java.util.List;

public class GateOutputModel {
	
	public List<String> text;
	public List<String> results;

	public GateOutputModel(List<String> r,List<String> t) {
        text = t;
        results = r;
    }
	
}

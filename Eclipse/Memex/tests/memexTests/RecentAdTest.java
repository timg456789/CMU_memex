package memexTests;

import gate.util.GateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;
import memex.BackpageAdParser;
import memex.GateOutputModel;

import org.junit.Test;

public class RecentAdTest {
	
	@Test
	public void test_example_files() throws IOException, InterruptedException, GateException {
		
		String sampleText =
				"http://us.backpage.com/ http://abilene.backpage.com/WomenSeekMen/some-ad-id-needs-to-exist I am the REAL Girl💋in💥 PICS 💥❗👉# 1 PROVIDER Call me Angeline 7272218966 - 27,>I'm willing travel ur place or host at mine. I do not provide any kind of qv.. I am an upscale professional with skills that will bl⏺w your mind! Avail any, time .... Call me and I'll be ready 7272218966 https://www.facebook.com/kyra.orr.921\r\n";
		StringReader inputString = new StringReader(sampleText);
		BufferedReader bufferedReader = new BufferedReader(inputString);
		
		BackpageAdParser backpageAdParser = new BackpageAdParser();
		GateOutputModel result = backpageAdParser.Parse(
				1,
				bufferedReader);
		
		Assert.assertEquals(1, result.results.size());
		Assert.assertEquals("http://us.backpage.com/,http://abilene.backpage.com/WomenSeekMen/some-ad-id-needs-to-exist,8,0,kyra,27,,,,,,,,,,,,,,,,7272218966;7272218966,FL;FL,st. petersburg;st. petersburg,,www.facebook.com,",
				result.results.get(0));
	}

}

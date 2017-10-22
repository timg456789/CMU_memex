package memexTests;

import gate.util.GateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;
import memex.BackpageAdParser;
import memex.GateOutputModel;

import org.junit.Test;

public class SourceIdentificationTest {

	@Test
	public void test_example_files() throws IOException, InterruptedException, GateException {
		
		String sampleText = "http://us.backpage.com/ http://abilene.backpage.com/WomenSeekMen/audreyy-skills-fr%CE%B5%CE%B1k%CE%B3/16724522 Audreyy âœ¨âœ´SKiLLÅžðŸ‘ŒðŸ‘„ ðŸŽ†FrÎµÎ±kÎ³ðŸ‘… - 25,> I'm Audrey full of Fun&pleasure my goal is to leave you satisfied I'm funðŸ‘ŒðŸ‘…ðŸ’¦ I'll leave you relaxed, ready to come back AudreyðŸ’‹4256576056 www.my-crappy-website.com\r\n";
		StringReader inputString = new StringReader(sampleText);
		BufferedReader bufferedReader = new BufferedReader(inputString);

		BackpageAdParser backpageAdParser = new BackpageAdParser();
		GateOutputModel result = backpageAdParser.Parse(
				1,
				bufferedReader);
		
		Assert.assertEquals(1, result.results.size());
		Assert.assertEquals("http://us.backpage.com/,http://abilene.backpage.com/WomenSeekMen/audreyy-skills-fr%CE%B5%CE%B1k%CE%B3/16724522,5,0,audrey,25,,,,,,,,,,,,,,,,4256576056,WA,everett  redmond  northern/eastern seattle area,,www.my-crappy-website.com,",
				result.results.get(0));
	}

}

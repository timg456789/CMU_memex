package memex;

import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;

public class GateWrapper {

	protected GateOutputModel processRecords(
			CorpusController application,
			List<String> records) throws Exception {
	
		Corpus corpus = Factory.newCorpus("BatchProcessApp Corpus");
		application.setCorpus(corpus);
		
		GateOutputModel model = new GateOutputModel();
	
		for(int record_num=0;record_num < records.size(); ++record_num){
			processRecord(records.get(record_num), model, corpus, application);
		}
		
		Factory.deleteResource(corpus);
		
		return model;
	}
	
	private void processRecord(String body, GateOutputModel model, Corpus corpus, CorpusController application) throws ResourceInstantiationException, ExecutionException {
		String title_age = "-1";
		String sep = "..THIS IS MY SEPARATION STRING..";
		String title = "";
		Boolean trimmed = false;
		String line = "";
		
		int sourceUrlIndexEnd = body.indexOf(" ");
		String sourceUrl = body.substring(0, sourceUrlIndexEnd);
		body = body.substring(sourceUrlIndexEnd + 1, body.length());
		line += sourceUrl + ",";
		
		int exactUrlIndexEnd = body.indexOf(" ");
		String exactUrl = body.substring(0, exactUrlIndexEnd);
		body = body.substring(exactUrlIndexEnd + 1, body.length());
		line += exactUrl + ",";
		
		int age_end = body.indexOf(",>");
		if (age_end>=0 && age_end < body.length()) {
			int age_start = body.lastIndexOf("-",age_end);
			if(age_start>=0 && age_start<age_end) {
				title_age = body.substring(age_start+1,age_end).trim();
				
				if (!Validation.isInteger(title_age)) {
					title_age = "-1";
				}
				else {
					title = body.substring(0,age_start);
					body = body.substring(age_end+2,body.length());
					body = title + sep + body;
					trimmed = true;
				}
			}
			if(!trimmed) {
				title = body.substring(0,age_end);
				body = body.substring(age_end+2,body.length());
				body = title + sep + body;
				trimmed = true;
			}
		}
		
		org.jsoup.nodes.Document htmldoc = Jsoup.parseBodyFragment(body.replaceAll("COMMA_GOES_HERE",","));
		Elements links = htmldoc.select("a[href]");
		Elements media = htmldoc.select("[src]");
		Elements imports = htmldoc.select("link[href]");
		
		model.text.add(htmldoc.text().replace(sep," "));
		Document doc = Factory.newDocument(htmldoc.text());
		
		corpus.add(doc);
		application.execute();
		corpus.clear();
		
		AnnotationSet Annots = doc.getAnnotations("");
		
		Integer FirstPersonCount=0, ThirdPersonCount=0;
		AnnotationSet FirstPerson = Annots.get("FirstPerson");
		if( FirstPerson != null ){
			FirstPersonCount = FirstPerson.size();
		}
		AnnotationSet ThirdPerson = Annots.get("ThirdPerson");
		if( ThirdPerson != null ) {
			ThirdPersonCount = ThirdPerson.size();
		}
		line += FirstPersonCount.toString()+","+ThirdPersonCount.toString()+",";
		line += getNames(Annots.get("Name"));
		
		AnnotationSet Age = Annots.get("Age");
		if(Age == null || Age.size() < 1) {
			line += title_age + ",";
		}
		else {
			Iterator<Annotation> Iter = Age.inDocumentOrder().iterator();
			line += title_age + ";";
			while(Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("age");
				if(Feat!=null) {
					line+= Feat.toString();
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		
		line += getCost(Annots.get("Cost"));
		line += getHeight(Annots.get("height"));
		
		AnnotationSet weight = Annots.get("weight");
		if(weight == null || weight.size() < 1) {
			line += ",";
		}
		else{
			Iterator<Annotation> Iter = weight.inDocumentOrder().iterator();
			while (Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("pounds");
				if(Feat!=null) {
					line+= Feat.toString();
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		
		line += getMeasurements(Annots.get("measurement"));
		
		AnnotationSet Ethnicity = Annots.get("Ethnicity");
		if(Ethnicity == null || Ethnicity.size() < 1) {
			line += ",";
		}
		else {
			Iterator<Annotation> Iter = Ethnicity.inDocumentOrder().iterator();
			while(Iter.hasNext()){
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("ethnicity");
				if(Feat!=null) {
					line+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ");
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		
		AnnotationSet SkinColor = Annots.get("SkinColor");
		if(SkinColor == null || SkinColor.size() < 1) {
			line += ",";
		}
		else {
			Iterator<Annotation> Iter = SkinColor.inDocumentOrder().iterator();
			while(Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("color");
				if(Feat!=null) {
					line+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ");
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		
		AnnotationSet EyeColor = Annots.get("EyeColor");
		if(EyeColor == null || EyeColor.size() < 1 ) {
			line += ",";
		}
		else {
			Iterator<Annotation> Iter = EyeColor.inDocumentOrder().iterator();
			while(Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("color");
				if(Feat!=null) {
					line+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ");
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		
		AnnotationSet HairColor = Annots.get("HairColor");
		if(HairColor == null || HairColor.size() < 1) {
			line += ",";
		}
		else {
			Iterator<Annotation> Iter = HairColor.inDocumentOrder().iterator();
			while(Iter.hasNext()){
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("color");
				if(Feat!=null) {
					line+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ");
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		
		line += getRestrictions(Annots.get("Restriction"));		
		line += getPhone(Annots.get("PhoneNumber"));
		
		String Emails = "";
		AnnotationSet Email = Annots.get("Email");
		if(Email == null || Email.size() < 1) {
			Emails = "";
		}
		else{
			Iterator<Annotation> Iter = Email.inDocumentOrder().iterator();
			while(Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("email");
				if(Feat!=null) {
					Emails+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ")+";";
				}
			}
		}
		if(links !=null) {
			for(Element l : links){
				String href = l.attr("abs:href");
				if(href==null) {
					continue;
				}
				if(href.length()>7 && href.substring(0,7).toLowerCase().equals("mailto:")) {
					Emails += href.substring(7,href.length()).replaceAll(","," ").replaceAll(";"," ") + ";";
				}
			}
		}
		if(Emails.length()>0 && Emails.substring(Emails.length()-1,Emails.length()).equals(";")) {
			Emails = Emails.substring(0,Emails.length()-1);
		}
		line += Emails + ",";
		
		String Urls = "";
		AnnotationSet Url = Annots.get("Url");
		if( Url == null || Url.size() < 1 ) {
			Urls = "";
		}
		else {
			Iterator<Annotation> Iter = Url.inDocumentOrder().iterator();
			while(Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("url");
				if(Feat!=null) {
					Urls+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ")+";";
				}
			}
		}
		if(links !=null ) {
			for(Element l : links) {
				String href = l.attr("abs:href");
				if(href==null) {
					continue;
				}
				if(href.length()<=7 || !href.substring(0,7).toLowerCase().equals("mailto:")) {
					Urls += href.replaceAll(","," ").replaceAll(";"," ") + ";";
				}
			}
		}
		if(imports != null) {
			for(Element l : imports){
				String href = l.attr("abs:href");
				if(href==null) {
					continue;
				}
				Urls += href.replaceAll(","," ").replaceAll(";"," ") + ";";
			}
		}
		if(Urls.length()>0 && Urls.substring(Urls.length()-1,Urls.length()).equals(";")) {
			Urls = Urls.substring(0,Urls.length()-1);
		}
		line += Urls + ",";
		
		String Medias = "";
		if(media != null){
			for(Element l : media) {
				String src = l.attr("abs:src");
				if(src==null){
					continue;
				}
				Medias += src.replaceAll(","," ").replaceAll(";"," ") + ";";
			}
		}
		if(Medias.length()>0 && Medias.substring(Medias.length()-1,Medias.length()).equals(";")) {
			Medias = Medias.substring(0,Medias.length()-1);
		}
		line += Medias;
		
		model.results.add(line);
		Factory.deleteResource(doc);
	}
	
	private String getNames(AnnotationSet names) {
		String line = "";
		if (names == null || names.size() < 1) {
			line += ",";
		}
		else {
			Iterator<Annotation> Iter = names.inDocumentOrder().iterator();
			while(Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("name");
				if(Feat!=null) {
					line+= Feat.toString();
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		return line;
	}
	
	private String getMeasurements(AnnotationSet measurements) {
		String line = "";
		if(measurements == null || measurements.size() < 1) {
			line += ",,,,";
		}
		else{
			String cup = "";
			String chest = "";
			String waist = "";
			String hip = "";
			Iterator<Annotation> Iter = measurements.inDocumentOrder().iterator();
			while (Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("cup");
				if(Feat!=null) {
					cup+= Feat.toString();
				}
				else {
					cup+="none";
				}
				Feat = Ann.getFeatures().get("chest");
				if (Feat!=null) {
					chest += Feat.toString();
				}
				else {
					chest+="none";
				}
				Feat = Ann.getFeatures().get("waist");
				if(Feat!=null) {
					waist+= Feat.toString();
				}
				else {
					waist+="none";
				}
				Feat = Ann.getFeatures().get("hip");
				if(Feat!=null) {
					hip+= Feat.toString();
				}
				else {
					hip+="none";
				}
				if (Iter.hasNext()) {
					cup += ";";
					chest += ";";
					waist += ";";
					hip += ";";
				}
			}
			line += cup+","+chest+","+waist+","+hip+",";
		}
		return line;
	}
	
	private String getRestrictions(AnnotationSet restrictions) {
		String line = "";
		if (restrictions == null || restrictions.size() < 1) {
			line += ",,,";
		}
		else {
			String type = "";
			String ethnicity = "";
			String age = "";
			Iterator<Annotation> Iter = restrictions.inDocumentOrder().iterator();
			while (Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("type");
				if(Feat!=null) {
					type+= Feat.toString();
				}
				else {
					type+="none";
				}
				Feat = Ann.getFeatures().get("ethnicity");
				if(Feat!=null) {
					ethnicity+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ");
				}
				else {
					ethnicity+="none";
				}
				Feat = Ann.getFeatures().get("age");
				if(Feat!=null) {
					age+=Feat.toString();
				}
				else {
					age+="none";
				}
				if(Iter.hasNext()){
					type += ";";
					ethnicity += ";";
					age += ";";
				}
			}
			line += type+","+ethnicity+","+age+",";
		}
		return line;
	}
	
	private String getPhone(AnnotationSet phone) {
		String line = "";
		if(phone == null || phone.size() < 1) {
			line += ",,,";
		}
		else {
			String value = "";
			String state = "";
			String city = "";
			Iterator<Annotation> Iter = phone.inDocumentOrder().iterator();
			while(Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("value");
				if(Feat!=null) {
					value+= Feat.toString();
				}
				else {
					value+="none";
				}
				Feat = Ann.getFeatures().get("state");
				if(Feat!=null) {
					state+= Feat.toString();
				}
				else {
					state+="none";
				}
				Feat = Ann.getFeatures().get("area");
				if(Feat!=null) {
					city+= Feat.toString().toLowerCase().replaceAll(","," ").replaceAll(";"," ");
				}
				else {
					city+="none";
				}
				if(Iter.hasNext()){
					value += ";";
					state += ";";
					city += ";";
				}
			}
			line += value+","+state+","+city+",";
		}
		return line;
	}
	
	private String getHeight(AnnotationSet height) {
		String line = "";
		if (height == null || height.size() < 1) {
			line += ",,";
		}
		else {
			String ft = "";
			String inch = "";
			Iterator<Annotation> Iter = height.inDocumentOrder().iterator();
			while (Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("feet");
				if(Feat!=null) {
					ft+= Feat.toString();
				}
				else {
					ft+="none";
				}
				Feat = Ann.getFeatures().get("inches");
				if(Feat!=null) {
					inch+= Feat.toString();
				}
				else {
					inch+="none";
				}
				if (Iter.hasNext()) {
					ft += ";";
					inch += ";";
				}
			}
			line += ft+","+inch+",";
		}
		return line;
	}
	
	private String getCost(AnnotationSet cost) {
		String line = "";
		if (cost == null || cost.size() < 1) {
			line += ",";
		}
		else {
			Iterator<Annotation> Iter = cost.inDocumentOrder().iterator();
			while (Iter.hasNext()) {
				Annotation Ann = Iter.next();
				Object Feat = Ann.getFeatures().get("value");
				if (Feat!=null) {
					line+= Feat.toString();
				}
				else {
					line+="none";
				}
				line+="/";
				Feat = Ann.getFeatures().get("target_value");
				if(Feat!=null) {
					line+= Feat.toString();
				}
				else {
					line+="none";
				}
				line+="/";
				Feat = Ann.getFeatures().get("target_type");
				if(Feat!=null) {
					line+= Feat.toString();
				}
				else {
					line+="none";
				}
				if(Iter.hasNext()) {
					line += ";";
				}
			}
			line += ",";
		}
		
		return line;
	}
	
}

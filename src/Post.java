import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.dom4j.Element;



class Post implements Serializable {
	
	static int sid=0;
	static int dqid=1;//id of question detected
	int postID;
	String author;
	String date;
	Threadf atThreadf;
	List<ParsedSentence> hasSentences;
	
	Post(int id, String author, String date, Element postelement, Threadf threadf){
		
		if(id==0){
			sid=0;
			dqid=1;
		}
		this.postID = id;
		this.author = author;
		this.date = date;
		this.atThreadf = threadf;
		this.hasSentences = new ArrayList<ParsedSentence>();
		boolean isTraining = !postelement.isTextOnly();
		
		if(isTraining){
			Iterator it = postelement.elementIterator();
			while (it.hasNext()) {
				Element qca = (Element)it.next();
				String qcatext = qca.getTextTrim();
				System.out.println("Block: "+qcatext);//prevent empty block
				if(qcatext.length()>1){
					String[] sentences = qcatext.split("(?<=\\.|\\?|:|!)(?=\\s|\"|[A-Z])");
					System.out.println("# of sentences contained: "+sentences.length);//
					String role = qca.getName();
					String qid="0";//PLAIN's qid=0
					if(qca.attributes().size()!=0){
						qid = qca.attributeValue("id");
					}
					if(role=="context"){
						for(int i=0; i<sentences.length; i++){
							String baseSentence = sentences[i].trim().replaceAll("[^a-zA-Z0-9]{3,}", "");
							if(baseSentence.length()>5){//prevent less than 2 words sentences
								ParsedSentence asentence = new ParsedSentence(sid, qid, baseSentence, ParsedSentence.Role.CONTEXT, this);
								System.out.print(asentence.sentenceInfo());//
								this.hasSentences.add(asentence);
								sid+=1;
							}
						}
					}
					if(role=="question"){
						for(int i=0; i<sentences.length; i++){
							String baseSentence = sentences[i].trim().replaceAll("[^a-zA-Z0-9]{3,}", "");
							if(baseSentence.length()>5){
								ParsedSentence asentence = new ParsedSentence(sid, qid, baseSentence, ParsedSentence.Role.QUESTION, this);
								System.out.print(asentence.sentenceInfo());//
								this.hasSentences.add(asentence);
								sid+=1;
							}
						}
					}
					if(role=="answer"){
						for(int i=0; i<sentences.length; i++){
							String baseSentence = sentences[i].trim().replaceAll("[^a-zA-Z0-9]{3,}", "");
							if(baseSentence.length()>5){
								ParsedSentence asentence = new ParsedSentence(sid, qid, baseSentence, ParsedSentence.Role.ANSWER, this);
								System.out.print(asentence.sentenceInfo());//
								this.hasSentences.add(asentence);
								sid+=1;
							}
						}
					}
					if(role=="plain"){
						for(int i=0; i<sentences.length; i++){
							String baseSentence = sentences[i].trim().replaceAll("[^a-zA-Z0-9]{3,}", "");
							if(baseSentence.length()>5){
								ParsedSentence asentence = new ParsedSentence(sid, qid, baseSentence, ParsedSentence.Role.PLAIN, this);
								System.out.print(asentence.sentenceInfo());//
								this.hasSentences.add(asentence);
								sid+=1;
							}
						}
					}
				}
			}
		}else{
			String posttext = postelement.getTextTrim();
			if(posttext.length()>1){//Empty post's sentence list is 0-length
				String[] sentences = posttext.split("(?<=\\.|\\?|:|!)(?=\\s|\"|[A-Z])");
				for(int i=0; i<sentences.length; i++){
					String asentence = sentences[i].trim().replaceAll("[^a-zA-Z0-9]{3,}", "");
					if(asentence.length()>5){
						if(asentence.endsWith("?")){//detect ordinary questions and assign an qid to it ???
							ParsedSentence ps = new ParsedSentence(sid, String.valueOf(dqid), asentence, ParsedSentence.Role.QUESTION, this);
			    			//System.out.print(ps.sentenceInfo());//
			    			this.hasSentences.add(ps);
			    			sid+=1;
							dqid+=1;
						}else{
							ParsedSentence ps = new ParsedSentence(sid, "0", asentence, ParsedSentence.Role.UNKNOWN, this);
			    			//System.out.print(ps.sentenceInfo());//
			    			this.hasSentences.add(ps);
			    			sid+=1;
						}
					}
	    		}
			}
		}
	}
}

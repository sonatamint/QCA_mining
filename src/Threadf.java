import java.io.PrintStream;
import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

class Threadf implements Serializable {

	static List<Threadf> allThreadfs = new ArrayList<Threadf>();
	static Map<String, Integer> df = new HashMap<String, Integer>();//keys are in lower-case, nouns are singularized
	static Map<String, Map<String, Double>> coTable= new HashMap<String, Map<String, Double>>();//Normalized mutual information between key(15%|D|>frq>1) nouns (singular, lower-case)
	String threadfPath;
	String threadfTitle;
	int threadfID;
	List<Post> hasPosts;
	Map<String, Double> tfidf;//keys are in lower-case, nouns are singularized
	transient List<Map<String, String>> crfFeatures;//one map per sentence per question
	
	
	Threadf(File xmlfile, int id){
		
		this.threadfPath = xmlfile.getAbsolutePath();
		this.threadfID = id;
		this.hasPosts = new ArrayList<Post>();
		this.tfidf = new HashMap<String, Double>();
		this.crfFeatures=new ArrayList<Map<String, String>>();
		SAXReader reader = new SAXReader();
	    try{
	    	Document document = reader.read(xmlfile);
	    	this.threadfTitle = document.getRootElement().attributeValue("title").trim();
		    List postlist=document.selectNodes("//post");
		    List authorlist=document.selectNodes("//author");
		    List datelist=document.selectNodes("//date");
		    System.out.println("Begin to process thread "+ id +" : "+this.threadfTitle+" @"+xmlfile.getAbsolutePath());//
		    //System.out.println("Size of current thread: "+postlist.size()+"; "+authorlist.size()+"; "+datelist.size());//
		    try{
		    	for(int i=0; i<postlist.size(); i++){//Incomplete thread!
		    		Element author = (Element)authorlist.get(i);
		    		Element date = (Element)datelist.get(i);
		    		Element post = (Element)postlist.get(i);
		    		Post apost = new Post(i, author.getTextTrim(), date.getTextTrim(), post, this);
		    		this.hasPosts.add(apost);
		    	} 	
		    }catch (Exception e){	
		    	System.out.println(e);
		    	System.out.println("Cannot pick up posts from thread:'"+this.threadfPath+"'.");
		    }
		    //allThreadfs.add(this);
	    }catch (DocumentException e){
	    	System.out.println(e);
	    	System.out.println("Cannot read xml file:'"+this.threadfPath+"'.");
	    }	
	}
	
	void computeTfidf(){//This method should be called after all threads have been added and the computeDf function is called.
		for(String wd:this.tfidf.keySet()){
			double tf = this.tfidf.get(wd);
			double idf = (double)Threadf.allThreadfs.size()/df.get(wd);
			double tfidf = tf * Math.log(idf);
			this.tfidf.put(wd, tfidf);//before this the tfidf contains only tf
		}
	}
	
	static void computeDf(){//This method should be called after all threads have been added.
		for(Threadf thrf: allThreadfs){
			for(String wd: thrf.tfidf.keySet()){
				if (df.containsKey(wd)) {
					int count = df.get(wd);
					df.put(wd, count + 1);
				} else {
					df.put(wd, 1);
				}
			}
		}
	}
	
	static void computeCooccur(){//This method should be called after DF is calculated.
		/*suffixing co-occurrence
		for(Threadf thrf: allThreadfs){
			for(Post pst:thrf.hasPosts){
				for(ParsedSentence par:pst.hasSentences){
					String previousNoun = null;
					for(int i=0;i<par.wordSequence.length;i++){
						String tag = par.posTags[i];
						String word = par.wordSequence[i];
						if(tag.startsWith("NN")&&df.containsKey(word)&&(df.get(word)>1)){
							if((previousNoun!=null)&&(!previousNoun.equals(word))){
								if (coTable.containsKey(word)) {
									Map<String, Double> thisrow = coTable.get(word);
									if(thisrow.containsKey(previousNoun)){
										double d = thisrow.get(previousNoun)+1;
										thisrow.put(previousNoun, d);
										double dd = thisrow.get("suffxcount")+1;
										thisrow.put("suffxcount", dd);
									}
								}else{
									Map<String, Double> newthisrow = new HashMap<String, Double>();
									newthisrow.put(previousNoun, (double)1);
									newthisrow.put("suffxcount", (double)1);
									coTable.put(word, newthisrow);
								}
							}
							previousNoun = word;
						}else{
							previousNoun = null;
						}
					}
				}
			}
		}
		*/
		for(Threadf thrf: allThreadfs){
			Set<String> keyNouns = new HashSet<String>();
			for(Post pst:thrf.hasPosts){
				for(ParsedSentence par:pst.hasSentences){
					for(int i=0;i<par.wordSequence.length;i++){
						/*Original pure NN
						String tag = par.posTags[i];
						String word = (tag.startsWith("NNS")) ? PlrSgl.INSTANCE.singularize(par.wordSequence[i]) : par.wordSequence[i];
						if((tag.startsWith("NN"))&&(df.containsKey(word))){
							int times = df.get(word);
							if((times>1)&&((double)times/allThreadfs.size()<0.15)){
								keyNouns.add(word);
							}
						}
						*/
						String word = par.wordSequence[i];
						if(df.containsKey(word)){
							int times = df.get(word);
							if(times>5){
								keyNouns.add(word);
							}
						}
					}
				}
			}
			if(keyNouns.size()>1){
				for(String wdi:keyNouns){
					if (coTable.containsKey(wdi)) {
						Map<String, Double> table= coTable.get(wdi);
						for(String wdj:keyNouns){
							if(!wdi.equals(wdj)){
								if(table.containsKey(wdj)){
									double cooccount = table.get(wdj);
									table.put(wdj, cooccount+1);
								}else {
									table.put(wdj, (double)1);
								}
							}
						}
					}else{
						Map<String, Double> table= new HashMap<String, Double>();
						for(String wdj:keyNouns){
							if(!wdi.equals(wdj)){
								table.put(wdj, (double)1);
							}
						}
						coTable.put(wdi, table);
					}
				}
			}
		}
		for(String wdi: coTable.keySet()){
			int itimes = df.get(wdi);
			Map<String, Double> rowi = coTable.get(wdi);
			for(String wdj: rowi.keySet()){
				int jtimes = df.get(wdj);
				double cotimes = rowi.get(wdj);
				double d = cotimes*allThreadfs.size()/(itimes*jtimes);
				double r = Math.log(d)/Math.log(0.5*allThreadfs.size());//normalizing mutual information
				if(r>0){
					rowi.put(wdj, r);
				}else{
					rowi.put(wdj, (double)0);
				}
				
			}
		}
	}
	
	void computeCrfFeatures(){
		List<ParsedSentence> questions = new ArrayList<ParsedSentence>();
		List<ParsedSentence> commonSentences = new ArrayList<ParsedSentence>();
		Map<Set<ParsedSentence>, Double[]> simTable= new HashMap<Set<ParsedSentence>, Double[]>();//similarity between sentence pair
		int maxQid = 0;
		for(Post post:this.hasPosts){
			for(ParsedSentence ps:post.hasSentences){
				if(ps.role==ParsedSentence.Role.QUESTION){
					questions.add(ps);
					int qid = ps.qids.toArray(new Integer[1])[0];
					if(qid > maxQid){
						maxQid = qid;
					}
				}else{
					commonSentences.add(ps);
				}
			}
		}
		for(int i=1; i<=maxQid; i++){//generate feature set for each question
			for(int j=0;j<commonSentences.size();j++){
				ParsedSentence ps= commonSentences.get(j);
				Map<String, String> features = new HashMap<String, String>();
				if(ps.qids.contains(i)){
					if(ps.role==ParsedSentence.Role.CONTEXT){
						features.put("aalabel", "CONTEXT");
					}
					if(ps.role==ParsedSentence.Role.ANSWER){
						features.put("aalabel", "ANSWER");
					}
				}else{
					if(ps.role==ParsedSentence.Role.UNKNOWN){
						features.put("aalabel", "UNKNOWN");
					}else{
						features.put("aalabel", "PLAIN");//context/answer not for current question is also labeled as plain
					}
				}
				
				double cosScoreQ = 0;//question similarity
				double simScoreQ = 0;
				int qPosition = 0;
				String author = "";
				for(ParsedSentence qps:questions){
					if(qps.qids.contains(i)){
						qPosition = qps.id;//
						author = qps.atPost.author;
						double s = ps.cosSimWith(qps);
						double m = ps.sentenceSim(qps);//
						if(s > cosScoreQ){
							cosScoreQ=s;
						}
						if(m > simScoreQ){
							simScoreQ=m;
						}
					}
				}
				features.put("cosSimQ", Double.toString(cosScoreQ));
				features.put("wnSimQ", Double.toString(simScoreQ));
				
				double cosScoreN = 0;//neighborhood similarity
				double simScoreN = 0;
				boolean samePostwithPre = true;
				if(j==0){
					ParsedSentence next = commonSentences.get(j+1);
					boolean found = false;
					for(Set<ParsedSentence> pair:simTable.keySet()){
						if(pair.contains(ps)&&pair.contains(next)){
							System.out.println(">>Has found:>>"+ps+" ('s sim with) "+next);//
							cosScoreN=simTable.get(pair)[0];
							simScoreN=simTable.get(pair)[1];
							found=true;
							break;
						}
					}
					if(!found){
						System.out.println(">>Computing:>>"+ps+" ('s sim with) "+next);//
						cosScoreN=ps.cosSimWith(next);
						simScoreN=ps.sentenceSim(next);
						Double[] scores = new Double[]{cosScoreN,simScoreN};
						Set<ParsedSentence> newPair = new HashSet<ParsedSentence>();
						newPair.add(ps);
						newPair.add(next);
						simTable.put(newPair, scores);
					}
					samePostwithPre = false;
				}
				if(j>0&&j==commonSentences.size()-1){
					ParsedSentence prev = commonSentences.get(j-1);
					for(Set<ParsedSentence> pair:simTable.keySet()){
						if(pair.contains(ps)&&pair.contains(prev)){
							System.out.println(">>Has found:>>"+ps+" ('s sim with) "+prev);//
							cosScoreN=simTable.get(pair)[0];
							simScoreN=simTable.get(pair)[1];
							break;
						}
					}
					if(ps.atPost.equals(commonSentences.get(j-1).atPost)){
						samePostwithPre = true;
					}
				}
				if(j>0&&j<commonSentences.size()-1){
					double cosScoreNp = 0;
					double cosScoreNn = 0;
					double simScoreNp = 0;
					double simScoreNn = 0;
					boolean found = false;
					ParsedSentence next = commonSentences.get(j+1);
					for(Set<ParsedSentence> pair:simTable.keySet()){
						if(pair.contains(ps)&&pair.contains(next)){
							System.out.println(">>Has found:>>"+ps+" ('s sim with) "+next);//
							cosScoreNn=simTable.get(pair)[0];
							simScoreNn=simTable.get(pair)[1];
							found=true;
							break;
						}
					}
					if(!found){
						System.out.println(">>Computing:>>"+ps+" ('s sim with) "+next);//
						cosScoreN=ps.cosSimWith(next);
						simScoreN=ps.sentenceSim(next);
						Double[] scores = new Double[]{cosScoreN,simScoreN};
						Set<ParsedSentence> newPair = new HashSet<ParsedSentence>();
						newPair.add(ps);
						newPair.add(next);
						simTable.put(newPair, scores);
					}
					ParsedSentence prev = commonSentences.get(j-1);
					for(Set<ParsedSentence> pair:simTable.keySet()){
						if(pair.contains(ps)&&pair.contains(prev)){
							System.out.println(">>Has found:>>"+ps+" ('s sim with) "+prev);//
							cosScoreNp=simTable.get(pair)[0];
							simScoreNp=simTable.get(pair)[1];
						}
					}
					cosScoreN=0.5*(cosScoreNp+cosScoreNn);
					simScoreN=0.5*(simScoreNp+simScoreNn);
					if(ps.atPost.equals(commonSentences.get(j-1).atPost)){
						samePostwithPre = true;
					}
				}
				features.put("cosSimN", Double.toString(cosScoreN));
				features.put("wnSimN", Double.toString(simScoreN));
				
				features.put("qPosition", Integer.toString(Math.abs(ps.id-qPosition)));
				features.put("byQauthor", Boolean.toString(ps.atPost.author.equals(author)));
				features.put("samePostwithPre", Boolean.toString(samePostwithPre));
				
				int prpNumber = 0;
				for(String tag:ps.posTags){
					if(tag.contains("PRP")){
						prpNumber+=1;
					}
				}
				features.put("prpNumber", Integer.toString(prpNumber));
				boolean acknowledgment = ps.baseSentence.toLowerCase().matches(".*\\b(yes|ok|uh)\\b.*");
				features.put("acknowledgment", Boolean.toString(acknowledgment));
				features.put("nonStopWords", Integer.toString(ps.hasWords.size()));
				//features.put("zoriSentence-->", ps.baseSentence);//
				this.crfFeatures.add(features);
			}
			this.crfFeatures.add(new HashMap<String, String>());//training instance separator
		}
		
	}
	
	void featurePrint(PrintStream prt){
		
		for(Map<String, String> mp:this.crfFeatures){
			if(mp.size()>0){
				List<Map.Entry<String, String>> featureListing = new ArrayList<Map.Entry<String, String>>(mp.entrySet());
				Collections.sort(featureListing, new Comparator<Map.Entry<String, String>>(){
					public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2){
						return o1.getKey().compareTo(o2.getKey());
					}
				});
				for(Map.Entry<String, String> en:featureListing){//output label first
					if(en.getKey().equals("aalabel")){
						prt.print(en.getValue()+"\t");
					}else{
						String key = en.getKey();
						if(key.equals("byQauthor")||key.equals("samePostwithPre")||key.equals("acknowledgment")){
							prt.print(key+"="+en.getValue()+"\t");
						}else{
							prt.print(key+"=n:"+en.getValue()+"\t");
						}
					}
				}
				prt.println();
			}else{
				prt.println();
			}
		}
	}

}

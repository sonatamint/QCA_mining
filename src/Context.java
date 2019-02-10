import java.io.EOFException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


public class Context implements Serializable, Cloneable {
	
	boolean virtue;//true if context is manually constructed, e.g. cluster center and input environment info
	String threadPath;
	String threadTitle;
	String[] SVO;
	Map<String, Double> weightedWords;
	Map<String, Double> nounPhrases;//like "a/JJ b/NN c/NNS"
	
	//clone provoking stack overflow error!
	public Context clone(){
		return this.clone();
	}
	
	Context(String str){
		this.virtue = true;
		this.threadPath = "";
		this.threadTitle = "";
		this.SVO = new String[]{"-","-","-"};
		String[] sentences = str.split("(?<=\\.|\\?|:|!)(?=\\s|\"|[A-Z])");
		Map<String, Double> NPs = new LinkedHashMap<String, Double>();
		this.weightedWords = new HashMap<String, Double>(); 
		for(int i=0; i<sentences.length; i++){
			String baseSentence = sentences[i].trim().replaceAll("[^a-zA-Z0-9]{3,}", "");
			ParsedSentence ps = new ParsedSentence(baseSentence);
			for(String aword : ps.wordSequence){
				if(!weightedWords.containsKey(aword)){
					if(Threadf.df.containsKey(aword)){
						double idf = (double)QcaRecord.contextDB.size()/Threadf.df.get(aword);//One context per thread
						double tfidf = Math.log(idf);
						weightedWords.put(aword, tfidf);
					}
				}else{
					double idf = (double)QcaRecord.contextDB.size()/Threadf.df.get(aword);
					double tfidf = Math.log(idf);
					weightedWords.put(aword, weightedWords.get(aword)+tfidf);
				}
			}
			NPs.putAll(findNP(ps));
		}
		this.nounPhrases = new HashMap<String, Double>();
		double seq = (double)1;
		Iterator<String> itr = NPs.keySet().iterator();
		while(itr.hasNext()){
			String aNP = itr.next();
			double d = NPs.get(aNP);//Math.log(seq+1)*NPs.get(aNP);
			nounPhrases.put(aNP, d);
			seq++;
		}
		nounPhrases = SortSMap(nounPhrases, 20, 0);
	}
	
	static Map<String, Double> SortSMap(Map<String, Double> tobeSorted, int length, double threshold){
		List<Map.Entry<String, Double>> map2list = new ArrayList<Map.Entry<String, Double>>(tobeSorted.entrySet());
		Collections.sort(map2list, new Comparator<Map.Entry<String, Double>>(){
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2){
				return (-1) * o1.getValue().compareTo(o2.getValue());
			}
		});
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for(int i=0; (i<map2list.size())&&(i<length); i++){
			String aNP = map2list.get(i).getKey();
			double score = map2list.get(i).getValue();
			if(score > threshold){
				sortedMap.put(aNP, score);
			}else{
				break;
			}
		}
		return sortedMap;
	}
	
	Context(QcaRecord record){
		
		this.virtue = false;
		this.weightedWords = new HashMap<String, Double>();
		Map<String, Double> NP2Rank = new HashMap<String, Double>();
		ParsedSentence psQ = record.questionList.get(0);
		this.weightedWords = psQ.atPost.atThreadf.tfidf;
		this.threadPath=psQ.atPost.atThreadf.threadfPath;
		this.threadTitle=psQ.atPost.atThreadf.threadfTitle;
		ParsedSentence psT = new ParsedSentence(threadTitle);
		Map<String, Double> titleNPs = findNP(psT);
		Map<String, Double> questNPs = findNP(psQ);
		if(!findSVO(psQ,questNPs)){
			psT.parseSentence();
			mustFindSVO(psT,titleNPs);
		}
		NP2Rank.putAll(questNPs);
		for(ParsedSentence aps:record.contextList){
			NP2Rank.putAll(findNP(aps));
		}
		this.nounPhrases = SortSMap(NP2Rank, 20, 5);//5!!! Serialization problem if uses subList
	}
	
	boolean findSVO(ParsedSentence ps, Map<String, Double> map){
		
		if(ps.typedDependencies!=null){
			List<String[]> candiSV = new ArrayList<String[]>();//like {(surfaces-contact),(solution-converges),...}
			List<String[]> candiVO = new ArrayList<String[]>();//like {(show-contact),(mesh-area),...}
			for(int i=0;i<ps.typedDependencies.size();i++){
				String relation=ps.typedDependencies.get(i).toString(true);
				if(relation.startsWith("nsubj")){
					String subj=relation.substring(relation.indexOf(" ")+1, relation.indexOf(")"));
					String pred=relation.substring(relation.indexOf("(")+1, relation.indexOf(","));
					for(String aNP : map.keySet()){
						if(aNP.substring(aNP.lastIndexOf(" ")+1).equals(subj)){
							candiSV.add(new String[]{aNP,pred});
						}
					}
				}
				if(relation.startsWith("dobj")){
					String obj=relation.substring(relation.indexOf(" ")+1, relation.indexOf(")"));
					String pred=relation.substring(relation.indexOf("(")+1, relation.indexOf(","));
					for(String aNP : map.keySet()){
						if(aNP.substring(aNP.lastIndexOf(" ")+1).equals(obj)){
							candiVO.add(new String[]{pred,aNP});
						}
					}
				}
			}
			double maxScore = 0;
			for(String[] np : candiSV){
				double d = map.get(np[0]);
				if(d > maxScore){
					maxScore = d;
					this.SVO = new String[]{np[0],np[1],"-"};
				}
			}
			for(String[] np : candiVO){
				double d = map.get(np[1]);
				if(d > maxScore){
					maxScore = d;
					this.SVO = new String[]{"-",np[0],np[1]};
				}
			}
			if(maxScore > 4){
				return true;
			}else{
				this.SVO = null;
				return false;
			}
		}else{
			return false;
		}
	}
	
	void mustFindSVO(ParsedSentence ps, Map<String, Double> map){
		if(!findSVO(ps,map)){
			int length = 0;
			for(String np : map.keySet()){
				if(np.length()>length){
					length = np.length();
					this.SVO = new String[]{np,"-","-"};
				}
			}
			if(length==0){
				this.SVO = new String[]{"-","-","-"};
			}
		}
	}
	
	Map<String, Double> findNP (ParsedSentence s){//May return empty map, configured for constructed context NP weighting and SVO finding
		Map<String, Double> found = new LinkedHashMap<String, Double>();
		boolean isNN = false;
		boolean isJJ = false;
		double tfidfSum = 0;
		String tempPhr = "";
		for(int i=0;i<s.wordSequence.length;i++){
			if(s.posTags[i].startsWith("NN")){
				String wd = s.wordSequence[i];//Plural nouns are singularized
				tempPhr += wd+" ";
				tfidfSum += (weightedWords.containsKey(wd))? weightedWords.get(wd): 0;
				if(i==s.wordSequence.length-1){
					found.put(tempPhr.trim(),tfidfSum);
				}
				isNN = true;
				isJJ = false;
			}else{
				if(s.posTags[i].startsWith("JJ")){
					String wd = s.wordSequence[i];
					if(isNN){
						found.put(tempPhr.trim(),tfidfSum);
					}
					double scr = (weightedWords.containsKey(wd))? weightedWords.get(wd): 0;
					tempPhr = isJJ ? tempPhr+wd+" " : wd+" ";
					tfidfSum = isJJ ? tfidfSum + scr : scr;
					isJJ = true;
				}else{
					if(isNN){
						found.put(tempPhr.trim(),tfidfSum);
					}
					tempPhr = "";
					tfidfSum = 0;
					isJJ = false;
				}
				isNN = false;
			}
		}
		return found;
	}
	
	public String toString(){
		String out = ">>> "+SVO[0]+" "+SVO[1]+" "+SVO[2]+"\r\n";
		out += " > "+this.threadTitle+"\r\n";
		out += " > "+this.threadPath+"\r\n > ";
		for(String cphr: nounPhrases.keySet()){
			double tfidf = nounPhrases.get(cphr);
			out += cphr+" "+String.valueOf(tfidf)+";  ";
		}
		out += "\r\n\r\n";
		return out;
	}
}

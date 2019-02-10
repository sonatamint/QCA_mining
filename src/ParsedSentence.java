import java.io.Serializable;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.regex.*;
import java.io.StringReader;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;   
import edu.stanford.nlp.trees.*;

import shef.nlp.wordnet.similarity.SimilarityMeasure;

class ParsedSentence implements Serializable {
	
	int id;
	Set<Integer> qids;//Respecting to which question
	Post atPost;
	Role role;
	boolean isEmpty;
	Set<String> hasWords;//Non-repetitive low-case words (nouns are singularized), with stop words removed, may be empty
	String baseSentence;//All words are in lower-case, plural nouns are transformed into singular form, may be empty string
	String[] wordSequence;//May contains stop words and zero-length words, nouns are singularized, may be null
	String[] posTags;//May be null

	List<TypedDependency> typedDependencies;
	
	enum Role{
		CONTEXT, QUESTION, ANSWER, PLAIN, UNKNOWN
	}
	
	
	ParsedSentence(int id, String qid, String sentence, Role r, Post atpost){
		
		this.id = id;
		this.role = r;
		this.atPost = atpost;
		this.qids = new HashSet<Integer>();//A sentence can address multiple questions
		String[] split = qid.split(",");
		for(int i=0;i<split.length;i++){
			this.qids.add(Integer.parseInt(split[i]));
		}
		this.hasWords = new HashSet<String>();
		this.baseSentence = preProcessing(sentence.replaceAll("[\\(\\)]", " ")).toLowerCase();//May return a 0 length sentence
		if(baseSentence.length()>0){
			String tagged = MainClass.posTagger.tagString(baseSentence);
			String[] sections = tagged.split(" ");
			this.wordSequence = new String[sections.length];
			this.posTags = new String[sections.length];
			for(int i=0;i<sections.length;i++){
				String[] temp = sections[i].split("/");
				if((sections[i].length()>5)&&(temp.length==2)){//5, pay attention to punctuation
					wordSequence[i] = temp[0];
					posTags[i] = temp[1];
					String aword = null;
					if(posTags[i].startsWith("NNS")){
						aword = PlrSgl.INSTANCE.singularize(wordSequence[i]);
						this.baseSentence = baseSentence.replaceAll("\\b"+wordSequence[i]+"\\b", aword);
						wordSequence[i] = aword;
					}else{
						aword = wordSequence[i];
					}
					if(!Stopwords.is(aword)){
						this.hasWords.add(aword);
						//System.out.print(aword+"  ");//
						Map<String, Double> map = this.atPost.atThreadf.tfidf;//Here computing tf based on threads
						if (map.containsKey(aword)) {
							double count = map.get(aword);
							map.put(aword, count + 1);
						} else {
							map.put(aword, (double) 1);
						}
					}
				}else{
					wordSequence[i] = "";
					posTags[i] = "";
				}
			}
			//System.out.println();//
			//System.out.println(baseSentence);//
		}else{
			this.wordSequence = new String[0];
			this.posTags = new String[0];
		}
	}
	
	ParsedSentence(String sentence){//only used in topical sentence selection and title parsing
		
		this.baseSentence=sentence.toLowerCase();
		this.hasWords=new HashSet<String>();
		if(baseSentence.length()>0){
			String tagged = MainClass.posTagger.tagString(baseSentence);
			String[] sections = tagged.split(" ");
			this.wordSequence = new String[sections.length];
			this.posTags = new String[sections.length];
			for(int i=0;i<sections.length;i++){
				String[] temp = sections[i].split("/");
				if((sections[i].length()>4)&&(temp.length==2)){
					wordSequence[i]=temp[0];
					posTags[i]=temp[1];
					String aword = null;
					if(posTags[i].startsWith("NNS")){
						aword = PlrSgl.INSTANCE.singularize(wordSequence[i]);
						this.baseSentence = baseSentence.replaceAll("\\b"+wordSequence[i]+"\\b", aword);
						wordSequence[i] = aword;
					}else{
						aword = wordSequence[i];
					}
					if(!Stopwords.is(aword)){
						this.hasWords.add(aword);
					}
				}else{
					wordSequence[i]="";
					posTags[i]="";
				}
			}
		}else{
			this.wordSequence = new String[0];
			this.posTags = new String[0];
		}
	}
	
	static String preProcessing(String orisenten){
		
		String[] tobeParsed = orisenten.split("\\s+");
		tobeParsed[tobeParsed.length-1] = tobeParsed[tobeParsed.length-1].replaceAll("\\.|\\?|:|!", "");//at least 6 characters long
		String finallyParsed = "";//filter out non-sentences
		Integer[] lable = new Integer[tobeParsed.length];//0: word, 1: non-word, 2: if neighboring then non-word
		Pattern nonchar = Pattern.compile("[^a-zA-Z]");
		
		for(int i=0; i<tobeParsed.length; i++){
			String a = tobeParsed[i];
			int noncharlength = 0;
			Matcher mch = nonchar.matcher(a);
			while(mch.find()){
				noncharlength += 1;
			}
			double noncharportion = (a.length()>0)? (double)noncharlength/a.length(): 0;
			if((a.length()>16)||(noncharportion>0.5)){
				lable[i]=1;
			}else{
				if(a.matches(".*[^a-z,].*")){//too rigorous, e.g. "using Fortran language," = "using" !!
					lable[i]=2;
				}else{
					lable[i]=0;
				}
			}
		}
		if(tobeParsed.length > 2){
			for(int j=0; j<tobeParsed.length; j++){
				if(lable[j]==0){
					finallyParsed+=tobeParsed[j]+" ";
				}
				if(lable[j]==2){
					if(j==0){
						if(lable[1]==0){
							finallyParsed+=tobeParsed[j]+" ";
						}
					}else{
						if(j==(tobeParsed.length-1)){
							if(lable[j-1]==0){
								finallyParsed+=tobeParsed[j]+" ";
							}
						}else{
							if((lable[j-1]==0)&&(lable[j+1]==0)){
								finallyParsed+=tobeParsed[j]+" ";
							}
						}
					}
				}
			}
		}
		if(finallyParsed.trim().length()>0){
			Pattern conticomma = Pattern.compile("(\\S{0,6},\\s*){3,}");
			Matcher mch = conticomma.matcher(finallyParsed);
			while(mch.find()){
				finallyParsed=finallyParsed.replace(mch.group(), "");
			}
		}
		return finallyParsed.trim();
	}
	
	void parseSentence(){
		
		boolean tooLong = false;
		boolean tooShort = false;

		int length = this.baseSentence.split("\\s+").length;
		if(length < 2){
			tooShort=true;
		}
		if(length > 45){
			tooLong=true;
		}
		if(!tooLong && !tooShort && !(this.role==Role.PLAIN) && !(this.role==Role.UNKNOWN)){
			System.out.println(">>>>Now parsing:>>>>>>"+baseSentence);//
			TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
			List<CoreLabel> rawWords2 = tokenizerFactory.getTokenizer(new StringReader(baseSentence)).tokenize();
			Tree parse = MainClass.stanfordParser.apply(rawWords2);
			TreebankLanguagePack tlp = new PennTreebankLanguagePack();
			GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
			this.typedDependencies = gs.typedDependenciesCCprocessed();
		}
	}	
	
	double cosSimWith(ParsedSentence ps){//tf is based on threads
		if(this.hasWords.size()*ps.hasWords.size()>0){
			Map<String, Double> map1 = this.atPost.atThreadf.tfidf;
			Map<String, Double> map2 = ps.atPost.atThreadf.tfidf;
			double sum1 = 0;
			double sum2 = 0;
			double sumsim = 0;
			for(String w:this.hasWords){	
				double x = map1.get(w);
				sum1+=x*x;
				if(ps.hasWords.contains(w)){
					sumsim+=x*map2.get(w);
				}
			}
			for(String r:ps.hasWords){
				sum2+=map2.get(r)*map2.get(r);
			}
			double rst = sumsim/(Math.sqrt(sum1)*Math.sqrt(sum2));
			return rst>0? rst : 0;
		}else{
			return 0;
		}
	}
	
	double sentenceSim(ParsedSentence ps){//JCn similarity by (Jiang and Conrath, 1997) between two words.
		
		double rst = 0;//should always compare the short sentence to the longer, in order to maintain the same sim value for a pair!
		if(this.hasWords.size()*ps.hasWords.size()>0){
			System.out.println("~~Comparing sentences~~");//
			for(String tw:this.hasWords){
				double score = 0;
				for(String pw:ps.hasWords){
					try{
						//System.out.print(tw+" 's sim with "+pw+" is: ");//
						double d = MainClass.simJCn.getSimilarity(tw,pw).getSimilarity();//synset vs pos
						if(d>1){//same words's similarity can go to E9+
							d=1;
						}
						//System.out.println(d);//
						if(d>score){
							score=d;
						}
					}catch(Exception e){
						System.out.println("  word out of the dictionary passed.");
					}
				}
				rst+=score;
			}
		}
		return rst;
	}
	
	String sentenceInfo(){
		
		String fout = "Sentence ID = "+this.id+"\n"+"Role: "+this.role.toString()+"\n"+"For question: "+this.qids+"\n"+this.baseSentence;
		if(this.typedDependencies!=null){
			for(TypedDependency td : typedDependencies){
				fout += "\n" + td.toString(true);
			}
		}
		return fout+"\n";
		
	}
	
}

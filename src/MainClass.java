import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.TypedDependency;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.dictionary.*;
import shef.nlp.wordnet.similarity.SimilarityMeasure;

public class MainClass {
	
	public static String learningPath = "D:/Research/QCA/tagged380";
	public static String pathFrom = "D:/paseudoContext/CAEQuality/All/www.xansys.org";
	public static String pathTo = "D:/paseudoContext";
	public static String topic = "contact";
	public static MaxentTagger posTagger;
	public static LexicalizedParser stanfordParser;
	public static SimilarityMeasure simJCn;
	public static Map<Context, Map<Context, Double>> recommendations;
	
	static Map<File, Map<String, Double>> fileTFIDF;
	static Map<String, Integer> fileDF;
	
	public static void main(String[] args) {
		
		try{
			
			//readTransProb("D:/RESEARCH/PythonTM/giza-pp/GIZA++-v2/Results");
			//posTagger = new MaxentTagger("left3words-wsj-0-18.tagger");
			//selectedCoTable(pathFrom, pathTo, new String[]{"load","stress","material","report","volume","pressure"});
			//System.out.println(posTagger.tagString("I am having a browsed file which is measured."));//
			//System.out.println(posTagger.tagString("I am having a openning file which is running."));//
			//RBRFinder("D:/paseudoContext/RBRFinder");//
			//QcaRecord.resumeDataBase(pathTo);
			ObjectInputStream objr = null;
			objr = new ObjectInputStream(new FileInputStream("D:/paseudoContext/SlectMuInTable.sav"));//("D:/RESEARCH/PythonTM/giza-pp/GIZA++-v2/Results/TransTable.sav"));
			Threadf.coTable = (Map<String,Map<String, Double>>)objr.readObject();//Use this translation coTable to replace the original mutual info
			objr.close();
			//showWord();
			//TransBasedStore("D:/paseudoContext/CAEQuality/All/www.xansys.org");
			//TransBasedRecommd("D:/paseudoContext/CAEQuality/All/www.xansys.org");
			//
			showWordPair("D:/RESEARCH/PythonTM/giza-pp/GIZA++-v2/Results/HighAssocTRAN.txt");
			/*
			//
			stanfordParser = LexicalizedParser.loadModel("englishPCFG.ser.gz");
			//
			Scanner scanner = new Scanner(System.in);
			while(true){
				String sentence = scanner.nextLine();
				ParsedSentence ps = new ParsedSentence(sentence);
				ps.parseSentence();
				for(TypedDependency td:ps.typedDependencies){
					System.out.println(td);
				}
			}
			//
			///
			JWNL.initialize(new FileInputStream("file_properties.xml")); 
			//Dictionary dic = Dictionary.getInstance();
			//dic.lookupAllIndexWords("steel");
			Map<String,String> params = new HashMap<String,String>();
			params.put("simType","shef.nlp.wordnet.similarity.JCn");
			params.put("infocontent","file:ic-bnc-resnik-add1.dat");
			params.put("mapping","file:domain_independent.txt");
			simJCn = SimilarityMeasure.newInstance(params);
			/*
			Scanner scanner = new Scanner(System.in);
			while(true){
				String wd1 = scanner.nextLine();
				String wd2 = scanner.nextLine();
				if(simJCn.getSimilarity(wd1, wd2).getSynset1().containsWord(wd2))
					System.out.println("they are synonyms");
				System.out.println(simJCn.getSimilarity(wd1, wd2).getSynset1().toString());
				System.out.println(simJCn.getSimilarity(wd1, wd2).getSynset2().toString());
				double s1 = simJCn.getSimilarity(wd1, wd2).getSimilarity();
				System.out.println("sim is: " + s1);
			}
			*/
			//BenceMark0(true,true);
			//Map<String, String> fileInfo = new LinkedHashMap<String, String>();
			//fileSelection(pathFrom, fileInfo);
			//paseudoContext(fileInfo, pathTo);
			//recordsGen(pathFrom,pathTo);
			//QcaRecord.resumeProject(pathTo);
			//QcaRecord.formDatabase(pathTo);
			//QcaRecord.resumeDataBase(pathTo);//            recommendation used  !!
			//posTagger = new MaxentTagger("left3words-wsj-0-18.tagger");
			//parallelQA("D:/paseudoContext/CAEQuality/All/www.xansys.org", pathTo);
			/*
			String pathSVO = "";
			int count = 0;
			for(Context ctx : QcaRecord.contextDB){
				if(!ctx.SVO[1].equals("-")&&!ctx.SVO[2].equals("-")){
					pathSVO += ctx.threadPath + "\t" + ctx.SVO[1] + " " + ctx.SVO[2] +"\r\n";
					count ++;
				}
			}
			writeFile("D:/paseudoContext/File-SVO.txt", pathSVO);
			System.out.println("Total "+count+" threads.");
			//Cluster.resumeRecmds(pathTo);
			//QcaRecord.reformDB(pathTo);
			//QcaRecord.showDataBase();
			//QcaRecord.showWord();
			//BenceMark0(true, false);
			*/
			//while(true){
			//	Recommend();
			//}
			///
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	static void RBRFinder(String pathfrom)throws Exception{
		List<File> todo = new ArrayList<File>();
		fileSelection(pathfrom, todo);
		int count = 0;
		for(File file: todo){
			Threadf athread = new Threadf(file, count);
			count++;
		}
	}
	
	static void TransBasedStore(String pathfrom)throws Exception{
		List<File> todo = new ArrayList<File>();
		fileSelection(pathfrom, todo);
		Map<File, Map<String, Integer>> qesFreq = new HashMap<File, Map<String, Integer>>();//for P(w|q)
		Map<File, Map<String, Integer>> ansFreq = new HashMap<File, Map<String, Integer>>();//for P(w|a)
		Map<String, Integer> total = new HashMap<String, Integer>();//for P(w|C)
		int count = 0;
		int All = 0;
		for(File file: todo){
			Threadf athread = new Threadf(file, count);
			count++;
			if(athread.hasPosts.size()>1){
				Map<String, Integer> QF = new HashMap<String, Integer>();
				Map<String, Integer> AF = new HashMap<String, Integer>();
				int qAll = 0;
				int aAll = 0;
				for(ParsedSentence qps : athread.hasPosts.get(0).hasSentences){
					qAll += qps.hasWords.size();
					All += qps.hasWords.size();
					for(String qwd : qps.hasWords){
						if(QF.containsKey(qwd)){
							QF.put(qwd, QF.get(qwd)+1);
						}else{
							QF.put(qwd, 1);
						}
						if(total.containsKey(qwd)){
							total.put(qwd, total.get(qwd)+1);
						}else{
							total.put(qwd, 1);
						}
					}
				}
				QF.put("--", qAll);//
				for(int i=1; i<athread.hasPosts.size(); i++){
					for(ParsedSentence aps : athread.hasPosts.get(i).hasSentences){
						aAll += aps.hasWords.size();
						All += aps.hasWords.size();
						for(String awd : aps.hasWords){
							if(AF.containsKey(awd)){
								AF.put(awd, AF.get(awd)+1);
							}else{
								AF.put(awd, 1);
							}
							if(total.containsKey(awd)){
								total.put(awd, total.get(awd)+1);
							}else{
								total.put(awd, 1);
							}
						}
					}
				}
				AF.put("--", aAll);//
				qesFreq.put(file, QF);
				ansFreq.put(file, AF);
			}
		}
		total.put("--", All);
		ObjectOutputStream objw = null;
		objw = new ObjectOutputStream(new FileOutputStream(pathfrom+"/TransProbInputFreq.sav"));
		objw.writeObject(qesFreq);
		objw.writeObject(ansFreq);
		objw.writeObject(total);
		objw.close();
	}
	
	static void TransBasedRecommd(String pathfrom)throws Exception{//Query likelyhood and translation based QA retrieval
		ObjectInputStream objr = null;
		objr = new ObjectInputStream(new FileInputStream(pathfrom+"/TransProbInputFreq.sav"));
		Map<File, Map<String, Integer>> qesFreq = (Map<File, Map<String, Integer>>)objr.readObject();
		Map<File, Map<String, Integer>> ansFreq = (Map<File, Map<String, Integer>>)objr.readObject();
		Map<String, Integer> total = (Map<String, Integer>)objr.readObject();
		int All = total.get("--");
		while(true){
			int inputNumber = 0;
			Scanner scanner=new Scanner(System.in);
			boolean correctInput=false;
			while(!correctInput){
				System.out.println("Input the input file number: ");
				String inputNumberS = scanner.nextLine();
				try{
					inputNumber=Integer.parseInt(inputNumberS);
					correctInput=true;
				}catch (NumberFormatException e){
					System.out.println("Input an Integer!");
				}
			}
			String sensedCtx = readFile(pathTo+"/Input"+inputNumber+".txt").replace("-", " ");
			String[] sentences = sensedCtx.split("(?<=\\.|\\?|:|!)(?=\\s|\"|[A-Z])");
			Set<String> query = new HashSet<String>();
			for(String snt : sentences){
				ParsedSentence ps = new ParsedSentence(snt);
				query.addAll(ps.hasWords);
			}
			Map<File, Double> results = new HashMap<File, Double>();
			/*
			//Iterator<File> itr = qesFreq.keySet().iterator();
			//File fl = itr.next();
			File fl = null;
			for(File f : qesFreq.keySet()){
				if(f.getAbsolutePath().contains("t=20245.xml")){
					fl = f;
					System.out.println(" --> "+fl.getAbsolutePath());//
				}
			}
			*/
			for(File fl : qesFreq.keySet()){
				double score = 1.0;
				Map<String, Integer> QF = qesFreq.get(fl);
				Map<String, Integer> AF = ansFreq.get(fl);
				int qn = QF.get("--");//may be 0
				int an = AF.get("--");//may be 0
				for(String w : query){
					if(total.containsKey(w)){
						double pmlwq = QF.keySet().contains(w)? (double)QF.get(w)/qn : 0;
						double pmlwa = AF.keySet().contains(w)? (double)AF.get(w)/an : 0;
						double pmlwc = (double)total.get(w)/All;
						double ptrwq = 0;
						for(String t : QF.keySet()){//may be containing only "--"
							if(!t.equals("--")){
								double ptrwt = 0;
								if(Threadf.coTable.containsKey(t)){
									ptrwt = Threadf.coTable.get(t).containsKey(w)? Threadf.coTable.get(t).get(w) : 0;
								}
								double pmltq = (double)QF.get(t)/qn;// when only "--" exist, lead to 0/0
								ptrwq += ptrwt*pmltq;
							}
						}
						double pmxwqa = 0.4*pmlwq + 0.5*ptrwq + 0.1*pmlwa;
						double pwqa = (double)(qn+an)/(qn+an+800)*pmxwqa + (double)800/(qn+an+800)*pmlwc;// set proper lambd
						//System.out.println(w+" : "+pwqa+" "+pmxwqa+" "+pmlwc);//
						//System.out.println("-- : "+pmlwq+" "+ptrwq+" "+pmlwa);//
						score = score*pwqa;
					}
				}
				results.put(fl, score);
			}//
			List<Map.Entry<File, Double>> map2list = new ArrayList<Map.Entry<File, Double>>(results.entrySet());
			Collections.sort(map2list, new Comparator<Map.Entry<File, Double>>(){
				public int compare(Map.Entry<File, Double> o1, Map.Entry<File, Double> o2){
					return (-1) * o1.getValue().compareTo(o2.getValue());
				}
			});
			for(int i=0; (i<30)&&(i<map2list.size()); i++){
				File t = map2list.get(i).getKey();
				System.out.println(Double.toString(map2list.get(i).getValue())+"  :  "+t.getAbsolutePath());
			}
		}
	}
	
	static String readLinuxFile(String path) throws Exception{
		File inputfile = new File(path);
		FileInputStream inStream = new FileInputStream(inputfile);
	    DataInputStream inDataStream = new DataInputStream(inStream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(inDataStream));
	    char[] fchar = new char[102500000];
	    int length = br.read(fchar);
	    br.close();
		inDataStream.close();
		inStream.close();
		String readed = new String(fchar,0,length-1);
		//System.out.println(readed);//
	    return readed;
	}
	
	static void readTransProb(String pathfrom) throws Exception{
		String[] sourceVcb = readLinuxFile(pathfrom+"/Source.vcb").split("\n");
		System.out.println("Spliting source file finished, total lines: "+sourceVcb.length);//
		String[] targetVcb = readLinuxFile(pathfrom+"/Target.vcb").split("\n");
		System.out.println("Spliting target file finished, total lines: "+targetVcb.length);//
		String[] transProb = readLinuxFile(pathfrom+"/Source_Target.t1.5").split("\n");
		System.out.println("Spliting translation file finished, total lines: "+transProb.length);//
		//
		Map<Integer, String> sourceTable = new HashMap<Integer, String>();
		Map<Integer, String> targetTable = new HashMap<Integer, String>();
		Map<String, Map<String, Double>> transTable = new HashMap<String, Map<String, Double>>();
		for(String entry : sourceVcb){
			String[] pair = entry.trim().split(" ");
			sourceTable.put(Integer.parseInt(pair[0]), pair[1]);
		}
		for(String entry : targetVcb){
			String[] pair = entry.trim().split(" ");
			targetTable.put(Integer.parseInt(pair[0]), pair[1]);
		}
		int count = 0;
		for(String entry : transProb){
			String[] triple = entry.trim().split(" ");
			String sourceword = sourceTable.get(Integer.parseInt(triple[0]));
			if(transTable.containsKey(sourceword)){
				Map<String, Double> vct = transTable.get(sourceword);
				vct.put(targetTable.get(Integer.parseInt(triple[1])), Double.parseDouble(triple[2]));
			}else{
				Map<String, Double> vct = new HashMap<String, Double>();
				vct.put(targetTable.get(Integer.parseInt(triple[1])), Double.parseDouble(triple[2]));
				transTable.put(sourceword, vct);
			}
			count++;
			System.out.println(count+" of "+transProb.length);//
		}
		Threadf.coTable = transTable;
		ObjectOutputStream objw = null;
		objw = new ObjectOutputStream(new FileOutputStream(pathfrom+"/TransTable.sav"));
		objw.writeObject(Threadf.coTable);
		objw.close();
		//
	}
	
	static void showWord(){//for word pairs, call QcaRecord.showWordPair()
		while(true){
			System.out.println("Input a word: ");//
			Scanner scanner=new Scanner(System.in);
			String input = scanner.nextLine();
			if(Threadf.coTable.containsKey(input)){
				Map<String, Double> vct = Threadf.coTable.get(input);
				vct = Context.SortSMap(vct, 20, 0.0);
				for(String tr : vct.keySet()){
					System.out.println(input+" "+tr+" "+vct.get(tr));
				}
			}
		}
	}
	
	static void showWordPair(String wd1, String wd2)throws Exception{
		if(wd1.length()*wd2.length()!=0){
			int df1 = Threadf.df.containsKey(wd1)? Threadf.df.get(wd1):0;
			int df2 = Threadf.df.containsKey(wd2)? Threadf.df.get(wd2):0;
			double co = 0;
			if(Threadf.coTable.containsKey(wd1)){
				co = Threadf.coTable.get(wd1).containsKey(wd2)? Threadf.coTable.get(wd1).get(wd2):0;
			}
			System.out.println(wd1+" "+String.valueOf(df1)+" "+wd2+" "+String.valueOf(df2)+" : "+String.valueOf(co));//
		}
	}
	
	static void showWordPair(String filepath)throws Exception{
		String[] entries = readFile(filepath).split("\r\n");
		for(String entry : entries){
			String[] keyval = entry.split(" ");
			if(keyval.length==3){
				showWordPair(keyval[0],keyval[1]);
			}
		}
	}
	
	static void parallelQA(String pathfrom, String pathto) throws Exception{// 为每个提问首贴选取小写、单数、df大于1的名词，与每个回帖分别组成平行文本
		
		QcaRecord.resumeDataBase(pathto);//require only df
		
		FileOutputStream fout = new FileOutputStream(pathto+"/Source.txt");
		PrintStream prtS = new PrintStream(fout);
		FileOutputStream fout1 = new FileOutputStream(pathto+"/Target.txt");
		PrintStream prtT = new PrintStream(fout1);
		List<File> todo = new ArrayList<File>();
		fileSelection(pathfrom, todo);
		int count = 0;
		for(File file: todo){
			Threadf athread = new Threadf(file, count);
			if(athread.hasPosts.size()>1){
				String ques = "";
				int lt = 0;
				Post question = athread.hasPosts.get(0);
				for(ParsedSentence par:question.hasSentences){
					for(int i=0;i<par.wordSequence.length;i++){
						String word = par.wordSequence[i];
						if(Threadf.df.containsKey(word)){//df contains non-stopwords
							int times = Threadf.df.get(word);
							if((times>5)&&(lt<36)){//5
								ques += word+" ";
								lt++;
							}
						}///
					}
				}
				for(int j=1; j<athread.hasPosts.size();j++){
					String rply = "";
					int lmt = 0;
					Post pst = athread.hasPosts.get(j);
					for(ParsedSentence par:pst.hasSentences){
						for(int i=0;i<par.wordSequence.length;i++){
							String word = par.wordSequence[i];
							if(Threadf.df.containsKey(word)){
								int times = Threadf.df.get(word);
								if((times>5)&&(lmt<36)){//5
									rply += word+" ";
									lmt++;
								}
							}
						}
					}
					if((ques.trim().length()>1)&&(rply.trim().length()>1)){
						prtS.print(ques+"\n");
						prtT.print(rply+"\n");
						prtS.print(rply+"\n");
						prtT.print(ques+"\n");
					}
				}
			}
			count+=1;
		}
	}
	
	static void Recommend() throws Exception{
		recommendations = new LinkedHashMap<Context, Map<Context, Double>>();
		//
		int inputNumber = 0;
		double simThreshold = 10;
		//
		Scanner scanner=new Scanner(System.in);
		boolean correctInput=false;
		while(!correctInput){
			System.out.println("Input the input file number: ");
			String inputNumberS = scanner.nextLine();
			try{
				inputNumber=Integer.parseInt(inputNumberS);
				correctInput=true;
			}catch (NumberFormatException e){
				System.out.println("Input an Integer!");
			}
		}
		correctInput=false;
		while(!correctInput){
			System.out.println("Input the lower bound of similarity for determining a candidate context: ");
			String titlesimThresholdS = scanner.nextLine();
			try{
				simThreshold=Double.parseDouble(titlesimThresholdS);
				correctInput=true;
			}catch (NumberFormatException e){
				System.out.println("Input an Double!");
			}
		}
		//
		Context sensedCtx = new Context(readFile(pathTo+"/Input"+inputNumber+".txt"));
		QcaRecord.reselectNPs(sensedCtx);
		/*//for knowledge map construction
		Map<Context, Double> reclist = Cluster.simpleMatchWith(sensedCtx, simThreshold);
		Cluster.linkingCtxs(reclist);
		Iterator<Context> itr = reclist.keySet().iterator();
		Cluster.viewOn(itr.next(), reclist);
		while(true){
			System.out.println("Input the context file path: ");
			String queryPath = scanner.nextLine();
			if(queryPath.trim().length()!=0){
				for(Context ct: Cluster.ctxChains.keySet()){
					if(ct.threadPath.contains(queryPath)){
						Cluster.viewOn(ct, reclist);
						break;
					}
				}
			}
			System.out.println("Quit querying? Y/N");
			String ans = scanner.nextLine();
			if(ans.trim().equalsIgnoreCase("y")){
				break;
			}
		}
		Cluster.storeRecmds(pathTo);
		*/
		recommendations.put(sensedCtx, Cluster.simpleMatchWith(sensedCtx, simThreshold));
		Cluster.showOutput(recommendations, 5, 40, 0);
	}
	
	static void BenceMark0(boolean use1stPost, boolean useRlvfbk) throws Exception{//tf-idf and cosine similarity based file recommendation
		List<File> filelist = new ArrayList<File>();
		fileSelection("D:/paseudoContext/CpltThrd", filelist);
		fileTFIDF = new HashMap<File, Map<String, Double>>();
		fileDF = new HashMap<String, Integer>(); 
		SAXReader reader = new SAXReader();
		for(File file : filelist){
			String threadContent = "";
			Map<String, Double> tf = new HashMap<String, Double>();
			try{
				Document document = reader.read(file);
		    	threadContent = document.getRootElement().attributeValue("title").trim()+" ";
			    List postlist = document.selectNodes("//post");
			    if(use1stPost){
			    	Element post = (Element)postlist.get(0);
		    		threadContent += post.getTextTrim()+" ";
			    }else{
			    	for(int i=0; i<postlist.size(); i++){
			    		Element post = (Element)postlist.get(i);
			    		threadContent += post.getTextTrim()+" ";
				    }
			    }
			}catch (Exception e){//on xmiError
				threadContent = readFile(file.getAbsolutePath());
				if(use1stPost){
					threadContent = threadContent.substring(threadContent.indexOf("<post>")+6, threadContent.indexOf("</post>"));
				}
			}
			String used = threadContent.toLowerCase().replaceAll("[^a-z0-9]{3,}", "");
		    Pattern wp = Pattern.compile("\\b\\w*[a-z]+\\w*\\b");
			Matcher wm = wp.matcher(used);
			while(wm.find()){
				String aword = wm.group();
				if(!Stopwords.is(aword)){
					if (tf.containsKey(aword)) {
						double count = tf.get(aword);
						tf.put(aword, count + 1);
					} else {
						tf.put(aword, (double) 1);
					}
				}
			}
			fileTFIDF.put(file, tf);
		}
		for(File fl: fileTFIDF.keySet()){
			for(String wd: fileTFIDF.get(fl).keySet()){
				if (fileDF.containsKey(wd)) {
					int count = fileDF.get(wd);
					fileDF.put(wd, count + 1);
				} else {
					fileDF.put(wd, 1);
				}
			}
		}
		for(File fl: fileTFIDF.keySet()){
			Map<String, Double> tfInt = fileTFIDF.get(fl);
			for(String wd: tfInt.keySet()){
				double tf = tfInt.get(wd);
				double idf = (double)fileTFIDF.size()/fileDF.get(wd);
				double tfidf = tf * Math.log(idf);
				tfInt.put(wd, tfidf);
			}
		}
		while(true){//read input and compare
			int inputNumber = 0;
			int howmany = 20;//800 to construct Kmap
			//
			Scanner scanner=new Scanner(System.in);
			boolean correctInput=false;
			while(!correctInput){
				System.out.println("Input the input file number: ");
				String inputNumberS = scanner.nextLine();
				try{
					inputNumber=Integer.parseInt(inputNumberS);
					correctInput=true;
				}catch (NumberFormatException e){
					System.out.println("Input an Integer!");
				}
			}
			correctInput=false;
			while(!correctInput){
				System.out.println("How many results to be output: ");
				String howmanyS = scanner.nextLine();
				try{
					howmany=Integer.parseInt(howmanyS);
					correctInput=true;
				}catch (NumberFormatException e){
					System.out.println("Input an Integer!");
				}
			}
			String str = readFile(pathTo+"/Input"+inputNumber+".txt");//input task description pre-processing
			Map<String, Double> weightedWords = new HashMap<String, Double>();
			String used = str.toLowerCase().replaceAll("[^a-z0-9]{3,}", "");
		    Pattern wp = Pattern.compile("\\b\\w*[a-z]+\\w*\\b");
			Matcher wm = wp.matcher(used);
			while(wm.find()){
				String aword = wm.group();
				if(!weightedWords.keySet().contains(aword)){
					if(fileDF.keySet().contains(aword)){//keySet does not contain stop words
						double idf = (double)fileTFIDF.size()/fileDF.get(aword);
						double tfidf = Math.log(idf);
						weightedWords.put(aword, tfidf);
					}
				}else{
					double idf = (double)fileTFIDF.size()/fileDF.get(aword);
					double tfidf = Math.log(idf);
					double added = weightedWords.get(aword) + tfidf;
					weightedWords.put(aword, added);
				}
			}
			Map<File, Double> simofFiles = new HashMap<File, Double>();//cosine similarity with every file
			for(File file : fileTFIDF.keySet()){
				double rst = 0;
				double sum1 = 0;
				double sum2 = 0;
				double sumsim = 0;
				Map<String, Double> tfidf = fileTFIDF.get(file);
				for(String w: weightedWords.keySet()){	
					double x = weightedWords.get(w);
					sum1 += x * x;
					if(tfidf.keySet().contains(w)){
						sumsim += x * tfidf.get(w);
					}
				}
				for(String r:tfidf.keySet()){
					double y = tfidf.get(r);
					sum2 += y * y;
				}
				rst = (sum1*sum2>0)? sumsim/(Math.sqrt(sum1)*Math.sqrt(sum2)): 0;
				simofFiles.put(file, rst);
			}
			List<Map.Entry<File, Double>> map2list = new ArrayList<Map.Entry<File, Double>>(simofFiles.entrySet());
			Collections.sort(map2list, new Comparator<Map.Entry<File, Double>>(){
				public int compare(Map.Entry<File, Double> o1, Map.Entry<File, Double> o2){
					return (-1) * o1.getValue().compareTo(o2.getValue());
				}
			});
			//Relevance feedback
			if(useRlvfbk){
				int topkf = 10;
				int topkt = 3;
				for(int i=0; (i<topkf)&&(i<map2list.size()); i++){
					File topf = map2list.get(i).getKey();
					Map<String, Double> temp = Context.SortSMap(fileTFIDF.get(topf), topkt, 0);
					for(String topt : temp.keySet()){
						if(weightedWords.containsKey(topt)&&(weightedWords.get(topt)>temp.get(topt))){
							
						}else{
							weightedWords.put(topt, temp.get(topt));
						}
					}
				}
				Map<File, Double> simofFiles2 = new HashMap<File, Double>();//cosine similarity with every file
				for(File file : fileTFIDF.keySet()){
					double rst = 0;
					double sum1 = 0;
					double sum2 = 0;
					double sumsim = 0;
					Map<String, Double> tfidf = fileTFIDF.get(file);
					for(String w: weightedWords.keySet()){	
						double x = weightedWords.get(w);
						sum1 += x * x;
						if(tfidf.keySet().contains(w)){
							sumsim += x * tfidf.get(w);
						}
					}
					for(String r:tfidf.keySet()){
						double y = tfidf.get(r);
						sum2 += y * y;
					}
					rst = (sum1*sum2>0)? sumsim/(Math.sqrt(sum1)*Math.sqrt(sum2)): 0;
					simofFiles2.put(file, rst);
				}
				map2list = new ArrayList<Map.Entry<File, Double>>(simofFiles2.entrySet());
				Collections.sort(map2list, new Comparator<Map.Entry<File, Double>>(){
					public int compare(Map.Entry<File, Double> o1, Map.Entry<File, Double> o2){
						return (-1) * o1.getValue().compareTo(o2.getValue());
					}
				});
			}
			/*
			for(int i=0; (i<howmany)&&(i<map2list.size()); i++){
				File t = map2list.get(i).getKey();
				System.out.println(Double.toString(map2list.get(i).getValue())+"  :  "+t.getAbsolutePath());
			}
			*/
			Map<Context, Double> reclist = new LinkedHashMap<Context, Double>();
			for(int i=0; (i<howmany)&&(i<map2list.size()); i++){
				File t = map2list.get(i).getKey();
				String relePath = t.getAbsolutePath().substring(t.getAbsolutePath().indexOf("www"));
				for(Context ct: QcaRecord.contextDB){
					if(ct.threadPath.contains(relePath)){
						reclist.put(ct, map2list.get(i).getValue());
						break;
					}
				}
			}
			Cluster.linkingCtxs(reclist);
			Iterator<Context> itr = reclist.keySet().iterator();
			Cluster.viewOn(itr.next(), reclist);
			while(true){
				System.out.println("Input the context file path: ");
				String queryPath = scanner.nextLine();
				if(queryPath.trim().length()!=0){
					for(Context ct: Cluster.ctxChains.keySet()){
						if(ct.threadPath.contains(queryPath)){
							Cluster.viewOn(ct, reclist);
							break;
						}
					}
				}
				System.out.println("Quit querying? Y/N");
				String ans = scanner.nextLine();
				if(ans.trim().equalsIgnoreCase("y")){
					break;
				}
			}
		}
	}
	
	static String readFile(String path) throws Exception{
		File inputfile = new File(path);
		FileReader fin = new FileReader(inputfile);
		BufferedReader br = new BufferedReader(fin);
		String content ="";
		String templine = null;
		while((templine = br.readLine()) != null){
			content += templine + "\r\n";
		}
		br.close();
		fin.close();
		content.replaceAll("<.*>", "");
		return content;
	}
	
	static void writeFile(String pathto, String content) throws Exception {
		File fl = new File(pathto);
		File dir = fl.getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		FileWriter fout = new FileWriter(fl);
		BufferedWriter bwr = new BufferedWriter(fout);
		bwr.write(content);
		bwr.close();
		fout.close();
	}
	
	static void paseudoContext(Map<String, String> titleXname, String pathto) throws Exception{
		
		for(String name : titleXname.values()){
			String content = readFile(name);
			String title = content.substring(content.indexOf("<QCA title=\"")+12, content.indexOf("\">")).trim();
			String firstPost = content.substring(content.indexOf("<post>")+6, content.indexOf("</post>")).trim();
			String[] sentences = firstPost.split("(?<=\\.|\\?|:|!)(?=\\s|\"|[A-Z])");
			double maxsim = 0;
			int kth = 0;//most similar sentence
			ParsedSentence topic = new ParsedSentence(title);
			for (int j=0; j<sentences.length; j++){
				ParsedSentence s = new ParsedSentence(sentences[j]);
				double score = topic.sentenceSim(s);
				System.out.println(String.valueOf(score)+"   "+s.baseSentence);//
				if(score > maxsim){
					maxsim = score;
					kth = j;
				}
				if((score == maxsim)&&(sentences[j].length()<sentences[kth].length())){
					kth = j;
				}
			}
			String completePath = pathto + name.substring(name.indexOf("www")-1);//!!
			String towrite = "";
			towrite += "<QCA title=\""+title+"\"><post>";
			for (int j=0; j<sentences.length; j++){
				if(j==kth){
					towrite += "<question id=\"1\">"+sentences[j]+"</question>";
				}else{
					towrite += "<context id=\"1\">"+sentences[j]+"</context>";
				}
			}
			towrite += "</post>";
			towrite += "<author>Song</author><date>20121116</date>";
			towrite += "</QCA>";
			writeFile(completePath, towrite);
		}
		System.out.println("Paseudo context written: "+String.valueOf(titleXname.size()));
	}
	
	static void fileSelection(String pathfrom, List<File> files) throws Exception{
		File root = new File(pathfrom);
		if(root.isFile()){
			System.out.println("Choose a folder!");
			return;
		}
		File[] filelist = root.listFiles();
		for(File file: filelist){
			if(file.isFile()){
				/*
				String content = readFile(file.getAbsolutePath());
				String question = content.substring(content.indexOf("<question")+17, content.indexOf("</question>")).trim();
				String title = content.substring(content.indexOf("<QCA title=\"")+12, content.indexOf("\">")).trim();
				if(question.contains("I ")||title.contains("load")){
					files.add(file);
				}
				/*
				String order = ParsedSentence.preProcessing(question);
				if(order.split("\\s+").length>36){
					files.add(file);
				}
				*/
				files.add(file);
			}else if(file.isDirectory()){
				fileSelection(file.getAbsolutePath(), files);
			}
		}
	}
	
	static void fileSelection(String pathfrom, Map<String, String> titleAndName) throws Exception{
		File root = new File(pathfrom);
		if(root.isFile()){
			System.out.println("Choose a folder!");
			return;
		}
		File[] filelist = root.listFiles();
		for(File file: filelist){
			if(file.isFile()){
				String abspath = file.getAbsolutePath();
				if((!abspath.contains("&"))&&(file.length()>250)){
					int number = Integer.parseInt(abspath.substring(abspath.indexOf("t=")+2, abspath.indexOf(".xml")));
					String content = readFile(abspath);
					String title = content.substring(content.indexOf("<QCA title=\"")+12, content.indexOf("\">")).trim();
					String prePathOfSameTitle = titleAndName.keySet().contains(title)? titleAndName.get(title): null;
					if((prePathOfSameTitle==null)||(Integer.parseInt(prePathOfSameTitle.substring(prePathOfSameTitle.indexOf("t=")+2, prePathOfSameTitle.indexOf(".xml")))>number)){
						titleAndName.put(title, abspath);
					}
				}
			}else if(file.isDirectory()){
				fileSelection(file.getAbsolutePath(), titleAndName);
			}
		}
	}
	
	static void fileMove(String pathfrom, String pathto) throws Exception{//Choose and copy files whose names are within a certain list.
		List<File> named = new ArrayList<File>();
		fileSelection(pathfrom, named);
		List<File> collection = new ArrayList<File>();
		fileSelection("D:/RESEARCH/WebQA/spider/USING", collection);//
		for(File nf: named){
			for(File cf: collection){
				if(nf.getName().equals(cf.getName())){//nf.getName().substring(2,7).equals(cf.getName().substring(2,7))){
					String abspath = cf.getAbsolutePath();
					writeFile(pathto+abspath.substring(abspath.indexOf("www")-1), readFile(abspath));//
					break;
				}
			}
		}
		System.out.println("done");
	}
	
	public static void featuringCRF(String pathfrom, String pathto) throws Exception{//Reads xml files tagged with QCA roles and generates training samples according to desired feature format.  
		/*
		JWNL.initialize(new FileInputStream("file_properties.xml")); 
		//Create a map to hold the similarity config params
		Map<String,String> params = new HashMap<String,String>();
		//the simType parameter is the class name of the measure to use
		params.put("simType","shef.nlp.wordnet.similarity.JCn");
		//this param should be the URL to an infocontent file (if required
		//by the similarity measure being loaded)
		params.put("infocontent","file:ic-bnc-resnik-add1.dat");//file:test/
		//this param should be the URL to a mapping file if the
		//user needs to make synset mappings
		params.put("mapping","file:domain_independent.txt");//file:test/
		//create the similarity measure
		SimilarityMeasure sim = SimilarityMeasure.newInstance(params);
		*/
		File[] filelist = new File(pathfrom).listFiles();
		for(int i=0; i < filelist.length; i++){
			Threadf athread = new Threadf(filelist[i], i);
		}
		Threadf.computeDf();
		FileOutputStream fout = new FileOutputStream(pathto+"/CRF.txt");
		PrintStream prt = new PrintStream(fout);
		int processedThread = 0;
		for(Threadf thrf : Threadf.allThreadfs){
			thrf.computeTfidf();
			thrf.computeCrfFeatures();//tf is based on threads
			thrf.featurePrint(prt);
			processedThread += 1;
			System.out.println("-------->Rate of progress: "+(processedThread/filelist.length));
		}
	}
	
	public static void recordsGen(String pathfrom, String pathto) throws Exception{//Reads tagged QCA files (maybe of different formats) and generates the QCA Database.
		List<File> todo = new ArrayList<File>();
		fileSelection(pathfrom, todo);
		//System.out.println(String.valueOf(todo.size()));//1133 reformed questions with more than 36 words
		ObjectOutputStream objw =  new ObjectOutputStream(new FileOutputStream(pathto+"/QCA.tmp"));
		int count = 0;
		for(File file: todo){
			Threadf athread = new Threadf(file, count);
			objw.writeObject(athread);
			count+=1;
		}
		objw.writeObject(new HashMap<Integer, Integer>());
		objw.close();
		ObjectInputStream objr = new ObjectInputStream(new FileInputStream(pathto+"/QCA.tmp"));
		while(true){
			Object o = objr.readObject();
			if(o instanceof Threadf){
				Threadf.allThreadfs.add((Threadf)o);
			}
			if(o instanceof Map){
				break;
			}
		}
		objr.close();
		Threadf.computeDf();
		for(Threadf thrf:Threadf.allThreadfs){
			thrf.computeTfidf();
		}
		Threadf.computeCooccur();
		QcaRecord.serializingProject(pathto);
	}
	
	static void selectedCoTable(String pathfrom, String pathto, String[] wordstocompu)throws Exception{//to include words other than nouns, coTable too big

		QcaRecord.resumeDataBase(pathto);//require only df
		
		List<File> todo = new ArrayList<File>();
		fileSelection(pathfrom, todo);
		for(String wslec : wordstocompu){
			Map<String, Double> table= new HashMap<String, Double>();
			Threadf.coTable.put(wslec, table);
		}
		int count = 0;
		for(File file: todo){
			Threadf thrf = new Threadf(file, count);
			Set<String> keyWords = new HashSet<String>();
			for(Post pst:thrf.hasPosts){
				for(ParsedSentence par:pst.hasSentences){
					for(int i=0;i<par.wordSequence.length;i++){
						String word = par.wordSequence[i];
						if(Threadf.df.containsKey(word)){
							int times = Threadf.df.get(word);
							if(times>5){
								keyWords.add(word);
							}
						}
					}
				}
			}
			
			if(keyWords.size()>1){
				for(String wdi:keyWords){
					if (Threadf.coTable.containsKey(wdi)) {
						Map<String, Double> table= Threadf.coTable.get(wdi);
						for(String wdj:keyWords){
							if(!wdi.equals(wdj)){
								if(table.containsKey(wdj)){
									double cooccount = table.get(wdj);
									table.put(wdj, cooccount+1);
								}else {
									table.put(wdj, (double)1);
								}
							}
						}
					}
				}
			}
			count+=1;
		}
		for(String wdi: Threadf.coTable.keySet()){
			int itimes = Threadf.df.get(wdi);
			Map<String, Double> rowi = Threadf.coTable.get(wdi);
			for(String wdj: rowi.keySet()){
				int jtimes = Threadf.df.get(wdj);
				double cotimes = rowi.get(wdj);
				double d = cotimes*todo.size()/(itimes*jtimes);//size of todo equals the file set used to compute df
				double r = Math.log(d)/Math.log(0.5*todo.size());
				if(r>0){
					rowi.put(wdj, r);
				}else{
					rowi.put(wdj, (double)0);
				}
				
			}
		}
		ObjectOutputStream cotbwr = null;
		cotbwr = new ObjectOutputStream(new FileOutputStream(pathto+"/SlectMuInTable.sav"));
		cotbwr.writeObject(Threadf.coTable);
		cotbwr.close();
	}

}

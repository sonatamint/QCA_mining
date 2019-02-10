import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.util.Map;

public class QcaRecord {

	static List<Context> contextDB = new ArrayList<Context>();
	static List<QcaRecord> parsedQCA = new ArrayList<QcaRecord>();
	List<ParsedSentence> contextList;
	List<ParsedSentence> questionList;
	List<ParsedSentence> answerList;
	
	QcaRecord(){
		
		this.contextList=new ArrayList<ParsedSentence>();
		this.questionList=new ArrayList<ParsedSentence>();
		this.answerList=new ArrayList<ParsedSentence>();
		parsedQCA.add(this);
		
	}
	
	static void CreateRecord(ParsedSentence ps){//create a record for a new question
		
		boolean found = false;
		for(QcaRecord aQCA:QcaRecord.parsedQCA){
			ParsedSentence existPs = aQCA.questionList.get(0);
			if(ps.qids.containsAll(existPs.qids) && (ps.atPost.atThreadf==existPs.atPost.atThreadf)){
				aQCA.questionList.add(ps);// May have multiple sentences in a question
				found = true;
				break;
			}
		}
		if(!found){
			QcaRecord newRecord = new QcaRecord();
			newRecord.questionList.add(ps);
		}
	}
	
	static void AddRecord(ParsedSentence ps){
		
		if((ps.role==ParsedSentence.Role.CONTEXT)||(ps.role==ParsedSentence.Role.ANSWER)){
			for(QcaRecord aQCA:QcaRecord.parsedQCA){
				ParsedSentence existPs = aQCA.questionList.get(0);
				if(ps.qids.containsAll(existPs.qids) && ps.atPost.atThreadf==existPs.atPost.atThreadf){
					if(ps.role==ParsedSentence.Role.CONTEXT){
						aQCA.contextList.add(ps);
					}
					if(ps.role==ParsedSentence.Role.ANSWER){
						aQCA.answerList.add(ps);
					}
				}
			}
		}
	}
	
	static void reselectNPs(Context ctx) throws Exception{//maximum 2 word long NPs, with averaged idf score
		Map<String, Double> newNPs = new HashMap<String, Double>();
		for(String aNP: ctx.nounPhrases.keySet()){
			String[] nouns = aNP.split(" ");
			if(nouns.length>1){
				String head = nouns[nouns.length-1];
				String modi = nouns[nouns.length-2];
				if(!Stopwords.is(head)){//Should further filter heads according to global DF!! Better try suffixing co-occurrence!
					if(Threadf.coTable.containsKey(modi)){//then modi is noun and not a stop word
						double dh = (double)contextDB.size()/Threadf.df.get(head);
						double dm = (double)contextDB.size()/Threadf.df.get(modi);
						newNPs.put(modi+" "+head, 0.5*Math.log(dm)+0.5*Math.log(dh));//Geometric means
					}else{
						double dh = (double)contextDB.size()/Threadf.df.get(head);
						newNPs.put(head, Math.log(dh));
						/*leaving adj for next paper (semantic feature comparing)
						String typedAdj = null;
						double dh = (double)contextDB.size()/Threadf.df.get(head);
						if(modi.matches("(big|large|long|thick|multiple).*")){
							typedAdj = "scale-up";
						}
						if(modi.matches("(small|short|thin).*")){
							typedAdj = "scale-dw";
						}
						if(modi.matches("(high|full|major|great|significant|extensive).*")){
							typedAdj = "extnt-up";
						}
						if(modi.matches("(low|minor|weak|little).*")){
							typedAdj = "extnt-dw";
						}
						if(modi.matches("(fast|quick|sudden|abrupt|frequent|rapid).*")){
							typedAdj = "timed-up";
						}
						if(modi.matches("(slow|temporary|lazy).*")){
							typedAdj = "timed-dw";
						}
						if(typedAdj!=null){
							newNPs.put(typedAdj+" "+head, Math.log(dh));
						}else{
							newNPs.put(modi+" "+head, Math.log(dh));
						}
						*/
					}
				}
			}else{
				double da = (double)contextDB.size()/Threadf.df.get(aNP);
				newNPs.put(aNP, Math.log(da));
			}
		}
		ctx.nounPhrases = Context.SortSMap(newNPs, 20, 0);
	}
	
	static void reformDB(String path) throws Exception{
		ObjectOutputStream objw = new ObjectOutputStream(new FileOutputStream(path+"/QCA.db"));
		objw.writeObject(Threadf.df);
		for(Context ct: contextDB){
			reselectNPs(ct);
			objw.writeObject(ct);
		}
		objw.writeObject(Threadf.coTable);
		objw.close();
	}
	
	static void formDatabase(String path) throws Exception{
		ObjectInputStream objr = new ObjectInputStream(new FileInputStream(path+"/QCA.sav"));
		Threadf.df = (Map<String, Integer>)objr.readObject();
		ObjectOutputStream objw = new ObjectOutputStream(new FileOutputStream(path+"/QCA0.db"));//分若干个文件写入对象，防止Stream占用内存过高
		objw.writeObject(Threadf.df);//
		int i = 1;
		while(true){
			Object o = objr.readObject();
			if(o instanceof Threadf){//
				if(i%7000==0){//
					objw.close();
					objw = new ObjectOutputStream(new FileOutputStream(path+"/QCA"+i/7000+".db"));//
				}
				System.out.println("Processing the "+String.valueOf(i)+"th record...");//
				Threadf athread = (Threadf)o;
				for(Post pst:athread.hasPosts){
					for(ParsedSentence par:pst.hasSentences){
						if(par.role==ParsedSentence.Role.QUESTION){
							par.parseSentence();
							QcaRecord.CreateRecord(par);
						}
					}
				}
				for(Post pst:athread.hasPosts){
					for(ParsedSentence par:pst.hasSentences){
						QcaRecord.AddRecord(par);//sentences added to a record according to which question they address
					}
				}
				for(QcaRecord record:parsedQCA){
					objw.writeObject(new Context(record));
				}
				parsedQCA.clear();
				//i++;
			}
			if(o instanceof Map){
				Threadf.coTable=(Map<String, Map<String, Double>>)o;
				objw.writeObject(Threadf.coTable);
				break;
			}
			i++;
		}
		objw.close();
		objr.close();
	}
	
	public static void showDataBase()throws Exception{
		FileOutputStream fout=new FileOutputStream("D:/paseudoContext/DB_readable.txt");
		PrintStream ps=new PrintStream(fout);
		while(true){
			String name = "";
			String path = "";
			Scanner scanner=new Scanner(System.in);
			System.out.println("Input a word that context contains in the title: ");
			name = scanner.nextLine();
			System.out.println("Input a part of the path of context: ");
			path = scanner.nextLine();
			if((name.trim().length()+path.trim().length())!=0){
				for(Context ctx: contextDB){
					String svo = ctx.SVO[0]+" "+ctx.SVO[1]+" "+ctx.SVO[2];
					//if(ctx.threadPath.contains(path)||ctx.threadTitle.toLowerCase().contains(name.toLowerCase())){
					if(svo.contains(name)){
						System.out.print(ctx);
						ps.print(ctx);
					}
				}
			}else{
				ps.close();
				fout.close();
				break;
			}
		}
	}
	
	public static void resumeDataBase(String path)throws Exception{
		System.out.println("Resuming DataBase...");//
		ObjectInputStream objr = null;
		objr = new ObjectInputStream(new FileInputStream(path+"/QCA0.db"));
		Threadf.df = (Map<String, Integer>)objr.readObject();
		/*
		String dfprint = "";
		List<Map.Entry<String, Integer>> map2list = new ArrayList<Map.Entry<String, Integer>>(Threadf.df.entrySet());
		Collections.sort(map2list, new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2){
				return (-1) * o1.getValue().compareTo(o2.getValue());
			}
		});
		for(Map.Entry<String, Integer> entr: map2list){
			dfprint+=entr.getKey()+" "+String.valueOf(entr.getValue())+";  ";
		}
		MainClass.writeFile(path+"/dfs.txt", dfprint);
		
		//Hereafter is disabled when generating parallel QA , is effective when recommending
		int i = 0;
		while(true){
			try{
				Object o = objr.readObject();
				if(o instanceof Context){
					contextDB.add((Context)o);
				}
				if(o instanceof Map){
					Threadf.coTable = (Map<String, Map<String, Double>>)o;
					break;
				}
			}catch (EOFException eo){
				i++;
				objr.close();
				objr = new ObjectInputStream(new FileInputStream(path+"/QCA"+i+".db"));
			}
		}
		objr.close();
		*/
		System.out.println("DataBase resumed.");//
	}
	
	static void serializingProject(String path) throws Exception{
		ObjectOutputStream objw = null;
		objw = new ObjectOutputStream(new FileOutputStream(path+"/QCA.sav"));
		objw.writeObject(Threadf.df);
		for(Threadf thrd:Threadf.allThreadfs){
			objw.writeObject(thrd);
		}
		objw.writeObject(Threadf.coTable);
		objw.close();
	}
	
	static void resumeProject(String path) throws Exception{
		System.out.println("Resuming Project...");//
		ObjectInputStream objr = null;
		objr = new ObjectInputStream(new FileInputStream(path+"/QCA.sav"));
		Threadf.df = (Map<String, Integer>)objr.readObject();
		while(true){
			Object o = objr.readObject();
			if(o instanceof Threadf){
				Threadf.allThreadfs.add((Threadf)o);
			}
			if(o instanceof Map){
				Threadf.coTable=(Map<String, Map<String, Double>>)o;
				break;
			}
		}
		objr.close();
		System.out.println("Project resumed.");//
		/*
		for(Threadf thd : Threadf.allThreadfs){
			if(thd.threadfPath.contains("20269")){
				for(Post pst : thd.hasPosts){
					for(ParsedSentence ps : pst.hasSentences){
						if(ps.role==ParsedSentence.Role.QUESTION){
							System.out.print(ps.sentenceInfo());
						}
					}
					
				}
			}
		}
		*/
	}
}
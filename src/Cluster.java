import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;


public class Cluster implements Serializable {
	
	static Map<Context, Map<Context, Double>> simContexts = new HashMap<Context, Map<Context, Double>>();
	static Map<String, Map<String, Boolean>> wsimTable = new HashMap<String, Map<String, Boolean>>();//cache the words that have been semantically compared
	static Map<Context, Map<Context, SuperNode>> ctxChains = new HashMap<Context, Map<Context, SuperNode>>();
	
	public Cluster() throws Exception{
		
	}
	
	static public void linkingCtxs(Map<Context, Double> rankedCtxs) throws Exception {
		
		for(Context ctx : rankedCtxs.keySet()){
			if(!ctxChains.containsKey(ctx)){
				Map<Context, SuperNode> row = new HashMap<Context, SuperNode>();
				ctxChains.put(ctx, row);
			}
		}
		for(Context ctx : rankedCtxs.keySet()){
			String Xtarg = (ctx.SVO[0].equals("-"))? ctx.SVO[2] : ctx.SVO[0];
			for(Context cty: rankedCtxs.keySet()){
				if((!ctx.equals(cty))&&(!ctxChains.get(ctx).containsKey(cty))){
					String Ytarg = (cty.SVO[0].equals("-"))? cty.SVO[2] : cty.SVO[0];
					if(containPhrase(Ytarg,Xtarg)){//equal&&verb
						double simXY = computeCSim(ctx,cty);
						ctxChains.get(ctx).put(cty, new SuperNode(SuperNode.Label.LOW,simXY));
						ctxChains.get(cty).put(ctx, new SuperNode(SuperNode.Label.UP,simXY));
					}
					if(containPhrase(Xtarg,Ytarg)){//equal&&verb
						double simXY = computeCSim(ctx,cty);
						ctxChains.get(ctx).put(cty, new SuperNode(SuperNode.Label.UP,simXY));
						ctxChains.get(cty).put(ctx, new SuperNode(SuperNode.Label.LOW,simXY));
					}
					for(String np: ctx.nounPhrases.keySet()){
						double simXY = computeCSim(ctx,cty);
						if(containPhrase(np,Ytarg)){//svo
							ctxChains.get(ctx).put(cty, new SuperNode(SuperNode.Label.LEFT,simXY));
							ctxChains.get(cty).put(ctx, new SuperNode(SuperNode.Label.RIGHT,simXY));
							break;
						}
					}
					for(String np: cty.nounPhrases.keySet()){
						double simXY = computeCSim(ctx,cty);
						if(containPhrase(np,Xtarg)){//svo
							ctxChains.get(ctx).put(cty, new SuperNode(SuperNode.Label.RIGHT,simXY));
							ctxChains.get(cty).put(ctx, new SuperNode(SuperNode.Label.LEFT,simXY));
							break;
						}
					}
				}
			}
		}
	}
	
	static void viewOn (Context centre, Map<Context, Double> rankedCtxs) throws Exception {//500 input
		Map<Context, SuperNode> data = ctxChains.get(centre);
		System.out.println("@@@@@@For context: ");
		System.out.print(centre.toString());
		System.out.println("------Following are upper nodes:");
		for(Context ct: data.keySet()){
			if(data.get(ct).label==SuperNode.Label.UP){
				double score = data.get(ct).asWeight * rankedCtxs.get(ct);
				System.out.println(score+" "+ct.SVO[0]+" "+ct.SVO[1]+" "+ct.SVO[2]+"  "+ct.threadPath);
			}
		}
		System.out.println("------Following are preceding nodes:");
		for(Context ct: data.keySet()){
			if(data.get(ct).label==SuperNode.Label.LEFT){
				double score = data.get(ct).asWeight * rankedCtxs.get(ct);
				System.out.print(score+ct.toString());
			}
		}
		System.out.println("------Following are lower nodes:");
		for(Context ct: data.keySet()){
			if(data.get(ct).label==SuperNode.Label.LOW){
				double score = data.get(ct).asWeight * rankedCtxs.get(ct);
				System.out.println(score+" "+ct.SVO[0]+" "+ct.SVO[1]+" "+ct.SVO[2]+"  "+ct.threadPath);
			}
		}
		System.out.println("------Following are succeding nodes:");
		for(Context ct: data.keySet()){
			if(data.get(ct).label==SuperNode.Label.RIGHT){
				double score = data.get(ct).asWeight * rankedCtxs.get(ct);
				System.out.print(score+ct.toString());
			}
		}
	}
	
	static boolean containPhrase(String a, String b) throws Exception {
		String[] bs = b.split(" ");
		for(String t: bs){
			if(!a.contains(t)){
				return false;
			}
		}
		return true;
	}
	
	static void storeRecmds(String path) throws Exception{
		ObjectOutputStream objw = null;
		objw = new ObjectOutputStream(new FileOutputStream(path+"/Rec.sav"));
		objw.writeObject(simContexts);
		objw.writeObject(wsimTable);
		objw.close();
	}
	
	static void resumeRecmds(String path) throws Exception{
		System.out.println("Resuming Last...");//
		ObjectInputStream objr = null;
		objr = new ObjectInputStream(new FileInputStream(path+"/Rec.sav"));
		simContexts = (Map<Context, Map<Context, Double>>)objr.readObject();
		wsimTable = (Map<String, Map<String, Boolean>>)objr.readObject();
		objr.close();
		System.out.println("Resumed Last.");
	}
	
	static Map<Context, Map<Context, Double>> SortClusters(Map<Context, Map<Context, Double>> tobeSorted){//
		List<Map.Entry<Context, Map<Context, Double>>> Listing = new ArrayList<Map.Entry<Context, Map<Context, Double>>>(tobeSorted.entrySet());
		Collections.sort(Listing, new Comparator<Map.Entry<Context, Map<Context, Double>>>(){
			public int compare(Map.Entry<Context, Map<Context, Double>> o1, Map.Entry<Context, Map<Context, Double>> o2){	
				return (-1) * (o1.getValue().size()-o2.getValue().size());
			}
		});
		Map<Context, Map<Context, Double>> sortedClusters = new LinkedHashMap<Context, Map<Context, Double>>();
		for(Map.Entry<Context, Map<Context, Double>> entry : Listing){
			sortedClusters.put(entry.getKey(), entry.getValue());
		}
		return sortedClusters;
	}
	
	static Map<Context, Double> SortCMap(Map<Context, Double> tobeSorted, int length, double threshold){//·ºÐÍ
		List<Map.Entry<Context, Double>> map2list = new ArrayList<Map.Entry<Context, Double>>(tobeSorted.entrySet());
		Collections.sort(map2list, new Comparator<Map.Entry<Context, Double>>(){
			public int compare(Map.Entry<Context, Double> o1, Map.Entry<Context, Double> o2){
				return (-1) * o1.getValue().compareTo(o2.getValue());
			}
		});
		Map<Context, Double> sortedMap = new LinkedHashMap<Context, Double>();
		for(int i=0; (i<map2list.size())&&(i<length); i++){
			Context aCT = map2list.get(i).getKey();
			double score = map2list.get(i).getValue();
			if(score > threshold){
				sortedMap.put(aCT, score);
			}else{
				break;
			}
		}
		return sortedMap;
	}
	
	static boolean sameOrSynonyms(String s1, String s2, boolean syntest){//NOTE: this piece of code will be executed billions of times!
		boolean judge = false;
		if((s1.length()>1)&&(s2.length()>1)){
			//////////to accommodate word forms
			if(s1.equals(s2)){
				return true;
			}
			if(syntest){
				String ss = (s1.length()<=s2.length()) ? s1 : s2;//always use the shorter word as key
				String sl = (s1.length()>s2.length()) ? s1 : s2;
				if(wsimTable.keySet().contains(ss)){
					Map<String, Boolean> row = wsimTable.get(ss);
					if(row.keySet().contains(sl)){
						judge = row.get(sl);
					}else{
						try{
							double d = MainClass.simJCn.getSimilarity(ss,sl).getSimilarity();
							if(d > 0.6){//
								judge = true;
								row.put(sl,true);
							}else{
								judge = false;
								row.put(sl, false);
							}
							//System.out.println(s1+" "+s2+" "+Double.toString(d));//
						}catch(Exception e){
							judge = false;
							row.put(sl, false);
						}
					}
				}else{
					try{
						double dd = MainClass.simJCn.getSimilarity(ss,sl).getSimilarity();
						Map<String, Boolean> newRow = new HashMap<String, Boolean>();
						if(dd > 0.6){//
							judge = true;
							newRow.put(sl,true);
						}else{
							judge = false;
							newRow.put(sl, false);
						}
						wsimTable.put(ss, newRow);
						//System.out.println(s1+" "+s2+" "+Double.toString(dd));//
					}catch(Exception e){
						Map<String, Boolean> newRow = new HashMap<String, Boolean>();
						judge = false;
						newRow.put(sl, false);
						wsimTable.put(ss, newRow);
					}
				}
			}
		}
		/*
		try {
			IndexWord s11 = Dictionary.getInstance().lookupIndexWord(POS.NOUN, s1);
			IndexWord s12 = Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, s1);
			IndexWord s21 = Dictionary.getInstance().lookupIndexWord(POS.NOUN, s2);
			IndexWord s22 = Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, s2);
			if((s11!=null)&&(s21!=null)){
				Synset[] syns11 = s11.getSenses();//Maybe should consider some specific relationships
				for(Synset syn : syns11){
					if(syn.containsWord(s21.getLemma())){
						judge = true;
						break;
					}
				}
			}
			if((s12!=null)&&(s22!=null)){
				Synset[] syns12 = s12.getSenses();
				for(Synset syn : syns12){
					if(syn.containsWord(s22.getLemma())){
						judge = true;
						break;
					}
				}
			}
		}catch (Exception e) {
			System.out.println(e);
		}
		*/
		return judge;
	}
	
	static double phrSim(String a, String b, boolean syntest){//singular input, should always compare the short phrase to the longer, in order to maintain the same sim value for a pair
		if((a.length()<2)||(b.length()<2)){
			return (double)0;
		}
		String[] aa = a.split(" ");
		String[] bb = b.split(" ");
		String[] short_a = (aa.length <= bb.length) ? aa : bb;
		String[] long_b = (aa.length > bb.length) ? aa : bb;
		if(short_a.length==1){
			if(long_b.length > 1){
				int l = long_b.length;
				if(sameOrSynonyms(short_a[0], long_b[l-2], syntest)||sameOrSynonyms(short_a[0], long_b[l-1], syntest)){
					return (double)1;
				}else{
					Map<String, Double> mp = Threadf.coTable.get(short_a[0]);
					double d1 = (mp!=null)&&mp.containsKey(long_b[l-1])? mp.get(long_b[l-1]): 0;
					double d2 = (mp!=null)&&mp.containsKey(long_b[l-2])? mp.get(long_b[l-2]): 0;
					return (d1>d2)? d1*1: d2*1;                      //*3.237
				}
			}else{//b.length==1
				if(sameOrSynonyms(short_a[0], long_b[0], syntest)){
					return (double)1;                                  // 2
				}else{
					Map<String, Double> mp = Threadf.coTable.get(long_b[0]);
					double d = (mp!=null)&&mp.containsKey(short_a[0])? mp.get(short_a[0]): 0;
					return d*1;                                       //*3.237
				}
			}
		}else{//both length>=2
			int la = short_a.length;
			int lb = long_b.length;
			if(sameOrSynonyms(short_a[la-2], long_b[lb-2], syntest)&&sameOrSynonyms(short_a[la-1], long_b[lb-1], syntest)){
				return (double)4;
			}else{
				if(!(sameOrSynonyms(short_a[la-2], long_b[lb-2], syntest)||sameOrSynonyms(short_a[la-1], long_b[lb-1], syntest)||sameOrSynonyms(short_a[la-2], long_b[lb-1], syntest)||sameOrSynonyms(short_a[la-1], long_b[lb-2], syntest))){
					Map<String, Double> mp0 = Threadf.coTable.get(short_a[la-2]);
					double d01 = (mp0!=null)&&mp0.containsKey(long_b[lb-1])? mp0.get(long_b[lb-1]): 0;
					double d02 = (mp0!=null)&&mp0.containsKey(long_b[lb-2])? mp0.get(long_b[lb-2]): 0;
					double d0 = (d01>d02)? d01: d02;
					Map<String, Double> mp1 = Threadf.coTable.get(short_a[la-1]);
					double d11 = (mp1!=null)&&mp1.containsKey(long_b[lb-1])? mp1.get(long_b[lb-1]): 0;
					double d12 = (mp1!=null)&&mp1.containsKey(long_b[lb-2])? mp1.get(long_b[lb-2]): 0;
					double d1 = (d11>d12)? d11: d12;
					return (d0>d1)? d0*1: d1*1;                      //*3.237
				}else{
					return (double)1;
				}
			}
		}
	}
	
	static double computeCSim(Context x, Context y){
		//should always compare the short context to the longer, in order to maintain the same sim value for a pair
		Context xx = (x.nounPhrases.size() <= y.nounPhrases.size())? x : y;
		Context yy = (x.nounPhrases.size() > y.nounPhrases.size())? x : y;
		double targsim = 0;
		double cnstrsimsum = 0;
		boolean predsim = sameOrSynonyms(xx.SVO[1],yy.SVO[1],false);//
		if(((phrSim(xx.SVO[0],yy.SVO[0],false)>=1)||(phrSim(xx.SVO[2],yy.SVO[2],false)>=1))&&predsim){
			targsim = 10;//!!!
		}else{
			String p1 = (xx.SVO[0].equals("-"))? xx.SVO[2] : xx.SVO[0];
			String p2 = (yy.SVO[0].equals("-"))? yy.SVO[2] : yy.SVO[0];
			targsim = phrSim(p1, p2, false);
		}
		if(targsim==0){
			targsim=0.01;
		}
		for(String npx : xx.nounPhrases.keySet()){
			double smax = 0;
			//double xwt = xx.nounPhrases.get(npx);
			for(String npy : yy.nounPhrases.keySet()){
				//double ywt = yy.nounPhrases.get(npy);
				double temp = phrSim(npx, npy, false);
				//double temp = phrSim(npx, npy, false)*0.5*(xwt+ywt);//Geometric means?
				smax += temp;                                         //Summed similarity
				//if(temp > smax){
				//	smax = temp;
				//}                                                   //Max similarity
			}
			cnstrsimsum += smax;
		}
		//double cnstrsim = cnstrsimsum/(xx.nounPhrases.size()+2);//normalize with feature number
		return targsim * cnstrsimsum;//cnstrsim;
	}
	
	static void showOutput(Map<Context, Map<Context, Double>> tobeShown, int topicCount, int contentCount, double threshold){
		System.out.println("Process finished, showing...");//
		int i = 0;
		Iterator<Context> itr = tobeShown.keySet().iterator();
		while((i<topicCount)&&(itr.hasNext())){
			Context cx = itr.next();
			System.out.println("FOR CONTEXT: "+cx.toString());//
			Map<Context, Double> neibs= tobeShown.get(cx);
			boolean hasCloseNb = false;
			int j = 0;
			Iterator<Context> itt = neibs.keySet().iterator();
			while((j<contentCount)&&(itt.hasNext())){
				Context nb = itt.next();
				if((nb!=cx)&&(neibs.get(nb)>threshold)){//
					hasCloseNb = true;
					System.out.println("<<<sim=" + Double.toString(neibs.get(nb)) + nb.toString());//
					//computeCSim(nb,cx);//
					j++;
				}
			}
			if(hasCloseNb){
				System.out.println("  Total similar contexts number: " + neibs.size());//
				i++;
			}
		}
	}
	
	static Map<Context, Double> matchWith(Context incoming, double titleSimThreshold){//Pseudo relevance feedback
		Map<Context, Double> topsimCtxs = new  LinkedHashMap<Context, Double>();
		Map<Context, Double> simCtxs = new  LinkedHashMap<Context, Double>();
		for(Context everyCtx : QcaRecord.contextDB){
			topsimCtxs.put(everyCtx, 100*computeCSim(everyCtx, incoming));
		}
		topsimCtxs = SortCMap(topsimCtxs, 20, 0);//top 20 are used for RF
		for(Context topCtx : topsimCtxs.keySet()){//find NP that has maximum Mutual Information with incoming NPs
			double maxMI = 0;
			String toAdd = "";
			for(String aNP : topCtx.nounPhrases.keySet()){
				double maxMuIn = 0;
				for(String iNP : incoming.nounPhrases.keySet()){
					double sc = phrSim(iNP,aNP,false);
					if(sc > maxMuIn){
						maxMuIn = sc;
					}
				}
				if((maxMuIn < 1)&&(maxMuIn > maxMI)){
					maxMI = maxMuIn;
					toAdd = aNP;
				}
			}
			if(toAdd.length()>1){
				incoming.nounPhrases.put(toAdd, topCtx.nounPhrases.get(toAdd));
			}
		}
		for(Context everyCtx : QcaRecord.contextDB){
			simCtxs.put(everyCtx, 100*computeCSim(everyCtx, incoming));
		}
		simCtxs = SortCMap(simCtxs, 5000, 4);//!!!
		for(Context candi : simCtxs.keySet()){
			double simtitlecount = 0;
			for(Context candj : simCtxs.keySet()){
				double targsim = 0;
				boolean predsim = sameOrSynonyms(candi.SVO[1],candj.SVO[1],false);
				if(((phrSim(candi.SVO[0],candj.SVO[0],false)>=1)||(phrSim(candi.SVO[2],candj.SVO[2],false)>=1))&&predsim){
					targsim = 10;
				}else{
					String p1 = (candi.SVO[0].equals("-"))? candi.SVO[2] : candi.SVO[0];
					String p2 = (candj.SVO[0].equals("-"))? candj.SVO[2] : candj.SVO[0];
					targsim = phrSim(p1, p2, false);
				}
				if(targsim > titleSimThreshold){
					simtitlecount++;
				}
			}
			simCtxs.put(candi, Math.log(1+simtitlecount)*simCtxs.get(candi));
		}
		return SortCMap(simCtxs, 100, 0);
	}
	
	static Map<Context, Double> simpleMatchWith(Context incoming, double threshold){//not simple in fact
		Map<Context, Double> rcmmd = new  HashMap<Context, Double>();
		Map<Context, Double> constrSim = new HashMap<Context, Double>();
		List<Map<Context, Double>> forDiffNPs = new ArrayList<Map<Context, Double>>();
		int simedctxsum = 0;
		for(String aNP: incoming.nounPhrases.keySet()){
			double weight = incoming.nounPhrases.get(aNP);
			Map<Context, Double> hasSimTitl = new HashMap<Context, Double>();
			for(Context qt : QcaRecord.contextDB){
				if(!qt.SVO[2].equals("-")){///if VO
				String qtt = (qt.SVO[0].equals("-"))? qt.SVO[2] : qt.SVO[0];
				double topicSim = phrSim(aNP, qtt, false);///
				if(topicSim >= 0.6){///
					simedctxsum++;
					double constrsim = 0;
					if(constrSim.containsKey(qt)){
						constrsim = constrSim.get(qt);
					}else{
						constrsim = 100*computeCSim(qt, incoming);
						constrSim.put(qt, constrsim);
					}
					hasSimTitl.put(qt, weight*topicSim*constrsim);
				}
			}
			}
			forDiffNPs.add(hasSimTitl);
		}
		System.out.println("Total matched contexts in the 1st phase: "+simedctxsum);//
		Map<Integer, Set<Context>> belong2Topic = new HashMap<Integer, Set<Context>>();//to limit retrieved context number for each topic
		for(Context ctx: QcaRecord.contextDB){
			if(!ctx.SVO[2].equals("-")){///if VO
			double score = 0;//overall score considering all topics
			int maxfromTopic = -1;//from which topic this context is finally chosen
			for(int i=0; i < forDiffNPs.size(); i++){
				Map<Context, Double> amap = forDiffNPs.get(i);
				int count = 0;//in each topic, count the number of similar contexts as part of the score
				if(amap.containsKey(ctx)){
					for(Context nb: amap.keySet()){
						if(simContexts.containsKey(ctx)){//cache computed context pairs///////////redundant???????
							Map<Context, Double> calced = simContexts.get(ctx);
							if(calced.containsKey(nb)){
								if(calced.get(nb) > threshold){//ctx vs ctx calculated
									count++;
								}
							}else{
								double dd = computeCSim(ctx, nb);
								calced.put(nb, dd);
								if(simContexts.containsKey(nb)){
									simContexts.get(nb).put(ctx, dd);
								}else{
									Map<Context, Double> newrow = new HashMap<Context, Double>();
									newrow.put(ctx, dd);
									simContexts.put(nb, newrow);
								}
								if(dd > threshold){
									count++;
								}
							}
						}else{
							double dd = computeCSim(ctx, nb);
							Map<Context, Double> newCRow = new HashMap<Context, Double>();
							newCRow.put(nb, dd);
							simContexts.put(ctx, newCRow);
							if(simContexts.containsKey(nb)){
								simContexts.get(nb).put(ctx, dd);
							}else{
								Map<Context, Double> newNrow = new HashMap<Context, Double>();
								newNrow.put(ctx, dd);
								simContexts.put(nb, newNrow);
							}
							if(dd > threshold){
								count++;
							}
						}
					}
				}
				double d = amap.containsKey(ctx)? amap.get(ctx)*Math.log((double)count+1): 0;////////////
				if(d>score){
					score=d;
					maxfromTopic=i;
				}
			}
			if(score>0){
				rcmmd.put(ctx, score);
				if(belong2Topic.containsKey(maxfromTopic)){
					belong2Topic.get(maxfromTopic).add(ctx);
				}else{
					Set<Context> newTopic = new HashSet<Context>();
					newTopic.add(ctx);
					belong2Topic.put(maxfromTopic, newTopic);
				}
			}
			}
		}
		Map<Integer, Integer> eachHowmany = new HashMap<Integer, Integer>();//how many selected contexts for each topic
		Map<Context, Double> sorted = SortCMap(rcmmd, 500, 0);//Un-grouped results///////////
		Map<Context, Double> rst = new LinkedHashMap<Context, Double>();
		for(Context con: sorted.keySet()){
			for(int t: belong2Topic.keySet()){
				if(belong2Topic.get(t).contains(con)){
					if(eachHowmany.containsKey(t)){
						int n = eachHowmany.get(t);
						if(n<=5){//
							eachHowmany.put(t, n+1);
							rst.put(con, sorted.get(con));
						}
					}else{
						eachHowmany.put(t, 1);
						rst.put(con, sorted.get(con));
					}
					break;
				}
			}
		}
		return rst;///////////
	}
}

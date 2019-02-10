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

public class SuperNode {
	
	static Set<SuperNode> UpperNodes = new HashSet<SuperNode>();
	Context center;
	Set<Context> contexts;
	Set<SuperNode> children;
	String name;
	double asWeight;
	Label label;
	
	enum Label{
		UP, RIGHT, LEFT, LOW
	}
	
	SuperNode(Label lb, double score){
		this.label = lb;
		this.asWeight = score;
	}
}

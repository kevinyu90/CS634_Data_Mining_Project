package project1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class project_apriori {
	
	private static Double SUPPORT = 0.0; // initial support
	private static int DATA_SIZE = 0; // initial data size  
    private static Double CONFIDENCE = 0.0; // initial confidence 
    private final static String ITEM_SPLIT = ","; // splitted by 
    private final static String CON = "->"; // connected by
   
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		// define support
		System.out.println("Please input support(0-100)%: ");
		while(SUPPORT == 0){
			try {
				SUPPORT = Double.parseDouble(scanner.nextLine())/100;
			} catch (Exception e) {
				System.out.println("Wrong data format!");
				System.out.println("Please input support(0-100)%: ");
			}
		}
		// define confidence
		System.out.println("Please input CONFIDENCE(0-100)%: ");
		while(CONFIDENCE == 0){
			try {
				CONFIDENCE = Double.parseDouble(scanner.nextLine())/100;
			} catch (Exception e) {
				System.out.println("Wrong data format!");
				System.out.println("Please input CONFIDENCE(0-100)%: ");
			}
		}
	
		// get all the transcations
		ArrayList<String> dataList = new ArrayList<>();
		while(dataList.isEmpty()){
			FileReader fr;
			try {
				String file;
				System.out.println("Please input file name: ");
				file = scanner.nextLine();
				fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				while(br.ready()){
					dataList.add(br.readLine());
				}
				fr.close();
			} catch (FileNotFoundException e) {
				System.out.println("Wrong file name!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		scanner.close();
		DATA_SIZE = dataList.size();
        
        System.out.println("-----------Data Set---------------");  
        for(String string:dataList)  
        {  
            System.out.println(string);  
        }
        // using apriori to get frequent set
        Map<String, Integer> frequentSetMap = apriori(dataList);
        Set<String> keySet = frequentSetMap.keySet(); 
        System.out.println("Minimum support = "+ SUPPORT*100+"%");
        System.out.println("-----------Frequent Set-----------");
        for(String key:keySet)  
        {  
            System.out.println("{"+key+"} ("+frequentSetMap.get(key)+")");  
        }
        // using frequent set to get all relation rules
        System.out.println("Confidence = "+CONFIDENCE*100+"%");
        System.out.println("-----------Relation Rules---------");
        Map<String, Double> relationRules = getRelationRules(frequentSetMap);
        Set<String> relationRulesSet = relationRules.keySet();
        for(String key: relationRulesSet){
        	System.out.println(key +" : "+relationRules.get(key));
        }      
	}	
	
	// get all relation rules from frequent set
	public static Map<String, Double> getRelationRules(Map<String, Integer> frequentSetMap){
		Map<String, Double> relationsMap = new HashMap<>(); 
		Set<String> keySet = frequentSetMap.keySet();
		
		for(String key: keySet){
			// no relation rules from one item set
			if(key.length() == 1){
				continue;
			}
			List<String> keySubSet = subSet(key);
			for(String subSet: keySubSet){
				int count = frequentSetMap.get(subSet);
				if(count != 0){
					Double confidence = (1.0*frequentSetMap.get(key))/(1.0*count);
					if(confidence >= CONFIDENCE && !except(key, subSet).isEmpty()){
						relationsMap.put(subSet+CON+except(key, subSet), confidence);
					}
				}
			}
		}	
		return relationsMap;
	}
	
	// get frequent set from data
	public static Map<String, Integer> apriori(ArrayList<String> dataList){
		// get one item set
        Map<String, Integer> oneItemSet = findOneItemSet(dataList);	
        Set<String> oneItemKeySet = oneItemSet.keySet();
        
        // get one item frequent set 
        Map<String, Integer> stepFrequentSetMap = new HashMap<>();
        for(String candidate:oneItemKeySet)  
        {  
            Integer count = oneItemSet.get(candidate); 
            Double support =  ((double)count/DATA_SIZE);
            if(support>=SUPPORT){
            	stepFrequentSetMap.put(candidate, count);  
            }           
        }
        // all frequent item set
        Map<String, Integer> frequentSetMap = new HashMap<String, Integer>();
        frequentSetMap.putAll(stepFrequentSetMap);
        
        while(stepFrequentSetMap!=null && stepFrequentSetMap.size()>0)  
        {
        	// get next item set
        	 Map<String, Integer> candidateSetMap = nextItemSet(stepFrequentSetMap);             
             Set<String> candidateKeySet = candidateSetMap.keySet();             
          // count item set
             for(String data: dataList){
             	for(String candidate:candidateKeySet){
             		boolean flag = true;
             		String[] strings = candidate.split(ITEM_SPLIT);
             		for(String string: strings){
             			if(data.indexOf(string)==-1){
             				flag = false;
             				break;
             			}
             		}
             		if(flag){
             			candidateSetMap.put(candidate, candidateSetMap.get(candidate)+1);
             		}
             	} 
             }
             // check candidate >= support
             stepFrequentSetMap.clear();
             for(String candidate:candidateKeySet){  
	                Integer count = candidateSetMap.get(candidate);  
	                Double support =  ((double)count/DATA_SIZE);
	                if(support>=SUPPORT){
	                	stepFrequentSetMap.put(candidate, count);
	                }   
             }
             
             // put all the candidate >= support into frequentSet
             frequentSetMap.putAll(stepFrequentSetMap);
        }       
        return frequentSetMap;
	}
	
	// get next item set from item set
	public static Map<String, Integer> nextItemSet(Map<String, Integer> itemSet){
		Map<String, Integer> candidateSetMap = new HashMap<>();
		
		Set<String> candidateSet = itemSet.keySet();
		for(String string1: candidateSet){
			String[] strings1 = string1.split(ITEM_SPLIT);
			for(String string2: candidateSet){
				String[] strings2 = string2.split(ITEM_SPLIT);
				boolean flag = true;
				for(int i=0;i<strings1.length-1;i++)  
                {                 	
                    if(strings1[i].compareTo(strings2[i])!=0)  
                    {  
                        flag = false;  
                        break;  
                    }  
                }				
				if(flag && strings1[strings1.length-1].compareTo(strings2[strings1.length-1])<0){ 
					String temp = string1+ITEM_SPLIT+strings2[strings2.length-1];
					candidateSetMap.put(temp, 0);
				}
			}
		}		
		return candidateSetMap;
	}

	// get all subset
	public static List<String> subSet(String source){
		List<String> result = new ArrayList<String>();
		String[] strings = source.split(ITEM_SPLIT);
		
		for(int i=1; i<=(int)(Math.pow(2, strings.length))-1; i++){
			String item = "";
			String binary = "";
			int num = i;
			while(num>0){
				binary += ""+num%2;
				num = num/2;
			}
			for(int j=0; j<=binary.length()-1; j++){
				if(binary.charAt(j) == '1'){
					if(item == ""){
						item = strings[j];
					} else {
						item = item +ITEM_SPLIT+ strings[j];
					}
				}
			}
			result.add(item);
		}		
		return result;
	}
	
	// remove s2 from s1
	public static String except(String s1, String s2){
		String[] strings1 = s1.split(ITEM_SPLIT);
		String[] strings2 = s2.split(ITEM_SPLIT);
		StringBuilder result = new StringBuilder();
		for(int i=0; i<strings1.length; i++){
			boolean flag = true;
			for(int j=0; j<strings2.length; j++){
				if(strings1[i].compareTo(strings2[j]) == 0){
					flag = false;
					break;
				}
			}
			if(flag){
				result.append(strings1[i]+ITEM_SPLIT);
			}
		}
		if(result.length()>1){
			result.deleteCharAt(result.length()-1);
		}
		return result.toString();
	}
	
	// get one time set from data
	public static Map<String, Integer> findOneItemSet(ArrayList<String> dataList){
		Map<String, Integer> resultMap = new HashMap<>();
		for(String data: dataList){
			String[] strings = data.split(ITEM_SPLIT);
			for(String s: strings){
				if(resultMap.get(s) == null){
					resultMap.put(s, 1);
				} else {
					resultMap.put(s, resultMap.get(s)+1);
				}
			}
		}	
		return resultMap;
	}
}

package project1;

import java.util.ArrayList;

public class createTranscations {

	public static void main(String[] args) {
		String[] items = {"Milk","Cereal","Paper towel","Chip","Beer","Diaper","Ice cream","Vitamin","Apple","Egg"};
		ArrayList<String> dataList = new ArrayList<>();
		for(int i=0; i<20; i++){
			StringBuilder sb = new StringBuilder();
			int size = (int)(Math.random()*6)+1;
			ArrayList<String> picked = new ArrayList<>();
			while(picked.size()<size){
				int get = (int)(Math.random()*items.length);
				if(!picked.contains(items[get])){
					picked.add(items[get]);
				}
			}
			for(int k=0; k<picked.size()-1; k++){
				sb.append(picked.get(k)+",");
			}
			sb.append(picked.get(picked.size()-1));
			dataList.add(sb.toString());
		}
		for(String s: dataList){
			System.out.println(s);
		}
	}

}

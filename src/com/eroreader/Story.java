package com.eroreader;

public class Story implements Comparable<Story>{
	public int id;
	public String cathegory;
	public int pos;
	public String text;
	public boolean favorite;
	public int prime;
	public String title;
	public int last;
	public int weight;
	
	public Story(){
		weight = 100000;
	}
	
	public int getWeight(){
		int ans = 0;
		for (int i = 0; i < cathegory.length(); i++)
			if (cathegory.charAt(i) == '1')
				ans += i;
		return ans;
	}
	
	public int compareTo(Story another) {
		return (this.weight - another.weight);
	}
}

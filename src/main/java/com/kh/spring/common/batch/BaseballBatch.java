package com.kh.spring.common.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseballBatch {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BatchRepository batchR;
	
	
	
	// 
	public void jsupTest(){
		
		Document doc;
		try {
			doc = Jsoup.connect("https://www.koreabaseball.com/TeamRank/TeamRank.aspx").get();
			Elements list = doc.select("#cphContents_cphContents_cphContents_udpRecord > table > tbody > tr");
			List<Map<String, String>> dataList = getRankData(list);
			
			for (Map<String, String> map : dataList) {
				batchR.insertBaseballData(map);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<Map<String, String>>  getRankData(Elements list){
		
		String[] keyArr = {"rank", "name", "game", "win", "lose", "tie"};
		List<Map<String, String>> dataList = new ArrayList<>();
		
		for (Element webtoon : list) {
			Elements datas = webtoon.getElementsByTag("td");
			Map<String, String> data = new HashMap<String, String>();
			
			for (int i = 0; i < 6; i++) {
				data.put(keyArr[i], datas.get(i).text());
			} 
			
			dataList.add(data);
		}
		return dataList;
		
	}

}

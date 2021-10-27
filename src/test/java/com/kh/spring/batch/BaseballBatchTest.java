package com.kh.spring.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.kh.spring.common.batch.BatchRepository;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*-context.xml"})
public class BaseballBatchTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BatchRepository batchR;
	
	
	@Test
	public void jsupTest() throws IOException{
		
		Document doc = Jsoup.connect("https://www.koreabaseball.com/TeamRank/TeamRank.aspx").get();
		Elements list = doc.select("#cphContents_cphContents_cphContents_udpRecord > table > tbody > tr");
		
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
		
		for (Map<String, String> map : dataList) {
			batchR.insertBaseballData(map);
		}
	}

}

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BaseballBatch {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BatchRepository batchR;
	
	
	
	// cron 표현식
	// 초 분 시 일 월 요일 년(스프링에선 사용 안함)
	// * : 모든
	// , : 복수값 지정
	// 시작시간/단위 : 시작시간부터 저정한 단위마다 실행
	
	// 0 0 3 * * * => 0초 0분 3시 매일 매월 매요일
	// 0 0 3,6,22 * * * => 매일 매월 매요일 새벽 3,4시 저녘 10시에 돌려
	// 0 0/15 * * * * => 15분마다 배치 실행
	
	// 0 46/15 * * * * => 46분 이후 2분마다 배치 실행
	
	/* @Scheduled(cron = "0 46/2 * * * *") */
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

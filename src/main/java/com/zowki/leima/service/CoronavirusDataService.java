package com.zowki.leima.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zowki.leima.model.LocationStats;
@Service
public class CoronavirusDataService {

	private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/archived_data/archived_time_series/time_series_19-covid-Confirmed_archived_0325.csv";
	
	private List<LocationStats> allStats= new ArrayList<>();		
	
	public List<LocationStats> getAllStats() {
		return allStats;
	}

	public void setAllStats(List<LocationStats> allStats) {
		this.allStats = allStats;
	}

	@PostConstruct
	@Scheduled(cron="******")
    public void fetchVirusData() throws IOException, InterruptedException {
	
	List<LocationStats> newStats = new ArrayList<>();
	
		
	HttpClient client=HttpClient.newHttpClient();
	HttpRequest request=HttpRequest.newBuilder()
			.uri(URI.create(VIRUS_DATA_URL)).build();
	
	httpResponse<String> httpResponse=client.send(request,
			httpResponse.BodyHandlers.ofString());
	
StringReader csvBodyReader=new StringReader(httpResponse.body());
Iterable<CSVRecord> records=CSVFormat.DEFAULT.withFirstRecordAsHeader()
.parse(csvBodyReader);

for(CSVRecord record:records) {
	LocationStats locationstat= new LocationStats();
	locationstat.setState(record.get("Province/State"));
	locationstat.setCountry(record.get("country/Region"));
	locationstat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
    int latestCases = Integer.parseInt(record.get(record.size()-1))	;
    int prevDayCases= Integer.parseInt(record.get(record.size() - 2));
    locationstat.setLatestTotalCases(latestCases);
    locationstat.setDiffFromPrevDay(latestCases - prevDayCases);
    newStats.add(locationstat);



}

	this.allStats= newStats;
	
}
		
		
		
		
		
	}
		
 



















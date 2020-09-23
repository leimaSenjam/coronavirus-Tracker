package com.zowki.leima.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.zowki.leima.model.LocationStats;
import com.zowki.leima.service.CoronavirusDataService;

@Controller
public class Homecontroller {

	@Autowired
	CoronavirusDataService coronavirusDataService;
		
	
	@GetMapping("/")
	public String home(Model model) {
	
		
		
	List<LocationStats> allStats= coronavirusDataService.getAllStats();	
	int totalReportedCases =allStats.stream().flatMapToInt(stat -> stat.getLatestTotalCases()).sum();	
	int totalNewCases= allStats.parallelStream().flatMapToInt(stat -> stat.getDiffFromPrevDay()).sum();
	model.addAttribute("locationStats", "allStats");
	model.addAttribute("totalReportedCases", totalReportedCases);
	model.addAttribute("totalNewCases", totalNewCases);	
	return "home";
		
	}
	
	 
	
	
	
}

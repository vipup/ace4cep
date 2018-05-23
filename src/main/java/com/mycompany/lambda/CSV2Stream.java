package com.mycompany.lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSV2Stream {

	private static final String COMMA = ",";

	private List<YourJavaItem> processInputFile(String inputFilePath) {
		List<YourJavaItem> inputList = new ArrayList<YourJavaItem>();
		try {
			File inputF = new File(inputFilePath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			// skip the header of the csv
			inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
			br.close();
		} catch (IOException e) {
			//
		}
		return inputList;
	}

	private Function<String, YourJavaItem> mapToItem = (line) -> {
		String[] p = line.split(COMMA);// a CSV has comma separated lines
		YourJavaItem item = new YourJavaItem();
		item.setItemNumber(p[0]);// <-- this is the first column in the csv file
		if (p[3] != null && p[3].trim().length() > 0) {
			item.setSomeProeprty(p[3]);
		}
		// more initialization goes here
		return item;
	};
}

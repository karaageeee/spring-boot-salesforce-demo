package com.example.demo.infrastructure.salesforce;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {

	private int totalSize;
	private Boolean done;
	private String nextRecordsUrl;
	private List<HashMap<String, Object>> records;

}

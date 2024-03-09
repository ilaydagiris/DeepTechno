package com.company.untitled1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@RestController
public class DeepTechno {
	public static FilteredClassifier model;
	public static Instances data;

	public DeepTechno() throws Exception{
		this.model = (FilteredClassifier) SerializationHelper.read("C:\\Users\\PC\\Desktop\\proje\\projede kullanılanlar (2. dönem)\\wekaJava.model");
		ConverterUtils.DataSource source = new ConverterUtils.DataSource("C:\\Users\\PC\\Desktop\\proje\\projede kullanılanlar (2. dönem)\\arff_set\\KelimeEgitSet.arff");
		data = source.getDataSet();
		data.setClassIndex(1);

	}
	public static void main(String[] args) {
		SpringApplication.run(DeepTechno.class, args);
	}
	private static String classes[] = new String[]{"telefon","pc","oyun","borsa","vizyon","yazilim","app","arac","diger","ak","uz"};

	private String Test(String News) throws Exception {
		data.add(new DenseInstance(2));
		data.get(data.size()-1).setValue(0,News);
		return classes[(int)model.classifyInstance(data.get(data.size()-1))];
	}

	@GetMapping("/")
	public String sunucu(@RequestParam(value = "haber", defaultValue = "World") String name , final HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "http://local.lo:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, HEAD");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");


		return String.format("asdasdasd %s!", name);
	}

	@CrossOrigin(origins = "http://local.lo:3000")
	@PostMapping("/getlabel")
	public String veri(@RequestBody Body body, final HttpServletResponse response) throws Exception{
		response.setHeader("Access-Control-Allow-Origin", "http://local.lo:3000");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, HEAD");


		return Test(body.News);
	}
}

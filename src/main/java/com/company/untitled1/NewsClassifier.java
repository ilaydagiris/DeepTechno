package com.company.untitled1;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsClassifier {

    private static final String[] classes = {"telefon", "pc", "oyun", "borsa", "vizyon", "yazilim", "app", "arac", "diger", "ak", "uz"};

    public static void main(String[] args) {
        try {
            // Modeli test et
            testModel();
            // Veri kümesini eğit
            Instances instances = trainModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testModel() throws Exception {
        // Modeli yükle
        FilteredClassifier model = (FilteredClassifier) SerializationHelper.read("C:\\Users\\PC\\Desktop\\proje\\projede kullanılanlar (2. dönem)\\wekaJava.model");

        // Test veri setini yükle
        DataSource source = new DataSource("C:\\Users\\PC\\Desktop\\proje\\projede kullanılanlar (2. dönem)\\arff_set\\KelimeEgitSet.arff");
        Instances testData = source.getDataSet();
        testData.setClassIndex(1);

        // Tahminleri saklamak için bir liste oluştur
        List<String> predictions = new ArrayList<>();

        // Her bir veri örneği üzerinde modeli test et
        for (Instance instance : testData) {
            double prediction = model.classifyInstance(instance);
            predictions.add(classes[(int) prediction]);
        }

        // Tahminleri ekrana yazdır
        System.out.println("Tahminler: " + predictions);
    }

    private static Instances trainModel() throws Exception {
        // Veri kümesini yükle
        DataSource source = new DataSource("C:\\Users\\PC\\Desktop\\proje\\projede kullanılanlar (2. dönem)\\arff_set\\KelimeEgitSet.arff");
        Instances data = source.getDataSet();
        if (data.classIndex() == -1)
            data.setClassIndex(1);

        // Bayes sınıflandırıcısını oluştur
        NaiveBayesMultinomial naiveBayes = new NaiveBayesMultinomial();

        // Kelimeleri vektöre dönüştür
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(data);
        filter.setIDFTransform(true);
        filter.setLowerCaseTokens(true);

        // Filtrelenmiş sınıflandırıcı oluştur ve eğit
        FilteredClassifier classifier = new FilteredClassifier();
        classifier.setFilter(filter);
        classifier.setClassifier(naiveBayes);
        classifier.buildClassifier(data);

        // Modeli değerlendir
        Evaluation evaluation = new Evaluation(data);
        evaluation.crossValidateModel(classifier, data, 10, new Random(1));
        System.out.println(evaluation.toSummaryString());

        // Modeli diske kaydet
        SerializationHelper.write("C:\\Users\\PC\\Desktop\\proje\\projede kullanılanlar (2. dönem)\\wekaJava.model", classifier);

        return data;
    }
}

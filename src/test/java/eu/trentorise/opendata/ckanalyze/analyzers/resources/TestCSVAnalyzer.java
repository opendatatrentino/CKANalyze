package eu.trentorise.opendata.ckanalyze.analyzers.resources;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Test;

import eu.trentorise.opendata.ckanalyze.analyzers.resources.CSVAnalyzer.Datatype;
import eu.trentorise.opendata.ckanalyze.exceptions.CKAnalyzeException;

/**
 * Test the CSVAnalyzer class
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * @since Last modified by azanella On 19/lug/2013
 */

public class TestCSVAnalyzer {

	@Test
	/**
	 * Test CSVAnalyzer with an empty file.
	 * empty.csv is an empty file
	 * Must throw a CKANAlyzeException with message "Empty resource"
	 */
	public void analyzeEmptyFile() {
		try {
			CSVAnalyzer csva = new CSVAnalyzer(ClassLoader.getSystemResource(
					"csv/empty.csv").getPath(), "");
			csva.analyze();
			assertTrue(false);
		} catch (CKAnalyzeException e) {
			assertTrue("must throw an exception since the resource is empty", e
					.toString().contains("Empty resource"));
		}
	}

	@Test
	/**
	 * Test CSVAnalyzer with a CSV having headers only.
	 * headers_only.csv is a file with only 1 line of headers
	 * Must throw a CKANAlyzeException with message "Empty resource"
	 */
	public void analyzeHeadersOnlyCSV() {
		try {
			CSVAnalyzer csva = new CSVAnalyzer(ClassLoader.getSystemResource(
					"csv/headers_only.csv").getPath(), "");
			csva.analyze();
			assertTrue(false);
		} catch (CKAnalyzeException e) {
			assertTrue("must throw an exception since the resource is empty", e
					.toString().contains("Empty resource"));
		}
	}

	@Test
	/**
	 * Test CSVAnalyzer with a malformed CSV.
	 * malformed.csv is a file with an invalid number of column (1 extra column) at line 3.
	 * Must report the problem in a Warning log message without throwing exceptions;
	 */
	public void analyzeMalformedCSV() {
		StringWriter toCheck = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout(
				"%d{ISO8601} %p - %m%n"), toCheck);
		appender.setName("LOGGER_APPENDER");
		Logger.getRootLogger().addAppender(appender);
		appender.setThreshold(org.apache.log4j.Level.WARN);
		try {
			CSVAnalyzer csva = new CSVAnalyzer(ClassLoader.getSystemResource(
					"csv/malformed.csv").getPath(), "");
			csva.analyze();
			assertTrue(
					"must report a warning message since the CSV is malformed",
					!toCheck.toString().isEmpty());
		} catch (CKAnalyzeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

/*	@Test
	/**
	 * Test CSVAnalyzer with a demo CSV.
	 * prodotti_tradizionali.csv is a valid CSV file.
	 * Must perform the analysis without errors;
	 */
	public void analyzeProdottiTradizionali() {
		StringWriter toCheck = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout(
				"%d{ISO8601} %p - %m%n"), toCheck);
		appender.setName("LOGGER_APPENDER");
		Logger.getRootLogger().addAppender(appender);
		appender.setThreshold(org.apache.log4j.Level.WARN);
		try {
			CSVAnalyzer csva = new CSVAnalyzer(ClassLoader.getSystemResource(
					"csv/prodotti_tradizionali.csv").getPath(), "");
			csva.analyze();
			assertTrue(
					"must process the CSV prodotti_tradizionali without errors or warnings",
					toCheck.toString().isEmpty());
		} catch (CKAnalyzeException e) {
			assertTrue(false);
		}
	}

//	@Test
	/**
	 * Test CSVAnalyzer with a demo CSV and check the type analysis result.
	 * _XNz0Y0.csv is a valid CSV file with 1 DATE column and 2 number (INT) columns.
	 * Must perform the analysis without errors and checks data types identification;
	 */
	public void analyzeResourceWithTypeIdentification() {
		StringWriter toCheck = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout(
				"%d{ISO8601} %p - %m%n"), toCheck);
		appender.setName("LOGGER_APPENDER");
		Logger.getRootLogger().addAppender(appender);
		appender.setThreshold(org.apache.log4j.Level.WARN);
		try {
			CSVAnalyzer csva = new CSVAnalyzer(ClassLoader.getSystemResource(
					"csv/_XNz0Y0.csv").getPath(), "");
			csva.analyze();
			assertTrue("must process the CSV without errors or warnings",
					toCheck.toString().isEmpty());
			assertTrue(csva.getColsPerType().get(Datatype.INT) == 2);
			assertTrue(csva.getColsPerType().get(Datatype.DATE) == 1);
		} catch (CKAnalyzeException e) {
			assertTrue(false);
		}
	}

	@Test
	// Spese-per-funzione-2005.csv
	public void analyzeResourceWithStringMeasures() {
		StringWriter toCheck = new StringWriter();
		WriterAppender appender = new WriterAppender(new PatternLayout(
				"%d{ISO8601} %p - %m%n"), toCheck);
		appender.setName("LOGGER_APPENDER");
		Logger.getRootLogger().addAppender(appender);
		appender.setThreshold(org.apache.log4j.Level.WARN);
		try {
			CSVAnalyzer csva = new CSVAnalyzer(ClassLoader.getSystemResource(
					"csv/test_string.csv").getPath(), "");
			csva.analyze();
			assertTrue("must process the CSV without errors or warnings",
					toCheck.toString().isEmpty());
			assertTrue(csva.getColsPerType().get(Datatype.STRING) == 1);
			assertTrue(csva.getColsPerType().get(Datatype.INT) == 1);
			Map<Long, Long> stringDistribution = csva.getStringLengthDistribution();
			double avg = 0;
			long count = 0;
			for (Long length : stringDistribution.keySet()) {
				count = count + stringDistribution.get(length);
				avg = avg + (length*stringDistribution.get(length));
				if(length == 10) assertTrue(stringDistribution.get(length)==1);
				else if(length == 4) assertTrue(stringDistribution.get(length)==1);
				else if(length == 5) assertTrue(stringDistribution.get(length)==1);
				else if(length == 8) assertTrue(stringDistribution.get(length)==1);
				else assertTrue("Invalid string length"+length,false);
			}
			assertTrue("avg computation",csva.getStringLengthAvg() == (avg / count));
		} catch (CKAnalyzeException e) {
			assertTrue(false);
		}

	}

}

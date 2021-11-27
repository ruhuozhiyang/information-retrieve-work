package ir.utils;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

public class HighLightWord {

  public static String getHighLightString(Query query, Analyzer analyzer, String fieldName,
      String fieldContent, int fragmentSize) throws InvalidTokenOffsetsException, IOException {

    SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
    Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
    highlighter.setTextFragmenter(new SimpleFragmenter(fragmentSize));
    return highlighter.getBestFragment(analyzer, fieldName, fieldContent);
  }
  public static String getHighLightStringWild(Query query, Analyzer analyzer, String fieldName,
	      String fieldContent, int fragmentSize) throws InvalidTokenOffsetsException, IOException {

	    SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
	    Highlighter highlighter = new Highlighter(htmlFormatter, new QueryTermScorer(query));
	    highlighter.setTextFragmenter(new SimpleFragmenter(fragmentSize));
	    return highlighter.getBestFragment(analyzer, fieldName, fieldContent);
	  }
}

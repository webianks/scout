/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scout;

import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author R Ankit
 */
public class Scout {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        FileReader fileReader = new FileReader("C:\\Users\\R Ankit\\Desktop\\Appathon\\Input.txt");
        BufferedReader br = new BufferedReader(fileReader);
        
        String singleLine;
        int numberOfLines = 0;
        
          while((singleLine = br.readLine()) != null){
            
              numberOfLines++;
              Scout scout = new Scout();
              scout.processLine(singleLine);
              System.out.println();
            
          }
           br.close();
           //System.out.println("Number of lines "+numberOfLines);
           
       
    }
    
    private void processLine(String line) {
        
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
          
        Annotation document = new Annotation(line);
        pipeline.annotate(document);
        
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
       List<CoreMap> sentences = document.get(SentencesAnnotation.class);
       
       for(CoreMap sentence: sentences) {
       // traversing the words in the current sentence
       // a CoreLabel is a CoreMap with additional token-specific methods
         for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
          // this is the text of the token
          String word = token.get(TextAnnotation.class);
          // this is the POS tag of the token
          String pos = token.get(PartOfSpeechAnnotation.class);
         // this is the NER label of the token
          String ne = token.get(NamedEntityTagAnnotation.class);
               
        }
    
     // this is the parse tree of the current sentence
     Tree tree = sentence.get(TreeAnnotation.class);  
     // this is the Stanford dependency graph of the current sentence
     SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);  
     
    }
      
       // This is the coreference link graph
      // Each chain stores a set of mentions that link to each other,
     // along with a method for getting the most representative mention
    // Both sentence and token offsets start at 1!
     Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class); 
     
   }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
              scout.tokenizeTheLine(singleLine);
              System.out.println();
            
          }
           br.close();
           //System.out.println("Number of lines "+numberOfLines);
           
    }

    private void tokenizeTheLine(String singleLine) {
        for(String s : singleLine.split(" "))
           processWord(s);
    }

    private void processWord(String s) {
        System.out.println(s);
    }
}

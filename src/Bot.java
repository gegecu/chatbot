/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 *
 * @author ShenJingBing
 */
public class Bot {
    
    private String initialInput;
    private List<String> processedInput;
    private List<String> output;
    private Map<String, String> normalization;
    private Map<String, List<String>> replyOptions;
    
    public Bot() {
        initialInput = "";
        processedInput = new ArrayList<String>();
        output = new ArrayList<String>();
        normalization = new HashMap<String, String>();
        replyOptions = new HashMap<String, List<String>>();
        init();
    }
    
    private void init() {
        
        BufferedReader br = null;
        
        // putting normalization
        try {
            String line;
            String[] pairing;
            br = new BufferedReader(new FileReader("C:\\Users\\ShenJingBing\\Desktop\\normalization.txt"));
            
            while((line = br.readLine()) != null) {
                pairing = line.split(",");
                normalization.put(pairing[0], pairing[1]);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // adding reply knoweldge
        List<String> cuisine = new ArrayList();
        cuisine.add("What cuisine you want to eat?");
        replyOptions.put("cuisine", cuisine);
        
        List<String> budget = new ArrayList();
        cuisine.add("How much is your budget for food?");
        replyOptions.put("budget", budget);
        
        List<String> place = new ArrayList();
        place.add("Where do you want to eat?");
        replyOptions.put("place", place);
        
        List<String> time = new ArrayList();
        time.add("How long can you eat?");
        replyOptions.put("time", time);
    }
    
    public void normalize() {
        for (Map.Entry<String, String> entry: normalization.entrySet()) {
            for(int i = 0; i < processedInput.size(); i++) {
                processedInput.set(i, processedInput.get(i).replace(entry.getKey(), entry.getValue()));
            }
        }
    }
    
    public void processInput() throws FileNotFoundException {
        initialInput = initialInput.toLowerCase();
        initialInput = initialInput.replaceAll("\\s", " ");
        splitSentences();
        normalize();
    }
    
    public void splitSentences() throws FileNotFoundException {
        
        InputStream modelIn = null;
        SentenceModel model = null;
        SentenceDetectorME sentenceDetector = null;
        
        try {
            modelIn = new FileInputStream("C:\\Users\\ShenJingBing\\Desktop\\en-sent.bin");
            model = new SentenceModel(modelIn);
            sentenceDetector = new SentenceDetectorME(model);
            String sentences[] = sentenceDetector.sentDetect(initialInput);
            
            for(int i = 0; i < sentences.length; i++) {
                processedInput.add(sentences[i].substring(0, sentences[i].length()-1));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void converse() throws FileNotFoundException {
        Human man = new Human();
        boolean end = false;
        Scanner in = new Scanner(System.in);
        List<String> replyOption = new ArrayList();
        String regex = "";
        String sentence = "";
        
        while(!end) {
            initialInput = in.nextLine();
            processInput();
            
            for(int i = 0; i < processedInput.size(); i++) {
                sentence = processedInput.get(i);
                
                if(sentence.equalsIgnoreCase("bye")) {
                    output.add("Bye.");
                    end = true;
                }
              
                else if(sentence.equalsIgnoreCase("i am hungry")) {
                    if(man.isHungry() == null) {
                        man.setHungry(true);
                        output.add(("Ok."));
                    }
                    else if(man.isHungry()) {
                        output.add(("You already told me before."));
                    } else {
                        man.setHungry(true);
                        output.add(("You were full before."));
                    }
                }
                
                else if(sentence.equalsIgnoreCase("i am not hungry") ||
                        sentence.equalsIgnoreCase("I am full")) {
                    if(man.isHungry() == null) {
                        man.setHungry(false);
                        output.add(("Ok."));
                    }
                    else if(man.isHungry()) {
                        man.setHungry(false);
                        output.add(("You were hungry before."));
                    } else {
                        output.add(("You already told me before."));
                    }
                }
                
                else if(sentence.matches("i want (.*) cuisine")) {
                    if(man.isHungry() != null && man.isHungry()) {
                        man.setCuisine(sentence.split(" ")[2]);
                        output.add("I want " + sentence.split(" ")[2] + " cuisine too!");
                    } else {
                        output.add("Ok.");
                    }
                }
            }
            
            if(!end) {
                if(man.isHungry() == null) {
                    output.add("Are you hungry?");
                }
                else {
                    if(man.getCuisine() == null) {
                        replyOption.addAll(replyOptions.get("cuisine"));
                    }
                    if(man.getBudget() == null) {
                        replyOption.addAll(replyOptions.get("budget"));
                    }
                    if(man.getDuration() == null) {
                        replyOption.addAll(replyOptions.get("time"));
                    }
                    if(man.getPlace() == null) {
                        replyOption.addAll(replyOptions.get("place"));
                    }
                }

                if(replyOption.size() > 0) {
                    Random rand = new Random();
                    int n = rand.nextInt(replyOption.size()) + 0;
                    output.add(replyOption.get(n));
                }

                replyOption.clear();
            }
            
            for(int i = 0; i < output.size(); i++) {
                if(i == 0) {
                    System.out.print(output.get(i));
                } else {
                    System.out.print(" " + output.get(i));
                }
                
            }
            
            System.out.println();
            output.clear();
            processedInput.clear();
        }
        
    }
  
}

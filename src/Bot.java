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
import java.util.LinkedHashMap;
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
    private List<Restaurant> restaurants;
    
    public Bot() {
        initialInput = "";
        processedInput = new ArrayList<String>();
        output = new ArrayList<String>();
        normalization = new HashMap<String, String>();
        replyOptions = new HashMap<String, List<String>>();
        restaurants = new ArrayList<Restaurant>();
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
        List<String> cuisine = new ArrayList<String>();
        cuisine.add("What cuisine you want to eat?");
        replyOptions.put("cuisine", cuisine);
        
        List<String> place = new ArrayList<String>();
        place.add("Where do you want to eat?");
        replyOptions.put("place", place);
        
        List<String> budget = new ArrayList<String>();
        budget.add("How much is your budget?");
        replyOptions.put("budget", budget);
        
        List<String> time = new ArrayList<String>();
        time.add("How long can you eat?");
        replyOptions.put("duration", time);
        
        restaurants.add(new Restaurant("Chinese", (Double)150.0, "Agno", 30, "Wai Ying"));
        restaurants.add(new Restaurant("Filipino", (Double)500.0, "Taft", 60, "Bascilogan"));
        restaurants.add(new Restaurant("Spanish", (Double)250.0, "Agno", 30, "Something Spanish"));
        
        
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
        LinkedHashMap<String, List<String>> replyOption = new LinkedHashMap();
        String regex = "";
        String sentence = "";
        String previousQuestion = "";
        List<Restaurant> possibleRestaurants = new ArrayList();
        Random rand = new Random();
        
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
                	if(!previousQuestion.equalsIgnoreCase("cuisine")) {
                		output.add("I did not ask you that.");
                	}
                    if(man.isHungry() != null && man.isHungry()) {
                    	if(man.getCuisine() != null && man.getCuisine().equals(sentence.split(" ")[2])) {
                    		output.add("You already told me.");
                    	} 
                    	else {
                    		output.add("I want " + sentence.split(" ")[2] + " cuisine too!");
                    	}
                        man.setCuisine(sentence.split(" ")[2]);
                    } else {
                        output.add("Ok.");
                    }
                }
                
                else if(sentence.matches("i want to eat at (.*)")) {
                	if(!previousQuestion.equalsIgnoreCase("place")) {
                		output.add("I did not ask you that.");
                	}
                	if(man.isHungry() != null && man.isHungry()) {
                    	if(man.getPlace() != null && man.getPlace().equals(sentence.split(" ")[5])) {
                    		output.add("You already told me.");
                    	} 
                    	else {
                    		output.add("I know restaurants in " + sentence.split(" ")[5] + "!");
                    	}
                        man.setPlace(sentence.split(" ")[5]);
                    } else {
                        output.add("Ok.");
                    }
                }
                
                else if(sentence.matches("my budget is (.*)")) {
                	if(!previousQuestion.equalsIgnoreCase("budget")) {
                		output.add("I did not ask you that.");
                	}
                	if(man.isHungry() != null && man.isHungry()) {
                    	if(man.getBudget() != null && man.getBudget() == Float.parseFloat(sentence.split(" ")[3])) {
                    		output.add("You already told me.");
                    	} 
                    	else {
                    		output.add("I know restaurants that worth around " + sentence.split(" ")[3] + "!");
                    	}
                        man.setBudget(Double.parseDouble(sentence.split(" ")[3]));
                    } else {
                        output.add("Ok.");
                    }
                }
                
                else if(sentence.matches("i need (.*) minutes")) {
                	if(!previousQuestion.equalsIgnoreCase("duration")) {
                		output.add("I did not ask you that.");
                	}
                	if(man.isHungry() != null && man.isHungry()) {
                    	if(man.getDuration() != null && man.getDuration() == Integer.parseInt(sentence.split(" ")[2])) {
                    		output.add("You already told me.");
                    	} 
                    	else {
                    		output.add("I know restaurants that serves around " + sentence.split(" ")[2] + "!");
                    	}
                        man.setDuration(Integer.parseInt(sentence.split(" ")[2]));
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
                    	//System.out.println("cuisine");
                        replyOption.put("cuisine", replyOptions.get("cuisine"));
                    }
                    if(man.getDuration() == null) {
                    	//System.out.println("time");
                    	replyOption.put("duration", replyOptions.get("duration"));
                    }
                    if(man.getPlace() == null) {
                    	//System.out.println("place");
                    	replyOption.put("place", replyOptions.get("place"));
                    }
                    if(man.getBudget() == null) {
                    	//System.out.println("budget");
                    	replyOption.put("budget", replyOptions.get("budget"));
                    }
                }
                
                if(replyOption.size() > 0) {
                    int n = rand.nextInt(replyOption.size());
                    int counter = 0;
                    for(Map.Entry<String, List<String>> entry: replyOption.entrySet()) {
                    	if(counter == n) {
                    		//System.out.println(entry.getKey());
                    		int m = rand.nextInt(replyOption.get(entry.getKey()).size());
                    		previousQuestion = entry.getKey();
                    		//System.out.println(m);
                    		output.add(replyOption.get(entry.getKey()).get(m));
                    		break;
                    	}
                    	counter++;
                	}
                }
                replyOption.clear();

            }
            
            if(man.isHungry() && man.getBudget() != null && man.getCuisine() != null && man.getDuration() != null
            		&& man.getPlace() != null && !end) {
            	
            	//System.out.println(man.getBudget() + ", " + man.getCuisine() + ", " + man.getDuration() + ", " + man.getPlace());
            	
            	for(int i = 0; i < restaurants.size(); i++) {
            		if(restaurants.get(i).match(man.getCuisine(), (Double)man.getBudget(), man.getPlace(), man.getDuration())) {
            			possibleRestaurants.add(restaurants.get(i));
            		}
            	}
            	
            	if(possibleRestaurants.size() > 0) {
                    int n = rand.nextInt(possibleRestaurants.size());
                    output.add("You can eat at " + possibleRestaurants.get(n).getName());
            	}
            	else {
            		output.add("Sorry, I cannot suggest restaurants matches all your preference.");
            	}
            	
            	possibleRestaurants.clear();
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

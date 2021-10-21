import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class SecretSantaVersion3{
    public static ArrayList<String> names = new ArrayList<String>(); //ArrayList of players names
    public static Scanner scnr = new Scanner(System.in);
    public static String input;
	
	public static void main(String [] args){
            
      System.out.println("Ho, ho, ho! Welcome to the Secret Santa Generator!");
      boolean loop = true;
      
      while(loop){
          //program sorts names whenever the program reenters this loop with more than one player
    	  if(names.size()>1){
              Collections.sort(names);
          }
          
    	  System.out.println("Enter a unique name or \"Go\" if done. Type \"Players\" to see who is in so far, or \"Remove\" to remove a participant.");
          input = scnr.nextLine();
          
          //this if statement prevents the program from crashing from the input either being empty or only being a space character
          if(input.length() == 0 || input.equals(" ")) {
        	  continue;
          }
          
          //this if statement prevents the program from crashing because the user's input ended with a space character
          if(input.charAt(input.length()-1) == ' ') {
        	  input = input.substring(0, input.length()-1);
          }
          input = input.toLowerCase();
          
          switch(input){
              case "go":
            	  if(names.size()<3) {
            		  System.out.println("Not enough players!");
            		  continue;
            	  }
                  loop = false;
                  break;
              case "players":
                  players();
            	  break;
              case "remove":
                  remove();
                  break;
              default:
                  insert();    
          }   
      }
      
      //For the code below, it assigns every player a number and then prints the number of the person they're giving to.
      //I did it this way so whomever is hosting does not immediately see their name attached to their Secret Santa.
      
      int giftees[] = new int[names.size()];
      
      Random randGen = new Random();
      
      for(int i=0; i<giftees.length;i++){
          int result = randGen.nextInt(names.size())+1;
          giftees[i] = result;
          for(int j=0;j<i;j++){
             if(result == giftees[j]){ //Makes sure every number is unique
                 giftees[i]=0;
                 i--;
             }
          }
      }
      
      //This portion of code assigns players a second number representing the person they are giving to
      
      int givers[] = new int[names.size()];
      for(int i=0; i<names.size();i++){
          int whomst = randGen.nextInt(names.size())+1;
          givers[i] = whomst;
          if(givers[i]==giftees[i]){ //Makes sure every assigned number is unique
              i--;
              continue;
          }
          for(int j=0;j<i;j++){
             if(whomst == givers[j]){ //This makes sure no one gets their own number
                 givers[i]=0; //soft reset for a player if they received their own number to be reassigned
                 i--;
             }
          }
      }
      
      System.out.println("Making files for results");
      
      try {
		  PrintWriter numbers = new PrintWriter(new FileOutputStream("Secret Santa Assigned Numbers (1st File).txt"));
		  PrintWriter assigned = new PrintWriter(new FileOutputStream("Secret Santa Giving To (2nd File).txt"));
		  
		  numbers.println(names.size() + " participants.\n");
	      numbers.println("Here are everyone's numbers. Ho!\n");
	      for(int i=0;i<names.size();i++){
	          numbers.println(names.get(i) + ": " + giftees[i]);
	      }
	      
	      assigned.println(names.size() + " participants.\n");
	      assigned.println("Who you give the ho to:");
	      for(int i=0;i<names.size();i++){
	          assigned.println(names.get(i) + ": " + givers[i]);
	      }
	      
	      System.out.println("Results file made! Check code folder.");
	      
	      numbers.close();
	      assigned.close();
	  }
	  catch(FileNotFoundException e){
		  System.out.println("Cannot make file");
		  System.exit(0);
	  }
      
  }
	
	
	public static void players() {
		if(names.size()==0) {
			System.out.println("There are no players yet.");
			return;
		}
		else {
			System.out.println("Total participants: " + names.size());
	        for(int i=0;i<names.size()-1;i++){
	            System.out.print(names.get(i) + ", ");
	        }
	        System.out.println(names.get(names.size()-1));
		}
	}
	
	
	public static void remove(){
		if(names.size() == 0){
			System.out.println("You need to have some players first!");
			return;
		}
		else{
			int fail = 0;
			for(int i=0; i<names.size()-1;i++){
				System.out.print(names.get(i) + ", ");
				names.set(i,names.get(i).toLowerCase()); //Names accepted in the ArrayList receive a capital letter (shown below), so this reverts them back to lower case
			}
			System.out.println(names.get(names.size()-1));
	        
			names.set(names.size()-1,names.get(names.size()-1).toLowerCase());
	        System.out.println("Who would you like to remove?");
	        String delete = scnr.nextLine();
	        delete = delete.toLowerCase();
	        for(int i=0; i<names.size(); i++){
	            if(delete.equals(names.get(i))){
	                fail = 1;
	                names.remove(i);
	                System.out.println("Player deleted");
	                break;
	            }
	        }
	        
	        if(fail==0){
	            System.out.println("Player not found");
	        }
	        
	        capital();
	        if(names.size()>0) {
	        	players();
	        }
		}
	}
	
	
	public static void insert() {
		if(names.size() == 0) {
			System.out.println("Player added");
			names.add(input);
		}
		else{
			for(int i=0; i<names.size();i++) {
				names.set(i,names.get(i).toLowerCase());
			}
			boolean test = true;
            for(int i=0;i<names.size();i++){ //Does not allow for two of the same name
                if(input.equals(names.get(i))){
                    System.out.println("Name already entered! Try another.");
                    test = false;
                    continue;
                }
            }
            if(test) {
            	names.add(input);
            	System.out.println("Player added");
            }
        }
		
		capital(); //call to capital method to set names with proper case value
	}
	
	
	public static void capital() {
		for(int i=0; i<names.size(); i++) {
			for(int j=0; j<names.get(i).length(); j++) {
				if(names.get(i).charAt(j)== ' ') {
					String temp = names.get(i).substring(j+1);
					names.set(i, names.get(i).substring(0,j+1) + temp.substring(0,1).toUpperCase() + temp.substring(1));
					continue;
				}
				else {
					names.set(i, names.get(i).substring(0,1).toUpperCase() + names.get(i).substring(1));
				}
			}
		}
	}

}

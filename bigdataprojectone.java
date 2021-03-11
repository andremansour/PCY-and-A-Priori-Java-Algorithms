
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.IOException;






public class bigdataprojectone {

    public static void APrioriAlgorithm(ArrayList<ArrayList<Integer>> DATAHOLD, int supportthresh, int datanum) throws FileNotFoundException
	{
		// two dimensional array for multi level data for frequent pair
		int[][] pairfrq = new int[2500][2];

        // storing the frequency of each item
		int[] countsarr = new int[20000];
	
        // creating our frequent item array list to store frequent items
		ArrayList<Integer> countfreq = new ArrayList<Integer>();
		
		// retrieving the frequencies of each item within the data set 
		for(int i = 0; i < DATAHOLD.size(); i++) { // loop through array list 
            
			for(int j = 0; j < DATAHOLD.get(i).size(); j++) { // during loop we return every elements position within the entire list 
				
                countsarr[DATAHOLD.get(i).get(j)]++; // we store these elements inside of our frequency array
			}
		}
		
		// determining if the item is frequent within the item set
		
        for(int i = 0; i < countsarr.length; i++) {  // we go through our array with our stored items 
			
            if(countsarr[i] > supportthresh) { // if our item within our array is greater than the support 
				
                countfreq.add(i); // we add the item to our frequent item list 
			}
		}
        
		 
		
		PrintWriter writer = new PrintWriter("results-APrioriAlgorithm.txt");
		
		// PASS 2: Search algorithm for looking for frequent pairs in each basket
		
        int amtofpairs = 0;
		
        boolean prespair = false;
		

        // loop through data    
		for(int i = 0; i < datanum; i++) {
				
			// loop through new list
            for(int j = 0; j < countfreq.size(); j++)
				
				// if we return elements that are contained within countfreq
                if(DATAHOLD.get(i).contains(countfreq.get(j))) {
					
					// go through the new list and for frequent pairs but do not re count only count pairs one at a time 
                    for(int k = countfreq.size() - 1; k > j; k--) {
						
						if(DATAHOLD.get(i).contains(countfreq.get(k)) && DATAHOLD.get(i).contains(countfreq.get(j))) {
							

							for(int l = 0; l < 2450; l++) {
								
								if(((pairfrq[l][0] == countfreq.get(j) && pairfrq[l][1] ==  countfreq.get(k)) ||  (pairfrq[l][1] == countfreq.get(j) && pairfrq[l][0] ==  countfreq.get(k)))) {
									prespair = true;
								}
							}
							

							if(!prespair && amtofpairs < 2500) {

								pairfrq[amtofpairs][0] = countfreq.get(j);

								pairfrq[amtofpairs][1] = countfreq.get(k);
                                
								writer.println("Pair No.: "+amtofpairs +" --> " +pairfrq[amtofpairs][0] +" "+pairfrq[amtofpairs][1]);
								
                                amtofpairs++; // Increments the number of pairs in variable (amtofpairs) after writing to file
							}
							
							prespair = false;
							
					}
				}
			}
		}
		writer.close();	
	}



	// PCYAlgorithm Algorithm
	public static void PCYAlgorithm(ArrayList<ArrayList<Integer>> DATAHOLD, int supportthresh, int datanum) throws FileNotFoundException{
		
		// creating an array for the frequency of each item 
		int[] countsarr = new int[20000];
        
        // creating an arraylist to count frequent items
		ArrayList<Integer> countfreq = new ArrayList<Integer>();
		
		
        
        int ourhash = 20000;
		// Mapping all the elements in order to find their length.Mapping all the elements in order to find their length.
		Map<Integer, Integer> PCYHASH = new HashMap<Integer, Integer>(); // using map to store key value pairs, each key can map to at most one value so this will ensure there is no duplicates
		

		//PART 1 OBTAIN THE FREQUENCIES OF EACH ITEM 
        // loop through our entire dataset
		for(int i = 0; i < datanum; i++) {

			for(int j = 0; j < DATAHOLD.get(i).size(); j++) {

				countsarr[DATAHOLD.get(i).get(j)]++;

			}

		}
				
	    // Storing all the frequent items in countfreq array list
		for(int i = 0; i < countsarr.length; i++) {

			if(countsarr[i] > supportthresh) {

				countfreq.add(i);
			}
		}
		
		// Applying Hash Function to give us the basket numbers 
		for(int basket = 0; basket < datanum; basket++) {

			for(int i = 0; i < DATAHOLD.get(basket).size(); i++) {

				for(int j = i + 1; j < DATAHOLD.get(basket).size(); j++) {

					int itemI = DATAHOLD.get(basket).get(i);

					int itemJ = DATAHOLD.get(basket).get(j);
					
					int valhash = (itemI + itemJ) % ourhash;
					
					if(PCYHASH.containsKey(valhash)) {

						int n = PCYHASH.get(valhash);

						n++;

						PCYHASH.put(valhash, n);
					}
                    
					else {

						PCYHASH.put(valhash, 1);

					}
				}
			}
		}
		
		// PASS 1 - Part 2: Converting hashmap to bitmap 
		Map<Integer, Boolean> bitmap = new HashMap<Integer, Boolean>();
		
		for(Integer k: PCYHASH.keySet()) {
			if(PCYHASH.get(k) > supportthresh) {
				bitmap.put(k, true);
			}
			else {
				bitmap.put(k, false);
			}
		}
		
		// PASS 2
		
		//HashMap<Integer, Integer> freqHash = new HashMap<Integer, Integer>();
		
		ArrayList<ArrayList<Integer>> numfrqpair = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> pairstemp = new ArrayList<Integer>();

			for(int i = 0; i < countfreq.size(); i++) {
				for(int j = i + 1; j < countfreq.size(); j++) {
					
					int itemI = countfreq.get(i);

					int itemJ = countfreq.get(j);
					
					int hashfrqval = (itemI+itemJ) % ourhash;
					
					if(bitmap.get(hashfrqval)) {

						pairstemp.add(itemI);

						pairstemp.add(itemJ);
                        
						numfrqpair.add(pairstemp);

						pairstemp = new ArrayList<Integer>();
					}
				}
			}
			
		for(int i = 0; i < numfrqpair.size(); i++) {

			System.out.println("Pair #"+i+": "+numfrqpair.get(i).get(0) +" "+numfrqpair.get(i).get(1));

		}
	


	// PASS 3 - PART OF MULTISTAGE
	// implemtenting multistage PCY 

	Map<Integer, Boolean> bitmap_ = new HashMap<Integer, Boolean>();
		
		for(Integer k: PCYHASH.keySet()) {
			if(PCYHASH.get(k) > supportthresh) {
				bitmap_.put(k, true);
			}
			else {
				bitmap_.put(k, false);
			}
		}



	ArrayList<ArrayList<Integer>> numfrqpairS = new ArrayList<ArrayList<Integer>>();
		
	ArrayList<Integer> pairstempS = new ArrayList<Integer>();

		for(int i = 0; i < countfreq.size(); i++) {
			for(int j = i + 1; j < countfreq.size(); j++) {
				
				int itemI = countfreq.get(i);

				int itemJ = countfreq.get(j);
				
				int hashfrqval = (itemI+itemJ) % ourhash;
				
				if(bitmap.get(hashfrqval)) {

					pairstempS.add(itemI);

					pairstempS.add(itemJ);
					
					numfrqpairS.add(pairstempS);

					pairstempS = new ArrayList<Integer>();
				}
			}
		}
		
	for(int i = 0; i < numfrqpair.size(); i++) {

		System.out.println("Pair #"+i+": "+numfrqpairS.get(i).get(0) +" "+numfrqpairS.get(i).get(1));

	}
	
}

	// PCY Multi-Hash Algorithm Algorithm
	public static void PCYAlgorithmmultihash(ArrayList<ArrayList<Integer>> DATAHOLD, int supportthresh, int datanum) throws FileNotFoundException{
		//Modify the first pass of the PCY to divide available main memory into two (or several) hash tables

		// creating an array for the frequency of each item 
		int[] countsarr = new int[20000];
        
        // creating an arraylist to count frequent items
		ArrayList<Integer> countfreq = new ArrayList<Integer>();
		
		// creating an array for the frequency of each item 
		int[] countsarrA = new int[20000];
	
		// creating an arraylist to count frequent items
		ArrayList<Integer> countfreqA = new ArrayList<Integer>();


        
        int ourhash = 20000;
		// Mapping all the elements in order to find their length.Mapping all the elements in order to find their length.
		Map<Integer, Integer> PCYHASH = new HashMap<Integer, Integer>(); // using map to store key value pairs, each key can map to at most one value so this will ensure there is no duplicates
		

		//PART 1 OBTAIN THE FREQUENCIES OF EACH ITEM 
        // loop through our entire dataset
		for(int i = 0; i < datanum; i++) {

			for(int j = 0; j < DATAHOLD.get(i).size(); j++) {

				countsarr[DATAHOLD.get(i).get(j)]++;
				countsarrA[DATAHOLD.get(i).get(j)]++;

			}

		}
				
	    // Storing all the frequent items in countfreq array list
		for(int i = 0; i < countsarr.length; i++) {

			if(countsarr[i] > supportthresh) {

				countfreq.add(i);
			}
		}

		// Storing all the frequent items in countfreqA array list
		for(int i = 0; i < countsarrA.length; i++) {

			if(countsarrA[i] > supportthresh) {

				countfreqA.add(i);
			}
		}
		
		// Applying Hash Function to give us the basket numbers 
		for(int basket = 0; basket < datanum; basket++) {

			for(int i = 0; i < DATAHOLD.get(basket).size(); i++) {

				for(int j = i + 1; j < DATAHOLD.get(basket).size(); j++) {

					int itemI = DATAHOLD.get(basket).get(i);

					int itemJ = DATAHOLD.get(basket).get(j);
					
					int valhash = (itemI + itemJ) % ourhash;
					
					if(PCYHASH.containsKey(valhash)) {

						int n = PCYHASH.get(valhash);

						n++;

						PCYHASH.put(valhash, n);
					}
                    
					else {

						PCYHASH.put(valhash, 1);

					}
				}
			}
		}
		
		// PASS 1 - Part 2: Converting hashmap to bitmap 
		Map<Integer, Boolean> bitmap = new HashMap<Integer, Boolean>();
		
		for(Integer k: PCYHASH.keySet()) {
			if(PCYHASH.get(k) > supportthresh) {
				bitmap.put(k, true);
			}
			else {
				bitmap.put(k, false);
			}
		}
		
		// PASS 2
		
		//HashMap<Integer, Integer> freqHash = new HashMap<Integer, Integer>();
		
		ArrayList<ArrayList<Integer>> numfrqpair = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> pairstemp = new ArrayList<Integer>();

			for(int i = 0; i < countfreq.size(); i++) {
				for(int j = i + 1; j < countfreq.size(); j++) {
					
					int itemI = countfreq.get(i);

					int itemJ = countfreq.get(j);
					
					int hashfrqval = (itemI+itemJ) % ourhash;
					
					if(bitmap.get(hashfrqval)) {

						pairstemp.add(itemI);

						pairstemp.add(itemJ);
                        
						numfrqpair.add(pairstemp);

						pairstemp = new ArrayList<Integer>();
					}
				}
			}
			
		for(int i = 0; i < numfrqpair.size(); i++) {

			System.out.println("Pair #"+i+": "+numfrqpair.get(i).get(0) +" "+numfrqpair.get(i).get(1));

		}
	


	// PASS 3 - PART OF MULTISTAGE
	// implemtenting multistage PCY 

	Map<Integer, Boolean> bitmap_ = new HashMap<Integer, Boolean>();
		
		for(Integer k: PCYHASH.keySet()) {
			if(PCYHASH.get(k) > supportthresh) {
				bitmap_.put(k, true);
			}
			else {
				bitmap_.put(k, false);
			}
		}



	ArrayList<ArrayList<Integer>> numfrqpairS = new ArrayList<ArrayList<Integer>>();
		
	ArrayList<Integer> pairstempS = new ArrayList<Integer>();

		for(int i = 0; i < countfreq.size(); i++) {
			for(int j = i + 1; j < countfreq.size(); j++) {
				
				int itemI = countfreq.get(i);

				int itemJ = countfreq.get(j);
				
				int hashfrqval = (itemI+itemJ) % ourhash;
				
				if(bitmap.get(hashfrqval)) {

					pairstempS.add(itemI);

					pairstempS.add(itemJ);
					
					numfrqpairS.add(pairstempS);

					pairstempS = new ArrayList<Integer>();
				}
			}
		}
		
	for(int i = 0; i < numfrqpair.size(); i++) {

		System.out.println("Pair #"+i+": "+numfrqpairS.get(i).get(0) +" "+numfrqpairS.get(i).get(1));

	}
	
}

    public static ArrayList<ArrayList<Integer>> THEPROCESSOR(int numstotal) throws FileNotFoundException {
		
		 // creating our arraylist for our data 
        ArrayList<String> ourdata = new ArrayList<String>();
		
        // Saving all items in each basket within this array 
		int[] sizeofrow = new int[88162];
		
		
        Scanner scannn = new Scanner(new File("retail.txt"));
		

        // While loop will scan each one of the line as strings back to the ourdata ArrayList
        
        while(scannn.hasNextLine()){

			ourdata.add(scannn.nextLine());

		}

		scannn.close();
		
		// Calculates how many items are in each basket
		for(int i = 0; i < numstotal; i++) {

			String[] rowtempp = ourdata.get(i).split(" ");
            
				sizeofrow[i] = rowtempp.length;

		}
     
		
		Scanner scannnn = new Scanner(new File("retail.txt"));
        
        // DATAHOLD arraylist this will contain all our data
        ArrayList<ArrayList<Integer>> DATAHOLD = new ArrayList<ArrayList<Integer>>(); 
		
        // our data temp list, used for tranfer of each rows data 
        ArrayList<Integer> inside = new ArrayList<Integer>(); 
 		
		// loop used to save all data within our Array List 
		for(int i = 0; i < numstotal; i++) {

			for(int j = 0; j < sizeofrow[i]; j++) {

					inside.add(scannnn.nextInt());

			}

			DATAHOLD.add(inside);

			inside = new ArrayList<Integer>();
		}
		
		scannnn.close();

		return DATAHOLD;
	}


    public static void main(String[] args) throws IOException {
    
		 
		try
		{	

            float supp[] = {1, 5, 10};
            float pers[] = {1, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    
            for(int i = 0; i<supp.length; i++) {
    
                for(int n = 0; n < pers.length; n++) {
    
                    float perc = pers [n];
    
                    float suppo = supp [i]; 


                System.out.println("The file is found");
                
                int numstotal = 88162;
                

                // data collection of each of our samples 

                // float samplepercent = 1;

                int datanum = (int) Math.floor((perc/100)*numstotal);
                
                
                //support calculations for our samples 

                // float supportenduser = 1;
                
                int supportthresh = (int) Math.floor((suppo/100)*datanum); 
                
                ArrayList<ArrayList<Integer>> thefulldata = THEPROCESSOR(numstotal);
                


                // CPU TIMER PCYAlgorithm -- comment out third pass for multistage !!

                PrintWriter writer1 = new PrintWriter(new FileOutputStream(new File("runTimes-PCYAlgorithm.txt"), true));
                
                long timerstart = System.currentTimeMillis(); // CPU Timer START 
                            
                PCYAlgorithm(thefulldata, supportthresh, datanum);
                            
                long timertask = System.currentTimeMillis() - timerstart; // CPU Timer END
                            
                writer1.append("\nSupport = " +suppo +" Percent = "+perc  +"% RUNTIME = "+timertask);
                            
                writer1.close();


				// CPU TIMER PCY multihash Algorithm

				PrintWriter writerr = new PrintWriter(new FileOutputStream(new File("runTimes-PCYAlgorithm-multihash.txt"), true));
                
				long timerstartt = System.currentTimeMillis(); // CPU Timer START 
										
				PCYAlgorithm(thefulldata, supportthresh, datanum);
										
				long timertaskk = System.currentTimeMillis() - timerstartt; // CPU Timer END
										
				writerr.append("\nSupport = " +suppo +" Percent = "+perc  +"% RUNTIME = "+timertaskk);
										
				writerr.close();
                
                
                // A-Priori CPU TIMER
                
                PrintWriter writer = new PrintWriter(new FileOutputStream(new File("runTimes-APrioriAlgorithm.txt"), true));
            
                long startTime = System.currentTimeMillis(); // CPU Timer START 
                
                APrioriAlgorithm(thefulldata, supportthresh, datanum);
                
                long taskTime = System.currentTimeMillis() - startTime; // CPU Timer END
                
                writer.append("\nSupport = " +suppo +" Percent = "+perc  +"% RUNTIME = "+taskTime);
                    
                writer.close();
    
                }
            }

            //establish our supports 
            // supports = {1, 5, 10}
            // percents = {1, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 }

            //establish our percents 
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found!");
		}
		
	}
}
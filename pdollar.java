package pdollar_java;

import java.util.Arrays;

import java.util.List;
import java.io.*;

import java.util.ArrayList;


public class pdollar {
    
    
    int state_aish = GESTURE_PROCESSED;
	static final int GESTURE_PROCESSED_aish = 0;
    static final int STROKE_COMPLETE_aish = 2;
    static final int STROKE_IN_PROGRESS_aish = 1;
    int _currentStrokeId_aish = 0;
	static final String DEFAULT_USER_DEFINED_STRING_aish = "Type name here...";

    public static void main(String args[]) throws Exception {
        PDollarRecognizer pDollarR=new PDollarRecognizer();
		
        File file = new File("save.txt");
        BufferedWriter outputWriter = null;
        FileWriter fw;
		
        fw = new FileWriter(file,true);
        
		outputWriter = new BufferedWriter(fw);

           try {
               if(args.length==1)
               {
                   System.out.println("Please enter the correct arguments");
                   System.out.println("Usage:");
                   System.out.println("pdollar –t <gesturefile>: Adds the gesture file to the list of gesture templates");
                   System.out.println("pdollar –r: Clears the templates");
                   System.out.println("pdollar <eventstream>: Prints the name of gestures as they are recognized from the event stream.");

                   System.exit(0);

               }
               else {
                   if (args[1].equalsIgnoreCase("-r")) {

                       FileOutputStream writer = new FileOutputStream("save.txt");
                   } else if (args[1].equalsIgnoreCase("-t")) {
                       BufferedReader br = new BufferedReader(new FileReader(args[2]));
                       try {
                           String gestureName = br.readLine();
                           outputWriter.append(gestureName + "\n");
                          
                           int Stroke_id = 0;
                           String line = " ";
                           while ((line = br.readLine()) != null) {
                               if (line.equalsIgnoreCase("BEGIN")) {

                                   Stroke_id++;
                               } else if (line.equalsIgnoreCase("END")) {

                                  
                               } else {
                                   outputWriter.append(line + "," + Stroke_id + "\n");
                               }
                           }
                           outputWriter.append( "*****\n");

                       } finally {
                           br.close();
                           outputWriter.close();
                       }
                   } else {

                       BufferedReader br = new BufferedReader(new FileReader(args[1]));

                       BufferedReader gestureFileReader = new BufferedReader(new FileReader("save.txt"));
                       ArrayList<Point> eventList = new ArrayList<Point>();

                       try {
                           int Stroke_id = 0;
                           String line = " ";
                           while ((line = br.readLine()) != null) {
                               if (line.equalsIgnoreCase("MOUSEDOWN")) {
                                   Stroke_id++;

                               } else if (line.equalsIgnoreCase("MOUSEUP")) {
                                   
                               } else if (line.equalsIgnoreCase("RECOGNIZE")) {
                                  
                               } else {
                                   String P[] = line.split(",");
                                   double x = Double.parseDouble(P[0]);
                                   double y = Double.parseDouble(P[1]);

                                   eventList.add(new Point(x, y, Stroke_id));
                               }
                           }
                       } finally {
                           br.close();
                       }
                       try {
                           String line = " ";
                           String gestureName=" ";
                           ArrayList<Point> gestureList = new ArrayList<Point>();
                               while ((line = gestureFileReader.readLine())!=null) {
                                   if (!line.contains(",")&& !line.contains("*")) {
                                       gestureList=new ArrayList<>();
                                       gestureName = line;

                                   }
                                   else if (line.contains("*")) {
                                       //System.out.println(gestureName);
                                       pDollarR.addGesture(gestureName, gestureList);
                                   }
                                   else {
                                       String P[] = line.split(",");
                                       double x = Double.parseDouble(P[0]);
                                       double y = Double.parseDouble(P[1]);
                                       int Stroke_id = Integer.parseInt(P[2]);
                                       gestureList.add(new Point(x, y, Stroke_id));
                                   }




                               }
                           pDollarR.Recognize(eventList);

                       } finally {
                           gestureFileReader.close();
                       }
                   }
               }
           } catch (Exception e) {
               System.out.println(e.getMessage());
               System.exit(1);
               return;
           }
    }
}
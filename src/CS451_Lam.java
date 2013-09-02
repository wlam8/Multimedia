import java.util.Scanner;

/*******************************************************
 CS451 Multimedia Software Systems
 @ Author: Elaine Kang
 *******************************************************/

// Template Code

public class CS451_Lam{  
	public static void main(String[] args) throws Exception{
		//TEST  
		//args = new String[]{"HW3_sample/red.ppm"};
	    //Image img = new Image(args[0]);
		
		//TEST2
		/*String testimage = "IDB/Walk_005.ppm";
	    Image test = new Image(testimage);
	    test.display("005");
		String testimage2 = "IDB/Walk_060.ppm";
	    Image test2 = new Image(testimage2);
	    test2.display("060");
		    
	    Scanner reader = new Scanner(System.in);
		Options.motionComp(reader);
	    Options.removeMotion(reader);
	    //System.exit(1);*/

	    
	    if(args.length != 1) hw2();
	    else hw1(args);

	    System.exit(1);
	}
	

	public static void hw1(String[] args){
	    Scanner reader = new Scanner(System.in);
	    int input = 1;
	    
	    while(true){
		    System.out.println("--Welcome to Multimedia Software System--");
		    System.out.println("Main Menu-----------------------------------");
		    System.out.println("1. Conversion to Gray-scale Image (24bits->8bits)");
		    System.out.println("2. Conversion to Bi-level Image (24bits->1bits)");
		    System.out.println("3. Conversion to Quad-level Image (24bits->2bits)");
		    System.out.println("4. Conversion to 8bit Indexed Color Image using Uniform Color Quantization (24bits->8bits)");
		    System.out.println("5. Convert to JPEG");
		    System.out.println("6. Quit");
		    System.out.println();
		    System.out.print("Please enter the task number [1-6]: ");
		
		    input = reader.nextInt();
		    if(input == 6) break;
		    
		    //Duplicate original image
		    Image img = new Image(args[0]);
		    img.write2PPM("out.ppm");
		    
		    //Display original image
		    //img.display(args[0]+"-out");
		    
		    Image output = new Image("out.ppm");
		    
		    switch (input) {
			    case 1:  output = Options.conv1(output);
			             break;
			    case 2:  output = Options.conv2(output, reader);
			             break;
			    case 3:  output = Options.conv3(output);
			             break;
			    case 4:  output = Options.conv4(output, args[0]);
	             		 break;
			    case 5:  output = Options.toJpeg(output, reader);
	             		 break;

			    default: break;
		    }
		    
		    //Display new image
		    output.write2PPM("out.ppm");
		    output.display("out.ppm");
	    }
	    reader.close();
	
	    System.out.println("--Good Bye--");  
	}

	private static void hw2() throws Exception {
	    Scanner reader = new Scanner(System.in);
	    int input = 1;
	    
	    while(true){
		    System.out.println("--Welcome to Multimedia Software System--");
		    System.out.println("Main Menu-----------------------------------");
		    System.out.println("1. Aliasing");
		    System.out.println("2. Dictionary Coding");
		    System.out.println("3. Block-Based Motion Compensation");
		    System.out.println("4. Removing Moving Objects");
		    System.out.println("5. Quit");
		    System.out.println();
		    System.out.print("Please enter the task number [1-5]: ");
		
		    input = reader.nextInt();
		    if(input == 5) break;
		    
		    switch (input) {
			    case 1:  Options.aliasing(reader);
			    			break;
			    case 2:  Options.dictionary(reader);
			 	 			break;
			    case 3:  Options.motionComp(reader);
			 	 			break;
			    case 4:  Options.removeMotion(reader);
			 	 			break;
			    default: break;
		    }
	    }
	    reader.close();
	
	    System.out.println("--Good Bye--");  		
	}

	public static void usage(){
		System.out.println("\nUsage: java CS451_Lam [inputfile]\n");
	}
}
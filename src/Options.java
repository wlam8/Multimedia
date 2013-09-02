import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Options {
	static Image conv1(Image output) {
	    //For each pixel, change its color
	    for(int x = 0; x < output.getW(); x++){
	        for(int y = 0; y < output.getH(); y++){
	        	//Get pixel color
	        	int[] rgb = new int[3];
	        	output.getPixel(x, y, rgb);
	        	
	        	//Convert pixel color to gray scale
	        	int gray = (int) Math.floor(0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);

	        	//Set pixel color
	        	output.setPixel(x, y, new int[]{gray,gray,gray});
	        }
	    }
	    return output;
	}
  
	static Image conv2(Image output, Scanner reader) {
	    System.out.println("1. Bi-level using threshold");
	    System.out.println("2. Bi-level using error-diffusion");
	    System.out.println();
	    System.out.print("Please enter the task number [1-2]: ");
	    int input = reader.nextInt();

	    //Convert using threshold
		if(input == 1){
			//Convert to grey scale
			output = conv1(output);
			
			//Get Ga, avg gray scale of all pixels
			int ga = 0;
		    for(int x = 0; x < output.getW(); x++){
		        for(int y = 0; y < output.getH(); y++){
		        	int[] rgb = new int[3];
		        	output.getPixel(x, y, rgb);
		        	ga += rgb[0];
		        }
		    }
		    ga = ga/(output.getH()*output.getW());

		    //For each pixel, change its color
		    for(int x = 0; x < output.getW(); x++){
		        for(int y = 0; y < output.getH(); y++){
		        	int[] rgb = new int[3];
		        	output.getPixel(x, y, rgb);
		        	int gr = rgb[0];
		        	
		        	//Set pixel color
		        	if(gr > ga){
			        	output.setPixel(x, y, new int[]{255,255,255});
		        	}else{
			        	output.setPixel(x, y, new int[]{0,0,0});
		        	}
		        }
		    }
		}
		
	    //Convert using error-diffusion
		if(input == 2){
			//Convert to grey scale
			output = conv1(output);
			
		    //For each pixel, change its color
			for(int y = 0; y < output.getH(); y++){
				for(int x = 0; x < output.getW(); x++){
		        	//Get pixel color
		        	int[] rgb = new int[3];
		        	output.getPixel(x, y, rgb);
		        	int pixel = rgb[0];
		        	
		        	//Calculate quantization error
		        	//Set pixel color
		        	int e = 0;
		        	if(pixel <= 127){
			        	output.setPixel(x, y, new int[]{0,0,0});
		        		e = pixel - 0;
		        	}else if(pixel > 127){
			        	output.setPixel(x, y, new int[]{255,255,255});
		        		e = pixel - 255;
		        	}
		        	
		        	//Distribute e to nearest neighbors
		        	if(x+1 != output.getW())
		        		output.incrementPixel(x+1, y, new int[]{e*7/16,e*7/16,e*7/16});
		        	if(y+1 != output.getH()){
		        		output.incrementPixel(x, y+1, new int[]{e*5/16,e*5/16,e*5/16});
		        		if(x != 0)
			        		output.incrementPixel(x-1, y+1, new int[]{e*3/16,e*3/16,e*3/16});
		        		if(x+1 != output.getW())
			        		output.incrementPixel(x+1, y+1, new int[]{e*1/16,e*1/16,e*1/16});
		        	}
		        }
		    }
		}
		return output;
	}
  
	static Image conv3(Image output) {
		//Convert to grey scale
		output = conv1(output);
		
	    //For each pixel, change its color
		for(int y = 0; y < output.getH(); y++){
			for(int x = 0; x < output.getW(); x++){
	        	//Get pixel color
	        	int[] rgb = new int[3];
	        	output.getPixel(x, y, rgb);
	        	int pixel = rgb[0];
	        	
	        	//Calculate quantization error
	        	//Set pixel color
	        	int newPixel = Math.round(pixel / 85) * 85;
	        	int e = pixel - newPixel;
	        	output.setPixel(x, y, new int[]{newPixel,newPixel,newPixel});
	        	
	        	//Distribute e to nearest neighbors
	        	if(x+1 != output.getW())
	        		output.incrementPixel(x+1, y, new int[]{e*7/16,e*7/16,e*7/16});
	        	if(y+1 != output.getH()){
	        		output.incrementPixel(x, y+1, new int[]{e*5/16,e*5/16,e*5/16});
	        		if(x != 0)
		        		output.incrementPixel(x-1, y+1, new int[]{e*3/16,e*3/16,e*3/16});
	        		if(x+1 != output.getW())
		        		output.incrementPixel(x+1, y+1, new int[]{e*1/16,e*1/16,e*1/16});
	        	}
	        }
	    }
		return output;
	}

	static Image conv4(Image output, String inputFile) {
		//Generate lookup table
	    int[][] lut = new int[256][4];
	    int index = 0;
	    System.out.println("Index	R	G	B");
	    for(int rAxis = 0; rAxis < 8; rAxis++){
	        for(int gAxis = 0; gAxis < 8; gAxis++){
	            for(int bAxis = 0; bAxis < 4; bAxis++){
	            	lut[index][0] = index;
	            	lut[index][1] = rAxis*32+16;
	            	lut[index][2] = gAxis*32+16;
	            	lut[index][3] = bAxis*64+32;
	        	    System.out.print(lut[index][0] + "	");
	        	    System.out.print(lut[index][1] + "	");
	        	    System.out.print(lut[index][2] + "	");
	        	    System.out.println(lut[index][3]);
	        	    index++;
	            }
	        }
	    }

	    //Create index file
	    output.write2PPM(inputFile + "-index.ppm");
	    Image indexValueFile = new Image(inputFile + "-index.ppm");

		//Convert to index values
	    for(int x = 0; x < indexValueFile.getW(); x++){
	        for(int y = 0; y < indexValueFile.getH(); y++){
	        	//Get pixel color
	        	int[] rgb = new int[3];
	        	indexValueFile.getPixel(x, y, rgb);
	        	
	        	int r = rgb[0]/32 * 32;
	        	int g = rgb[1]/32 * 4;
	        	int b = rgb[2]/64;
	        	int indexValue = r+g+b;
	        	
	        	indexValueFile.setPixel(x, y, new int[]{indexValue,indexValue,indexValue});
	        }
	    }    	
	    indexValueFile.write2PPM(inputFile + "-index.ppm");

		//Display image using look up table
	    for(int x = 0; x < output.getW(); x++){
	        for(int y = 0; y < output.getH(); y++){
	        	//Get pixel color
	        	int[] rgb = new int[3];
	        	indexValueFile.getPixel(x, y, rgb);
	        	int pixel = rgb[0];
	        		        	
	        	//Set pixel color
	        	output.setPixel(x, y, new int[]{lut[pixel][1],lut[pixel][2],lut[pixel][3]});
	        }
	    }
		return output;
	}
 
	/*private static Image conv4Old(Image output) {
	    /*
	    0	0-31		0-63
	    1	32-63 		64-127
	    2	64-95 		128-191
	    3	96-127 		192-255
	    4	128-159 
	    5	160-191 
	    6	192-223 
	    7	224-255		
	    
	    //Create RGB cube: [RedAxis][GreenAxis][BlueAxis][RGBValue]
	    //Split cube into areas
	    int[][][][] rgbCube = new int[8][8][4][3];
	    
	    //Determine the average for each area
	    for(int rAxis = 0; rAxis < 8; rAxis++){
	        for(int gAxis = 0; gAxis < 8; gAxis++){
	            for(int bAxis = 0; bAxis < 4; bAxis++){
	            	rgbCube[rAxis][gAxis][bAxis][0] = rAxis*32+15;
	            	rgbCube[rAxis][gAxis][bAxis][1] = gAxis*32+15;
	            	rgbCube[rAxis][gAxis][bAxis][2] = bAxis*64+31;
	            }
	        }
	    }
	    
	    //For each pixel, change its color
	    for(int x = 0; x < output.getW(); x++){
	        for(int y = 0; y < output.getH(); y++){
	        	//Get pixel color
	        	int[] rgb = new int[3];
	        	output.getPixel(x, y, rgb);
	        	
	        	//Divide red and green by 32, blue by 64
	        	int r = rgb[0]/32;
	        	int g = rgb[1]/32;
	        	int b = rgb[2]/64;
	        	        	
	        	//Set pixel color
	        	output.setPixel(x, y, rgbCube[r][g][b]);
	        }
	    }    
	    return output;
	}*/

	static void aliasing(Scanner reader) {
	    //Get m,n,k from user
	    System.out.print("Thickness of each circle in pixels: ");
	    int m = reader.nextInt();
	    System.out.print("Difference between successive radii in pixels: ");
	    int n = reader.nextInt();
	    System.out.print("Number to subsample by: ");    
	    int k = reader.nextInt();

	    //Create new 512x512 image
	    Image img = new Image(512,512);
	    
	    //Fill with white
	    for(int i=0; i<img.getW(); i++){
		    for(int j=0; j<img.getH(); j++){
				  img.setPixel(i, j, new int[]{255,255,255});
		    }
	    }
	    
	    //Create all circles
	    int radius = n;
	    while(radius+m < 256){
	    	img.drawCircle(256, 256, radius, m, new int[]{0,0,0});
	    	radius = radius + n;
	    }
	    
	    img.display("out.ppm");
	    
	    //Subsample by k
	    //Resize without filter
	    Aliasing.noFilter(img, k).display("No Filter");
	    Aliasing.avgFilter(img, k).display("Average Filter");
	    Aliasing.filter1(img, k).display("3x3 Filter 1");
	    Aliasing.filter2(img, k).display("3x3 Filter 2");
	}

	static void dictionary(Scanner scanner) throws Exception {
	    //Get m,n,k from user
	    System.out.print("File Name: ");
	    String fileName = scanner.next();
	    //String fileName = "HW2_sample/LZW_test4.txt";
	    System.out.print("Dictionary Size: ");
	    int size = scanner.nextInt();
	    
	    //Convert input values into array
    	System.out.println("Original Text:");
	    BufferedReader reader = new BufferedReader(new FileReader(fileName));
	    List<String> inputArray = new ArrayList<String>();
	    while(reader.ready()){
	    	char n = (char)reader.read();
	    	System.out.print(n);
	    	inputArray.add(String.valueOf(n));
	    }
	    
	    //Create dictionary containing all strings of length 1
	    List<String> dictionary = new ArrayList<String>();
	    for (String c : inputArray)
	    	if(!dictionary.contains(c))
	    		dictionary.add(String.valueOf(c));
	    
	    //List of indexes
	    List<Integer> indexList = new ArrayList<Integer>();
	    
	    //Fill dictionary
	    for (int i=0; i<inputArray.size();){
	    	String currentString = inputArray.get(i);
	    	while(dictionary.contains(currentString) && i<inputArray.size()){
	    		if(i==inputArray.size()-1) currentString = currentString + " ";
	    		else currentString = currentString + inputArray.get(i+1);
	    		i++;
	    	}
	    		    	
	    	//Finds the index of the current string minus the last character
	    	//and adds the index to the list
	    	indexList.add(dictionary.indexOf(currentString.substring(0, currentString.length() - 1)));
	    	
	    	//Add new string to dictionary
	    	if(dictionary.size()<size && i!=inputArray.size())
	    		dictionary.add(currentString);
    		
     		//Break if index reaches end of input
    		if(i>=inputArray.size()) break;
	    }

	    //Display output
    	System.out.println("\n\nIndex		Entry");
    	System.out.println("---------------------");
	    for (int i=0; i<dictionary.size(); i++)
	    	System.out.println(i + "		" + dictionary.get(i));

    	System.out.println("\nEncoded Text:");
    	for (int i=0; i<indexList.size(); i++)
	    	System.out.print(indexList.get(i) + " ");

    	System.out.print("\n\nDecoded Text:	");
	    for (int i=0; i<indexList.size(); i++)
	    	System.out.print(dictionary.get(indexList.get(i)));
	    
    	System.out.println("\n\nCompression Ratio:	" + (float)inputArray.size()/(float)indexList.size() + "\n");
	    
	    reader.close();
	}

	static Image toJpeg(Image output, Scanner scanner){
	    System.out.print("Compression Quality: ");
	    int quality = scanner.nextInt();
	    //int quality = 1;
	    float s = output.getH()*output.getW()*24;
	    float d = 0;
		
		//F1. Read and resize the input image
	    int height = output.getH();
	    int width = output.getW();
		output = Jpeg.addPadding(output);
	    
		
	    //F2. Color space transformation and Subsampling
		//Convert RGB to YCbCr
		float[][][] YCbCr;
		YCbCr = Jpeg.toYCbCr(output);
		float[][] componentY = YCbCr[0];
		float[][] componentCb = YCbCr[1];
		float[][] componentCr = YCbCr[2];
		//Subsample Cb and Cr
		componentCb = Jpeg.subsample(componentCb);
		componentCr = Jpeg.subsample(componentCr);
		
		
		//F3. Discrete Cosine Transform & F4. Quantization
		componentY = Jpeg.dct(componentY, quality, 0);
		componentCb = Jpeg.dct(componentCb, quality, 1);
		componentCr = Jpeg.dct(componentCr, quality, 1);

		
		//F5. Compression Ratio
		System.out.println("\nFor a quantization level n = " + quality);
		System.out.println("The original image cost, (S), is " + s + " bits.");
		float y = Jpeg.compressionRatio(componentY, quality, 0);
		System.out.println("The Y values cost is " + y + " bits.");
		d += y;
		y = Jpeg.compressionRatio(componentCb, quality, 1);
		System.out.println("The Cb values cost is " + y + " bits.");
		d += y;
		y = Jpeg.compressionRatio(componentCr, quality, 1);
		System.out.println("The Cr values cost is " + y + " bits.");
		d += y;
		System.out.println("The total compressed image cost, (D), is " + d + " bits.");
		System.out.println("The compression ratio, (S/D), is " + s/d + ".\n");

		
		//I2. Inverse DCT & I1. De-quantization
		componentY = Jpeg.idct(componentY, quality, 0);
		componentCb = Jpeg.idct(componentCb, quality, 1);
		componentCr = Jpeg.idct(componentCr, quality, 1);

		
		//I3. Inverse Color space transformation and Supersampling
		//Supersample Cb and Cr
		componentCb = Jpeg.supersample(componentCb);
		componentCr = Jpeg.supersample(componentCr);
		//Convert YCbCr to RGB
	    output = Jpeg.toRGB(componentY, componentCb, componentCr);

	    
	    //I4. Remove Padding and Display the image
		output = Jpeg.removePadding(output, width, height);
		
		return output;
	}
	
	static void motionComp(Scanner scanner) {
	    System.out.print("Target image: ");
	    String t = scanner.next();
	    //String t = "IDB/Walk_060.ppm";
	    System.out.print("Reference image: ");
	    String r = scanner.next();
	    //String r = "IDB/Walk_058.ppm";

	    Image target = new Image(t);
	    Image reference = new Image(r);
	    Image errorFrame = new Image(t);
	    int p = 12;
	    
	    System.out.println("# Target image name: " + t);
	    System.out.println("# Reference image name: " + r);
	    System.out.print("# Number of target macro blocks: " + target.getW()/16 + " x " + target.getH()/16);
	    System.out.println(" (Image size is " + target.getW() + " x " + target.getH() + ")");
	    
		//Split into 16x16 blocks
		for(int row = 0; row < target.getH(); row+=16){
			for(int col = 0; col < target.getW(); col+=16){
				//Create macroblock
				Image macroblock = new Image(16,16);
				macroblock = target.createBlock(col, row, 16, 16);
				
				int[] bestVector = {0,0};
				double bestMSD = Integer.MAX_VALUE;
				Image bestBlock = new Image(16,16);
				
				//For all candidate blocks
				for(int candidateRow=-1*p; candidateRow<=p; candidateRow++){
					for(int candidateCol=-1*p; candidateCol<=p; candidateCol++){
						//Ignore if out of bounds
						if(		row+candidateRow < 0 ||
								col+candidateCol < 0 ||
								row+candidateRow+16 > reference.getH() ||
								col+candidateCol+16 > reference.getW()) continue;
						
						Image prevblock = reference.createBlock(col+candidateCol, row+candidateRow, 16, 16);
						double currentMSD = MotionVector.msd(prevblock, macroblock);
						
						if(currentMSD < bestMSD){
							bestMSD = currentMSD;
							bestVector[0] = candidateCol;
							bestVector[1] = candidateRow;
							bestBlock = prevblock;
						}
					}
				}
				
				//Error residual
				Image errorBlock = MotionVector.residual(macroblock, bestBlock);
				
				//Apply error block to the error frame
				for(int bh = 0; bh < 16; bh++)
					for(int bw = 0; bw < 16; bw++)
						errorFrame.setPixel(col+bw, row+bh, errorBlock.getPixel(bw, bh));
				
				System.out.print("[" + bestVector[0] + "," + bestVector[1] + "]	");
			}
			System.out.println();
		}
		
		errorFrame.display("error_" + t);
	}

	static void removeMotion(Scanner scanner) {
	    System.out.print("Target image n: ");
	    int n = scanner.nextInt();
	    //int n = 60;
	    String targetPath;
	    if(n<10) targetPath = "IDB/Walk_00" + n + ".ppm";
	    else if(n<100) targetPath = "IDB/Walk_0" + n + ".ppm";
	    else targetPath = "IDB/Walk_" + n + ".ppm";
	    Image target = new Image(targetPath);

	    n=n-2;
	    String referencePath;
	    if(n<10) referencePath = "IDB/Walk_00" + n + ".ppm";
	    else if(n<100) referencePath = "IDB/Walk_0" + n + ".ppm";
	    else referencePath = "IDB/Walk_" + n + ".ppm";
	    Image reference = new Image(referencePath);
		
	    Image fifthFrame = new Image("IDB/Walk_005.ppm");
	    ArrayList<int[]> staticBlocks = new ArrayList<int[]>();
	    
		Image objRemove1 = new Image(targetPath);
		Image objRemove2 = new Image(targetPath);
	    int p = 12;
		//Split into 16x16 blocks
		for(int row = 0; row < target.getH(); row+=16){
			for(int col = 0; col < target.getW(); col+=16){
				//Create macroblock
				Image macroblock = new Image(16,16);
				macroblock = target.createBlock(col, row, 16, 16);
				
				int[] bestVector = {0,0};
				double bestMSD = Integer.MAX_VALUE;
				Image bestBlock = new Image(16,16);
				
				//For all candidate blocks
				for(int candidateRow=-1*p; candidateRow<=p; candidateRow++){
					for(int candidateCol=-1*p; candidateCol<=p; candidateCol++){
						//Ignore if out of bounds
						if(		row+candidateRow < 0 ||
								col+candidateCol < 0 ||
								row+candidateRow+16 > reference.getH() ||
								col+candidateCol+16 > reference.getW()) continue;
						
						Image prevblock = reference.createBlock(col+candidateCol, row+candidateRow, 16, 16);
						double currentMSD = MotionVector.msd(prevblock, macroblock);
						
						if(currentMSD < bestMSD){
							bestMSD = currentMSD;
							bestVector[0] = candidateCol;
							bestVector[1] = candidateRow;
							bestBlock = prevblock;
						}
					}
				}
				//If current block is static
				if(bestVector[0] == 0 && bestVector[1] == 0){
					staticBlocks.add(new int[]{row, col});
				}

				
				//If current block is dynamic
				if(bestVector[0] != 0 || bestVector[1] != 0){
					//First approach: Replace with nearest static block
					
					//Second approach: Replace with 5th frame
					for(int bh = 0; bh < 16; bh++)
						for(int bw = 0; bw < 16; bw++)
							objRemove2.setPixel(col+bw, row+bh, fifthFrame.getPixel(col+bw, row+bh));

				}
			}
		}

		//objRemove1.display("obj_remove1");
		objRemove2.display("obj_remove2");
	}

}

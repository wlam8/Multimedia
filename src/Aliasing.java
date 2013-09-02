
public class Aliasing {
	public static Image noFilter(Image img, int subsample) {
	    Image output = new Image(img.getW()/subsample, img.getH()/subsample);

	    //Take every pixel from original image starting from 0,0 skipping by subsample
	    for(int i=0; i<img.getW(); i+=subsample){
		    for(int j=0; j<img.getH(); j+=subsample){
	        	int[] rgb = new int[3];
	        	img.getPixel(i, j, rgb);
		    	
		    	output.setPixel(i/subsample, j/subsample, rgb);
		    }
	    }
		return output;		
	}

	public static Image avgFilter(Image img, int subsample) {
	    Image output = new Image(img.getW()/subsample, img.getH()/subsample);

	    //i and j: for each pixel in the new image
	    for(int i=0; i<output.getW(); i++){
		    for(int j=0; j<output.getH(); j++){
			    //get all corresponding pixels in original image and average it
		    	int average = 0;
	        	int[] rgb = new int[3];
	        	
			    for(int k=0; k<subsample; k++){
				    for(int l=0; l<subsample; l++){
			        	img.getPixel(i*subsample+k, j*subsample+l, rgb);
			        	average += rgb[0];
				    }
			    }
			    average = (int) (average/Math.pow(subsample, 2));
			    output.setPixel(i, j, new int[]{average, average, average});
		    }
	    }
		return output;		
	}

	public static Image filter1(Image img, int subsample) {
	    Image output = new Image(img.getW()/subsample, img.getH()/subsample);

	    //Take every pixel from original image starting from 0,0 skipping by subsample
	    for(int i=0; i<img.getW(); i+=subsample){
		    for(int j=0; j<img.getH(); j+=subsample){		    	
	        	int[] rgb = new int[3];
	        	
	        	img.getPixel(i, j, rgb);
	        	rgb[0] = Math.round(rgb[0]/9);
	        	rgb[1] = Math.round(rgb[1]/9);
	        	rgb[2] = Math.round(rgb[2]/9);
	        	output.setPixel(i/subsample, j/subsample, rgb);

	        	//Top left
	        	if(i!=0 && j!=0){
	        		img.getPixel(i-1, j-1, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Top middle
	        	if(j!=0){
	        		img.getPixel(i, j-1, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Top right
	        	if(i!=img.getW()-1 && j!=0){
	        		img.getPixel(i+1, j-1, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}

	        	//Middle left
	        	if(i!=0){
	        		img.getPixel(i-1, j, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Middle right
	        	if(i!=img.getW()-1){
	        		img.getPixel(i+1, j, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}

	        	//Bottom left
	        	if(i!=0 && j!=img.getH()-1){
	        		img.getPixel(i-1, j+1, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Bottom middle
	        	if(j!=img.getH()-1){
	        		img.getPixel(i, j+1, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}

	        	//Bottom right
	        	if(i!=img.getW()-1 && j!=img.getH()-1){
	        		img.getPixel(i+1, j+1, rgb);
		        	rgb[0] = Math.round(rgb[0]/9);
		        	rgb[1] = Math.round(rgb[1]/9);
		        	rgb[2] = Math.round(rgb[2]/9);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}		    	
		    }
	    }
		return output;	
	}

	public static Image filter2(Image img, int subsample) {
	    Image output = new Image(img.getW()/subsample, img.getH()/subsample);

	    //Take every pixel from original image starting from 0,0 skipping by subsample
	    for(int i=0; i<img.getW(); i+=subsample){
		    for(int j=0; j<img.getH(); j+=subsample){		    	
	        	int[] rgb = new int[3];
	        	
	        	img.getPixel(i, j, rgb);
	        	rgb[0] = Math.round(rgb[0]/4);
	        	rgb[1] = Math.round(rgb[1]/4);
	        	rgb[2] = Math.round(rgb[2]/4);
	        	output.setPixel(i/subsample, j/subsample, rgb);

	        	//Top left
	        	if(i!=0 && j!=0){
	        		img.getPixel(i-1, j-1, rgb);
		        	rgb[0] = Math.round(rgb[0]/16);
		        	rgb[1] = Math.round(rgb[1]/16);
		        	rgb[2] = Math.round(rgb[2]/16);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Top middle
	        	if(j!=0){
	        		img.getPixel(i, j-1, rgb);
		        	rgb[0] = Math.round(rgb[0]/8);
		        	rgb[1] = Math.round(rgb[1]/8);
		        	rgb[2] = Math.round(rgb[2]/8);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Top right
	        	if(i!=img.getW()-1 && j!=0){
	        		img.getPixel(i+1, j-1, rgb);
		        	rgb[0] = Math.round(rgb[0]/16);
		        	rgb[1] = Math.round(rgb[1]/16);
		        	rgb[2] = Math.round(rgb[2]/16);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}

	        	//Middle left
	        	if(i!=0){
	        		img.getPixel(i-1, j, rgb);
		        	rgb[0] = Math.round(rgb[0]/8);
		        	rgb[1] = Math.round(rgb[1]/8);
		        	rgb[2] = Math.round(rgb[2]/8);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Middle right
	        	if(i!=img.getW()-1){
	        		img.getPixel(i+1, j, rgb);
		        	rgb[0] = Math.round(rgb[0]/8);
		        	rgb[1] = Math.round(rgb[1]/8);
		        	rgb[2] = Math.round(rgb[2]/8);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}

	        	//Bottom left
	        	if(i!=0 && j!=img.getH()-1){
	        		img.getPixel(i-1, j+1, rgb);
		        	rgb[0] = Math.round(rgb[0]/16);
		        	rgb[1] = Math.round(rgb[1]/16);
		        	rgb[2] = Math.round(rgb[2]/16);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}
	        	
	        	//Bottom middle
	        	if(j!=img.getH()-1){
	        		img.getPixel(i, j+1, rgb);
		        	rgb[0] = Math.round(rgb[0]/8);
		        	rgb[1] = Math.round(rgb[1]/8);
		        	rgb[2] = Math.round(rgb[2]/8);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}

	        	//Bottom right
	        	if(i!=img.getW()-1 && j!=img.getH()-1){
	        		img.getPixel(i+1, j+1, rgb);
		        	rgb[0] = Math.round(rgb[0]/16);
		        	rgb[1] = Math.round(rgb[1]/16);
		        	rgb[2] = Math.round(rgb[2]/16);
		        	output.incrementPixel(i/subsample, j/subsample, rgb);
	        	}		    	
		    }
	    }
		return output;	
	}
}

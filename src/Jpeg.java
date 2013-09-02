import java.util.ArrayList;


public class Jpeg {
	private static float[][] luminance = new float[][]{
		new float[]{ 4, 4, 4, 8, 8,16,16,32},
		new float[]{ 4, 4, 4, 8, 8,16,16,32},
		new float[]{ 4, 4, 8, 8,16,16,32,32},
		new float[]{ 8, 8, 8,16,16,32,32,32},
		new float[]{ 8, 8,16,16,32,32,32,32},
		new float[]{16,16,16,32,32,32,32,32},
		new float[]{16,16,32,32,32,32,32,32},
		new float[]{32,32,32,32,32,32,32,32}
	};


	private static float[][] chrominance = new float[][]{
		new float[]{ 8, 8, 8,16,32,32,32,32},
		new float[]{ 8, 8, 8,16,32,32,32,32},
		new float[]{ 8, 8,16,32,32,32,32,32},
		new float[]{16,16,32,32,32,32,32,32},
		new float[]{32,32,32,32,32,32,32,32},
		new float[]{32,32,32,32,32,32,32,32},
		new float[]{32,32,32,32,32,32,32,32},
		new float[]{32,32,32,32,32,32,32,32}
	};


	//Pads the image so that its width and height is divisible by 8
	public static Image addPadding(Image output) {
		//Get new width and height
		int height = output.getH();
		int width = output.getW();
		while(width%8 != 0) width++;
		while(height%8 != 0) height++;
		
		Image newImg = new Image(width, height);
		//Copys pixels from old image into new image
		for(int y = 0; y < output.getH(); y++){
			for(int x = 0; x < output.getW(); x++){
	        	newImg.setPixel(x, y, output.getPixel(x, y));
			}
		}
		return newImg;
	}
	
	//Removes padding using original width and height
	public static Image removePadding(Image output, int width, int height) {
		Image newImg = new Image(width, height);
		//Copys pixels from old image into new image
		for(int y = 0; y < newImg.getH(); y++){
			for(int x = 0; x < newImg.getW(); x++){
	        	newImg.setPixel(x, y, output.getPixel(x, y));
			}
		}
		return newImg;
	}

	//Removes padding, try to guess original width and height
	public static Image removePadding(Image output) {
		int maxH = 0;
		int maxW = 0;
		for(int y = 0; y < output.getH(); y++){
			for(int x = 0; x < output.getW(); x++){
				int[] rgb = new int[3];
	        	output.getPixel(x, y, rgb);
	        	if(rgb[0] != 0 || rgb[1] != 0 || rgb[2] != 0){
	        		if(x > maxW) maxW = x;
	        		if(y > maxH) maxH = y;
	        	}
			}			
		}
		
		Image newImg = new Image(maxW+1, maxH+1);
		//Copys pixels from old image into new image
		for(int y = 0; y < newImg.getH(); y++){
			for(int x = 0; x < newImg.getW(); x++){
	        	newImg.setPixel(x, y, output.getPixel(x, y));
			}
		}
		return newImg;
	}

	public static float[][][] toYCbCr(Image output) {
		float[][] componentY = new float[output.getH()][output.getW()];
		float[][] componentCb = new float[output.getH()][output.getW()];
		float[][] componentCr = new float[output.getH()][output.getW()];
		
		//Convert to YCbCr components
		for(int h = 0; h < output.getH(); h++){
			for(int w = 0; w < output.getW(); w++){
				int[] rgb = new int[3];
	        	output.getPixel(w, h, rgb);
	        	
	        	int r = rgb[0];
	        	int g = rgb[1];
	        	int b = rgb[2];

	        	componentY[h][w] = (float) (0.299*r + 0.587*g + 0.144*b - 128);
	        	componentCb[h][w] = (float) (-0.168736*r + -0.331264*g + 0.5*b - .5);
	        	componentCr[h][w] = (float) (0.5*r + -0.418588*g + -0.081312*b - .5);	        	
	        	//0.299 0.587 0.144
	        	//-0.168736 -0.331264 0.5
	        	//0.5 -0.418588 -0.081312
			}
	    }

		return new float[][][]{componentY, componentCb, componentCr};
	}

	public static Image toRGB(float[][] componentY, float[][] componentCb, float[][] componentCr) {
		Image output = new Image(componentY[0].length, componentY.length);
		
		for(int h = 0; h < componentY.length; h++)
			for(int w = 0; w < componentY[h].length; w++)
				componentY[h][w] += 128;

		for(int h = 0; h < componentCb.length; h++)
			for(int w = 0; w < componentCb[h].length; w++)
				componentCb[h][w] += .5;

		for(int h = 0; h < componentCr.length; h++)
			for(int w = 0; w < componentCr[h].length; w++)
				componentCr[h][w] += .5;
		
		for(int h = 0; h < output.getH(); h++){
			for(int w = 0; w < output.getW(); w++){
				int[] rgb = new int[3];
				rgb[0] = (int)(1*componentY[h][w] + 0*componentCb[h][w] + 1.402*componentCr[h][w]);
				rgb[1] = (int)(1*componentY[h][w] + -.3441*componentCb[h][w] + -.7141*componentCr[h][w]);
				rgb[2] = (int)(1*componentY[h][w] + 1.772*componentCb[h][w] + 0*componentCr[h][w]);
				//1 0 1.402
				//1 -0.3441 -0.7141
				//1 1.772 0
				if(rgb[0] > 255) rgb[0] = 255;
				if(rgb[0] < 0) rgb[0] = 0;
				if(rgb[1] > 255) rgb[1] = 255;
				if(rgb[1] < 0) rgb[1] = 0;
				if(rgb[2] > 255) rgb[2] = 255;
				if(rgb[2] < 0) rgb[2] = 0;

				output.setPixel(w, h, rgb);
			}	
		}
		return output;
	}

	public static float[][] subsample(float[][] sample) {
		//Subsample must be divisible by 8
		int width = sample.length/2;
		int height = sample[0].length/2;
		while(width%8 != 0) width++;
		while(height%8 != 0) height++;
		
		float[][] subsample = new float[width][height];
		//Populate with zeros
		for(int h = 0; h < subsample.length; h++)
			for(int w = 0; w < subsample[h].length; w++)
				subsample[h][w] = 0;

		//Fill subsample with averages from sample
		for(int h = 0; h < sample.length/2; h++){
			for(int w = 0; w < sample[h].length/2; w++){
				subsample[h][w] = 
						(sample[h*2][w*2] + 
						sample[h*2+1][w*2] + 
						sample[h*2][w*2+1] + 
						sample[h*2+1][w*2+1])/4;
			}
		}

		return subsample;
	}

	public static float[][] supersample(float[][] sample) {
		float[][] supersample = new float[sample.length*2][sample[0].length*2];
		
		for(int h = 0; h < sample.length; h++){
			for(int w = 0; w < sample[h].length; w++){
				supersample[h*2][w*2] = sample[h][w];
				supersample[h*2+1][w*2] = sample[h][w];
				supersample[h*2][w*2+1] = sample[h][w];
				supersample[h*2+1][w*2+1] = sample[h][w];
			}
		}
		return supersample;
	}
	
	public static float[][] dct(float[][] component, int quality, int table) {
		//Split component into 8x8 blocks
		for(int h = 0; h < component.length; h+=8){
			for(int w = 0; w < component[h].length; w+=8){
				//w and h is top left corner of current 8x8 block
				//Create new block and fill
				float[][] block = new float[8][8];
				for(int bh = 0; bh < 8; bh++)
					for(int bw = 0; bw < 8; bw++)
						block[bh][bw] = component[h+bh][w+bw];

				//Apply DCT to block
				block = dctTransform(block);
				
				//F4. Quantization
				block = quantization(block, quality, table);

				//Apply block to the component
				for(int bh = 0; bh < 8; bh++)
					for(int bw = 0; bw < 8; bw++)
						component[h+bh][w+bw] = block[bh][bw];
			}
		}
		return component;
	}

	public static float[][] idct(float[][] component, int quality, int table) {
		//Split component into 8x8 blocks
		for(int h = 0; h < component.length; h+=8){
			for(int w = 0; w < component[h].length; w+=8){
				//w and h is top left corner of current 8x8 block
				//Create new block and fill
				float[][] block = new float[8][8];
				for(int bh = 0; bh < 8; bh++)
					for(int bw = 0; bw < 8; bw++)
						block[bh][bw] = component[h+bh][w+bw];
				
				//I1. De-quantization
				block = dequantization(block, quality, table);
	
				//Apply inverse DCT to block
				block = idctTransform(block);
				
				//Apply block to the component
				for(int bh = 0; bh < 8; bh++)
					for(int bw = 0; bw < 8; bw++)
						component[h+bh][w+bw] = block[bh][bw];
			}
		}
		return component;
	}

	private static float[][] dctTransform(float[][] f) {
		float[][] bigF = new float[8][8];
		for(int urow = 0; urow < 8; urow++){
			for(int vcol = 0; vcol < 8; vcol++){
				
				//Calculate the summation part of the formula
				float summation = 0;
				for(int xrow = 0; xrow < 8; xrow++)
					for(int ycol = 0; ycol < 8; ycol++)
						summation += f[xrow][ycol]*
								Math.cos(urow*Math.PI*(2*xrow+1)/16)*
								Math.cos(vcol*Math.PI*(2*ycol+1)/16);
				
				bigF[urow][vcol] = summation*c(urow)*c(vcol)/4;
				
				//Make sure it does not surpass max/min value
				if(bigF[urow][vcol] > 1024) bigF[urow][vcol] = 1024;
				if(bigF[urow][vcol] < -1024) bigF[urow][vcol] = -1024;
			}
		}
		return bigF;		
	}

	private static float[][] idctTransform(float[][] bigF) {
		float[][] f = new float[8][8];
		for(int xrow = 0; xrow < 8; xrow++){
			for(int ycol = 0; ycol < 8; ycol++){
				
				//Calculate the summation part of the formula
				float summation = 0;
				for(int urow = 0; urow < 8; urow++)
					for(int vcol = 0; vcol < 8; vcol++)
						summation += bigF[urow][vcol]*c(urow)*c(vcol)*
								Math.cos(urow*Math.PI*(2*xrow+1)/16)*
								Math.cos(vcol*Math.PI*(2*ycol+1)/16);
				
				f[xrow][ycol] = summation/4;
			}
		}
		return f;		
	}

	private static float c(int x) {
		if(x == 0) return (float) (1/Math.sqrt(2));
		return 1;
	}

	//If t=0 use Luminance table, if t=1 use Chrominance table
	private static float[][] quantization(float[][] bigF, int quality, int t) {
		float[][] quantized = new float[8][8];
		float[][] q = new float[8][8];
		if(t==0) q = luminance;
		else q = chrominance;

		//Perform quantization
		for(int urow = 0; urow < 8; urow++)
			for(int vcol = 0; vcol < 8; vcol++)
				quantized[urow][vcol]
						= (int) Math.round(bigF[urow][vcol]/(q[urow][vcol]*Math.pow(2, quality)));

		return quantized;	
	}

	private static float[][] dequantization(float[][] quantized, int quality, int t) {
		float[][] bigF = new float[8][8];
		float[][] q = new float[8][8];
		if(t==0) q = luminance;
		else q = chrominance;

		//Perform dequantization
		for(int urow = 0; urow < 8; urow++)
			for(int vcol = 0; vcol < 8; vcol++)
				bigF[urow][vcol]
						= (float) (quantized[urow][vcol]*q[urow][vcol]*Math.pow(2, quality));

		return bigF;
	}

	static float compressionRatio(float[][] component, int quality, int t) {
		float componentSize = 0;
		//Split component into 8x8 blocks
		for(int h = 0; h < component.length; h+=8){
			for(int w = 0; w < component[h].length; w+=8){
				//w and h is top left corner of current 8x8 block
				//Create new block and fill
				float[][] block = new float[8][8];
				for(int bh = 0; bh < 8; bh++)
					for(int bw = 0; bw < 8; bw++)
						block[bh][bw] = component[h+bh][w+bw];

				//Get the size of the block
				ArrayList<Float> sequence = new ArrayList<Float>();
				int xrow = 0;
				int ycol = 1;
				//Move up right when true, down left when false
				boolean direction = false;
				
				while(xrow<8 && ycol<8){
					sequence.add(block[xrow][ycol]);
					
					if(direction){
						if(xrow == 0){
							//Hits top border
							ycol++;
							direction = false;
						}else if(ycol == 7){
							//Hits right border
							xrow++;
							direction = false;
						}else{
							//Move up right
							xrow--;
							ycol++;
						}
					}else{
						if(xrow == 7){
							//Hits bottom border
							ycol++;
							direction = true;
						}else if(ycol == 0){
							//Hits left border
							xrow++;
							direction = true;
						}else{
							//Move down left
							xrow++;
							ycol--;
						}
					}
				}
		
				//Run-length coding
				float currentCode = sequence.get(0);
				@SuppressWarnings("unused")
				int currentLength = 0;
				float pairs = 1;
				for(int i=0; i<sequence.size(); i++){
					if(sequence.get(i) == currentCode){
						currentLength++;
					}else{
						pairs++;
						//System.out.print("(" + currentCode + "," + currentLength + ")");
						currentCode = sequence.get(i);
						currentLength = 1;
					}
				}
				if(t==0) componentSize += (pairs*(9-quality+6)+(9-quality));
				else componentSize += (pairs*(8-quality+6)+(8-quality));
			}
		}
		return componentSize;
	}
}
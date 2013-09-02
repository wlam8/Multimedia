
public class MotionVector {
	public static Image residual(Image macroblock, Image bestBlock) {
		Image output = new Image(16,16);
		for(int row=0; row<16; row++){
			for(int col=0; col<16; col++){
				int difference = (int) (toGray(macroblock, col, row) - toGray(bestBlock, col, row));
				//if(difference < 0) difference = 0;
				difference = Math.abs(difference);
				if(difference > 255) difference = 255;
				
				int[] rgb = {difference,difference,difference};
				output.setPixel(col, row, rgb);
			}
		}
		return output;
	}

	//Takes 2 blocks as arguments and outputs the MSD
	public static double msd(Image A, Image B) {
		double output = 0;
		for(int p=0; p<16; p++)
			for(int q=0; q<16; q++)
				output += Math.pow(toGray(A, p, q) - toGray(B, p, q), 2);

		return output/(16*16);
	}

	private static long toGray(Image block, int col, int row) {
		//Get the gray value
		int[] rgb = block.getPixel(col, row);
		int r = rgb[0];
		int g = rgb[1];
		int b = rgb[2];
		return Math.round(0.299 * r + 0.587 * g + 0.114 * b);		
	}

}

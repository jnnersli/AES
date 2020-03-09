package AES;

public class NewDriver{
	public static short[][] plainText=new short[][]{{0x54,0x77,0x6f,0x20},{0x4f,
		0x6e,0x65,0x20},{0x4e,0x69,0x6e,0x65},{0x20,0x54,0x77,0x6f}};
	public static short[] key = {0x54,0x68,0x61,0x74,0x73,0x20,0x6D,
						0x79,0x20,0x4B,0x75,0x6E,0x67,0x20,0x46,0x75};
	public static void main(String[] args){

		short[][] keyMtx = keyExpansionTest();
		short[][] addedMtx = addRoundKeyTest(key,plainText);
		subBytesTest(addedMtx);

	}

	public static short[][] keyExpansionTest(){
		//Uses key "Thats my Kung Fu"

		
		short[][] somebuddy = KeyExpansion.keyExpansion10(key);
		System.out.print(somebuddy[3][0]+",");
		System.out.print(somebuddy[3][1]+",");
		System.out.print(somebuddy[3][2]+",");
		System.out.print(somebuddy[3][3]+",");
		System.out.println("we done");
		for(int i = 0 ; i < somebuddy.length; i++){

			for(int j = 0 ; j < somebuddy[i].length; j++ ){

				System.out.print(somebuddy[i][j]+",");
			}
			System.out.println();

		}
		return somebuddy;

	}
	public static short[][] addRoundKeyTest(short[] key2, short[][] plainerText ){

		short[][] addedMtx = KeyExpansion.addRoundKey(key2,plainerText);
		System.out.println("***********************");
		for(int i = 0 ; i < addedMtx.length; i++){

			for(int j = 0 ; j < addedMtx.length; j++){

				System.out.print(addedMtx[i][j]+",");


			}
			System.out.println();


		}
		return addedMtx;

	}
	public static short[][] subBytesTest(short[][] alteredMtx){

		short[][] subbedMtx = KeyExpansion.subBytes(alteredMtx);
		System.out.println("***********************");
		for(int i = 0 ; i < subbedMtx.length; i++){

			for(int j = 0 ; j < subbedMtx.length; j++){

				System.out.print(subbedMtx[i][j]+",");


			}
			System.out.println();


		}
		return subbedMtx;



	}

}
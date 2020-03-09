package AES;

public class Driver{

	public static void main(String[] args){

		keyExpansionTest();


	}

	public static void keyExpansionTest(){
		//Uses key Thats  my Kung Fu
		short[] key = {0x54,0x68,0x61,0x74,0x73,0x20,0x6D,
						0x79,0x20,0x4B,0x75,0x6E,0x67,0x20,0x46,0x75};
		
		KeyExpansion.keyExpansion10(key);


	}

}
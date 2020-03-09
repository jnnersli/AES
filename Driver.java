package AES;
import KeyExpansion;

public class Driver{

	public static void main(String[] args){
        short[][] plainText = {{0x54,0x77,0x6f,0x20},  // 128-bit plain text
                              {0x4f,0x6e,0x65,0x20},
                              {0x4e,0x69,0x6e,0x65},
                              {0x20,0x54,0x77,0x6f}};
                                            
        short[] key = {0x54,0x68,0x61,0x74,  // 128-bit key
                       0x73,0x20,0x6D,0x79,
                       0x20,0x4B,0x75,0x6E,
                       0x67,0x20,0x46,0x75};

		short[][] cipherText = KeyExpansion.AES(plainText); //aes or whatever it's gonna be called
        
        for(int i = 0; i < cipherText.length; i++) {
            for(int j = 0; j < cipherText[i].length; j++) {
                System.out.print(Integer.toHexString(cipherText[i][j]) + " ");
            }
        }
	}

	public static void keyExpansionTest(){
		//Uses key Thats  my Kung Fu
		short[] key = {0x54,0x68,0x61,0x74,0x73,0x20,0x6D,
						0x79,0x20,0x4B,0x75,0x6E,0x67,0x20,0x46,0x75};
		
		KeyExpansion.keyExpansion10(key);


	}

}
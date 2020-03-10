/*  
    AES Key Expansion.
    
    Sources: 
        https://www.brainkart.com/article/AES-Key-Expansion_8410/
*/
package AES;

public class AES{
    static final int[] RC = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36};
    static short[][] RCon = generateRCon10();  // round constant array.
    static final int[][] sbox = {{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76}, 
                           {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0}, 
                           {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15}, 
                           {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75}, 
                           {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84}, 
                           {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf}, 
                           {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8}, 
                           {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2}, 
                           {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73}, 
                           {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb}, 
                           {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79}, 
                           {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08}, 
                           {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a}, 
                           {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e}, 
                           {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf}, 
                           {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};


    /*  
        Generate 10 round constants for Key Expansion.  
        
        The round constant is a word in which the three rightmost bytes are always 0. 
        Thus, the effect of an XOR of a word with Rcon is to only perform an XOR on the 
        left- most short of the word. 
        
        The round constant is different for each round and is defined as
        Rcon[j] = (RC[j], 0, 0, 0), with RC[1] = 1, RC[j] = 2 RC[j -1].
    */
    static short[][] generateRCon10() {
        short[][] rcon = new short[10][4];
        
        for(int i = 0; i < 10; i++) {
            rcon[i][0] = (short) RC[i];
            rcon[i][1] = (short) 0;
            rcon[i][2] = (short) 0;
            rcon[i][3] = (short) 0;
        }
        
        return rcon;
    }


    /*  
        Performs a byte substitution on each byte of its input word, using the S-box
        for Key Expansion.
    */
    static short[] subWord(short[] word) {
        short[] sword = new short[word.length];
        
        for(int i = 0; i < word.length; i++) {
            sword[i] = s(word[i]);
        }
        
        return sword;
    }
    
    /*
        Helper function for subWord and subBytes.
    */
    static short s(short b) {
        //  Convert byte to bit string.
        String bitstring = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        short row = Short.valueOf(Short.parseShort(bitstring.substring(0,4), 2));
        short col = Short.valueOf(Short.parseShort(bitstring.substring(4,8), 2));
        
        return (short) sbox[row][col];
    }
    

    /*  
        Performs a one-byte circular left shift on a word for KeyExpansion.
    */
    static short[] rotWord(short[] word) {
        short[] rword = new short[word.length];
        
        rword[word.length-1] = word[0];
        
        for(int i = 0; i < word.length-1; i++) {
            rword[i] = word[i+1];
        }
        
        return rword;
    }
    
    
    /*
        The AES key expansion algorithm takes as input a four-word (16-byte) key 
        and produces a linear array of 44 words (176 bytes).
    */
    static short[][] keyExpansion10(short[] key){
        short[][] w = new short[44][4];  // array of 44 words (4 bytes)
        short[] temp = new short[4];  // temp words
        
        //  The key is copied into the first four words of the expanded key.
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                w[i][j] = key[4*i+j];
            }
        }
        
        //  The remainder of the expanded key is filled in four words at a time.
        for(int i = 4; i < w.length; i++) {
            //  Each added word w[i] depends on the immediately preceding word, 
            //  w[i  - 1], and the word four positions back, w[i - 4].
            for( int p = 0; p < temp.length ; p++){
                temp[p]=w[i-1][p];
            }
            
            //  For a word whose position in the w array is a multiple of 4, 
            //  a more complex function is used.
            if(i % 4 == 0) {
                for(int j = 0; j < temp.length; j++) {
                    w[i][j] = (short)(w[i-4][j] ^ subWord(rotWord(temp))[j] ^ RCon[(i-4)/4][j]);
                }
            }
            
            //In three out of four cases, a simple XOR is used.
            else {
                for(int j = 0; j < temp.length; j++) {
                    w[i][j] = (short)(w[i-4][j] ^ temp[j]);
                }
            }
        }
        
        return w;
    }
    
    
    /*
        addRoundKey performs XOR operation with 
    */
    static short[][] addRoundKey(short[] key, short[][] mtx){

        short[][] roundKey = new short[4][4];
        short[][] returnMtx = new short[4][4];
        
        for(int i = 0 ; i < roundKey.length; i++){
            for(int j = 0 ; j < roundKey.length; j++){
                roundKey[i][j]=keyExpansion10(key)[i][j];
            }
        }

        for(int i = 0 ; i < roundKey.length; i++){
            for( int j = 0 ; j < roundKey.length; j++){
                System.out.println(mtx[i][j]);
                System.out.println(roundKey[i][j]);
                returnMtx[j][i]=(short)(mtx[i][j] ^ roundKey[i][j]);
            }
        }
        
        return returnMtx;
    }
    
    
    //Performs the subBytes step in the AES algorithm, this
    //switches each entry with its corresponding entry in the
    //sbox matrix.
    static short[][] subBytes(short[][] mtxEntry){

        for(int i = 0 ; i < mtxEntry.length ; i++){
            for(int j = 0 ; j < mtxEntry.length ; j++)
                mtxEntry[i][j] = s(mtxEntry[i][j]);

        }

        return mtxEntry;

    }
    
    
    /*
        sbMatrix means sub bytes matrix which is the matrix that is the
        result from the previous step. The shift rows step does not move
        the first row at all, the second row it moves one time to the left
        circularly, and the second and third row are shifted two and three times
        respectively.
    */
    static short[][] shiftRows(short[][] sbMatrix){
        //I know theres an easier way to do this, but this is
        //just what I thought of at the time.
        short[][] copyMatrix= new short[4][4];
   
        //haha
        
        copyMatrix[0][0]=sbMatrix[0][0];
        copyMatrix[0][1]=sbMatrix[0][1];
        copyMatrix[0][2]=sbMatrix[0][2];
        copyMatrix[0][3]=sbMatrix[0][3];

        copyMatrix[1][0]=sbMatrix[1][1];
        copyMatrix[1][1]=sbMatrix[1][2];
        copyMatrix[1][2]=sbMatrix[1][3];
        copyMatrix[1][3]=sbMatrix[1][0];


        copyMatrix[2][0]=sbMatrix[2][2];
        copyMatrix[2][1]=sbMatrix[2][3];
        copyMatrix[2][2]=sbMatrix[2][0];
        copyMatrix[2][3]=sbMatrix[2][1];

        copyMatrix[3][0]=sbMatrix[3][3];
        copyMatrix[3][1]=sbMatrix[3][0];
        copyMatrix[3][2]=sbMatrix[3][1];
        copyMatrix[3][3]=sbMatrix[3][2];


        return copyMatrix;

    }
    

    /*
        Mix column uses matrix multiplication with each column
        using a predefined matrix given by
        2 3 1 1
        1 2 3 1
        1 1 2 3
        3 1 1 2

    */
    static short[] mixColumns(short[] column){
        short[][] mixMtx = {{2,3,1,1},
                            {1,2,3,1},
                            {1,1,2,3},
                            {3,1,1,2}};
        short columnSum=0;
        short[] returnArray= new short[4];
        //matrix multiplication: note only two loops rather than the
        //usual three because we are operating solely on columns.
        for(int i = 0 ; i < mixMtx[0].length ; i++){
            for(int j = 0 ; j < mixMtx[0].length ; j++){

                columnSum+=mixMtx[i][j]*column[j];

            }   
            returnArray[i]=columnSum;
            columnSum=0;
        }

        return returnArray;
    }
    
    
    public static short[][] AES(short[][] plainText, short[] key) {
        short[][] roundKeys = keyExpansion10(key);
        
        //  Round 1
        short[][] state = addRoundKey(roundKeys[0], plainText);
        
        //  Rounds 2-9
        for(int i = 0; i < 9; i++) {
            state = subBytes(state);
            state = shiftRows(state);
            for(int j = 0; j < state.length; j++) {
                state[j] = mixColumns(state[j]);
            }
            state = addRoundKey(roundKeys[i], state); // possibly needs to be changed to specify which round
        }
        
        //  Final round
        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(roundKeys[roundKeys.length-1], state);
        
        return state;
    }
}


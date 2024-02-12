package com.snabeel.dappethr;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class helper {

    private static Map<String,Integer> hexMap = new HashMap<String,Integer>();
    static {
        hexMap.put("0",0);
        hexMap.put("1",1);
        hexMap.put("2",2);
        hexMap.put("3",3);
        hexMap.put("4",4);
        hexMap.put("5",5);
        hexMap.put("6",6);
        hexMap.put("7",7);
        hexMap.put("8",8);
        hexMap.put("9",9);
        hexMap.put("A",10);
        hexMap.put("B",11);
        hexMap.put("C",12);
        hexMap.put("D",13);
        hexMap.put("E",14);
        hexMap.put("F",15);
        hexMap.put("a",10);
        hexMap.put("b",11);
        hexMap.put("c",12);
        hexMap.put("d",13);
        hexMap.put("e",14);
        hexMap.put("f",15);
    }

    public class UTFException extends Exception {
        public UTFException(String s) {
            
        }
    }

    public List<Integer> hexToInt(String hex) throws UTFException {
        List<Integer> retVal = new ArrayList<Integer>();
        Integer i=0;
        while(i<hex.length()) {

// http://en.wikipedia.org/wiki/UTF-8
            int numberOfBytes=1;
            int byte1=0, byte2=0, byte3=0, byte4=0;
            int utfCode=0;
            byte1=(hexMap.get(hex.substring(i,i+1)) * 16) + (hexMap.get(hex.substring(i+1,i+2))) ;

//invalid sequences for byte1
            if(byte1>=128 && byte1<=191) {
                throw new UTFException("UTF-8:Continuation byte as first byte");
            }
            if(byte1>=192 && byte1<=193) {
                throw new UTFException("UTF-8:Invalid 2-byte sequence");
            }
            if(byte1>=245) {
                throw new UTFException("UTF-8:Invalid 4,5 or 6-byte sequence");
            }

            if(byte1>=192) {
                numberOfBytes=2;
                byte2=(hexMap.get(hex.substring(i+2,i+2+1)) * 16) + (hexMap.get(hex.substring(i+2+1,i+2+2))) ;
            }
            if(byte1>=224) {
                numberOfBytes=3;
                byte3=(hexMap.get(hex.substring(i+4,i+4+1)) * 16) + (hexMap.get(hex.substring(i+4+1,i+4+2))) ;
            }
            if(byte1>=240) {
                numberOfBytes=4;
                byte4=(hexMap.get(hex.substring(i+6,i+6+1)) * 16) + (hexMap.get(hex.substring(i+6+1,i+6+2))) ;
            }
            if(numberOfBytes==1) {
                utfCode=byte1;
            } else if(numberOfBytes==2) {
                utfCode= Math.floorMod(byte1,32)*64+Math.floorMod(byte2,64);
            } else if(numberOfBytes==3) {
                utfCode=Math.floorMod(byte1,16)*64*64+Math.floorMod(byte2,64)*64+Math.floorMod(byte3,64);
            } else if(numberOfBytes==4) {
                utfCode=Math.floorMod(byte1,8)*64*64*64+Math.floorMod(byte2,64)*64*64+Math.floorMod(byte3,64)*64+Math.floorMod(byte4,64);
            }

            retVal.add( utfCode );
            i+=2*numberOfBytes;
        }
        return retVal;
    }

    public String HexToUTF(String hex) throws UTFException {
        String text= String.valueOf(hexToInt(hex));
        return text;
    }
}

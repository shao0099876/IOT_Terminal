package com.hit_src.iot_terminal.tools;

public class BitOperation {
    public static byte[] IntegertoBytes(int p,int len){
        byte[] res=new byte[len];
        for(int i=0;i<len;i++){
            res[i]=(byte)(p&0x0ff);
            p>>=8;
        }
        return res;
    }
    public static int BytestoInteger(byte[] p){
        int res=0;
        for(int i=p.length-1;i>=1;i--){
            res|=p[i]&0x0ff;
            res<<=8;
        }
        res|=p[0]&0x0ff;
        return res;
    }
    public static boolean[] BytetoBits(byte p){
        boolean[] res=new boolean[8];
        for(int i=0;i<8;i++){
            res[i]=(p&1)==1;
            p>>=1;
        }
        return res;
    }
    public static byte BitstoByte(boolean[] p){
        byte res=0;
        for(int i=7;i>=1;i--){
            res|=p[i]?1:0;
            res<<=1;
        }
        res|=p[0]?1:0;
        return res;
    }
    public static byte[] BitstoBytes(boolean[][] p){
        byte[] res=new byte[p.length];
        for(int i=0;i<p.length;i++){
            res[i]=BitstoByte(p[i]);
        }
        return res;
    }
    public static boolean[][] BytestoBits(byte[] p){
        boolean[][] res=new boolean[p.length][];
        for(int i=0;i<p.length;i++){
            res[i]=BytetoBits(p[i]);
        }
        return res;
    }
    public static boolean[] XOR(boolean[] p1, boolean[] p2) {
        boolean[] res=new boolean[8];
        for(int i=0;i<p1.length;i++){
            if(p1[i]&&p2[i]){
                res[i]=false;
            }
            if(p1[i]&&!p2[i]){
                res[i]=true;
            }
            if(!p1[i]&&p2[i]){
                res[i]=true;
            }
            if(!p1[i]&&!p2[i]){
                res[i]=false;
            }
        }
        return res;
    }
    public static boolean[][] RightMoves(boolean[][] p1, int p2) {
        int t=BytestoInteger(BitstoBytes(p1));
        t>>=p2;
        return BytestoBits(IntegertoBytes(t,2));
    }
    public static boolean[][] XORS(boolean[][] p1, boolean[][] p2) {
        boolean[][] res=new boolean[p1.length][];
        for(int i=0;i<p1.length;i++){
            res[i]=XOR(p1[i],p2[i]);
        }
        return res;
    }

}

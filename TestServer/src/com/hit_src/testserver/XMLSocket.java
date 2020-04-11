package com.hit_src.testserver;

import java.io.*;
import java.net.Socket;

public class XMLSocket {
    private Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mainSocket.getOutputStream()));
                String cmd = reader.readLine();
                if (cmd.equals("getList")) {
                    writer.write(getList());
                    writer.flush();
                } else if (cmd.equals("add") || cmd.equals("update")) {
                    writer.write(getFile(reader.readLine()));
                    writer.flush();
                }
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    private String getList() throws IOException {
        File file=new File("./xmlList");
        BufferedReader reader=new BufferedReader(new FileReader(file));
        int cnt=0;
        StringBuilder sb=new StringBuilder();
        while(true){
            String s=reader.readLine();
            cnt+=1;
            if(s==null){
                break;
            }
            sb.append(s);sb.append("\n");
        }
        reader.close();
        return cnt+"\n"+sb.toString();
    }
    private String getFile(String name) throws IOException {
        File file=new File("./"+name+".xml");
        BufferedReader reader=new BufferedReader(new FileReader(file));
        int cnt=0;
        StringBuilder sb=new StringBuilder();
        while(true){
            String s=reader.readLine();
            cnt+=1;
            if(s==null){
                break;
            }
            sb.append(s);
            sb.append("\n");
        }
        reader.close();
        return cnt+"\n"+sb.toString();
    }
    private Socket mainSocket=null;
    public XMLSocket(Socket socket){
        mainSocket=socket;
        thread.start();
    }
    public XMLSocket(){}
    public void stop() throws IOException {
        if(mainSocket!=null){
            mainSocket.close();
        }
    }
}

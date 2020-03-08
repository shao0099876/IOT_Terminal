import socket
import struct
def CalCRC(data):
    crc=0xffff
    for i in data:
        crc=(crc&0xff00)+(i^(crc&0x00ff)&0x00ff)
        j=1
        while(j<=8):
            crc>>=1
            if crc&0x0001==1:
                crc^=0xA001
            j+=1
    return crc

def pack(addr,func,off,len):
    data = struct.pack('BBHH', addr, func, off, len)
    data = bytearray(data)
    crc = CalCRC(data)
    data = struct.pack('BBHHH', addr, func, off, len, crc)
    return data

def readDevNum(sock):
    addr=0x01
    func=0x03
    off=0xffff
    len=0x0001
    sock.sendall(pack(addr,func,off,len))
    rep=sock.recv(1024)
    rep=struct.unpack('BBHH',rep)
    addr=rep[0]
    func=rep[1]
    data=rep[2]
    crc=rep[3]
    if(CalCRC(bytearray(struct.pack('BBH',addr,func,data)))==crc):
        return data

s=socket.socket()
host=socket.gethostname()
port=2020
s.bind((host,port))
s.listen(5)
print("listening")
while(True):
    sock,addr=s.accept()
    print("connected!")
    print(addr)
    while(True):
        cmd=input()
        print(cmd)
        if cmd=='readDevNum':
            print(readDevNum(sock))
        else:
            print("Wrong Cmd![readDevNum]")


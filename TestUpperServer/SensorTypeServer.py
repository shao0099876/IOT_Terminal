import socket

s=socket.socket()
host=socket.gethostname()
port=2021
s.bind((host,port))
s.listen(5)
print("listened")
while(True):
    sock,addr=
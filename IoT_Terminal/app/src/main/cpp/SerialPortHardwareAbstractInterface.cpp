//
// Created by shao0 on 2020/3/12.
//

#include <jni.h>
#include <fcntl.h>
#include <asm/termios.h>
#include <termios.h>
#include <cstdlib>
#include <unistd.h>
#include <memory.h>
void makeConfid(termios* ctlStruct){
    //波特率115200
    cfsetispeed(ctlStruct,B115200);
    cfsetospeed(ctlStruct,B115200);
    //无校验
    ctlStruct->c_cflag&= ~PARENB;
    ctlStruct->c_cflag &= ~CSTOPB;
    ctlStruct->c_cflag &= ~CSIZE;

    cfmakeraw(ctlStruct);
}
char* DEV_NAME="/dev/ttyUSB0";
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_SerialPort_read(JNIEnv *env, jclass clazz, jint length) {
    // TODO: implement read()
    jbyteArray res=env->NewByteArray(length+1);
    jbyte *p=env->GetByteArrayElements(res,NULL);
    p[0]=0;
    int fd=open(DEV_NAME,O_RDWR);
    if(fd==-1){
        p[0]=-1;
    }
    else{
        termios ctlStruct;
        tcgetattr(fd,&ctlStruct);
        makeConfid(&ctlStruct);
        int err=tcsetattr(fd,TCSANOW,&ctlStruct);
        if(err!=0){
            p[0]=-2;
        }
        else{
            char* buffer=(char*)malloc(length);
            int cnt=read(fd,buffer,length);
            p[0]=cnt;
            for(int i=1;i<=cnt;i++){
                p[i]=buffer[i-1];
            }
            free(buffer);
        }
        close(fd);
    }
    env->ReleaseByteArrayElements(res,p,0);
    return res;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_hit_1src_iot_1terminal_hardware_SerialPort_write(JNIEnv *env, jclass clazz,
                                                          jbyteArray content) {
    // TODO: implement write()
    int res=0;

    int fd=open(DEV_NAME,O_RDWR);
    if(fd==-1){
        res=-1;
    }
    else{
        termios ctlStruct;
        tcgetattr(fd,&ctlStruct);
        makeConfid(&ctlStruct);
        int err=tcsetattr(fd,TCSANOW,&ctlStruct);
        if(err!=0){
            res=-2;
        }
        else{
            int length=env->GetArrayLength(content);
            jbyte *p =env->GetByteArrayElements(content,NULL);
            char* buffer=(char*)malloc(length);
            for(int i=0;i<length;i++){
                buffer[i]=p[i];
            }
            res=write(fd,buffer,length);

            free(buffer);
            env->ReleaseByteArrayElements(content,p,0);
        }
        close(fd);
    }
    return res;
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_SerialPort_write_1read(JNIEnv *env, jclass clazz,
                                                                jbyteArray content, jint read_length) {
    // TODO: implement write_read()
    jbyteArray res;
    res=env->NewByteArray(read_length);

    int fd=open(DEV_NAME,O_RDWR);
    if(fd==-1){

    }
    else{
        termios ctlStruct;
        tcgetattr(fd,&ctlStruct);
        makeConfid(&ctlStruct);
        int err=tcsetattr(fd,TCSANOW,&ctlStruct);
        if(err!=0){

        }
        else{
            int write_length=env->GetArrayLength(content);
            jbyte *p =env->GetByteArrayElements(content,NULL);
            char* write_buffer=(char*)malloc(write_length*sizeof(char));
            memcpy(write_buffer,p,write_length*sizeof(char));
            int write_st=write(fd,write_buffer,write_length*sizeof(char));
            free(write_buffer);
            env->ReleaseByteArrayElements(content,p,0);

            char* read_buffer=(char*) malloc(read_length*sizeof(char));
            int cnt=read(fd,read_buffer,read_length*sizeof(char));

            p=env->GetByteArrayElements(res,NULL);
            for(int i=0;i<cnt;i++){
                p[i]=read_buffer[i-1];
            }
            free(read_buffer);
            env->ReleaseByteArrayElements(res,p,0);
        }
        close(fd);
    }
    return res;
}
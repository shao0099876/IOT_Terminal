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
#include <android/log.h>
#include <ctime>

#define   LOG_TAG    "SRCDEBUG_SERIAL"
#define   LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define   LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define   LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
void makeConfig(termios* ctlStruct){
    //波特率115200
    cfsetispeed(ctlStruct,B115200);
    cfsetospeed(ctlStruct,B115200);
    //无校验
    ctlStruct->c_cflag&= ~PARENB;
    ctlStruct->c_cflag &= ~CSTOPB;
    ctlStruct->c_cflag &= ~CSIZE;
    cfmakeraw(ctlStruct);
    ctlStruct->c_cc[VTIME]=30;
    ctlStruct->c_cc[VMIN]=0;

}
char* DEV_NAME="/dev/ttyUSB0";
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_SerialPort_read(JNIEnv *env, jclass clazz, jint length) {
    // TODO: implement read()
    jbyteArray res=env->NewByteArray(length);
    jbyte *p=env->GetByteArrayElements(res,NULL);
    int fd=open(DEV_NAME,O_RDWR);
    bool check=true;
    if(fd==-1){
        LOGD("OPENFAILED");
    }
    else{
        termios ctlStruct;
        tcgetattr(fd,&ctlStruct);
        makeConfig(&ctlStruct);
        int err=tcsetattr(fd,TCSANOW,&ctlStruct);
        if(err!=0){
        }
        else{
            int cnt=read(fd,p,length);
            if(cnt<length){
                check=false;
            }
        }
        close(fd);
    }
    env->ReleaseByteArrayElements(res,p,0);
    if(check){
        return res;
    }
    else{
        return NULL;
    }
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
        makeConfig(&ctlStruct);
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
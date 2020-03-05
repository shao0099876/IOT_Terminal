//
// Created by shao0 on 2020/3/1.
//

#include <jni.h>

#include<net/if.h>
#include<sys/ioctl.h>
#include<linux/sockios.h>
#include<cstring>
#include <cstdlib>
#include<arpa/inet.h>
#include<string>
#include<sstream>
#include<cerrno>

extern int errno;
void reqSetName(JNIEnv* env,ifreq *req,jbyteArray name){
    memset(req->ifr_name,'\0',16*sizeof(char));
    int length=env->GetArrayLength(name);
    jbyte *p=env->GetByteArrayElements(name,NULL);
    for(int i=0;i<length;++i){
        req->ifr_name[i]=p[i];
    }
    env->ReleaseByteArrayElements(name,p,0);
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_getName(JNIEnv *env,
                                                                               jclass clazz,
                                                                               jint index) {
    // TODO: implement getName_HardWare()
    ifreq req;
    req.ifr_ifindex=index;
    int s=socket(AF_INET,SOCK_DGRAM,0);
    ioctl(s,SIOCGIFNAME,&req);
    char* tmp=req.ifr_name;
    int length=strlen(tmp);
    jbyteArray res=env->NewByteArray(strlen(tmp));
    jbyte * p=env->GetByteArrayElements(res,NULL);
    for(int i=0;i<length;i++){
        p[i]=tmp[i];
    }
    env->ReleaseByteArrayElements(res,p,0);
    return res;
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_getMAC(JNIEnv *env, jclass clazz,
                                                                    jbyteArray name) {
    // TODO: implement getMAC()
    ifreq req;
    char s[1024];
    memset(s,'\0',sizeof(s));
    reqSetName(env,&req,name);
    int sock=socket(AF_INET,SOCK_DGRAM,0);

    if(sock<0){
        strcpy(s,"socket error");
    }
    else{
        int res=ioctl(sock,SIOCGIFHWADDR,&req);
        if(res==-1){
            sprintf(s,"errno:%d des:%s\0",errno,strerror(errno));
        }
        else{
            sockaddr* addr_p=(sockaddr*)&(req.ifr_hwaddr);
            char* mac_array=addr_p->sa_data;
            sprintf(s,"%02x:%02x:%02x:%02x:%02x:%02x\0",mac_array[0],mac_array[1],
                    mac_array[2],mac_array[3],mac_array[4],mac_array[5]);
        }
    }
    jbyteArray res=env->NewByteArray(strlen(s));
    jbyte * p=env->GetByteArrayElements(res,NULL);
    memcpy(p,s,strlen(s));
    env->ReleaseByteArrayElements(res,p,0);
    return res;

}extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_getADDR(JNIEnv *env, jclass clazz,
                                                                     jbyteArray name) {
    // TODO: implement getADDR()
    ifreq req;
    char s[1024];
    memset(s,'\0',sizeof(s));
    reqSetName(env,&req,name);
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        strcpy(s,"socket error");
    }
    else{
        int res=ioctl(sock,SIOCGIFADDR,&req);
        if(res==-1){
            sprintf(s,"errno:%d des:%s\0",errno,strerror(errno));
        }
        else{
            sockaddr_in* addr_p=(sockaddr_in*) &req.ifr_addr;
            unsigned int addr=addr_p->sin_addr.s_addr;
            sprintf(s,"%d.%d.%d.%d\0",(addr>>0)&255,(addr>>8)&255,(addr>>16)&255,(addr>>24)&255);
        }
    }
    jbyteArray res=env->NewByteArray(strlen(s));
    jbyte * p=env->GetByteArrayElements(res,NULL);
    memcpy(p,s,strlen(s));
    env->ReleaseByteArrayElements(res,p,0);
    return res;
}extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_getBROADADDR(JNIEnv *env, jclass clazz,
                                                                          jbyteArray name) {
    // TODO: implement getBROADADDR()
    ifreq req;
    char s[1024];
    memset(s,'\0',sizeof(s));
    reqSetName(env,&req,name);
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        strcpy(s,"socket error");
    }
    else{
        int res=ioctl(sock,SIOCGIFBRDADDR,&req);
        if(res==-1){
            sprintf(s,"errno:%d des:%s\0",errno,strerror(errno));
        }
        else{
            sockaddr_in* addr_p=(sockaddr_in*) &req.ifr_broadaddr;
            unsigned int addr=addr_p->sin_addr.s_addr;
            sprintf(s,"%d.%d.%d.%d\0",(addr>>0)&255,(addr>>8)&255,(addr>>16)&255,(addr>>24)&255);
        }
    }
    jbyteArray res=env->NewByteArray(strlen(s));
    jbyte * p=env->GetByteArrayElements(res,NULL);
    memcpy(p,s,strlen(s));
    env->ReleaseByteArrayElements(res,p,0);
    return res;
}extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_getNetMask(JNIEnv *env, jclass clazz,
                                                                        jbyteArray name) {
    // TODO: implement getNetMask()
    ifreq req;
    char s[1024];
    memset(s,'\0',sizeof(s));
    reqSetName(env,&req,name);
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        strcpy(s,"socket error");
    }
    else{
        int res=ioctl(sock,SIOCGIFNETMASK,&req);
        if(res==-1){
            sprintf(s,"errno:%d des:%s\0",errno,strerror(errno));
        }
        else{
            sockaddr_in* addr_p=(sockaddr_in*) &req.ifr_netmask;
            unsigned int addr=addr_p->sin_addr.s_addr;
            sprintf(s,"%d.%d.%d.%d\0",(addr>>0)&255,(addr>>8)&255,(addr>>16)&255,(addr>>24)&255);
        }
    }
    jbyteArray res=env->NewByteArray(strlen(s));
    jbyte * p=env->GetByteArrayElements(res,NULL);
    memcpy(p,s,strlen(s));
    env->ReleaseByteArrayElements(res,p,0);
    return res;
}extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_getFlags(JNIEnv *env, jclass clazz,
                                                                      jbyteArray name) {
    // TODO: implement getFlags()
    ifreq req;
    char s[1024];
    memset(s,'\0',sizeof(s));
    reqSetName(env,&req,name);
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        strcpy(s,"socket error");
    }
    else{
        int res=ioctl(sock,SIOCGIFFLAGS,&req);
        if(res==-1){
            sprintf(s,"errno:%d des:%s\0",errno,strerror(errno));
        }
        else{
            short res_raw=req.ifr_flags;
            char* FLAGS[16]={"UP","BROADCAST","DEBUG","LOOPBACK","POINTTOPOINT","NOTRAILERS","RUNNING","NOARP","PROMISC","ALLMULTI","MASTER","SLAVE",
                             "MULTICAST","PORTSEL","AUTOMEDIA","DYNAMIC"};
            for(int i=0;i<16;i++) {
                if (res_raw & (1 << i)) {
                    strcat(s, FLAGS[i]);
                    strcat(s, "|");
                }
            }
        }
    }
    jbyteArray res=env->NewByteArray(strlen(s));
    jbyte * p=env->GetByteArrayElements(res,NULL);
    memcpy(p,s,strlen(s));
    env->ReleaseByteArrayElements(res,p,0);
    return res;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_setMAC(JNIEnv *env, jclass clazz,
                                                                    jbyteArray name,
                                                                    jbyteArray mac) {
    // TODO: implement setMAC()
    ifreq req;
    int res=0;
    reqSetName(env,&req,name);
    char array[6];
    char* mac_tmp;
    int length=env->GetArrayLength(mac);
    mac_tmp=(char*) malloc(sizeof(char)*(length+1));
    jbyte *p=env->GetByteArrayElements(mac,NULL);
    memcpy(mac_tmp,p,length);
    mac_tmp[length]='\0';
    sscanf(mac_tmp,"%02x:%02x:%02x:%02x:%02x:%02x",&array[0],&array[1],&array[2],&array[3],&array[4],&array[5]);
    free(mac_tmp);
    sockaddr* addr_p=(sockaddr*)&(req.ifr_hwaddr);
    strcpy(addr_p->sa_data,array);
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        res=1;
    }
    else{
        int no=ioctl(sock,SIOCSIFHWADDR,&req);
        if(no==-1){
            res=errno;
        }
        else{
            res=0;
        }
    }
    return res;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_setADDR(JNIEnv *env, jclass clazz,
                                                                     jbyteArray name,
                                                                     jbyteArray addr) {
    // TODO: implement setADDR()
    ifreq req;
    int res=0;
    reqSetName(env,&req,name);
    int array[4];
    char* addr_tmp;
    int length=env->GetArrayLength(addr);
    addr_tmp=(char*) malloc(sizeof(char)*(length+1));
    jbyte *p=env->GetByteArrayElements(addr,NULL);
    memcpy(addr_tmp,p,length);
    addr_tmp[length]='\0';
    sscanf(addr_tmp,"%d.%d.%d.%d",&array[0],&array[1],&array[2],&array[3]);
    free(addr_tmp);
    unsigned int addr_val=array[0]+array[1]<<8+array[2]<<16+array[3]<<24;
    sockaddr_in* addr_p=(sockaddr_in*)&(req.ifr_addr);
    addr_p->sin_addr.s_addr=addr_val;
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        res=1;
    }
    else{
        int no=ioctl(sock,SIOCSIFADDR,&req);
        if(no==-1){
            res=errno;
        }
        else{
            res=0;
        }
    }
    return res;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_setNetmask(JNIEnv *env, jclass clazz,
                                                                        jbyteArray name,
                                                                        jbyteArray netmask) {
    // TODO: implement setNetmask()
    ifreq req;
    int res=0;
    reqSetName(env,&req,name);
    int array[4];
    char* netmask_tmp;
    int length=env->GetArrayLength(netmask);
    netmask_tmp=(char*) malloc(sizeof(char)*(length+1));
    jbyte *p=env->GetByteArrayElements(netmask,NULL);
    memcpy(netmask_tmp,p,length);
    netmask_tmp[length]='\0';
    sscanf(netmask_tmp,"%d.%d.%d.%d",&array[0],&array[1],&array[2],&array[3]);
    free(netmask_tmp);
    unsigned int netmask_val=array[0]+array[1]<<8+array[2]<<16+array[3]<<24;
    sockaddr_in* netmask_p=(sockaddr_in*)&(req.ifr_netmask);
    netmask_p->sin_addr.s_addr=netmask_val;
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        res=1;
    }
    else{
        int no=ioctl(sock,SIOCSIFNETMASK,&req);
        if(no==-1){
            res=errno;
        }
        else{
            res=0;
        }
    }
    return res;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_hit_1src_iot_1terminal_hardware_EthernetNetworkCard_setFLAGS(JNIEnv *env, jclass clazz,
                                                                      jbyteArray name,
                                                                      jshort addr) {
    // TODO: implement setFLAGS()
    ifreq req;
    int res=0;
    reqSetName(env,&req,name);
    req.ifr_flags=addr;
    int sock=socket(AF_INET,SOCK_DGRAM,0);
    if(sock<0){
        res=1;
    }
    else{
        int no=ioctl(sock,SIOCSIFFLAGS,&req);
        if(no==-1){
            res=errno;
        }
        else{
            res=0;
        }
    }
    return res;
}
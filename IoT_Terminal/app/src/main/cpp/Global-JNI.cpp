//
// Created by shao0 on 2020/3/19.
//
#include <jni.h>
#include <cstdlib>
#include <cstring>

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hit_1src_iot_1terminal_Global_CRC(JNIEnv *env, jclass clazz, jbyteArray data) {
    // TODO: implement CRC()
    jbyte* data_p=env->GetByteArrayElements(data, NULL);
    size_t data_len=(size_t)env->GetArrayLength(data);
    unsigned char* a=(unsigned char*)malloc(sizeof(unsigned char)*data_len);
    memcpy(a,data_p,data_len);
    env->ReleaseByteArrayElements(data,data_p,NULL);
    unsigned short crc=0xFFFF;//1)	预置16位CRC校验寄存器为FFFF.
    int i=0;
    do{
        crc=crc xor (unsigned short)a[i];//2)	把第1个8位数据与CRC校验寄存器的低8位数据异或，结果放于CRC校验寄存器。
        //3)	CRC校验寄存器内容右移1位，检查寄存器最低位，如果为1，则与0xA001异或；如果为0，无需异或。
        for(int j=0;j<8;j++){//4)	重复步骤3，直到右移8次。
            crc<<=1;
            if(crc != 0){
                crc^=0xA001;
            }
        }
    }while(++i<data_len);//5)	重复步骤2-4，对下一个8位数据进行处理。
    free(a);
    //6)	最后得到的CRC寄存器即为CRC码
    jbyteArray res=env->NewByteArray(2);
    jbyte *res_p=env->GetByteArrayElements(res,NULL);
    res_p[0]=(jbyte)((unsigned char)crc&0xFF);
    res_p[1]=(unsigned char)((crc&0xFF00)>>8);
    env->ReleaseByteArrayElements(res,res_p,NULL);
    return res;
}
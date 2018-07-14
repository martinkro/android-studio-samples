//
// Created by sundayliu on 2017/6/15.
//

#include "log.h"
#include <cstdio>
#include <cstring>
#include <ctime>
#include <cctype>
#include <cerrno>

#include <sys/time.h>

const char* LOG_TAG = "ELFViewer";
static const char* log_path = "/sdcard/sdk/loveai.log";

NS_LOVEAI_BEGIN
void log_hex_dump(const uint8_t* data, uint32_t size)
{
    if (data == NULL || size == 0)
    {
        return;
    }

    char str[16*4] = {0};
    uint32_t i;
    uint32_t j = 0;
    for (i = 0; i < size; i++)
    {
        snprintf(str+j*3, 5, "%02X ", data[i]);
        j++;
        j = j % 16;
        if (j == 0)
        {
            DEBUG_LOG("%p:%s", (const void*)(data+i), str);
        }
    }
    
    if (size < 16)
    {
        DEBUG_LOG("%p:%s", (const void*)(data), str);
    }
}

Log* Log::instance = NULL;

Log* Log::getInstance()
{
    if (instance == NULL)
    {
        instance = new Log();
    }
    return instance;
}

void Log::print(const char* fmt,...)
{
    char buf[1024] = {0};
    va_list arg;
    va_start(arg,fmt);
    vsnprintf(buf,sizeof(buf),fmt,arg);
    va_end(arg);

    DoLog(buf);
}

void Log::printBinary(const uint8_t* data, size_t len)
{
    char msg[1024*4+1] = {0};
    size_t i = 0;
    for (i = 0; i < len && i*2 < sizeof(msg) -1; i++)
    {
        snprintf(msg + i*2 , 3, "%02X", data[i]);
    }

    DoLog(msg);
}

void Log::printHexdump(const uint8_t* data, size_t len)
{
    char msg[16*6] = {0};
    char hex[16*3+1] = {0};
    char ascii[16+1] = {0};
    size_t i = 0;
    snprintf(msg,sizeof(msg),"0x%zx ",(uintptr_t)data);
    for (i = 0; i < len; i++)
    {
        snprintf(hex+(i%16)*3, 4, "%02X ", data[i]);
        if (isprint(data[i]))
        {
            snprintf(ascii+i%16,2,"%c",data[i]);
        }
        else
        {
            snprintf(ascii+i%16,2,".");
        }

        if (i != 0 && (i + 1)% 16 == 0)
        {
            strcat(msg,hex);
            strcat(msg,ascii);
            DoLog(msg);

            snprintf(msg,sizeof(msg),"0x%zx ",(uintptr_t)data+i+1);
        }
    }

    if (len % 16 != 0)
    {
        memset(hex+(len%16)*3,0x20,(16-len%16)*3);
        strcat(msg,hex);
        strcat(msg,ascii);
        DoLog(msg);
    }

}

void Log::DoLog(const char* buf)
{
    FILE* fp = fopen(log_path,"a+");
    if (fp != NULL)
    {
        timeval tv;
        gettimeofday(&tv, NULL);
        struct tm* p = localtime(&tv.tv_sec);
        fprintf(fp,"[%04d/%02d/%02d %02d:%02d:%02d %ld]%s",
                p->tm_year + 1990,
                p->tm_mon + 1,
                p->tm_mday,
                p->tm_hour,
                p->tm_min,
                p->tm_sec,
                tv.tv_usec,
                buf);
        if (buf[strlen(buf)-1] != '\n')
        {
            fprintf(fp,"\n");
        }
        fclose(fp);
    } else
    {
        /*
         * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
         */
        __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,"fopen %s fail:%d|%s", log_path, errno, strerror(errno));
    }
}
NS_LOVEAI_END


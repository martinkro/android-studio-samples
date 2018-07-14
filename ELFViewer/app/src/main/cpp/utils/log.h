//
// Created by sundayliu on 2017/6/15.
//

#ifndef UTILS_LOG_H
#define UTILS_LOG_H

#include <android/log.h>
#include <inttypes.h>
#include "config.h"

NS_LOVEAI_BEGIN
class Log
{
public:
    static Log* getInstance();
    ~Log(){}
    void print(const char* fmt,...);
    void printBinary(const uint8_t* data, size_t len);
    void printHexdump(const uint8_t* data, size_t len);
private:
    Log(){}
    void DoLog(const char* buf);
private:
    static Log* instance;
};
void log_hex_dump(const uint8_t* addr, uint32_t size);
NS_LOVEAI_END


#ifdef __ENABLE_LOG__
extern const char* LOG_TAG;
#define sLog NS_LOVEAI_NAME::Log::getInstance()
//#define DEBUG_LOG(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
//#define ERROR_LOG(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define DEBUG_LOG(...) sLog->print(__VA_ARGS__)
#define ERROR_LOG(...) sLog->print(__VA_ARGS__)
#define LOG_HEX_DUMP(data,size) sLog->printHexdump((data),(size))
#else
#define DEBUG_LOG(...) ((void)0)
#define ERROR_LOG(...) ((void)0)
#define LOG_HEX_DUMP(data,size) ((void)0)
#endif

#endif //UTILS_LOG_H

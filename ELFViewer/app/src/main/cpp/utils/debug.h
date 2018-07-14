#ifndef UTILS_DEBUG_H
#define UTILS_DEBUG_H

#include "config.h"

NS_LOVEAI_BEGIN

void debug_assert(int express, const char* file,int line);

NS_LOVEAI_END

#ifdef __ENABLE_ASSERT__
#define __ASSERT(express) NS_LOVEAI_NAME::debug_assert((express), __FILE__,__LINE__)
#else
#define __ASSERT(express)
#endif

#endif